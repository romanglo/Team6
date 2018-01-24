package db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

import entities.Complaint;
import entities.ComplaintsReport;
import entities.Costumer;
import entities.CostumerServiceEmployee;
import entities.IEntity;
import entities.IncomesReport;
import entities.Item;
import entities.ItemInReservation;
import entities.ItemInShop;
import entities.Reservation;
import entities.ReservationsReport;
import entities.ShopCostumer;
import entities.ShopEmployee;
import entities.ShopManager;
import entities.Survey;
import entities.SurveyResult;
import entities.SurveysReport;
import entities.User;
import logger.LogManager;

/**
 *
 * QueryGenerator: A factory of queries based on {@link IEntity}. It is
 * impossible to create an instance of this class, all the methods in this class
 * is static.
 */
public class QueryGenerator {

	// region Constructor

	/**
	 * The constructor is empty to ensure that it will not be possible to create
	 * instance of this class.
	 */
	private QueryGenerator() {

	}

	// end region -> Constructors

	// region Fields

	private final static SimpleDateFormat s_yearFormat = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat s_dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat s_dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private static Logger s_logger = null;

	// end region -> Fields

	// region Public Methods

	// region Stored Procedures Queries

	/**
	 * @return A query which calling to stored procedure that returns the ID of the
	 *         last inserted row.
	 */
	public static String generateLastIdQuery() {
		return "SELECT LAST_INSERT_ID();";
	}

	/**
	 * @param shopManagerId
	 *            The ID of the required shop catalog.
	 * @return A query which calling to stored procedure that returns catalog of a
	 *         specific shop.
	 */
	public static String generateShopCatalogQuery(int shopManagerId) {
		return "CALL getShopCatalog(" + shopManagerId + ");";
	}

	/**
	 * @return A query which calling to stored procedure that checks the costumers
	 *         in shop subscription expire Date.
	 * 
	 */
	public static String generateCheckCostumersSubscriptionQuery() {
		return "CALL checkCostumersSubsription();";
	}

	/**
	 * @param shopManagerId
	 *            The ID of the required shop incomes report.
	 * @param year
	 *            the year of the required incomes report.
	 * @param querter
	 *            the quarter of the required incomes report.
	 * @return A query which calling to stored procedure that returns incomes report
	 *         of a specific shop.
	 */
	public static String generateIncomesReportQuery(int shopManagerId, String year, int querter) {
		return "CALL generateIncomesReport(" + shopManagerId + ",'" + year + "'," + querter + ")";
	}

	/**
	 * @param shopManagerId
	 *            The ID of the required shop reservations report.
	 * @param year
	 *            the year of the required reservations report.
	 * @param querter
	 *            the quarter of the required reservations report.
	 * @return A query which calling to stored procedure that returns reservations
	 *         report of a specific shop.
	 */
	public static String generateReservationsReportQuery(int shopManagerId, String year, int querter) {
		return "CALL generateReservationsReport(" + shopManagerId + ",'" + year + "'," + querter + ")";
	}

	/**
	 * @param shopManagerId
	 *            The ID of the required shop complaints report.
	 * @param year
	 *            the year of the required complaints report.
	 * @param querter
	 *            the quarter of the required complaints report.
	 * @return A query which calling to stored procedure that returns complaints
	 *         report of a specific shop.
	 */
	public static String generateComplaintsReportQuery(int shopManagerId, String year, int querter) {
		return "CALL generateComplaintsReport(" + shopManagerId + ",'" + year + "'," + querter + ")";
	}

	/**
	 * @param shopManagerId
	 *            The ID of the required shop surveys report.
	 * @param year
	 *            the year of the required surveys report.
	 * @param querter
	 *            the quarter of the required surveys report.
	 * @return A query which calling to stored procedure that returns surveys report
	 *         of a specific shop.
	 */
	public static String generateSurveysReportQuery(int shopManagerId, String year, int querter) {
		return "CALL generateSurveysReport(" + shopManagerId + ",'" + year + "'," + querter + ")";
	}

	/**
	 * @return A query which set all 'Connected' {@link User}s status to
	 *         'Disconnected'.
	 */
	public static String generateUpdateAllUsersToDisconnectQuery() {
		return "UPDATE users SET uStatus = 'Disconnected' WHERE uStatus = 'Connected' ;";
	}

	/**
	 * 
	 * Generate "INSERT" query by the received {@link IEntity} type and states.
	 *
	 * @param <TData>
	 *            An instance which implements {@link IEntity}.
	 * @param entity
	 *            An {@link IEntity} of the requested type.
	 * @return A "INSERT" query if the type is supported and all the necessary
	 *         states exists, and <code>null</code> id does not.
	 */
	public static <TData extends IEntity> String generateInsertQuery(TData entity) {
		if (entity == null) {
			logMessage("generateInsertQuery method received null parameter.");
			return null;
		}

		Class<? extends IEntity> entityType = entity.getClass();
		if (entityType.equals(Item.class)) {
			return insertItemQuery((Item) entity);
		}
		if (entityType.equals(Reservation.class)) {
			return insertReservationQuery((Reservation) entity);
		}
		if (entityType.equals(Complaint.class)) {
			return insertComplaintQuery((Complaint) entity);
		}
		if (entityType.equals(ItemInShop.class)) {
			return insertItemInShopQuery((ItemInShop) entity);
		}
		if (entityType.equals(ItemInReservation.class)) {
			return insertItemInReservationQuery((ItemInReservation) entity);
		}
		if (entityType.equals(ShopCostumer.class)) {
			return insertShopCostumerQuery((ShopCostumer) entity);
		}
		if (entityType.equals(Survey.class)) {
			return insertSurveyQuery((Survey) entity);
		}
		if (entityType.equals(SurveyResult.class)) {
			return insertSurveyResultQuery((SurveyResult) entity);
		}
		logMessage("generateInsertQuery method received unsupported entity, type: " + entity.getClass().getName());
		return null;
	}

	/**
	 * 
	 * Generate "DELETE" query by the received {@link IEntity} type and states.
	 *
	 * @param <TData>
	 *            An instance which implements {@link IEntity}.
	 * @param entity
	 *            An {@link IEntity} of the requested type.
	 * @return A "DELETE" query if the type is supported and all the necessary
	 *         states exists, and <code>null</code> id does not.
	 */
	public static <TData extends IEntity> String generateDeleteQuery(@NotNull TData entity) {
		if (entity == null) {
			logMessage("generateDeleteQuery method received null parameter.");
			return null;
		}

		Class<? extends IEntity> entityType = entity.getClass();
		if (entityType.equals(Item.class)) {
			return deleteItemQuery((Item) entity);
		}
		if (entityType.equals(ItemInShop.class)) {
			return deleteItemInShopQuery((ItemInShop) entity);
		}
		if (entityType.equals(ShopCostumer.class)) {
			return deleteShopCostumerQuery((ShopCostumer) entity);
		}
		logMessage("generateDeleteQuery method received unsupported entity, type: " + entity.getClass().getName());
		return null;
	}

	/**
	 * 
	 * Generate "SELECT" query by the received {@link IEntity} type and states.
	 *
	 * @param <TData>
	 *            An instance which implements {@link IEntity}.
	 * @param entity
	 *            An {@link IEntity} of the requested type.
	 * @return A "SELECT" query if the type is supported and all the necessary
	 *         states exists, and <code>null</code> id does not.
	 */
	public static <TData extends IEntity> String generateSelectAllQuery(@NotNull TData entity) {
		if (entity == null) {
			logMessage("generateSelectAllQuery method received null parameter.");
			return null;
		}

		Class<? extends IEntity> entityType = entity.getClass();
		if (entityType.equals(Item.class)) {
			return selectAllItemQuery();
		}
		if (entityType.equals(Reservation.class)) {
			return selectAllReservationQuery();
		}
		if (entityType.equals(Complaint.class)) {
			return selectAllComplaintQuery((Complaint) entity);
		}
		if (entityType.equals(ComplaintsReport.class)) {
			return selectAllComplaintsReportQuery((ComplaintsReport) entity);
		}
		if (entityType.equals(Costumer.class)) {
			return selectAllCostumerQuery();
		}
		if (entityType.equals(CostumerServiceEmployee.class)) {
			return selectAllCostumerServiceEmployeeQuery();
		}
		if (entityType.equals(IncomesReport.class)) {
			return selectAllIncomesReportQuery((IncomesReport) entity);
		}
		if (entityType.equals(ReservationsReport.class)) {
			return selectAllReservationsReportQuery((ReservationsReport) entity);
		}
		if (entityType.equals(ShopCostumer.class)) {
			return selectAllShopCostumerQuery((ShopCostumer) entity);
		}
		if (entityType.equals(ShopEmployee.class)) {
			return selectAllShopEmployeeQuery();
		}
		if (entityType.equals(ShopManager.class)) {
			return selectAllShopManagerQuery();
		}
		if (entityType.equals(Survey.class)) {
			return selectAllSurveyQuery();
		}
		if (entityType.equals(SurveyResult.class)) {
			return selectAllSurveyResultQuery();
		}
		if (entityType.equals(SurveysReport.class)) {
			return selectAllSurveysReportQuery((SurveysReport) entity);
		}
		if (entityType.equals(User.class)) {
			return selectAllUserQuery();
		}
		if (entityType.equals(ItemInReservation.class)) {
			return selectAllItemInReservationQuery((ItemInReservation) entity);
		}
		if (entityType.equals(ItemInShop.class)) {
			return selectAllItemInShopQuery((ItemInShop) entity);
		}
		logMessage("generateSelectAllQuery method received unsupported entity, type: " + entity.getClass().getName());
		return null;
	}

	/**
	 * 
	 * Generate "SELECT" query by the received {@link IEntity} type and states, this
	 * query should select only one {@link IEntity} by the {@link IEntity} ID.
	 *
	 * @param <TData>
	 *            An instance which implements {@link IEntity}.
	 * @param entity
	 *            An {@link IEntity} of the requested type.
	 * @return A "SELECT" query if the type is supported and all the necessary
	 *         states exists, and <code>null</code> id does not.
	 */
	public static <TData extends IEntity> String generateSelectQuery(@NotNull TData entity) {
		if (entity == null) {
			logMessage("generateSelectQuery method received null parameter.");
			return null;
		}

		Class<? extends IEntity> entityType = entity.getClass();
		if (entityType.equals(Item.class)) {
			return selectItemQuery((Item) entity);
		}
		if (entityType.equals(Reservation.class)) {
			return selectReservationQuery((Reservation) entity);
		}
		if (entityType.equals(Complaint.class)) {
			return selectComplaintQuery((Complaint) entity);
		}
		if (entityType.equals(ComplaintsReport.class)) {
			return selectComplaintsReportQuery((ComplaintsReport) entity);
		}
		if (entityType.equals(Costumer.class)) {
			return selectCostumerQuery((Costumer) entity);
		}
		if (entityType.equals(CostumerServiceEmployee.class)) {
			return selectCostumerServiceEmployeeQuery((CostumerServiceEmployee) entity);
		}
		if (entityType.equals(IncomesReport.class)) {
			return selectIncomesReportQuery((IncomesReport) entity);
		}
		if (entityType.equals(ReservationsReport.class)) {
			return selectReservationsReportQuery((ReservationsReport) entity);
		}
		if (entityType.equals(ShopCostumer.class)) {
			return selectShopCostumerQuery((ShopCostumer) entity);
		}
		if (entityType.equals(ShopEmployee.class)) {
			return selectShopEmployeeQuery((ShopEmployee) entity);
		}
		if (entityType.equals(ShopManager.class)) {
			return selectShopManagerQuery((ShopManager) entity);
		}
		if (entityType.equals(Survey.class)) {
			return selectSurveyQuery((Survey) entity);
		}
		if (entityType.equals(SurveyResult.class)) {
			return selectSurveyResultQuery((SurveyResult) entity);
		}
		if (entityType.equals(SurveysReport.class)) {
			return selectSurveysReportQuery((SurveysReport) entity);
		}
		if (entityType.equals(User.class)) {
			return selectUserQuery((User) entity);
		}

		logMessage("generateSelectQuery method received unsupported entity, type: " + entity.getClass().getName());
		return null;
	}

	/**
	 * 
	 * Generate "UPDATE" query by the received {@link IEntity} type and states.
	 *
	 * @param <TData>
	 *            An instance which implements {@link IEntity}.
	 * @param entity
	 *            An {@link IEntity} of the requested type.
	 * @return A "UPDATE" query if the type is supported and all the necessary
	 *         states exists, and <code>null</code> id does not.
	 */
	public static <TData extends IEntity> String generateUpdateQuery(@NotNull TData entity) {
		if (entity == null) {
			logMessage("generateUpdateQuery method method received null parameter.");
			return null;
		}

		Class<? extends IEntity> entityType = entity.getClass();
		if (entityType.equals(Item.class)) {
			return updateItemQuery((Item) entity);
		}
		if (entityType.equals(Reservation.class)) {
			return updateReservationQuery((Reservation) entity);
		}
		if (entityType.equals(Complaint.class)) {
			return updateComplaintQuery((Complaint) entity);
		}
		if (entityType.equals(Costumer.class)) {
			return updateCostumerQuery((Costumer) entity);
		}
		if (entityType.equals(ItemInShop.class)) {
			return updateItemInShopQuery((ItemInShop) entity);
		}
		if (entityType.equals(ShopCostumer.class)) {
			return updateShopCostumerQuery((ShopCostumer) entity);
		}
		if (entityType.equals(ShopEmployee.class)) {
			return updateShopEmployeeQuery((ShopEmployee) entity);
		}
		if (entityType.equals(ShopManager.class)) {
			return updateShopManagerQuery((ShopManager) entity);
		}
		if (entityType.equals(Survey.class)) {
			return updateSurveyQuery((Survey) entity);
		}
		if (entityType.equals(SurveyResult.class)) {
			return updateSurveyResultQuery((SurveyResult) entity);
		}
		if (entityType.equals(User.class)) {
			return updateUserQuery((User) entity);
		}

		logMessage("generateUpdateQuery method received unsupported entity, type: " + entity.getClass().getName());
		return null;
	}
	// end region -> Stored Procedures Queries

	// end region -> Public Methods

	private static void logMessage(@NotNull String message) {
		if (s_logger == null) {
			s_logger = LogManager.getLogger();
		}
		s_logger.warning(message);

	}

	// region Items Entity

	private static String insertItemQuery(Item item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO items (iName,iType,iPrice,iImage,iDomainColor) VALUES ('");
		stringBuilder.append(item.getName());
		stringBuilder.append("','");
		stringBuilder.append(item.getType().toString());
		stringBuilder.append("',");
		stringBuilder.append(item.getPrice());
		stringBuilder.append(",?,'");
		stringBuilder.append(item.getDomainColor());
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	private static String deleteItemQuery(Item item) {

		int id = item.getId();
		if (id < 1) {
			logMessage("Could not generate remove Item query with impossiable id: " + id);
			return "";
		}

		return "UPDATE items SET iDeleted = 1 WHERE iId = " + id + " ;";
	}

	private static String updateItemQuery(Item item) {

		int id = item.getId();
		if (id < 1) {
			logMessage("Could not generate update Item query with impossiable id: " + id);
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE items SET");
		stringBuilder.append(" iName = '");
		stringBuilder.append(item.getName());
		stringBuilder.append("', iType = '");
		stringBuilder.append(item.getType().toString());
		stringBuilder.append("', iPrice = ");
		stringBuilder.append(item.getPrice());
		stringBuilder.append(", iDomainColor = '");
		stringBuilder.append(item.getDomainColor());
		stringBuilder.append("' , iImage = ? WHERE iId = ");
		stringBuilder.append(item.getId());
		stringBuilder.append(';');

		return stringBuilder.toString();
	}

	private static String selectItemQuery(Item item) {

		int id = item.getId();
		if (id < 1) {
			logMessage("Could not generate select Item query with impossiable id: " + id);
			return "";
		}

		return "SELECT * FROM catalog WHERE iId = " + id + " ;";
	}

	private static String selectAllItemQuery() {
		return "SELECT * FROM catalog;";
	}

	// End region -> Item Entity

	// region -> User Entity

	private static String selectAllUserQuery() {
		return "SELECT * FROM users;";
	}

	private static String selectUserQuery(User user) {
		String userName = user.getUserName();
		if (userName == null || userName.isEmpty()) {
			logMessage("Could not generate select all users query with null or empty username");
			return null;
		}

		return "SELECT * FROM users WHERE uUserName = '" + userName + "';";
	}

	private static String updateUserQuery(User user) {
		if (user == null) {
			return null;
		}

		String userName = user.getUserName();
		if (!(userName != null && !userName.isEmpty())) {
			logMessage("Could not generate update user query with null or empty username");
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE users SET ");
		stringBuilder.append("uPassword = '");
		stringBuilder.append(user.getPassword());
		stringBuilder.append("', uEmail = '");
		stringBuilder.append(user.getEmail());
		stringBuilder.append("', uPrivilege = '");
		stringBuilder.append(user.getPrivilege());
		stringBuilder.append("', uStatus = '");
		stringBuilder.append(user.getStatus());
		stringBuilder.append("' WHERE uUserName = '");
		stringBuilder.append(user.getUserName());
		stringBuilder.append("';");

		return stringBuilder.toString();
	}

	// End region -> User Entity

	// region Costumer Entity

	private static String selectCostumerQuery(Costumer costumer) {
		String userName = costumer.getUserName();
		int costumerId = costumer.getId();

		String returningString = null;

		if (costumerId > 0) {
			returningString = "SELECT * FROM costumers WHERE cid = " + costumerId + ";";
		} else if (userName != null && !userName.isEmpty()) {
			returningString = "SELECT * FROM costumers WHERE uUserName = '" + userName + "';";
		} else {
			logMessage("Received request to select costumer entity without any identification data.");
		}
		return returningString;
	}

	private static String selectAllCostumerQuery() {
		return "SELECT * FROM costumers;";
	}

	private static String updateCostumerQuery(Costumer costumer) {
		String userName = costumer.getUserName();
		int costumerId = costumer.getId();

		if (!(costumerId > 0 && userName != null && !userName.isEmpty())) {
			logMessage("Received request to update costumer entity with impossiable ID's: costumer ID = " + costumerId
					+ ", user name =" + userName);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE costumers SET ");
		stringBuilder.append("cBalance = ");
		stringBuilder.append(costumer.getBalance());
		stringBuilder.append("WHERE ");
		if (costumerId > 0) {
			stringBuilder.append("cId = ");
			stringBuilder.append(costumerId);
		} else {
			stringBuilder.append("uUserName = '");
			stringBuilder.append(userName);
			stringBuilder.append('\'');

		}
		stringBuilder.append(';');
		return stringBuilder.toString();
	}

	// End region -> Costumer Entity

	// region -> Reservation Entity

	private static String selectReservationQuery(Reservation reservation) {
		int id = reservation.getId();

		if (id < 1) {
			logMessage("Received request to select reservation entity with impossiable id: " + id);
			return null;
		}
		return "SELECT * FROM reservations WHERE rId = " + id + ';';
	}

	private static String selectAllReservationQuery() {
		return "SELECT * FROM reservations ;";

	}

	private static String updateReservationQuery(Reservation reservation) {
		int reservationId = reservation.getId();
		if (reservationId < 1) {
			logMessage("Received request to update reservation entity with impossiable ID: " + reservationId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE reservations SET ");
		stringBuilder.append("rCreditCard = '");
		stringBuilder.append(reservation.getCreditCard());
		stringBuilder.append("', rType = '");
		stringBuilder.append(reservation.getType());
		stringBuilder.append("', rNumberOfItems = ");
		stringBuilder.append(reservation.getNumberOfItems());
		stringBuilder.append(", rPrice = ");
		stringBuilder.append(reservation.getPrice());
		stringBuilder.append(", rBlessingCard = '");
		stringBuilder.append(reservation.getBlessingCard());
		stringBuilder.append("', rDeliveryDate = '");
		stringBuilder.append(s_dateTimeFormat.format(reservation.getDeliveryDate()));
		stringBuilder.append("', rDeliveryType = '");
		stringBuilder.append(reservation.getDeliveryType());
		stringBuilder.append("', rDeliveryAddress = '");
		stringBuilder.append(reservation.getDeliveryAddress());
		stringBuilder.append("', rDeliveryPhone = '");
		stringBuilder.append(reservation.getDeliveryPhone());
		stringBuilder.append("', rDeliveryName = '");
		stringBuilder.append(reservation.getDeliveryName());
		stringBuilder.append("' WHERE rId = ");
		stringBuilder.append(reservationId);
		stringBuilder.append(';');

		return stringBuilder.toString();
	}

	private static String insertReservationQuery(Reservation reservation) {
		int costumerId = reservation.getCostumerId();
		int shopManagerId = reservation.getShopManagerId();

		if (shopManagerId < 1 || costumerId < 1) {
			logMessage("Received request to insert reservation entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"INSERT INTO reservations (cId,smId,rCreditCard,rType,rNumberOfItems,rPrice,rBlessingCard,rDeliveryDate,rDeliveryType,rDeliveryAddress,rDeliveryPhone,rDeliveryName) VALUES (");
		stringBuilder.append(costumerId);
		stringBuilder.append(',');
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(reservation.getCreditCard());
		stringBuilder.append("','");
		stringBuilder.append(reservation.getType());
		stringBuilder.append("',");
		stringBuilder.append(reservation.getNumberOfItems());
		stringBuilder.append(',');
		stringBuilder.append(reservation.getPrice());
		stringBuilder.append(",'");
		stringBuilder.append(reservation.getBlessingCard());
		stringBuilder.append("','");
		stringBuilder.append(s_dateTimeFormat.format(reservation.getDeliveryDate()));
		stringBuilder.append("','");
		stringBuilder.append(reservation.getDeliveryType());
		stringBuilder.append("','");
		stringBuilder.append(reservation.getDeliveryAddress());
		stringBuilder.append("','");
		stringBuilder.append(reservation.getDeliveryPhone());
		stringBuilder.append("','");
		stringBuilder.append(reservation.getDeliveryName());
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	// end region -> Reservation Entity

	// region Survey Entity

	private static String insertSurveyQuery(Survey survey) {
		int shopManagerId = survey.getManagerId();

		if (shopManagerId < 1) {
			logMessage("Received request to insert survey entity with impossiable shop manager ID:" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO surveys (smId,suStartDate,suEndDate) VALUES (");
		stringBuilder.append(survey.getManagerId());
		stringBuilder.append(",'");
		stringBuilder.append(s_dateFormat.format(survey.getStartDate()));
		stringBuilder.append("','");
		stringBuilder.append(s_dateFormat.format(survey.getEndDate()));
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	private static String selectSurveyQuery(Survey survey) {
		int id = survey.getId();

		if (id < 1) {
			logMessage("Received request to select survey entity with impossiable id: " + id);
			return null;
		}
		return "SELECT * FROM surveys WHERE suId = " + id + ';';
	}

	private static String selectAllSurveyQuery() {
		return "SELECT * FROM surveys ;";
	}

	private static String updateSurveyQuery(Survey survey) {
		int id = survey.getId();
		if (id < 1) {
			logMessage("Received request to update survey entity with impossiable ID: " + id);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE surveys SET suStartDate = '");
		stringBuilder.append(s_dateFormat.format(survey.getStartDate()));
		stringBuilder.append("', suEndDate = '");
		stringBuilder.append(s_dateFormat.format(survey.getEndDate()));
		stringBuilder.append("' WHERE suId = ");
		stringBuilder.append(id);
		stringBuilder.append(';');
		return stringBuilder.toString();
	}

	// end region -> Survey Entity

	// region Complaints Entity

	private static String selectComplaintQuery(Complaint complaint) {
		int id = complaint.getId();
		if (id < 1) {
			logMessage("Received request to select complaint entity with impossiable ID: " + id);
			return null;
		}

		return "SELECT * FROM complaints WHERE coId = " + id + ';';
	}

	private static String selectAllComplaintQuery(Complaint complaint) {

		if (complaint == null) {
			return "SELECT * from complaints;";
		}

		int costumerId = complaint.getCostumerId();
		int shopManagerId = complaint.getShopManagerId();
		int costumerServiceId = complaint.getCostumerServiceEmployeeId();

		if (costumerId > 0 && shopManagerId > 0 && costumerServiceId > 0) {
			return "SELECT * FROM complaints WHERE cId = " + costumerId + " AND smId = " + shopManagerId
					+ " AND cseId = " + costumerServiceId + " ;";
		}
		if (costumerId > 0 && shopManagerId > 0) {
			return "SELECT * FROM complaints WHERE cId = " + costumerId + " AND smId = " + shopManagerId + " ;";
		}
		if (costumerId > 0 && costumerServiceId > 0) {
			return "SELECT * FROM complaints WHERE cId = " + costumerId + " AND cseId = " + costumerServiceId + " ;";
		}
		if (shopManagerId > 0 && costumerServiceId > 0) {
			return "SELECT * FROM complaints WHERE smId = " + shopManagerId + " AND cseId = " + costumerServiceId
					+ " ;";
		}
		if (costumerId > 0) {
			return "SELECT * FROM complaints WHERE cId = " + costumerId + " ;";
		}
		if (shopManagerId > 0) {
			return "SELECT * FROM complaints WHERE smId = " + shopManagerId + " ;";
		}
		if (costumerServiceId > 0) {
			return "SELECT * FROM complaints WHERE cseId = " + costumerServiceId + " ;";
		}
		return "SELECT * from complaints;";

	}

	private static String insertComplaintQuery(Complaint complaint) {
		int costumerId = complaint.getCostumerId();
		int shopManagerId = complaint.getShopManagerId();
		int costumerServiceEmployeeId = complaint.getCostumerServiceEmployeeId();
		if (shopManagerId < 1 || costumerId < 1 || costumerServiceEmployeeId < 1) {
			logMessage("Received request to insert complaint entity with impossiable ID's: costumer ID = " + costumerId
					+ ", shop manager ID = " + shopManagerId + ", costumer service employee ID = "
					+ costumerServiceEmployeeId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO complaints (cId,smId,cseId,coDate,coComplaint,coOpened) VALUES (");
		stringBuilder.append(costumerId);
		stringBuilder.append(',');
		stringBuilder.append(shopManagerId);
		stringBuilder.append(',');
		stringBuilder.append(costumerServiceEmployeeId);
		stringBuilder.append(",'");
		stringBuilder.append(s_dateFormat.format(complaint.getCreationDate()));
		stringBuilder.append("','");
		stringBuilder.append(complaint.getComplaint());
		stringBuilder.append("',");
		stringBuilder.append(complaint.isOpened() ? 1 : 0);
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	private static String updateComplaintQuery(Complaint complaint) {
		int id = complaint.getId();
		int costumerId = complaint.getCostumerId();
		int shopManagerId = complaint.getShopManagerId();
		int costumerServiceEmployeeId = complaint.getCostumerServiceEmployeeId();
		if (shopManagerId < 1 || costumerId < 1 || costumerServiceEmployeeId < 1) {
			logMessage("Received request to update complaint entity with impossiable ID's: costumer ID = " + costumerId
					+ ", shop manager ID = " + shopManagerId + ", costumer service employee ID = "
					+ costumerServiceEmployeeId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE complaints SET coDate = '");
		stringBuilder.append(s_dateFormat.format(complaint.getCreationDate()));
		stringBuilder.append("', coComplaint = '");
		stringBuilder.append(complaint.getComplaint());
		stringBuilder.append("', coSummary = '");
		stringBuilder.append(complaint.getSummary());
		stringBuilder.append("', coOpened = ");
		stringBuilder.append(complaint.isOpened() ? 1 : 0);
		stringBuilder.append(" WHERE coId = ");
		stringBuilder.append(id);
		stringBuilder.append(';');
		return stringBuilder.toString();
	}

	// end region -> Complaints Entity

	// region ItemsInShops Entity

	private static String selectAllItemInShopQuery(ItemInShop itemInShop) {

		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (itemId > 0 && shopManagerId > 0) {
			return "SELECT * FROM items_in_shops WHERE iId = " + itemId + " AND smId = " + shopManagerId + ';';
		}

		if (itemId > 0) {
			return "SELECT * FROM items_in_shops WHERE iId = " + itemId + ';';
		}

		if (shopManagerId > 0) {
			return "SELECT * FROM items_in_shops WHERE smId = " + shopManagerId + ';';
		}

		logMessage("Received request to select all shop items entity with impossiable ID's: item ID = " + itemId
				+ ", shop manager ID=" + shopManagerId);
		return null;
	}

	private static String updateItemInShopQuery(ItemInShop itemInShop) {
		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			logMessage("Received request to update shop item entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE items_in_shops SET ");
		stringBuilder.append("isDiscountedPrice = ");
		stringBuilder.append(itemInShop.getDiscountedPrice());
		stringBuilder.append(" WHERE iId = ");
		stringBuilder.append(itemId);
		stringBuilder.append(" AND smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(';');

		return stringBuilder.toString();

	}

	private static String insertItemInShopQuery(ItemInShop itemInShop) {
		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			logMessage("Received request to insert shop item entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO items_in_shops (smId,iId,isDiscountedPrice) VALUES (");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(',');
		stringBuilder.append(itemId);
		stringBuilder.append(',');
		stringBuilder.append(itemInShop.getDiscountedPrice());
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	private static String deleteItemInShopQuery(ItemInShop itemInShop) {

		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			logMessage("Received request to remove shop item entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DELETE FROM items_in_shops WHERE ");
		stringBuilder.append("iId = ");
		stringBuilder.append(itemId);
		stringBuilder.append(" AND smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(';');
		return stringBuilder.toString();
	}

	// end region -> ItemsInShops Entity

	// region ItemInReservation Entity

	private static String selectAllItemInReservationQuery(ItemInReservation itemInReservation) {
		int itemId = itemInReservation.getItemId();
		int reservationId = itemInReservation.getReservationId();
		if (itemId > 0 && reservationId > 0) {
			return "SELECT * FROM items_in_reservations WHERE iId = " + itemId + " AND rId = " + reservationId + ';';
		}

		if (itemId > 0) {
			return "SELECT * FROM items_in_reservations WHERE iId = " + itemId + ';';
		}

		if (reservationId > 0) {
			return "SELECT * FROM items_in_reservations WHERE rId = " + reservationId + ';';
		}

		logMessage("Received request to select reservation item entity with impossiable ID's: item ID = " + itemId
				+ ", reservation ID=" + reservationId);
		return null;
	}

	private static String insertItemInReservationQuery(ItemInReservation itemInReservation) {
		int itemId = itemInReservation.getItemId();
		int reservationId = itemInReservation.getReservationId();
		if (itemId < 1 || reservationId < 1) {
			logMessage("Received request to insert reservation item entity with impossiable ID's: item ID = " + itemId
					+ ", reservation ID=" + reservationId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO items_in_reservations (rId,iId,iName,irQuantity,irPrice) VALUES (");
		stringBuilder.append(reservationId);
		stringBuilder.append(',');
		stringBuilder.append(itemId);
		stringBuilder.append(",'");
		stringBuilder.append(itemInReservation.getItemName());
		stringBuilder.append("',");
		stringBuilder.append(itemInReservation.getQuantity());
		stringBuilder.append(',');
		stringBuilder.append(itemInReservation.getPrice());
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	// end region -> ItemInReservation Entity

	// region ShopEmployee Entity

	private static String selectShopEmployeeQuery(ShopEmployee shopEmployee) {
		String userName = shopEmployee.getUserName();
		int shopEmployeeId = shopEmployee.getId();

		String returningString = null;

		if (shopEmployeeId > 0) {
			returningString = "SELECT * FROM shop_employees WHERE seId = " + shopEmployeeId + ";";
		} else if (userName != null && !userName.isEmpty()) {
			returningString = "SELECT * FROM shop_employees WHERE uUserName = '" + userName + "';";
		} else {
			logMessage("Received request to select shop employee entity without any identification data.");
		}
		return returningString;
	}

	private static String selectAllShopEmployeeQuery() {
		return "SELECT * FROM shop_employees ;";
	}

	private static String updateShopEmployeeQuery(ShopEmployee shopEmployee) {
		String userName = shopEmployee.getUserName();
		int id = shopEmployee.getId();

		if (userName != null && !userName.isEmpty() && id > 1) {
			return "UPDATE shop_employees SET smId = " + shopEmployee.getShopManagerId() + " WHERE uUserName = '"
					+ userName + "' AND seId = " + id + ";";
		}

		if (userName != null && !userName.isEmpty()) {
			return "UPDATE shop_employees SET smId = " + shopEmployee.getShopManagerId() + " WHERE uUserName = '"
					+ userName + "' ;";
		}

		if (id > 1) {
			return "UPDATE shop_employees SET smId = " + shopEmployee.getShopManagerId() + " WHERE seId = " + id + ";";
		}

		logMessage("Received request to update Shop Employee entity with impossiable ID's: username = " + userName
				+ ", ID=" + id);
		return null;
	}
	// end region -> ShopEmployee Entity

	// region ShopManager Entity

	private static String selectShopManagerQuery(ShopManager shopManager) {
		String userName = shopManager.getUserName();
		int shopManagerId = shopManager.getId();

		String returningString = null;

		if (shopManagerId > 0) {
			returningString = "SELECT * FROM shop_managers WHERE smId = " + shopManagerId + ";";
		} else if (userName != null && !userName.isEmpty()) {
			returningString = "SELECT * FROM shop_managers WHERE uUserName = '" + userName + "';";
		} else {
			logMessage("Received request to select shop manager entity without any identification data.");
		}
		return returningString;
	}

	private static String selectAllShopManagerQuery() {
		return "SELECT * FROM shop_managers ;";
	}

	private static String updateShopManagerQuery(ShopManager shopManager) {
		String userName = shopManager.getUserName();
		int id = shopManager.getId();

		if (userName != null && !userName.isEmpty() && id > 1) {
			return "UPDATE shop_managers SET smName = '" + shopManager.getName() + "' WHERE uUserName = '" + userName
					+ "' AND smId = " + id + ";";
		}

		if (userName != null && !userName.isEmpty()) {
			return "UPDATE shop_managers SET smName = '" + shopManager.getName() + "' WHERE uUserName = '" + userName
					+ "' ;";
		}

		if (id > 1) {
			return "UPDATE shop_managers SET smName = '" + shopManager.getName() + "' smId = " + id + ";";
		}

		logMessage("Received request to update shop manager entity with impossiable ID's: username = " + userName
				+ ", ID=" + id);
		return null;
	}

	// end region -> ShopManager Entity

	// region ReservationsReport Entity

	private static String selectReservationsReportQuery(ReservationsReport reservationsReport) {
		int shopManagerId = reservationsReport.getShopManagerId();
		Date year = reservationsReport.getYear();
		int quarter = reservationsReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			logMessage("Received request to select reservations report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM reservations_reports WHERE ");
		stringBuilder.append("smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(" AND rrYear = '");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("' AND rrQuarter = ");
		stringBuilder.append(quarter);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}

	private static String selectAllReservationsReportQuery(ReservationsReport reservationsReport) {
		int shopManagerId = reservationsReport.getShopManagerId();
		Date year = reservationsReport.getYear();
		if (shopManagerId < 1 && year == null) {
			logMessage(
					"Received request to select all reservations report entity with impossiable ID's: Shop Manager ID = "
							+ shopManagerId + ", Year = " + s_yearFormat.format(year));
		}
		if (shopManagerId < 1) {
			return "SELECT * FROM reservations_reports WHERE rrYear = '" + s_yearFormat.format(year) + "' ;";
		}
		if (year == null) {
			return "SELECT * FROM reservations_reports WHERE smId = " + shopManagerId + " ;";

		}
		return "SELECT * FROM reservations_reports WHERE smId = " + shopManagerId + " AND rrYear ='"
				+ s_yearFormat.format(year) + "' ;";
	}

	// end region -> ReservationsReport Entity

	// region IncomesReport Entity

	private static String selectIncomesReportQuery(IncomesReport incomesReport) {
		int shopManagerId = incomesReport.getShopManagerId();
		Date year = incomesReport.getYear();
		int quarter = incomesReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			logMessage("Received request to select incomes report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM incomes_reports WHERE ");
		stringBuilder.append("smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(" AND irYear = '");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("' AND irQuarter = ");
		stringBuilder.append(quarter);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}

	private static String selectAllIncomesReportQuery(IncomesReport incomesReport) {
		int shopManagerId = incomesReport.getShopManagerId();
		Date year = incomesReport.getYear();
		if (shopManagerId < 1 && year == null) {
			logMessage("Received request to select all incomes report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year));
		}
		if (shopManagerId < 1) {
			return "SELECT * FROM incomes_reports WHERE irYear = '" + s_yearFormat.format(year) + "' ;";
		}
		if (year == null) {
			return "SELECT * FROM incomes_reports WHERE smId = " + shopManagerId + " ;";

		}
		return "SELECT * FROM incomes_reports WHERE smId = " + shopManagerId + " AND irYear ='"
				+ s_yearFormat.format(year) + "' ;";
	}

	// end region -> IncomesReport Entity

	// region SurveysReport Entity

	private static String selectSurveysReportQuery(SurveysReport surveysReport) {
		int shopManagerId = surveysReport.getShopManagerId();
		Date year = surveysReport.getYear();
		int quarter = surveysReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			logMessage("Received request to select survey report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM surveys_reports WHERE ");
		stringBuilder.append("smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(" AND srYear = '");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("' AND srQuarter = ");
		stringBuilder.append(quarter);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}

	private static String selectAllSurveysReportQuery(SurveysReport surveysReport) {
		int shopManagerId = surveysReport.getShopManagerId();
		Date year = surveysReport.getYear();
		if (shopManagerId < 1 && year == null) {
			logMessage("Received request to select all surveys report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year));
		}
		if (shopManagerId < 1) {
			return "SELECT * FROM surveys_reports WHERE srYear = '" + s_yearFormat.format(year) + "' ;";
		}
		if (year == null) {
			return "SELECT * FROM surveys_reports WHERE smId = " + shopManagerId + " ;";

		}
		return "SELECT * FROM surveys_reports WHERE smId = " + shopManagerId + " AND srYear ='"
				+ s_yearFormat.format(year) + "' ;";
	}

	// end region -> SurveysReport Entity

	// region ComplaintsReport Entity

	private static String selectComplaintsReportQuery(ComplaintsReport complaintsReport) {
		int shopManagerId = complaintsReport.getShopManagerId();
		Date year = complaintsReport.getYear();
		int quarter = complaintsReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			logMessage("Received request to select complaints report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM complaints_reports WHERE ");
		stringBuilder.append("smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(" AND crYear = '");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("' AND crQuarter = ");
		stringBuilder.append(quarter);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}

	private static String selectAllComplaintsReportQuery(ComplaintsReport complaintsReport) {
		int shopManagerId = complaintsReport.getShopManagerId();
		Date year = complaintsReport.getYear();
		if (shopManagerId < 1 && year == null) {
			logMessage(
					"Received request to select all complaints report entity with impossiable ID's: Shop Manager ID = "
							+ shopManagerId + ", Year = " + s_yearFormat.format(year));
		}
		if (shopManagerId < 1) {
			return "SELECT * FROM complaints_reports WHERE crYear = '" + s_yearFormat.format(year) + "' ;";
		}
		if (year == null) {
			return "SELECT * FROM complaints_reports WHERE smId = " + shopManagerId + " ;";

		}
		return "SELECT * FROM complaints_reports WHERE smId = " + shopManagerId + " AND crYear ='"
				+ s_yearFormat.format(year) + "' ;";
	}

	// end region -> ComplaintsReport Entity

	// region ShopCostumer Entity

	private static String selectShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();

		if (shopManagerId < 1 || costumerId < 1) {
			logMessage("Received request to select shop costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}

		return "SELECT * FROM costumers_in_shops WHERE cId = " + costumerId + " AND smId = " + shopManagerId + " ;";

	}

	private static String selectAllShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();

		if (shopManagerId < 1 && costumerId < 1) {
			return "SELECT * FROM costumers_in_shops;";
		}

		if (costumerId < 1) {
			return "SELECT * FROM costumers_in_shops WHERE smId = " + shopManagerId + " ;";
		}

		if (shopManagerId < 1) {
			return "SELECT * FROM costumers_in_shops WHERE cId = " + costumerId + " ;";
		}
		return "SELECT * FROM costumers_in_shops WHERE cId = " + costumerId + " AND smId = " + shopManagerId + " ;";
	}

	private static String insertShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();

		if (shopManagerId < 1 || costumerId < 1) {
			logMessage("Received request to insert shop costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"INSERT INTO costumers_in_shops (cId,smId,csCostumerSubscription,csCreditCard,csStartSubscriptionDate) VALUES (");
		stringBuilder.append(costumerId);
		stringBuilder.append(',');
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(shopCostumer.getCostumerSubscription());
		stringBuilder.append("','");
		stringBuilder.append(shopCostumer.getCreditCard());
		stringBuilder.append("','");
		stringBuilder.append(s_dateFormat.format(shopCostumer.getSubscriptionStartDate()));
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	private static String updateShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();
		if (shopManagerId < 1 || costumerId < 1) {
			logMessage("Received request to update shop costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE costumers_in_shops SET ");
		stringBuilder.append("csCostumerSubscription = '");
		stringBuilder.append(shopCostumer.getCostumerSubscription());
		stringBuilder.append("', csCreditCard = '");
		stringBuilder.append(shopCostumer.getCreditCard());
		stringBuilder.append("', csStartSubscriptionDate = '");
		stringBuilder.append(s_dateFormat.format(shopCostumer.getSubscriptionStartDate()));
		stringBuilder.append("', csCumulativePrice = ");
		stringBuilder.append(shopCostumer.getCumulativePrice());
		stringBuilder.append(" WHERE cId = ");
		stringBuilder.append(costumerId);
		stringBuilder.append(" AND smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}

	private static String deleteShopCostumerQuery(ShopCostumer shopCostumer) {

		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();
		if (shopManagerId < 1 || costumerId < 1) {
			logMessage("Received request to remove shop costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DELETE FROM costumers_in_shops WHERE ");
		stringBuilder.append("cId ");
		stringBuilder.append(costumerId);
		stringBuilder.append(" AND smId = ");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(';');
		return stringBuilder.toString();
	}

	// end region -> ShopCostumer Entity

	// region ShopSurvey Entity

	private static String selectSurveyResultQuery(SurveyResult surveyResult) {
		int id = surveyResult.getId();
		if (id < 1) {
			logMessage("Received request to select survey result entity with impossiable ID = " + id);
			return null;
		}
		return "SELECT * FROM survey_results WHERE srId = " + id + " ;";
	}

	private static String selectAllSurveyResultQuery() {
		return "SELECT * FROM survey_results WHERE srSummary IS NULL ;";
	}

	private static String insertSurveyResultQuery(SurveyResult surveyResult) {
		int surveyId = surveyResult.getSurveyId();
		if (surveyId < 1) {
			logMessage("Received request to insert survey entity with impossiable ID:  " + surveyId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"INSERT INTO survey_results (suId,srEnterDate,srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES (");
		stringBuilder.append(surveyId);
		stringBuilder.append(",'");
		stringBuilder.append(s_dateFormat.format(surveyResult.getEnterDate()));
		stringBuilder.append("',");
		stringBuilder.append(surveyResult.getFirstAnswer());
		stringBuilder.append(',');
		stringBuilder.append(surveyResult.getSecondAnswer());
		stringBuilder.append(',');
		stringBuilder.append(surveyResult.getThirdAnswer());
		stringBuilder.append(',');
		stringBuilder.append(surveyResult.getFourthAnswer());
		stringBuilder.append(',');
		stringBuilder.append(surveyResult.getFifthAnswer());
		stringBuilder.append(',');
		stringBuilder.append(surveyResult.getSixthAnswer());
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	private static String updateSurveyResultQuery(SurveyResult surveyResult) {
		int id = surveyResult.getId();

		if (id < 1) {
			logMessage("Received request to update shop survey entity with impossiable ID = " + id);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE survey_results SET ");
		stringBuilder.append("suId = ");
		stringBuilder.append(surveyResult.getSurveyId());
		stringBuilder.append(", srEnterDate = '");
		stringBuilder.append(s_dateFormat.format(surveyResult.getEnterDate()));
		stringBuilder.append("', srAnswer1 = ");
		stringBuilder.append(surveyResult.getFirstAnswer());
		stringBuilder.append(", srAnswer2 = ");
		stringBuilder.append(surveyResult.getSecondAnswer());
		stringBuilder.append(", srAnswer3 = ");
		stringBuilder.append(surveyResult.getThirdAnswer());
		stringBuilder.append(", srAnswer4 = ");
		stringBuilder.append(surveyResult.getFourthAnswer());
		stringBuilder.append(", srAnswer5 = ");
		stringBuilder.append(surveyResult.getFifthAnswer());
		stringBuilder.append(", srAnswer6 = ");
		stringBuilder.append(surveyResult.getSixthAnswer());
		stringBuilder.append(", srSummary = '");
		stringBuilder.append(surveyResult.getSummary());
		stringBuilder.append("' WHERE srId = ");
		stringBuilder.append(id);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}
	// end region -> ShopSurvey Entity

	// region Costumer Service Employee Entity

	private static String selectCostumerServiceEmployeeQuery(CostumerServiceEmployee costumerServiceEmployee) {
		int id = costumerServiceEmployee.getId();
		String userName = costumerServiceEmployee.getUserName();

		if (id > 1 && userName != null && !userName.isEmpty()) {
			return "SELECT * FROM costumer_service_employees WHERE cseId = " + id + " AND uUserName = '" + userName
					+ "' ;";

		}
		if (id > 1) {
			return "SELECT * FROM costumer_service_employees WHERE cseId = " + id + " ;";
		}

		if (userName != null && !userName.isEmpty()) {
			return "SELECT * FROM costumer_service_employees WHERE uUserName = '" + userName + "' ;";

		}

		logMessage(
				"Received request to select costumer service employee entity with impossiable ID's: costumer service ID = "
						+ id + ", user name = " + userName);
		return null;
	}

	private static String selectAllCostumerServiceEmployeeQuery() {
		return "SELECT * FROM costumer_service_employees;";
	}

	// end region -> Costumer Service Employee Entity

}
