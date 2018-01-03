package db;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import connectivity.Server;
import entities.CostumerEntity;
import entities.IEntity;
import entities.ItemEntity;
import entities.ReservationEntity;
import entities.UserEntity;
import entities.UserStatus;
import logger.LogManager;
import messages.EntitiesListData;
import messages.EntityData;
import messages.EntityDataCollection;
import messages.EntityDataOperation;
import messages.IMessageData;
import messages.LoginData;
import messages.Message;

/**
 *
 * MessagesResolver: An class that can resolve a {@link Message} to operation in
 * the DB.
 * 
 * @see connectivity.Server.MessagesHandler
 */
public class MessagesResolver implements Server.MessagesHandler {

	// region Fields

	private final static java.text.SimpleDateFormat s_dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

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
		IMessageData returnedMessageData = null;
		if (messageData instanceof LoginData) {
			returnedMessageData = onLoginDataReceived((LoginData) messageData);
		} else if (messageData instanceof EntityData) {
			returnedMessageData = onEntityDataReceived(messageData);
		} else if (messageData instanceof EntitiesListData) {

		} else if (messageData instanceof EntityDataCollection) {

		}

		if (returnedMessageData != null) {
			msg.setMessageData(returnedMessageData);
			return msg;
		}

		return null;
	}

	private IMessageData onLoginDataReceived(LoginData loginData) {
		IMessageData userEntityData = onUserEntityReceived(new UserEntity(loginData.getUserName()),
				EntityDataOperation.Get);

		if (userEntityData == null) {
			loginData.setMessage("The username does not exist.");
			return loginData;
		}

		IEntity entity = ((EntityData) userEntityData).getEntity();
		UserEntity userEntity = (UserEntity) entity;
		if (!loginData.getPassword().equals(userEntity.getPassword())) {
			loginData.setMessage("The password does not match!");
			return loginData;
		}

		if (loginData.isLogoutMessage()) {
			if (userEntity.getUserStatus() == UserStatus.Connected) {
				userEntity.setUserStatus(UserStatus.Disconnected);
				onUserEntityReceived(userEntity, EntityDataOperation.Update);
			}
			return null;
		}

		if (userEntity.getUserStatus() == UserStatus.Disconnected) {
			// TODO ROMAN - uncomment it when disconnected message will arrive.
			// userEntity.setUserStatus(UserStatus.Connected);
			// onUserEntityReceived(userEntity, EntityDataOperation.Update);
			// userEntity.setUserStatus(UserStatus.Disconnected);
		}

		return userEntityData;
	}

	private IMessageData onEntityDataReceived(IMessageData messageData) throws Exception {
		EntityData entityData = (EntityData) messageData;
		IEntity receivedEntity = entityData.getEntity();

		if (receivedEntity == null) {
			return null;
		}

		IMessageData returnedMessageData = null;
		if (receivedEntity instanceof UserEntity) {
			returnedMessageData = onUserEntityReceived((UserEntity) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof ItemEntity) {
			returnedMessageData = onItemEntityReceived((ItemEntity) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof CostumerEntity) {
			returnedMessageData = onCosutmerEntityReceived((CostumerEntity) receivedEntity, entityData.getOperation());
		} else if (receivedEntity instanceof ReservationEntity) {
			returnedMessageData = onReservationEntityReceived((ReservationEntity) receivedEntity,
					entityData.getOperation());
		}

		return returnedMessageData;

	}

	// end region -> Server.MessagesHandler Implementation

	// region ItemEntity Operations

	private IMessageData onItemEntityReceived(ItemEntity itemEntity, EntityDataOperation operation) {
		switch (operation) {
		case Get:
			return getItemEntityExecution(itemEntity);
		case GetALL:
			return getAllItemEntitiesExecution();
		case Update:
			updateItemEntityExecution(itemEntity);
			break;
		case Remove:
			removeItemEntityExecution(itemEntity);
			break;
		case Add:
			addItemEntityExecution(itemEntity);
			break;

		case UpdateAll:
		default:
			break;
		}

		return null;
	}

	private boolean addItemEntityExecution(ItemEntity itemEntity) {
		try {
			String query = "INSERT INTO items (iName,iType,iPrice,iImage,iDomainColor) VALUES (?,?,?,?,?);";
			PreparedStatement preparedStatement = m_dbController.getPreparedStatement(query);
			preparedStatement.setString(1, itemEntity.getName().toLowerCase());
			preparedStatement.setString(2, itemEntity.getItemType().toString());
			preparedStatement.setFloat(3, itemEntity.getItemPrice().floatValue());

			InputStream inputStream = itemEntity.getItemImageInputStream();
			if (inputStream != null) {
				preparedStatement.setBlob(4, inputStream);
			} else {
				preparedStatement.setNull(4, java.sql.Types.BLOB);
			}
			preparedStatement.setString(5, itemEntity.getColor().toLowerCase());
			if (preparedStatement.executeUpdate() == 0) {
				m_logger.info("The query: \"" + preparedStatement.toString() + "\" does not effect any row.");
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			m_logger.warning("Failed on try to create\\execute insert query of: " + itemEntity.toString()
					+ ", exception: " + e.getMessage());
		}
		return false;

	}

	private boolean removeItemEntityExecution(ItemEntity itemEntity) {
		int id = itemEntity.getId();
		if (id < 1) { // Wrong ID
			m_logger.warning("Received request to remove item entity with impossiable id: " + id);
			return false;
		}

		String removeQuery = "UPDATE items SET iDeleted = 1 WHERE iId = " + id + " ;";
		return m_dbController.executeQuery(removeQuery);
	}

	private boolean updateItemEntityExecution(ItemEntity itemEntity) {
		try {
			String query = "UPDATE items SET iName = ? , iType = ? , iPrice = ? , iImage = ?, iDomainColor = ? WHERE iId = ?";
			PreparedStatement preparedStatement = m_dbController.getPreparedStatement(query);
			preparedStatement.setString(1, itemEntity.getName().toLowerCase());
			preparedStatement.setString(2, itemEntity.getItemType().toString());
			preparedStatement.setFloat(3, itemEntity.getItemPrice().floatValue());

			InputStream inputStream = itemEntity.getItemImageInputStream();
			if (inputStream != null) {
				preparedStatement.setBlob(4, inputStream);
			} else {
				preparedStatement.setNull(4, java.sql.Types.BLOB);
			}
			preparedStatement.setString(5, itemEntity.getColor().toLowerCase());
			preparedStatement.setInt(6, itemEntity.getId());

			if (preparedStatement.executeUpdate() == 0) {
				m_logger.info("The query: \"" + preparedStatement.toString() + "\" does not effect any row.");
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			m_logger.warning("Failed on try to create\\execute update query of: " + itemEntity.toString()
					+ ", exception: " + e.getMessage());
		}
		return false;
	}

	private IMessageData getAllItemEntitiesExecution() {

		String selectQuery = "SELECT * FROM items WHERE iDeleted = 0;";
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			List<IEntity> itemEntities = EntitiesResolver.ResultSetToItemEntities(queryResult);
			if (itemEntities == null) {
				return null;
			}
			return itemEntities.isEmpty() ? null : new EntitiesListData(EntityDataOperation.None, itemEntities);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get all item entities request! query: " + selectQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}
		return null;
	}

	private IMessageData getItemEntityExecution(ItemEntity itemEntity) {
		int id = itemEntity.getId();
		if (id < 1) { // Wrong ID
			m_logger.warning("Received request to get item entity with impossiable id: " + id);
			return null;
		}

		String selectQuery = "SELECT * FROM items WHERE iId = " + id + ';';
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			List<IEntity> itemEntities = EntitiesResolver.ResultSetToItemEntities(queryResult);
			if (itemEntities == null) {
				return null;
			}
			return new EntityData(EntityDataOperation.None, itemEntities.get(0));

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get item entity request! query: " + selectQuery + ", exception: "
					+ e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}

		return null;
	}

	// end region -> ItemEntity Operations

	// region UserEntity Operations

	private IMessageData onUserEntityReceived(UserEntity userEntity, EntityDataOperation operation) {
		switch (operation) {
		case Get:
			return getUserEntityExecution(userEntity);
		case GetALL:
			return getAllUserEntitiesExecution();
		case Update:
			updateUserEntityExecution(userEntity);
			break;
		default:
			break;
		}
		return null;
	}

	private IMessageData getAllUserEntitiesExecution() {
		String selectQuery = "SELECT * FROM users;";
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			List<IEntity> userEntities = EntitiesResolver.ResultSetToUserEntities(queryResult);
			if (userEntities == null) {
				return null;
			}
			return userEntities.isEmpty() ? null : new EntitiesListData(EntityDataOperation.None, userEntities);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get all user entities request! query: " + selectQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}
		return null;
	}

	private IMessageData getUserEntityExecution(UserEntity userEntity) {
		String userName = userEntity.getUserName();
		if (userName == null || userName.isEmpty()) {
			m_logger.warning("Received request to get user entity with null or empty username");
			return null;
		}

		String selectQuery = "SELECT * FROM users WHERE uUserName = '" + userName + "';";
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			List<IEntity> usersEntities = EntitiesResolver.ResultSetToUserEntities(queryResult);
			if (usersEntities == null) {
				return null;
			}
			return new EntityData(EntityDataOperation.None, usersEntities.get(0));

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get user entities request! query: " + selectQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}

		return null;
	}

	private boolean updateUserEntityExecution(UserEntity userEntity) {
		if (userEntity == null) {
			return false;
		}

		String userName = userEntity.getUserName();
		if (!(userName != null && !userName.isEmpty())) {
			return false;
		}
		try {

			String query = "UPDATE users SET uPassword = \'" + userEntity.getPassword() + "\' , uEmail = \'"
					+ userEntity.getEmail() + "\' , uPrivilege = \'" + userEntity.getUserPrivilege()
					+ "\' , uStatus = \'" + userEntity.getUserStatus() + "\' WHERE uUserName = \'" + userName + "\';";
			return m_dbController.executeQuery(query);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute update query of: " + userEntity.toString() + ", exception: "
					+ e.getMessage());
		}
		return false;
	}

	// end region -> UserEntity Operations

	// region -> CostumerEntity Operations

	private IMessageData onCosutmerEntityReceived(CostumerEntity costumerEntity, EntityDataOperation operation) {
		switch (operation) {
		case Get:
			return getCostumerEntityExecution(costumerEntity);
		case GetALL:
			return getAllCostumerEntitiesExecution();
		case Update:
			updateCostumerEntityExecution(costumerEntity);
			break;
		default:
			break;
		}
		return null;
	}

	private IMessageData getCostumerEntityExecution(CostumerEntity costumer) {
		UserEntity userEntity = costumer.getUserEntity();
		Integer costumerId = costumer.getId();
		if (userEntity == null && costumerId == null) {
			m_logger.warning("Received request to get costumer entity without any identification data.");
			return null;
		}

		String selectQuery = null;
		if (!(costumerId != null && costumerId >= 1)) {
			String userName = userEntity.getUserName();
			if (!(userName != null && !userName.isEmpty())) {
				m_logger.warning("Received request to get costumer entity without any identification data.");
				return null;
			}
			selectQuery = "SELECT * FROM costumers WHERE uUserName = '" + userName + "';";
		} else {
			selectQuery = "SELECT * FROM costumers WHERE cid = " + costumerId + ";";
		}

		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			List<IEntity> costumersEntities = EntitiesResolver.ResultSetToCostumerEntities(queryResult);
			if (costumersEntities == null) {
				return null;
			}
			return new EntityData(EntityDataOperation.None, costumersEntities.get(0));

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get costumer entities request! query: " + selectQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}

		return null;
	}

	private IMessageData getAllCostumerEntitiesExecution() {
		String selectQuery = "SELECT * FROM costumers;";
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			List<IEntity> costumerEntities = EntitiesResolver.ResultSetToCostumerEntities(queryResult);
			if (costumerEntities == null) {
				return null;
			}
			return costumerEntities.isEmpty() ? null : new EntitiesListData(EntityDataOperation.None, costumerEntities);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get all costumer entities request! query: " + selectQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}
		return null;
	}

	private boolean updateCostumerEntityExecution(CostumerEntity costumerEntity) {
		if (costumerEntity == null) {
			return false;
		}

		Integer id = costumerEntity.getId();
		if (!(id != null && id >= 1)) {
			return false;
		}
		try {

			String query = "UPDATE costumers SET cCreditCard = \'" + costumerEntity.getCreditCard()
					+ "\' , cCostumerSubscription = \'" + costumerEntity.getSubscription().toString()
					+ "\' , uRefund = " + costumerEntity.getCostumerRefunds() + " WHERE cId = " + id + ';';
			return m_dbController.executeQuery(query);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute update query of: " + costumerEntity.toString() + ", exception: "
					+ e.getMessage());
		}
		return false;
	}

	// end region

	// region -> CostumerEntity Operations

	private IMessageData onReservationEntityReceived(ReservationEntity reservationEntity,
			EntityDataOperation operation) {
		switch (operation) {
		case Get:
			return getReservationEntityExecution(reservationEntity);
		case GetALL:
			return getAllReservationEntitiesExecution();
		case Update:
			updateReservationEntityExecution(reservationEntity);
			break;
		case Add:
			addReservationEntityExecution(reservationEntity);
			break;
		default:
			break;
		}
		return null;
	}

	private IMessageData getReservationEntityExecution(ReservationEntity reservation) {
		int id = reservation.getId();
		if (id < 1) { // Wrong ID
			m_logger.warning("Received request to get reservation entity with impossiable id: " + id);
			return null;
		}

		String selectQuery = "SELECT * FROM reservations WHERE rId = " + id + ';';
		ResultSet queryResult = null;

		try {
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			queryResult.next();

			IEntity entity = createReservationEntity(queryResult);

			return new EntityData(EntityDataOperation.None, entity);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get reservation entity request! query: " + selectQuery
					+ ", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:" + selectQuery
							+ ". Exception: " + e.getMessage());
				}
			}
		}

		return null;
	}

	private ReservationEntity createReservationEntity(ResultSet resultSet) {
		String[] costumerId = new String[1];
		ArrayList<String> itemsList = new ArrayList<>();
		ReservationEntity entity = (ReservationEntity) EntitiesResolver.ResultSetToReservationEntities(resultSet,
				costumerId, itemsList);
		if (entity == null) {
			return null;
		}

		int costumerIdInt = Integer.parseInt(costumerId[0]);
		CostumerEntity costumerEntity = new CostumerEntity(costumerIdInt);
		EntityData costumerEntityData = (EntityData) getCostumerEntityExecution(costumerEntity);

		costumerEntity = (CostumerEntity) (costumerEntityData.getEntity());
		entity.setCostumer(costumerEntity);

		for (String itemID : itemsList) {
			int intId = Integer.parseInt(itemID);
			ItemEntity itemEntity = new ItemEntity(intId);
			EntityData itemEntityData = (EntityData) getItemEntityExecution(itemEntity);
			itemEntity = (ItemEntity) itemEntityData.getEntity();
			entity.addReservation(itemEntity);
		}

		return entity;
	}

	private IMessageData getAllReservationEntitiesExecution() {
		String selectQuery = "SELECT * FROM reservations;";
		ResultSet queryResult = null;

		try {
			List<IEntity> reservationEntities = new ArrayList<>();
			queryResult = m_dbController.executeSelectQuery(selectQuery);
			while (queryResult.next()) {
				IEntity entity = createReservationEntity(queryResult);
				reservationEntities.add(entity);
			}
			return reservationEntities.isEmpty() ? null
					: new EntitiesListData(EntityDataOperation.None, reservationEntities);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute get all reservation entities request!query: \"" + selectQuery
					+ "\", exception: " + e.getMessage());
		} finally {
			if (queryResult != null) {
				try {
					queryResult.close();
				} catch (SQLException e) {
					m_logger.warning("Failed on try to close the ResultSet of the executed query:\"" + selectQuery
							+ "\". Exception: " + e.getMessage());
				}
			}
		}
		return null;
	}

	private boolean updateReservationEntityExecution(ReservationEntity reservationEntity) {
		if (reservationEntity == null) {
			return false;
		}

		Integer reservationId = reservationEntity.getId();
		Integer costumerId = reservationEntity.getCostumerId();

		if (!(reservationId != null && reservationId >= 1) || !(costumerId != null && costumerId >= 1)) {
			return false;
		}

		StringBuilder stringBuilder = new StringBuilder();
		ArrayList<ItemEntity> reservationList = reservationEntity.getReservationList();
		for (int i = 0, size = reservationList.size(); i < size; i++) {
			ItemEntity item = reservationList.get(i);
			stringBuilder.append(item.getId());
			if (i - 1 == size) {
				stringBuilder.append(',');
			}
		}

		String date = s_dateFormat.format(reservationEntity.getDeliveryDate());

		try {
			String query = "UPDATE reservations SET rType = '" + reservationEntity.getType() + "' , rItems = '"
					+ stringBuilder.toString() + "', rPrice = " + reservationEntity.getTotalPrice()
					+ ", rDeliveryDate = '" + date + "', rDeliveryType = '" + reservationEntity.getDeliveryType()
					+ "', BlessingCard = '" + reservationEntity.getBlessingCard() + "' WHERE cId = " + costumerId
					+ " AND rid = " + reservationId + ';';
			return m_dbController.executeQuery(query);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute update query of: " + reservationEntity.toString()
					+ ", exception: " + e.getMessage());
		}
		return false;
	}

	private boolean addReservationEntityExecution(ReservationEntity reservationEntity) {
		if (reservationEntity == null) {
			return false;
		}

		Integer costumerId = reservationEntity.getCostumerId();

		if (!(costumerId != null && costumerId >= 1)) {
			return false;
		}

		StringBuilder stringBuilder = new StringBuilder();
		ArrayList<ItemEntity> reservationList = reservationEntity.getReservationList();
		for (int i = 0, size = reservationList.size(); i < size; i++) {
			ItemEntity item = reservationList.get(i);
			stringBuilder.append(item.getId());
			if (i < size - 1) {
				stringBuilder.append(',');
			}
		}

		String date = s_dateFormat.format(reservationEntity.getDeliveryDate());

		try {
			String query = "INSERT INTO reservations (cId,rType,rItems,rPrice,rDeliveryDate,rDeliveryType,rBlessingCard) VALUES ("
					+ costumerId + ", '" + reservationEntity.getType() + "', '" + stringBuilder.toString() + "', "
					+ reservationEntity.getTotalPrice() + ", '" + date + "', '" + reservationEntity.getDeliveryType()
					+ "', '" + reservationEntity.getBlessingCard() + "' );";
			return m_dbController.executeQuery(query);

		} catch (Exception e) {
			m_logger.warning("Failed on try to execute update query of: " + reservationEntity.toString()
					+ ", exception: " + e.getMessage());
		}
		return false;
	}
	// end region
}
