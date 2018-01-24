package db;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import connectivity.Server;
import entities.Complaint;
import entities.IEntity;
import entities.Item;
import entities.ItemInReservation;
import entities.ItemInShop;
import entities.Reservation;
import entities.ShopCatalogData;
import entities.ShopCostumer;
import entities.Survey;
import entities.SurveyResult;
import entities.User;
import entities.EntitiesEnums.UserStatus;
import logger.LogManager;
import messages.EntitiesListData;
import messages.EntityData;
import messages.EntityDataCollection;
import messages.EntityDataOperation;
import messages.IMessageData;
import messages.LoginData;
import messages.Message;
import messages.RespondMessageData;

/**
 *
 * MessagesResolver: An class that can resolve a {@link Message} to operation in
 * the DB.
 * 
 * @see connectivity.Server.MessagesHandler
 */
public class MessagesResolver implements Server.MessagesHandler {

	// region Fields

	private Logger m_logger;

	private DbController m_dbController;

	// end region -> Fields

	// region Constructors

	/**
	 * Create an instance of {@link MessagesResolver}, A logger will be created by
	 * {@link LogManager}
	 *
	 * @param dbController
	 *            A connection with the DB.
	 */
	public MessagesResolver(DbController dbController) {
		m_dbController = dbController;
		m_logger = LogManager.getLogger();
	}

	/**
	 * Create an instance of {@link MessagesResolver}.
	 *
	 * @param logger
	 *            A logger that the instance will log to it.
	 * @param dbController
	 *            A connection with the DB.
	 */
	public MessagesResolver(Logger logger, DbController dbController) {
		m_dbController = dbController;
		m_logger = logger;
	}
	// end region -> Constructors

	// region Server.MessagesHandler Implementation

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Message onMessageReceived(Message msg) throws Exception {
		IMessageData messageData = msg.getMessageData();
		if (messageData == null) {
			return null;
		}

		IMessageData returnedMessageData = null;
		if (messageData instanceof LoginData) {
			returnedMessageData = onLoginDataReceived((LoginData) messageData);
		} else if (messageData instanceof EntityData) {
			returnedMessageData = onEntityDataReceived((EntityData) messageData);
		} else if (messageData instanceof EntitiesListData) {
			returnedMessageData = onEntitiesListDataReceived((EntitiesListData) messageData);
		} else if (messageData instanceof EntityDataCollection) {
			returnedMessageData = onEntityDataCollectionReceived((EntityDataCollection) messageData);
		} else if (messageData instanceof ShopCatalogData) {
			returnedMessageData = onShopCatalogDataReceived((ShopCatalogData) messageData);
		}

		if (returnedMessageData != null) {
			msg.setMessageData(returnedMessageData);
			return msg;
		}

		return null;
	}

	// end region -> Server.MessagesHandler Implementation

	/**
	 * 
	 * This method called by {@link MessagesResolver#onMessageReceived(Message)}
	 * when received a message with {@link ShopCatalogData} {@link IMessageData}.
	 *
	 * @param shopCatalogData
	 *            the received {@link ShopCatalogData}
	 * @return {@link EntitiesListData} if the operation succeed and
	 *         <code>null</code> if does not.
	 */
	private IMessageData onShopCatalogDataReceived(ShopCatalogData shopCatalogData) {
		int shopManagerId = shopCatalogData.getShopManagerId();
		if (shopManagerId < 1) {
			m_logger.warning("Received request for shop catalog with illegale shop manager ID: " + shopManagerId);
			return new RespondMessageData(shopCatalogData, false);
		}

		ResultSet queryResult = null;
		String getShopCatalogQuery = QueryGenerator.generateShopCatalogQuery(shopManagerId);

		try {
			CallableStatement callableStatement = m_dbController.getCallableStatement(getShopCatalogQuery);

			queryResult = callableStatement.executeQuery();
			if (queryResult == null) {
				return null;
			}
			List<IEntity> itemEntities = EntitiesResolver.ResultSetToShopCatalog(queryResult);

			if (itemEntities == null) {
				return null;
			}
			return itemEntities.isEmpty() ? null : new EntitiesListData(EntityDataOperation.None, itemEntities);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute call procedure query! query: " + getShopCatalogQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + getShopCatalogQuery
							+ ", exception: " + e.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * This method called by {@link MessagesResolver#onMessageReceived(Message)}
	 * when received a message with {@link LoginData} {@link IMessageData}.
	 *
	 * @param loginData
	 *            the received {@link LoginData}
	 * @return In case of login message {@link userEntityData} if the operation
	 *         succeed and {@link LoginData} if does not. In case of logout message
	 *         would return <code>null</code> in any situation.
	 */
	private IMessageData onLoginDataReceived(LoginData loginData) {
		User user = new User();
		user.setUserName(loginData.getUserName());
		IMessageData userEntityData = onUserEntityReceived(user, EntityDataOperation.Get);

		if (userEntityData instanceof RespondMessageData) {
			loginData.setMessage("The username does not exist.");
			m_logger.info("Login attempted to a username that does not exist in the system! Login Data: " + loginData);
			return loginData;
		}

		IEntity entity = ((EntityData) userEntityData).getEntity();
		User userEntity = (User) entity;
		if (!loginData.getPassword().equals(userEntity.getPassword())) {
			m_logger.info("An attempt was made to connect to a user with wrong password! User Details: " + userEntity
					+ ", Login Data: " + loginData);
			loginData.setMessage("The password does not match!");
			return loginData;
		}

		if (loginData.isLogoutMessage()) {
			if (userEntity.getStatus() == UserStatus.Connected) {
				userEntity.setStatus(UserStatus.Disconnected);
				onUserEntityReceived(userEntity, EntityDataOperation.Update);
				m_logger.info("An user disconnected from the system! User Details: " + userEntity);
			}
			return null;
		}

		// This is login message:

		if (userEntity.getStatus() == UserStatus.Connected) {
			loginData.setMessage("The user '" + userEntity.getUserName() + "' already connected to the system!");
			m_logger.info(
					"An attempt was made to connect to a user who is already logged on! User Details: " + userEntity);
			return loginData;
		}

		if (userEntity.getStatus() == UserStatus.Blocked) {
			loginData.setMessage("The user '" + userEntity.getUserName() + "' is blocked!");
			m_logger.info("An attempt was made to connect to a user who is blocked! User Details: " + userEntity);
			return loginData;
		}
		userEntity.setStatus(UserStatus.Connected);
		onUserEntityReceived(userEntity, EntityDataOperation.Update);
		m_logger.info("An user connected to the system! User Details: " + userEntity);
		return userEntityData;
	}

	/**
	 * 
	 * This method called by {@link MessagesResolver#onMessageReceived(Message)}
	 * when received a message with {@link EntityDataCollection}
	 * {@link IMessageData}.
	 *
	 * @param messageData
	 *            the received {@link EntityDataCollection}
	 * @return {@link RespondMessageData} with operation result.
	 */
	private IMessageData onEntityDataCollectionReceived(EntityDataCollection messageData) {
		List<EntityData> entities = messageData.getEntityDataList();
		boolean result = true;
		for (EntityData entityData : entities) {
			EntityDataOperation operation = entityData.getOperation();
			if (operation == EntityDataOperation.Get || operation == EntityDataOperation.GetALL
					|| operation == EntityDataOperation.None) {
				m_logger.warning(
						"EntitiesListData not supporting in SELECT operation! Received operation: " + operation);
				continue;
			}
			IMessageData returnedMessageData = onEntityDataReceived(entityData);
			if (returnedMessageData != null && returnedMessageData instanceof RespondMessageData) {
				result &= ((RespondMessageData) returnedMessageData).isSucceed();
			}
		}
		return new RespondMessageData(messageData, result);
	}

	/**
	 * 
	 * This method called by {@link MessagesResolver#onMessageReceived(Message)}
	 * when received a message with {@link EntitiesListData} {@link IMessageData}.
	 *
	 * @param messageData
	 *            the received {@link EntitiesListData}
	 * @return {@link RespondMessageData} with operation result.
	 */
	private IMessageData onEntitiesListDataReceived(EntitiesListData messageData) {
		EntityDataOperation operation = messageData.getOperation();
		if (operation == EntityDataOperation.Get || operation == EntityDataOperation.GetALL
				|| operation == EntityDataOperation.None) {
			m_logger.warning("EntitiesListData not supporting in SELECT operation! Received operation: " + operation);
			return null;
		}
		List<IEntity> entities = messageData.getEntities();
		boolean result = true;
		for (IEntity entity : entities) {
			EntityData entityData = new EntityData(operation, entity);
			IMessageData returnedMessageData = onEntityDataReceived(entityData);
			if (returnedMessageData != null && returnedMessageData instanceof RespondMessageData) {
				result &= ((RespondMessageData) returnedMessageData).isSucceed();
			}
		}
		return new RespondMessageData(messageData, result);
	}

	/**
	 * 
	 * This method called by {@link MessagesResolver#onMessageReceived(Message)}
	 * when received a message with {@link EntityData} {@link IMessageData}.
	 *
	 * @param entityData
	 *            the received {@link EntityData}
	 * @return {@link IMessageData} if the operation perform and <code>null</code>
	 *         if operation failed.
	 */
	private IMessageData onEntityDataReceived(EntityData entityData) {
		IEntity receivedEntity = entityData.getEntity();

		if (receivedEntity == null) {
			return null;
		}

		EntityDataOperation operation = entityData.getOperation();
		IEntity entity = entityData.getEntity();

		IMessageData returnedMessageData = null;
		switch (operation) {

		case Get:
			returnedMessageData = onEntityGetOperation(entityData);
			break;

		case GetALL:
			returnedMessageData = onEntityGetAllOperation(entityData);
			break;

		case Update:
			returnedMessageData = onEntityUpdateOperation(entityData);
			break;

		case Remove:
			returnedMessageData = onEntityRemoveOperation(entityData);
			break;

		case Add:
			returnedMessageData = onEntityAddOperation(entityData);
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + entity.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		return returnedMessageData;
	}

	/**
	 * 
	 * This method called by
	 * {@link MessagesResolver#onEntityDataReceived(EntityData)} when received a
	 * {@link EntityData} with {@link EntityDataOperation#Get} .
	 *
	 * @param entityData
	 *            the received {@link EntityData}
	 * @return {@link EntityData} if the operation succeed and
	 *         {@link RespondMessageData} if does not.
	 */
	private IMessageData onEntityGetOperation(EntityData entityData) {
		IEntity entity = entityData.getEntity();
		Class<? extends IEntity> entityType = entityData.getEntity().getClass();

		String selectQuery = QueryGenerator.generateSelectQuery(entity);
		if (selectQuery == null) {
			return new RespondMessageData(entityData, false);
		}
		IMessageData executeSelectQuery = executeSelectQuery(selectQuery, entityType);
		if (executeSelectQuery == null) {
			return new RespondMessageData(entityData, false);
		}
		IEntity entityResult = ((EntitiesListData) executeSelectQuery).getEntities().get(0);
		return new EntityData(EntityDataOperation.None, entityResult);

	}

	/**
	 * 
	 * This method called by
	 * {@link MessagesResolver#onEntityDataReceived(EntityData)} when received a
	 * {@link EntityData} with {@link EntityDataOperation#GetALL} .
	 *
	 * @param entityData
	 *            the received {@link EntityData}
	 * @return {@link EntitiesListData} if the operation succeed and
	 *         {@link RespondMessageData} if does not.
	 */
	private IMessageData onEntityGetAllOperation(EntityData entityData) {
		IEntity entity = entityData.getEntity();
		Class<? extends IEntity> entityType = entityData.getEntity().getClass();
		String selectAllQuery = QueryGenerator.generateSelectAllQuery(entity);
		if (selectAllQuery == null) {
			return new RespondMessageData(entityData, false);
		}
		IMessageData executeSelectQuery = executeSelectQuery(selectAllQuery, entityType);
		if (executeSelectQuery == null) {
			return new RespondMessageData(entityData, false);
		}
		return executeSelectQuery;
	}

	/**
	 * 
	 * This method called by
	 * {@link MessagesResolver#onEntityDataReceived(EntityData)} when received a
	 * {@link EntityData} with {@link EntityDataOperation#Update} .
	 *
	 * @param entityData
	 *            the received {@link EntityData}
	 * @return {@link RespondMessageData} with the result of operation perform.
	 */
	private IMessageData onEntityUpdateOperation(EntityData entityData) {
		IEntity entity = entityData.getEntity();
		Class<? extends IEntity> entityType = entityData.getEntity().getClass();
		boolean updateResult = false;
		if (entityType.equals(Item.class)) {
			updateResult = updateItemEntityExecution((Item) entity);
		} else {
			String updateQuery = QueryGenerator.generateUpdateQuery(entity);
			if (updateQuery != null) {
				updateResult = executeQuery(entity, updateQuery);
			}
		}
		RespondMessageData respondMessageData = new RespondMessageData(entityData, updateResult);
		return respondMessageData;
	}

	/**
	 * 
	 * This method called by
	 * {@link MessagesResolver#onEntityDataReceived(EntityData)} when received a
	 * {@link EntityData} with {@link EntityDataOperation#Remove} .
	 *
	 * @param entityData
	 *            the received {@link EntityData}
	 * @return {@link RespondMessageData} with the result of operation perform.
	 */
	private IMessageData onEntityRemoveOperation(EntityData entityData) {
		IEntity entity = entityData.getEntity();

		String removeQuery = QueryGenerator.generateDeleteQuery(entity);
		if (removeQuery == null) {
			return new RespondMessageData(entityData, false);
		}

		boolean removeResult = executeQuery(entity, removeQuery);
		RespondMessageData respondMessageData = new RespondMessageData(entityData, removeResult);
		return respondMessageData;
	}

	/**
	 * 
	 * This method called by
	 * {@link MessagesResolver#onEntityDataReceived(EntityData)} when received a
	 * {@link EntityData} with {@link EntityDataOperation#Add} .
	 *
	 * @param entityData
	 *            the received {@link EntityData}
	 * @return {@link RespondMessageData} with the result of operation perform.
	 */
	private IMessageData onEntityAddOperation(EntityData entityData) {
		IEntity entity = entityData.getEntity();
		Class<? extends IEntity> entityType = entityData.getEntity().getClass();

		boolean result = false;
		if (entityType.equals(Item.class)) {
			Item item = (Item) entity;
			result = addItemEntityExecution(item);
			if (result) {
				int lastInsertId = getLastInsertId();
				item.setId(lastInsertId);
			}
		} else if (entityType.equals(ItemInShop.class) || entityType.equals(ItemInReservation.class)
				|| entityType.equals(ShopCostumer.class) || entityType.equals(SurveyResult.class)) {
			String insertQuery = QueryGenerator.generateInsertQuery(entity);
			if (insertQuery != null) {
				result = executeQuery(entity, insertQuery);
			}
		} else {

			String insertQuery = QueryGenerator.generateInsertQuery(entity);
			if (insertQuery != null) {
				result = executeQuery(entity, insertQuery);
			}
			if (result) {
				int lastInsertId = getLastInsertId();

				if (entityType.equals(Reservation.class)) {
					((Reservation) entity).setId(lastInsertId);
				}
				if (entityType.equals(Survey.class)) {
					((Survey) entity).setId(lastInsertId);
				}
				if (entityType.equals(Complaint.class)) {
					((Complaint) entity).setId(lastInsertId);
				}
			}
		}

		return new RespondMessageData(entityData, result);
	}
	// end region -> Server.MessagesHandler Implementation

	private boolean addItemEntityExecution(Item item) {

		String insertItemQuery = QueryGenerator.generateInsertQuery(item);
		if (insertItemQuery == null) {
			return false;
		}
		try {
			return executeItemPreparedStatement(insertItemQuery, item);
		} catch (Exception ex) {
			m_logger.warning("Failed on try to create\\execute insert query of: " + item.toString() + ", exception: "
					+ ex.getMessage());
		}
		return false;
	}

	private boolean updateItemEntityExecution(Item item) {
		String updateItemQuery = QueryGenerator.generateUpdateQuery(item);
		if (updateItemQuery == null) {
			return false;
		}

		try {
			return executeItemPreparedStatement(updateItemQuery, item);
		} catch (Exception ex) {
			m_logger.warning("Failed on try to create\\execute insert query of: " + item.toString() + ", exception: "
					+ ex.getMessage());
		}
		return false;
	}

	private boolean executeItemPreparedStatement(String query, Item item) throws SQLException {
		PreparedStatement preparedStatement = m_dbController.getPreparedStatement(query);
		InputStream inputStream = item.getInputStream();
		if (inputStream != null) {
			preparedStatement.setBlob(1, inputStream);
		} else {
			preparedStatement.setNull(1, java.sql.Types.BLOB);
		}
		if (preparedStatement.executeUpdate() == 0) {
			m_logger.info("The query: \"" + preparedStatement.toString() + "\" does not effect any row.");
			return false;
		} else {
			return true;
		}
	}

	// end region -> Item Entity Operations

	// region User Entity Operations

	private IMessageData onUserEntityReceived(User user, EntityDataOperation operation) {

		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.generateSelectQuery(user);
			if (selectQuery == null) {
				break;
			}
			IMessageData executeSelectQuery = executeSelectQuery(selectQuery, User.class);
			if (executeSelectQuery == null) {
				break;
			}
			IEntity entity = ((EntitiesListData) executeSelectQuery).getEntities().get(0);
			return new EntityData(EntityDataOperation.None, entity);
		case GetALL:
			String selectAllQuery = QueryGenerator.generateSelectAllQuery(user);
			if (selectAllQuery == null) {
				break;
			}
			IMessageData executeSelectAllQuery = executeSelectQuery(selectAllQuery, User.class);
			if (executeSelectAllQuery != null) {
				return executeSelectAllQuery;
			}
			break;
		case Update:
			String updateQuery = QueryGenerator.generateUpdateQuery(user);
			if (updateQuery != null) {
				result = executeQuery(user, updateQuery);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + user.toString() + ", Operation: "
					+ operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> User Entity Operations

	// region private Methods

	private <TData extends IEntity> IMessageData executeSelectQuery(String selectQuery, Class<TData> expectedType) {
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			if (queryResult == null) {
				m_logger.warning("Failed on try to execute select query : " + selectQuery);
				return null;
			}
			List<IEntity> entities = EntitiesResolver.resultSetToEntity(queryResult, expectedType);
			return entities != null && !entities.isEmpty() ? new EntitiesListData(EntityDataOperation.None, entities)
					: null;

		} catch (Exception e) {
			m_logger.warning(
					"Failed on try to execute select query! query: " + selectQuery + ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ", exception: " + e.getMessage());
				}
			}
		}
		return null;
	}

	private boolean executeQuery(IEntity entity, String query) {
		boolean queryResult = false;
		try {
			queryResult = m_dbController.executeQuery(query);
		} catch (Exception ex) {
			m_logger.warning(
					"Failed on try to execute query of: " + entity.toString() + ", exception: " + ex.getMessage());
		}
		return queryResult;
	}

	private int getLastInsertId() {
		String lastIdQuery = QueryGenerator.generateLastIdQuery();
		if (lastIdQuery == null) {
			return -1;
		}
		ResultSet queryResult = null;
		int returningValue = -1;
		try {
			queryResult = m_dbController.executeSelectQuery(lastIdQuery);
			if (queryResult != null) {
				queryResult.next();
				returningValue = queryResult.getInt(1);
			}
		} catch (Exception ex) {
			m_logger.warning("Failed on try to execute query: " + lastIdQuery + ", exception: " + ex);
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + lastIdQuery
							+ ", exception: " + e.getMessage());
				}
			}
		}
		return returningValue;
	}

	// end region -> private methods

}
