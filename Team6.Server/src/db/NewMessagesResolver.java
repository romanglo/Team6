package db;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import connectivity.Server;
import logger.LogManager;
import newEntities.Complaint;
import newEntities.ComplaintsReport;
import newEntities.Costumer;
import newEntities.EntitiesEnums.UserStatus;
import newEntities.IEntity;
import newEntities.IncomesReport;
import newEntities.Item;
import newEntities.ItemInReservation;
import newEntities.ItemInShop;
import newEntities.Reservation;
import newEntities.ReservationsReport;
import newEntities.ShopEmployee;
import newEntities.ShopManager;
import newEntities.Survey;
import newEntities.SurveysReport;
import newEntities.User;
import newMessages.EntitiesListData;
import newMessages.EntityData;
import newMessages.EntityDataCollection;
import newMessages.EntityDataOperation;
import newMessages.IMessageData;
import newMessages.LoginData;
import newMessages.Message;
import newMessages.RespondMessageData;

/**
 *
 * MessagesResolver: An class that can resolve a {@link Message} to operation in
 * the DB.
 * 
 * @see connectivity.Server.MessagesHandler
 */
public class NewMessagesResolver implements Server.MessagesHandler {

	// region Fields

	private Logger m_logger;

	private DbController m_dbController;

	// end region -> Fields

	// region Constructors

	/**
	 * Create an instance of {@link NewMessagesResolver}, A logger will be created
	 * by {@link LogManager}
	 *
	 * @param dbController
	 *            A connection with the DB.
	 */
	public NewMessagesResolver(DbController dbController) {
		m_dbController = dbController;
		m_logger = LogManager.getLogger();
	}

	/**
	 * Create an instance of {@link NewMessagesResolver}.
	 *
	 * @param logger
	 *            A logger that the instance will log to it.
	 * @param dbController
	 *            A connection with the DB.
	 */
	public NewMessagesResolver(Logger logger, DbController dbController) {
		m_dbController = dbController;
		m_logger = logger;
	}
	// end region -> Constructors

	// region private Methods

	private <TData extends IEntity> IMessageData executeSelectQuery(String selectQuery, Class<TData> expectedType) {
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			if (queryResult == null) {
				return null;
			}
			List<IEntity> itemEntities = NewEntitiesResolver.ResultSetToEntity(queryResult, expectedType);
			if (itemEntities == null) {
				return null;
			}
			return itemEntities.isEmpty() ? null : new EntitiesListData(EntityDataOperation.None, itemEntities);

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
		String lastIdQuery = QueryGenerator.getLastIdQuery();
		if (lastIdQuery == null) {
			return -1;
		}
		ResultSet queryResult = null;
		int returningValue = -1;
		try {
			queryResult = m_dbController.executeSelectQuery(lastIdQuery);
			if (queryResult != null) {
				queryResult.next();
				returningValue = queryResult.getInt(0);
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
		}

		if (returnedMessageData != null) {
			msg.setMessageData(returnedMessageData);
			return msg;
		}

		return null;
	}

	// end region -> Server.MessagesHandler Implementation

	private IMessageData onLoginDataReceived(LoginData loginData) {
		User user = new User();
		user.setUserName(loginData.getUserName());
		IMessageData userEntityData = onUserEntityReceived(user, EntityDataOperation.Get);

		if (userEntityData instanceof RespondMessageData) {
			loginData.setMessage("The username does not exist.");
			return loginData;
		}

		IEntity entity = ((EntityData) userEntityData).getEntity();
		User userEntity = (User) entity;
		if (!loginData.getPassword().equals(userEntity.getPassword())) {
			loginData.setMessage("The password does not match!");
			return loginData;
		}

		if (loginData.isLogoutMessage()) {
			if (userEntity.getStatus() == UserStatus.Connected) {
				userEntity.setStatus(UserStatus.Disconnected);
				onUserEntityReceived(userEntity, EntityDataOperation.Update);
			}
			return null;
		}

		if (userEntity.getStatus() == UserStatus.Disconnected) {
			// TODO ROMAN - uncomment it when disconnected message will arrive.
			// userEntity.setUserStatus(UserStatus.Connected);
			// onUserEntityReceived(userEntity, EntityDataOperation.Update);
			// userEntity.setUserStatus(UserStatus.Disconnected);
		}

		return userEntityData;
	}

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

	private IMessageData onEntityDataReceived(EntityData entityData) {
		IEntity receivedEntity = entityData.getEntity();

		if (receivedEntity == null) {
			return null;
		}

		IMessageData returnedMessageData = null;
		if (receivedEntity instanceof User) {
			returnedMessageData = onUserEntityReceived((User) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof Item) {
			returnedMessageData = onItemEntityReceived((Item) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof Costumer) {
			returnedMessageData = onCostumerEntityReceived((Costumer) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof Reservation) {
			returnedMessageData = onReservationEntityReceived((Reservation) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof ShopManager) {
			returnedMessageData = onShopManagerEntityReceived((ShopManager) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof ShopEmployee) {
			returnedMessageData = onShopEmployeeEntityReceived((ShopEmployee) receivedEntity,
					entityData.getOperation());
		} else if (receivedEntity instanceof ItemInReservation) {
			returnedMessageData = onItemInReservationEntityReceived((ItemInReservation) receivedEntity,
					entityData.getOperation());
		} else if (receivedEntity instanceof ItemInShop) {
			returnedMessageData = onItemInShopEntityReceived((ItemInShop) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof Complaint) {
			returnedMessageData = onComplainteEntityReceived((Complaint) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof Survey) {
			returnedMessageData = onSurveyEntityReceived((Survey) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof ComplaintsReport) {
			returnedMessageData = onComplaintsReportEntityReceived((ComplaintsReport) receivedEntity,
					entityData.getOperation());
		} else if (receivedEntity instanceof SurveysReport) {
			returnedMessageData = onSurveysReportEntityReceived((SurveysReport) receivedEntity,
					entityData.getOperation());
		} else if (receivedEntity instanceof IncomesReport) {
			returnedMessageData = onIncomesReportEntityReceived((IncomesReport) receivedEntity,
					entityData.getOperation());
		} else if (receivedEntity instanceof ReservationsReport) {
			returnedMessageData = onReservationsReportEntityReceived((ReservationsReport) receivedEntity,
					entityData.getOperation());
		}
		if (returnedMessageData != null && returnedMessageData instanceof RespondMessageData) {
			((RespondMessageData) returnedMessageData).setMessageData(entityData);
		}

		return returnedMessageData;
	}

	// end region -> Server.MessagesHandler Implementation

	// region Item Entity Operations

	private IMessageData onItemEntityReceived(Item item, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectItemQuery(item);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, Item.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllItemsQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, Item.class);
			}
			break;
		case Update:
			result = updateItemEntityExecution(item);
			break;
		case Remove:
			String removeItemQuery = QueryGenerator.removeItemQuery(item);
			if (removeItemQuery != null) {
				result = executeQuery(item, removeItemQuery);
			}
			break;
		case Add:
			result = addItemEntityExecution(item);
			if (result) {
				int lastInsertId = getLastInsertId();
				item.setId(lastInsertId);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + item.toString() + ", Operation: "
					+ operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	private boolean addItemEntityExecution(Item item) {

		String insertItemQuery = QueryGenerator.insertItemQuery(item);
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
		String updateItemQuery = QueryGenerator.updateItemQuery(item);
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
			String selectQuery = QueryGenerator.selectUserQuery(user);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, User.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllUsersQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, User.class);
			}
			break;
		case Update:
			String updateQuery = QueryGenerator.updateUserQuery(user);
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

	// region Costumer Entity Operations

	private IMessageData onCostumerEntityReceived(Costumer costumer, EntityDataOperation operation) {

		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectCostumerQuery(costumer);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, Costumer.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllCostumersQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, Costumer.class);
			}
			break;
		case Update:
			String updateQuery = QueryGenerator.updateCostumerQuery(costumer);
			if (updateQuery != null) {
				result = executeQuery(costumer, updateQuery);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + costumer.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> Costumer Entity Operations

	// region Reservation Entity Operations

	private IMessageData onReservationEntityReceived(Reservation reservation, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectReservationQuery(reservation);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, Reservation.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllReservationsQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, Reservation.class);
			}
			break;
		case Update:
			String updateQuery = QueryGenerator.updateReservationQuery(reservation);
			if (updateQuery != null) {
				result = executeQuery(reservation, updateQuery);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertReservationQuery(reservation);
			if (insertQuery != null) {
				result = executeQuery(reservation, insertQuery);
			}
			if (result) {
				int lastInsertId = getLastInsertId();
				reservation.setId(lastInsertId);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + reservation.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> Reservation Entity Operations

	// region Survey Entity Operations

	private IMessageData onSurveyEntityReceived(Survey survey, EntityDataOperation operation) {

		boolean result = false;
		switch (operation) {
		case Add:
			String insertQuery = QueryGenerator.insertSurveyQuery(survey);
			if (insertQuery != null) {
				result = executeQuery(survey, insertQuery);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + survey.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> Survey Entity Operations

	// region Complaint Entity Operations

	private IMessageData onComplainteEntityReceived(Complaint complaint, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectComplaintQuery(complaint);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, Complaint.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllComplaintsQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, Complaint.class);
			}
			break;
		case Update:
			String updateQuery = QueryGenerator.updateComplaintQuery(complaint);
			if (updateQuery != null) {
				result = executeQuery(complaint, updateQuery);
			}
			break;

		case Add:
			String insertQuery = QueryGenerator.insertComplaintQuery(complaint);
			if (insertQuery != null) {
				result = executeQuery(complaint, insertQuery);
			}
			if (result) {
				int lastInsertId = getLastInsertId();
				complaint.setId(lastInsertId);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + complaint.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> Complaint Entity Operations

	// region ItemInShop Entity Operations

	private IMessageData onItemInShopEntityReceived(ItemInShop itemInShop, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllItemsInShopsQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, ItemInShop.class);
			}
			break;
		case Update:
			String updateQuery = QueryGenerator.updateItemInShopQuery(itemInShop);
			if (updateQuery != null) {
				result = executeQuery(itemInShop, updateQuery);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertItemInShopQuery(itemInShop);
			if (insertQuery != null) {
				result = executeQuery(itemInShop, insertQuery);
			}
			break;

		case Remove:
			String removeQuery = QueryGenerator.removeItemInShopQuery(itemInShop);
			if (removeQuery != null) {
				result = executeQuery(itemInShop, removeQuery);
			}
			break;

		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + itemInShop.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> Reservation ItemInShop Operations

	// region ItemInReservation Entity Operations

	private IMessageData onItemInReservationEntityReceived(ItemInReservation itemInReservation,
			EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case GetALL:
			String selectAllQuery = QueryGenerator.selectAllIItemsInReservationQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, ItemInReservation.class);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertItemInReservationQuery(itemInReservation);
			if (insertQuery != null) {
				result = executeQuery(itemInReservation, insertQuery);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + itemInReservation.toString()
					+ ", Operation: " + operation.toString());
			break;
		}

		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> ItemInReservation Entity Operations

	// region ShopEmployee Entity Operations

	private IMessageData onShopEmployeeEntityReceived(ShopEmployee shopEmployee, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectShopEmployeeQuery(shopEmployee);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, ShopEmployee.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectShopEmployeesQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, ShopEmployee.class);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + shopEmployee.toString()
					+ ", Operation: " + operation.toString());
			break;
		}
		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> ShopEmployee Entity Operations

	// region ShopManager Entity Operations

	private IMessageData onShopManagerEntityReceived(ShopManager shopManager, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectShopManagerQuery(shopManager);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, ShopManager.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectShopManagersQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, ShopManager.class);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + shopManager.toString()
					+ ", Operation: " + operation.toString());
			break;
		}
		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> ShopManager Entity Operations

	// region ReservationsReport Entity Operations

	private IMessageData onReservationsReportEntityReceived(ReservationsReport reservationsReport,
			EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectReservationsReportQuery(reservationsReport);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, ShopManager.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectReservationsReportsQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, ShopManager.class);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertReservationsReportQuery(reservationsReport);
			if (insertQuery != null) {
				result = executeQuery(reservationsReport, insertQuery);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + reservationsReport.toString()
					+ ", Operation: " + operation.toString());
			break;
		}
		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> Reservation Entity Operations

	// region IncomesReport Entity Operations

	private IMessageData onIncomesReportEntityReceived(IncomesReport incomesReport, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectIncomesReportQuery(incomesReport);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, IncomesReport.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectIncomesReportQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, IncomesReport.class);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertIncomesReportQuery(incomesReport);
			if (insertQuery != null) {
				result = executeQuery(incomesReport, insertQuery);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + incomesReport.toString()
					+ ", Operation: " + operation.toString());
			break;
		}
		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> IncomesReport Entity Operations

	// region SurveysReport Entity Operations

	private IMessageData onSurveysReportEntityReceived(SurveysReport surveysReport, EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectSurveysReportQuery(surveysReport);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, SurveysReport.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectSurveysReportQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, SurveysReport.class);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertSurveysReportQuery(surveysReport);
			if (insertQuery != null) {
				result = executeQuery(surveysReport, insertQuery);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + surveysReport.toString()
					+ ", Operation: " + operation.toString());
			break;
		}
		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> SurveysReport Entity Operations

	// region ComplaintsReport Entity Operations

	private IMessageData onComplaintsReportEntityReceived(ComplaintsReport complaintsReport,
			EntityDataOperation operation) {
		boolean result = false;
		switch (operation) {
		case Get:
			String selectQuery = QueryGenerator.selectComplaintsReporttQuery(complaintsReport);
			if (selectQuery != null) {
				return executeSelectQuery(selectQuery, ComplaintsReport.class);
			}
			break;
		case GetALL:
			String selectAllQuery = QueryGenerator.selectComplaintsReportQuery();
			if (selectAllQuery != null) {
				return executeSelectQuery(selectAllQuery, ComplaintsReport.class);
			}
			break;
		case Add:
			String insertQuery = QueryGenerator.insertComplaintsReportQuery(complaintsReport);
			if (insertQuery != null) {
				result = executeQuery(complaintsReport, insertQuery);
			}
			break;
		default:
			m_logger.warning("Received unsupported opertaion for IEntity! Entity: " + complaintsReport.toString()
					+ ", Operation: " + operation.toString());
			break;
		}
		RespondMessageData respondMessageData = new RespondMessageData();
		respondMessageData.setSucceed(result);
		return respondMessageData;
	}

	// end region -> ComplaintsReport Entity Operations

}
