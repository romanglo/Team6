package db;

import java.util.logging.Logger;

import logger.LogManager;
import newEntities.Complaint;
import newEntities.ComplaintsReport;
import newEntities.Costumer;
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

/**
 *
 * QueryGenerator: A factory of queries based on {@link IEntity}.
 */
@SuppressWarnings("javadoc")
public class QueryGenerator {

	/**
	 * The constructor is empty to ensure that it will not be possible to create
	 * instance of this class.
	 */
	private QueryGenerator() {

	}

	private final static java.text.SimpleDateFormat s_dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

	private final static java.text.SimpleDateFormat s_dateTimeFormat = new java.text.SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	private static Logger s_logger = null;

	private static void loggerLazyLoading() {
		if (s_logger == null) {
			s_logger = LogManager.getLogger();
		}
	}

	public static String getLastIdQuery() {
		return "SELECT LAST_INSERT_ID();";
	}

	public static String getShopCatalog(int shopManagerId) {
		return "CALL getShopCatalog(" + shopManagerId + ");";
	}

	// region Items Entity

	public static String insertItemQuery(Item item) {
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

	public static String removeItemQuery(Item item) {

		int id = item.getId();
		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Could not generate remove Item query with impossiable id: " + id);
			return "";
		}

		return "UPDATE items SET iDeleted = 1 WHERE iId = " + id + " ;";
	}

	public static String updateItemQuery(Item item) {

		int id = item.getId();
		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Could not generate update Item query with impossiable id: " + id);
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

	public static String selectItemQuery(Item item) {

		int id = item.getId();
		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Could not generate select Item query with impossiable id: " + id);
			return "";
		}

		return "SELECT * FROM catalog WHERE iId = " + id + " ;";
	}

	public static String selectAllItemsQuery() {
		return "SELECT * FROM catalog;";
	}

	// End region -> Item Entity

	// region -> User Entity

	public static String selectAllUsersQuery() {
		return "SELECT * FROM users;";
	}

	public static String selectUserQuery(User user) {
		String userName = user.getUserName();
		if (userName == null || userName.isEmpty()) {
			loggerLazyLoading();
			s_logger.warning("Could not generate select user query with null or empty username");
			return null;
		}

		return "SELECT * FROM users WHERE uUserName = '" + userName + "';";
	}

	public static String updateUserQuery(User user) {
		if (user == null) {
			return null;
		}

		String userName = user.getUserName();
		if (!(userName != null && !userName.isEmpty())) {
			loggerLazyLoading();
			s_logger.warning("Could not generate update user query with null or empty username");
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

	public static String selectCostumerQuery(Costumer costumer) {
		String userName = costumer.getUserName();
		int costumerId = costumer.getId();

		String returningString = null;

		if (costumerId > 0) {
			returningString = "SELECT * FROM costumers WHERE cid = " + costumerId + ";";
		} else if (userName != null && !userName.isEmpty()) {
			returningString = "SELECT * FROM costumers WHERE uUserName = '" + userName + "';";
		} else {
			loggerLazyLoading();
			s_logger.warning("Received request to get costumer entity without any identification data.");
		}
		return returningString;
	}

	public static String selectAllCostumersQuery() {
		return "SELECT * FROM costumers;";
	}

	public static String updateCostumerQuery(Costumer costumer) {
		String userName = costumer.getUserName();
		int costumerId = costumer.getId();

		if (!(costumerId > 0 && userName != null && !userName.isEmpty())) {
			loggerLazyLoading();
			s_logger.warning("Received request to get user entity with impossiable ID's: costumer ID = " + costumerId
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

	public static String selectReservationQuery(Reservation reservation) {
		int id = reservation.getId();

		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable id: " + id);
			return null;
		}
		return "SELECT * FROM reservations WHERE rId = " + id + ';';
	}

	public static String selectAllReservationsQuery() {
		return "SELECT * FROM reservations ;";

	}

	public static String updateReservationQuery(Reservation reservation) {
		int reservationId = reservation.getId();
		if (reservationId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable ID: " + reservationId);
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

	public static String insertReservationQuery(Reservation reservation) {
		int costumerId = reservation.getCostumerId();
		int shopManagerId = reservation.getShopManagerId();

		if (shopManagerId < 1 || costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable ID's: costumer ID = "
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

	public static String insertSurveyQuery(Survey survey) {
		int shopManagerId = survey.getId();

		if (shopManagerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get survey entity with impossiable shop manager ID:" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"INSERT INTO surveys (suId,suQuestion1,suQuestion2,suQuestion3,suQuestion4,suQuestion5,suQuestion6,suSummary) VALUES ('");

		stringBuilder.append(survey.getFirstQuestion());
		stringBuilder.append("','");
		stringBuilder.append(survey.getSecondQuestion());
		stringBuilder.append("','");
		stringBuilder.append(survey.getThirdQuestion());
		stringBuilder.append("','");
		stringBuilder.append(survey.getFourthQuestion());
		stringBuilder.append("','");
		stringBuilder.append(survey.getFifthQuestion());
		stringBuilder.append("','");
		stringBuilder.append(survey.getSixthQuestion());
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	// end region -> Survey Entity

	// region Complaints Entity

	public static String selectComplaintQuery(Complaint complaint) {
		int id = complaint.getId();
		if (id > 1) {
			return "SELECT * FROM complaints WHERE coId = " + id + ';';
		}
		int costumerId = complaint.getCostumerId();
		int shopManagerId = complaint.getShopManagerId();

		if (costumerId > 0 && shopManagerId > 0) {
			return "SELECT * FROM complaints WHERE cId = " + costumerId + " AND smId = " + shopManagerId + ';';
		}

		if (costumerId > 0) {
			return "SELECT * FROM complaints WHERE cId = " + costumerId + ';';
		}

		if (shopManagerId > 0) {
			return "SELECT * FROM complaints WHERE smId = " + shopManagerId + ';';
		}

		loggerLazyLoading();
		s_logger.warning("Received request to get complaint entity with impossiable ID's: complaint ID = " + id
				+ "costumer ID = " + costumerId + ", shop manager ID=" + shopManagerId);
		return null;

	}

	public static String selectAllComplaintsQuery() {
		return "SELECT * FROM complaints ;";
	}

	public static String insertComplaintQuery(Complaint complaint) {
		int costumerId = complaint.getCostumerId();
		int shopManagerId = complaint.getShopManagerId();

		if (shopManagerId < 1 || costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get complaint entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO complaints (cId,smId,coDate,coComplaint,coSummary,coOpened) VALUES (");
		stringBuilder.append(costumerId);
		stringBuilder.append(',');
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(s_dateFormat.format(complaint.getCreationDate()));
		stringBuilder.append("','");
		stringBuilder.append(complaint.getComplaint());
		stringBuilder.append("','");
		stringBuilder.append(complaint.getSummary());
		stringBuilder.append("',");
		stringBuilder.append(complaint.isOpened() ? 1 : 0);
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	public static String updateComplaintQuery(Complaint complaint) {
		int id = complaint.getId();
		int costumerId = complaint.getCostumerId();
		int shopManagerId = complaint.getShopManagerId();

		if (id < 1 || shopManagerId < 1 || costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get complaint entity with impossiable ID's: complain ID = " + id
					+ ", costumer ID = " + costumerId + ", shop manager ID=" + shopManagerId);
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

	public static String selectItemInShopsQuery(ItemInShop itemInShop) {

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

		loggerLazyLoading();
		s_logger.warning("Received request to get reservation entity with impossiable ID's: item ID = " + itemId
				+ ", shop manager ID=" + shopManagerId);
		return null;
	}

	public static String updateItemInShopQuery(ItemInShop itemInShop) {
		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE items_in_shops SET ");
		stringBuilder.append("isDiscountedPrice = ");
		stringBuilder.append(itemInShop.getDiscountedPrice());
		stringBuilder.append(" WHERE iId = ");
		stringBuilder.append(itemInShop.getItemId());
		stringBuilder.append(" AND smId = ");
		stringBuilder.append(itemInShop.getShopManagerId());
		stringBuilder.append(';');

		return stringBuilder.toString();

	}

	public static String insertItemInShopQuery(ItemInShop itemInShop) {
		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO items_in_shops (smId,iId,isDiscountedPrice) VALUES (");
		stringBuilder.append(itemId);
		stringBuilder.append(',');
		stringBuilder.append(shopManagerId);
		stringBuilder.append(',');
		stringBuilder.append(itemInShop.getDiscountedPrice());
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	public static String removeItemInShopQuery(ItemInShop itemInShop) {

		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DELETE FROM items_in_shops WHERE");
		stringBuilder.append(itemInShop.getItemId());
		stringBuilder.append(" AND smId = ");
		stringBuilder.append(itemInShop.getShopManagerId());
		stringBuilder.append(';');
		return stringBuilder.toString();
	}

	// end region -> ItemsInShops Entity

	// region ItemInReservation Entity

	public static String selectItemsInReservationQuery(ItemInReservation itemInReservation) {
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

		loggerLazyLoading();
		s_logger.warning("Received request to get reservation entity with impossiable ID's: item ID = " + itemId
				+ ", reservation ID=" + reservationId);
		return null;
	}

	public static String insertItemInReservationQuery(ItemInReservation itemInReservation) {
		int itemId = itemInReservation.getItemId();
		int reservationId = itemInReservation.getReservationId();
		if (itemId < 1 || reservationId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation entity with impossiable ID's: item ID = " + itemId
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

	public static String selectShopEmployeeQuery(ShopEmployee shopEmployee) {
		String userName = shopEmployee.getUserName();
		int shopEmployeeId = shopEmployee.getId();

		String returningString = null;

		if (shopEmployeeId > 0) {
			returningString = "SELECT * FROM shop_employees WHERE seId = " + shopEmployeeId + ";";
		} else if (userName != null && !userName.isEmpty()) {
			returningString = "SELECT * FROM shop_employees WHERE uUserName = '" + userName + "';";
		} else {
			loggerLazyLoading();
			s_logger.warning("Received request to get costumer entity without any identification data.");
		}
		return returningString;
	}

	public static String selectAllShopEmployeesQuery() {
		return "SELECT * FROM shop_employees ;";
	}

	// end region -> ShopEmployee Entity

	// region ShopManager Entity

	public static String selectShopManagerQuery(ShopManager shopManager) {
		String userName = shopManager.getUserName();
		int shopManagerId = shopManager.getId();

		String returningString = null;

		if (shopManagerId > 0) {
			returningString = "SELECT * FROM shop_managers WHERE smId = " + shopManagerId + ";";
		} else if (userName != null && !userName.isEmpty()) {
			returningString = "SELECT * FROM shop_managers WHERE uUserName = '" + userName + "';";
		} else {
			loggerLazyLoading();
			s_logger.warning("Received request to get costumer entity without any identification data.");
		}
		return returningString;
	}

	public static String selectShopManagersQuery() {
		return "SELECT * FROM shop_managers ;";
	}

	// end region -> ShopManager Entity

	// region ReservationsReport Entity

	public static String selectReservationsReportQuery(ReservationsReport reservationsReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String selectReservationsReportsQuery() {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String insertReservationsReportQuery(ReservationsReport reservationsReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	// end region -> ReservationsReport Entity

	// region IncomesReport Entity

	public static String selectIncomesReportQuery(IncomesReport incomesReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String selectIncomesReportQuery() {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String insertIncomesReportQuery(IncomesReport incomesReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	// end region -> IncomesReport Entity

	// region SurveysReport Entity

	public static String selectSurveysReportQuery(SurveysReport surveysReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String selectSurveysReportQuery() {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String insertSurveysReportQuery(SurveysReport surveysReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	// end region -> SurveysReport Entity

	// region ComplaintsReport Entity

	public static String selectComplaintsReporttQuery(ComplaintsReport complaintsReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String selectComplaintsReportQuery() {
		return null;
		// TODO Roman: Auto-generated method stub
	}

	public static String insertComplaintsReportQuery(ComplaintsReport complaintsReport) {
		return null;
		// TODO Roman: Auto-generated method stub
	}
	// end region -> ComplaintsReport Entity

}
