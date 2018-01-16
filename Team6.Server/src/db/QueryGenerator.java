package db;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import newEntities.ShopCostumer;
import newEntities.ShopEmployee;
import newEntities.ShopManager;
import newEntities.SurveyResult;
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

	private final static SimpleDateFormat s_yearFormat = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat s_dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat s_dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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

	public static String updateAllUsersToDisconnectQuery() {
		return "UPDATE users SET uStatus = 'Disconnected' WHERE uStatus = 'Connected' ;";
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
			s_logger.warning("Received request to get costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", user name =" + userName);
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
				"INSERT INTO surveys (smId,suStartDate,suEndDate) VALUES (");
		stringBuilder.append(survey.getManagerId());
		stringBuilder.append(",'");
		stringBuilder.append(s_dateFormat.format(survey.getStartDate()));
		stringBuilder.append("','");
		stringBuilder.append(s_dateFormat.format(survey.getEndDate()));
		stringBuilder.append("');");
		return stringBuilder.toString();
	}

	public static String selectSurveyQuery(Survey survey) {
		int id = survey.getId();

		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get survey entity with impossiable id: " + id);
			return null;
		}
		return "SELECT * FROM surveys WHERE suId = " + id + ';';
	}

	public static String selectAllSurveysQuery() {
		return "SELECT * FROM surveys ;";
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
		stringBuilder.append(");");
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
		s_logger.warning("Received request to get shop item entity with impossiable ID's: item ID = " + itemId
				+ ", shop manager ID=" + shopManagerId);
		return null;
	}

	public static String updateItemInShopQuery(ItemInShop itemInShop) {
		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop item entity with impossiable ID's: item ID = " + itemId
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
			s_logger.warning("Received request to get shop item entity with impossiable ID's: item ID = " + itemId
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
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	public static String removeItemInShopQuery(ItemInShop itemInShop) {

		int itemId = itemInShop.getItemId();
		int shopManagerId = itemInShop.getShopManagerId();

		if (shopManagerId < 1 || itemId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop item entity with impossiable ID's: item ID = " + itemId
					+ ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DELETE FROM items_in_shops WHERE ");
		stringBuilder.append("iId = ");
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
		s_logger.warning("Received request to get reservation item entity with impossiable ID's: item ID = " + itemId
				+ ", reservation ID=" + reservationId);
		return null;
	}

	public static String insertItemInReservationQuery(ItemInReservation itemInReservation) {
		int itemId = itemInReservation.getItemId();
		int reservationId = itemInReservation.getReservationId();
		if (itemId < 1 || reservationId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get reservation item entity with impossiable ID's: item ID = "
					+ itemId + ", reservation ID=" + reservationId);
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
			s_logger.warning("Received request to get shop employee entity without any identification data.");
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
			s_logger.warning("Received request to get shop manager entity without any identification data.");
		}
		return returningString;
	}

	public static String selectShopManagersQuery() {
		return "SELECT * FROM shop_managers ;";
	}

	// end region -> ShopManager Entity

	// region ReservationsReport Entity

	public static String selectReservationsReportQuery(ReservationsReport reservationsReport) {
		int shopManagerId = reservationsReport.getShopManagerId();
		Date year = reservationsReport.getYear();
		int quarter = reservationsReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning(
					"Received request to get reservations report entity with impossiable ID's: Shop Manager ID = "
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

	public static String selectAllReservationsReportsQuery(ReservationsReport reservationsReport) {
		int shopManagerId = reservationsReport.getShopManagerId();
		Date year = reservationsReport.getYear();
		if (shopManagerId < 1 && year == null) {
			loggerLazyLoading();
			s_logger.warning(
					"Received request to get reservations report entity with impossiable ID's: Shop Manager ID = "
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

	public static String insertReservationsReportQuery(ReservationsReport reservationsReport) {
		int shopManagerId = reservationsReport.getShopManagerId();
		Date year = reservationsReport.getYear();
		int quarter = reservationsReport.getQuarter();

		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning(
					"Received request to get reservations report entity with impossiable ID's: Shop Manager ID = "
							+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"INSERT INTO reservations_reports (smId,rrYear,rrQuarter,rrMonth1_Flower,rrMonth1_FlowerPot,rrMonth1_FlowerArrangement,rrMonth1_BridalBouquet,rrMonth2_Flower,rrMonth2_FlowerPot,rrMonth2_FlowerArrangement,rrMonth2_BridalBouquet,rrMonth3_Flower,rrMonth3_FlowerPot,rrMonth3_FlowerArrangement,rrMonth3_BridalBouquet) VALUES (");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("',");
		stringBuilder.append(quarter);
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowersInFirstMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowerPotsInFirstMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowerArrangementsInFirstMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedBridalBouquetsInFirstMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowersInSecondMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowerPotsInSecondMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowerArrangementsInSecondMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedBridalBouquetsInSecondMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowersInThirdMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowerPotsInThirdMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedFlowerArrangementsInThirdMonth());
		stringBuilder.append(',');
		stringBuilder.append(reservationsReport.getNumberOfOrderedBridalBouquetsInThirdMonth());
		stringBuilder.append(");");
		return stringBuilder.toString();

	}

	// end region -> ReservationsReport Entity

	// region IncomesReport Entity

	public static String selectIncomesReportQuery(IncomesReport incomesReport) {
		int shopManagerId = incomesReport.getShopManagerId();
		Date year = incomesReport.getYear();
		int quarter = incomesReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning("Received request to get incomes report entity with impossiable ID's: Shop Manager ID = "
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

	public static String selectAllIncomesReportQuery(IncomesReport incomesReport) {
		int shopManagerId = incomesReport.getShopManagerId();
		Date year = incomesReport.getYear();
		if (shopManagerId < 1 && year == null) {
			loggerLazyLoading();
			s_logger.warning("Received request to get incomes report entity with impossiable ID's: Shop Manager ID = "
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

	public static String insertIncomesReportQuery(IncomesReport incomesReport) {
		int shopManagerId = incomesReport.getShopManagerId();
		Date year = incomesReport.getYear();
		int quarter = incomesReport.getQuarter();

		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning("Received request to get incomes report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO incomes_reports (smId,irYear,irQuarter,irMonth1,irMonth2,irMonth3) VALUES (");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("',");
		stringBuilder.append(quarter);
		stringBuilder.append(',');
		stringBuilder.append(incomesReport.getIncomesInFirstMonth());
		stringBuilder.append(',');
		stringBuilder.append(incomesReport.getIncomesInSecondMonth());
		stringBuilder.append(',');
		stringBuilder.append(incomesReport.getIncomesInThirdMonth());
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	// end region -> IncomesReport Entity

	// region SurveysReport Entity

	public static String selectSurveysReportQuery(SurveysReport surveysReport) {
		int shopManagerId = surveysReport.getShopManagerId();
		Date year = surveysReport.getYear();
		int quarter = surveysReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning("Received request to get survey report entity with impossiable ID's: Shop Manager ID = "
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

	public static String selectAllSurveysReportQuery(SurveysReport surveysReport) {
		int shopManagerId = surveysReport.getShopManagerId();
		Date year = surveysReport.getYear();
		if (shopManagerId < 1 && year == null) {
			loggerLazyLoading();
			s_logger.warning("Received request to get surveys report entity with impossiable ID's: Shop Manager ID = "
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

	public static String insertSurveysReportQuery(SurveysReport surveysReport) {
		int shopManagerId = surveysReport.getShopManagerId();
		Date year = surveysReport.getYear();
		int quarter = surveysReport.getQuarter();

		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning("Received request to get incomes report entity with impossiable ID's: Shop Manager ID = "
					+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"INSERT INTO incomes_reports (smId,srYear,srQuarter,srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES (");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("',");
		stringBuilder.append(quarter);
		stringBuilder.append(',');
		stringBuilder.append(surveysReport.getFirstAnswerAverage());
		stringBuilder.append(',');
		stringBuilder.append(surveysReport.getSecondAnswerAverage());
		stringBuilder.append(',');
		stringBuilder.append(surveysReport.getThirdAnswerAverage());
		stringBuilder.append(',');
		stringBuilder.append(surveysReport.getFourthAnswerAverage());
		stringBuilder.append(',');
		stringBuilder.append(surveysReport.getFifthAnswerAverage());
		stringBuilder.append(',');
		stringBuilder.append(surveysReport.getSixthAnswerAverage());
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	// end region -> SurveysReport Entity

	// region ComplaintsReport Entity

	public static String selectComplaintsReportQuery(ComplaintsReport complaintsReport) {
		int shopManagerId = complaintsReport.getShopManagerId();
		Date year = complaintsReport.getYear();
		int quarter = complaintsReport.getQuarter();
		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning(
					"Received request to get complaints report entity with impossiable ID's: Shop Manager ID = "
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

	public static String selectAllComplaintsReportQuery(ComplaintsReport complaintsReport) {
		int shopManagerId = complaintsReport.getShopManagerId();
		Date year = complaintsReport.getYear();
		if (shopManagerId < 1 && year == null) {
			loggerLazyLoading();
			s_logger.warning(
					"Received request to get complaints report entity with impossiable ID's: Shop Manager ID = "
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

	public static String insertComplaintsReportQuery(ComplaintsReport complaintsReport) {
		int shopManagerId = complaintsReport.getShopManagerId();
		Date year = complaintsReport.getYear();
		int quarter = complaintsReport.getQuarter();

		if (shopManagerId < 1 || year == null || quarter < 1 || quarter > 4) {
			loggerLazyLoading();
			s_logger.warning(
					"Received request to get complaints report entity with impossiable ID's: Shop Manager ID = "
							+ shopManagerId + ", Year = " + s_yearFormat.format(year) + ", Quarter = " + quarter);
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("INSERT INTO complaints_reports (smId,crYear,crQuarter,crMonth1,crMonth2,crMonth3) VALUES (");
		stringBuilder.append(shopManagerId);
		stringBuilder.append(",'");
		stringBuilder.append(s_yearFormat.format(year));
		stringBuilder.append("',");
		stringBuilder.append(quarter);
		stringBuilder.append(',');
		stringBuilder.append(complaintsReport.getNumberOfComplaintsFirstMonth());
		stringBuilder.append(',');
		stringBuilder.append(complaintsReport.getNumberOfComplaintsSecondMonth());
		stringBuilder.append(',');
		stringBuilder.append(complaintsReport.getNumberOfComplaintsThirdMonth());
		stringBuilder.append(");");
		return stringBuilder.toString();
	}

	// end region -> ComplaintsReport Entity

	// region ShopCostumer Entity

	public static String selectShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();

		if (shopManagerId < 1 && costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}

		if (costumerId < 1) {
			return "SELECT * FROM costumers_in_shops WHERE smId = " + shopManagerId + " ;";
		}

		if (shopManagerId < 1) {
			return "SELECT * FROM costumers_in_shops WHERE cId = " + costumerId + " ;";
		}
		return "SELECT * FROM costumers_in_shops WHERE cId = " + costumerId + " AND smId = " + shopManagerId + " ;";

	}

	public static String selectAllShopCostumerReportQuery() {
		return "SELECT * FROM costumers_in_shops;";
	}

	public static String insertShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();

		if (shopManagerId < 1 || costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop costumer entity with impossiable ID's: costumer ID = "
					+ costumerId + ", shop manager ID=" + shopManagerId);
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO costumers_in_shops (cId,smId,csCostumerSubscription,csCreditCard,csStartSubscriptionDate) VALUES (");
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

	public static String updateShopCostumerQuery(ShopCostumer shopCostumer) {
		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();
		if (shopManagerId < 1 || costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop costumer entity with impossiable ID's: costumer ID = "
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

	public static String removeShopCostumerQuery(ShopCostumer shopCostumer) {

		int costumerId = shopCostumer.getCostumerId();
		int shopManagerId = shopCostumer.getShopManagerId();
		if (shopManagerId < 1 || costumerId < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop costumer entity with impossiable ID's: costumer ID = "
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

	public static String selectSurveyResultQuery(SurveyResult surveyResult) {
		int id = surveyResult.getId();
		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get survey result entity with impossiable ID = " + id);
			return null;
		}
		return "SELECT * FROM survey_results WHERE srId = " + id + " ;";
	}

	public static String selectAllSurveyResultsQuery() {
		return "SELECT * FROM surveys_in_shops WHERE srSummary IS NULL ;";
	}

	public static String insertShopSurveyQuery(SurveyResult surveyResult) {
		int surveyId = surveyResult.getSurveyId();
		if (surveyId < 1 ) {
			loggerLazyLoading();
			s_logger.warning("Received request to insert survey entity with impossiable ID:  " + surveyId) ;
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO survey_results (suId,srEnterDate,srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES (");
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

	public static String updateShopSurveyQuery(SurveyResult surveyResult) {
		int id = surveyResult.getId();

		if (id < 1) {
			loggerLazyLoading();
			s_logger.warning("Received request to get shop survey entity with impossiable ID = " + id);
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
		stringBuilder.append(" WHERE ssId = ");
		stringBuilder.append(id);
		stringBuilder.append(" ;");
		return stringBuilder.toString();
	}
	// end region -> ShopSurvey Entity

}
