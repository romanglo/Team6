package db;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logger.LogManager;

import newEntities.EntitiesEnums.CostumerSubscription;
import newEntities.EntitiesEnums.ProductType;
import newEntities.EntitiesEnums.ReservationDeliveryType;
import newEntities.EntitiesEnums.ReservationType;
import newEntities.EntitiesEnums.UserPrivilege;
import newEntities.EntitiesEnums.UserStatus;

import newEntities.IEntity;
import newEntities.IncomesReport;
import newEntities.Item;
import newEntities.ItemInReservation;
import newEntities.ItemInShop;
import newEntities.Complaint;
import newEntities.ComplaintsReport;
import newEntities.Costumer;
import newEntities.Reservation;
import newEntities.ReservationsReport;
import newEntities.ShopCostumer;
import newEntities.ShopEmployee;
import newEntities.ShopManager;
import newEntities.ShopSurvey;
import newEntities.Survey;
import newEntities.SurveysReport;
import newEntities.User;

/**
 *
 * EntitiesResolver: A class that can resolve and create an {@link List} of
 * instances of {@link IEntity} from received element.
 * 
 */
public class EntitiesResolver {

	/**
	 * The constructor is empty to ensure that it will not be possible to create
	 * instance of this class.
	 */
	private EntitiesResolver() {

	}

	private static Logger s_logger = null;

	private static void loggerLazyLoading() {
		if (s_logger == null) {
			s_logger = LogManager.getLogger();
		}
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link IEntity}.
	 * 
	 * @param <TData>
	 *            The expected {@link IEntity} that the {@link ResultSet} contains.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link IEntity}.
	 * @param expectedType
	 *            An {@link Class} of the expected {@link IEntity} that the
	 *            {@link ResultSet} contains.
	 * @return An {@link List} of {@link IEntity} if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	public static <TData extends IEntity> List<IEntity> ResultSetToEntity(ResultSet resultSet,
			Class<TData> expectedType) {
		loggerLazyLoading();

		if (resultSet == null || expectedType == null) {
			s_logger.warning("EntitiesResolver received at least one null parameter");
			return null;
		}

		List<IEntity> returningList = null;
		if (expectedType.equals(User.class)) {
			returningList = ResultSetToUserEntities(resultSet);
		} else if (expectedType.equals(Item.class)) {
			returningList = ResultSetToItemEntities(resultSet);
		} else if (expectedType.equals(Costumer.class)) {
			returningList = ResultSetToCostumerEntities(resultSet);
		} else if (expectedType.equals(Complaint.class)) {
			returningList = ResultSetToComplaintEntities(resultSet);
		} else if (expectedType.equals(ComplaintsReport.class)) {
			returningList = ResultSetToComplaintsReportEntities(resultSet);
		} else if (expectedType.equals(IncomesReport.class)) {
			returningList = ResultSetToIncomesReportEntities(resultSet);
		} else if (expectedType.equals(ItemInReservation.class)) {
			returningList = ResultSetToItemInReservationEntities(resultSet);
		} else if (expectedType.equals(ItemInShop.class)) {
			returningList = ResultSetToItemInShopEntities(resultSet);
		} else if (expectedType.equals(Reservation.class)) {
			returningList = ResultSetToReservationEntities(resultSet);
		} else if (expectedType.equals(ReservationsReport.class)) {
			returningList = ResultSetToReservationsReportEntities(resultSet);
		} else if (expectedType.equals(ShopEmployee.class)) {
			returningList = ResultSetToShopEmployeeEntities(resultSet);
		} else if (expectedType.equals(ShopManager.class)) {
			returningList = ResultSetToShopManagerEntities(resultSet);
		} else if (expectedType.equals(Survey.class)) {
			returningList = ResultSetToSurveyEntities(resultSet);
		} else if (expectedType.equals(SurveysReport.class)) {
			returningList = ResultSetToSurveysReportEntities(resultSet);
		} else if (expectedType.equals(ShopSurvey.class)) {
			returningList = ResultSetToShopSurveysEntities(resultSet);
		} else if (expectedType.equals(ShopCostumer.class)) {
			returningList = ResultSetToShopCostumersEntities(resultSet);
		}

		return returningList;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ShopCostumer} entity that describes a shop catalog.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ShopCostumer} entity.
	 * @return An {@link List} of {@link ShopCostumer} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */

	private static List<IEntity> ResultSetToShopCostumersEntities(ResultSet resultSet) {
		ArrayList<IEntity> ShopCostumerEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ShopCostumer shopCostumer = new ShopCostumer();
				try {
					shopCostumer.setCostumerId(resultSet.getInt(1));
					shopCostumer.setShopManagerId(resultSet.getInt(2));
					shopCostumer
							.setCostumerSubscription(Enum.valueOf(CostumerSubscription.class, resultSet.getString(3)));
					shopCostumer.setCreditCard(resultSet.getString(4));
					Date sqlDate = resultSet.getDate(5);
					if (sqlDate != null) {
						shopCostumer.setSubscriptionStartDate(new java.util.Date(sqlDate.getTime()));
					} else {
						shopCostumer.setSubscriptionStartDate(null);
					}
					ShopCostumerEntities.add(shopCostumer);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Shop Costumer entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Shop Costumer entity.");
		}

		return ShopCostumerEntities.isEmpty() ? null : ShopCostumerEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ShopSurvey} entity that describes a shop catalog.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ShopSurvey} entity.
	 * @return An {@link List} of {@link ShopSurvey} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToShopSurveysEntities(ResultSet resultSet) {
		ArrayList<IEntity> shopManagerEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ShopSurvey shopSurvey = new ShopSurvey();
				try {
					shopSurvey.setId(resultSet.getInt(1));
					shopSurvey.setSurveyId(resultSet.getInt(2));
					shopSurvey.setShopManagerId(resultSet.getInt(3));
					Date sqlDate = resultSet.getDate(4);
					if (sqlDate != null) {
						shopSurvey.setStartDate(new java.util.Date(sqlDate.getTime()));
					} else {
						shopSurvey.setStartDate(null);
					}
					shopSurvey.setAnswer1(resultSet.getInt(5));
					shopSurvey.setAnswer2(resultSet.getInt(6));
					shopSurvey.setAnswer3(resultSet.getInt(7));
					shopSurvey.setAnswer4(resultSet.getInt(8));
					shopSurvey.setAnswer5(resultSet.getInt(9));
					shopSurvey.setAnswer6(resultSet.getInt(10));
					shopSurvey.setNumberOfAnswers(resultSet.getInt(11));
					shopSurvey.setSummary(resultSet.getString(12));
					shopSurvey.setClosed(resultSet.getInt(13) == 1 ? true : false);
					shopManagerEntities.add(shopSurvey);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Shop Survey entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Shop Survey entity.");
		}

		return shopManagerEntities.isEmpty() ? null : shopManagerEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link Item} entity that describes a shop catalog.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link Item} entity.
	 * @return An {@link List} of {@link Item} entity if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	public static List<IEntity> ResultSetToShopCatalog(ResultSet resultSet) {
		ArrayList<IEntity> itemEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Item item = new Item();
				try {
					item.setId(resultSet.getInt(1));
					item.setName(resultSet.getString(2));
					item.setType(Enum.valueOf(ProductType.class, resultSet.getString(3)));
					float originalPrice = resultSet.getFloat(4);
					float discountedPrice = resultSet.getFloat(5);
					item.setPrice(discountedPrice == 0 ? originalPrice : discountedPrice);
					Blob blob = resultSet.getBlob(6);
					if (blob != null) {
						InputStream inputStream = blob.getBinaryStream();
						item.setImage(inputStream);
					}
					item.setDomainColor(resultSet.getString(7));
					itemEntities.add(item);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning(
					"Failed to resolve an ResultSet to Item entity of shop catalog, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Item entity of shop catalog.");
		}

		return itemEntities.isEmpty() ? null : itemEntities;

	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link SurveysReport} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link SurveysReport} entity.
	 * @return An {@link List} of {@link SurveysReport} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToSurveysReportEntities(ResultSet resultSet) {
		ArrayList<IEntity> surveysReportEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				SurveysReport surveyReport = new SurveysReport();
				try {
					surveyReport.setShopManagerId(resultSet.getInt(1));
					Date mysqlDate = resultSet.getDate(2);
					if (mysqlDate != null) {
						surveyReport.setYear(new java.util.Date(mysqlDate.getTime()));
					} else {
						surveyReport.setYear(null);
					}
					surveyReport.setQuarter(resultSet.getInt(3));
					surveyReport.setFirstAnswerAverage(resultSet.getFloat(4));
					surveyReport.setSecondAnswerAverage(resultSet.getFloat(5));
					surveyReport.setThirdAnswerAverage(resultSet.getFloat(6));
					surveyReport.setFourthAnswerAverage(resultSet.getFloat(7));
					surveyReport.setFifthAnswerAverage(resultSet.getFloat(8));
					surveyReport.setSixthAnswerAverage(resultSet.getFloat(9));
					surveysReportEntities.add(surveyReport);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to SurveysReport entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to SurveysReport entity");
		}
		return surveysReportEntities.isEmpty() ? null : surveysReportEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link Survey} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link Survey} entity.
	 * @return An {@link List} of {@link Survey} entity if the resolving succeed,
	 *         and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToSurveyEntities(ResultSet resultSet) {

		ArrayList<IEntity> surveyEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Survey survey = new Survey();
				try {
					survey.setId(resultSet.getInt(1));
					survey.setFirstQuestion(resultSet.getString(2));
					survey.setSecondQuestion(resultSet.getString(3));
					survey.setThirdQuestion(resultSet.getString(4));
					survey.setFourthQuestion(resultSet.getString(5));
					survey.setFifthQuestion(resultSet.getString(6));
					survey.setSixthQuestion(resultSet.getString(7));
					surveyEntities.add(survey);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Survey entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Survey entity");
		}
		return surveyEntities.isEmpty() ? null : surveyEntities;

	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ShopManager} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ShopManager} entity.
	 * @return An {@link List} of {@link ShopManager} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToShopManagerEntities(ResultSet resultSet) {
		ArrayList<IEntity> shopManagerEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ShopManager shopManager = new ShopManager();
				try {
					shopManager.setId(resultSet.getInt(1));
					shopManager.setUserName(resultSet.getString(2));
					shopManager.setName(resultSet.getString(3));
					shopManagerEntities.add(shopManager);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to ShopManager entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ShopManager entity");
		}
		return shopManagerEntities.isEmpty() ? null : shopManagerEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ShopEmployee} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ShopEmployee} entity.
	 * @return An {@link List} of {@link ShopEmployee} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToShopEmployeeEntities(ResultSet resultSet) {
		ArrayList<IEntity> shopEmployeeEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ShopEmployee shopEmployee = new ShopEmployee();
				try {
					shopEmployee.setId(resultSet.getInt(1));
					shopEmployee.setUserName(resultSet.getString(2));
					shopEmployee.setShopManagerId(resultSet.getInt(3));
					shopEmployeeEntities.add(shopEmployee);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to ShopEmployee entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ShopEmployee entity");
		}
		return shopEmployeeEntities.isEmpty() ? null : shopEmployeeEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ReservationsReport} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ReservationsReport} entity.
	 * @return An {@link List} of {@link ReservationsReport} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToReservationsReportEntities(ResultSet resultSet) {
		ArrayList<IEntity> reservationsReportEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ReservationsReport reservationsReport = new ReservationsReport();
				try {
					reservationsReport.setShopManagerId(resultSet.getInt(1));
					Date mysqlDate = resultSet.getDate(2);
					if (mysqlDate != null) {
						reservationsReport.setYear(new java.util.Date(mysqlDate.getTime()));
					} else {
						reservationsReport.setYear(null);
					}
					reservationsReport.setQuarter(resultSet.getInt(3));

					reservationsReport.setNumberOfOrderedFlowerPotsInFirstMonth(resultSet.getInt(4));
					reservationsReport.setNumberOfOrderedFlowerPotsInFirstMonth(resultSet.getInt(5));
					reservationsReport.setNumberOfOrderedFlowerArrangementsInFirstMonth(resultSet.getInt(6));
					reservationsReport.setNumberOfOrderedBridalBouquetsInFirstMonth(resultSet.getInt(7));

					reservationsReport.setNumberOfOrderedFlowerPotsInSecondMonth(resultSet.getInt(8));
					reservationsReport.setNumberOfOrderedFlowerPotsInSecondMonth(resultSet.getInt(9));
					reservationsReport.setNumberOfOrderedFlowerArrangementsInSecondMonth(resultSet.getInt(10));
					reservationsReport.setNumberOfOrderedBridalBouquetsInSecondMonth(resultSet.getInt(11));

					reservationsReport.setNumberOfOrderedFlowerPotsInThirdMonth(resultSet.getInt(12));
					reservationsReport.setNumberOfOrderedFlowerPotsInThirdMonth(resultSet.getInt(13));
					reservationsReport.setNumberOfOrderedFlowerArrangementsInThirdMonth(resultSet.getInt(14));
					reservationsReport.setNumberOfOrderedBridalBouquetsInThirdMonth(resultSet.getInt(15));

					reservationsReportEntities.add(reservationsReport);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning(
					"Failed to resolve an ResultSet to ReservationsReport entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ReservationsReport entity");
		}
		return reservationsReportEntities.isEmpty() ? null : reservationsReportEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ItemInShop} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ItemInShop} entity.
	 * @return An {@link List} of {@link ItemInShop} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToItemInShopEntities(ResultSet resultSet) {
		ArrayList<IEntity> itemInShopEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ItemInShop itemInShop = new ItemInShop();
				try {
					itemInShop.setShopManagerId(resultSet.getInt(1));
					itemInShop.setItemId(resultSet.getInt(2));
					itemInShop.setDiscountedPrice(resultSet.getFloat(3));
					itemInShopEntities.add(itemInShop);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to ItemInShop entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ItemInShop entity");
		}
		return itemInShopEntities.isEmpty() ? null : itemInShopEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ItemInReservation} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ItemInReservation} entity.
	 * @return An {@link List} of {@link ItemInReservation} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToItemInReservationEntities(ResultSet resultSet) {
		ArrayList<IEntity> itemInReservationEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ItemInReservation itemInReservation = new ItemInReservation();
				try {
					itemInReservation.setReservationId(resultSet.getInt(1));
					itemInReservation.setItemId(resultSet.getInt(2));
					itemInReservation.setItemName(resultSet.getString(3));
					itemInReservation.setQuantity(resultSet.getInt(4));
					itemInReservation.setPrice(resultSet.getFloat(5));
					itemInReservationEntities.add(itemInReservation);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to ItemInReservation entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ItemInReservation entity");
		}
		return itemInReservationEntities.isEmpty() ? null : itemInReservationEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link IncomesReport} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link IncomesReport} entity.
	 * @return An {@link List} of {@link IncomesReport} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToIncomesReportEntities(ResultSet resultSet) {
		ArrayList<IEntity> incomesReportEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				IncomesReport incomesReport = new IncomesReport();
				try {
					incomesReport.setShopManagerId(resultSet.getInt(1));
					Date mysqlDate = resultSet.getDate(2);
					if (mysqlDate != null) {
						incomesReport.setYear(new java.util.Date(mysqlDate.getTime()));
					} else {
						incomesReport.setYear(null);
					}
					incomesReport.setQuarter(resultSet.getInt(3));
					incomesReport.setIncomesInFirstMonth(resultSet.getFloat(4));
					incomesReport.setIncomesInSecondMonth(resultSet.getFloat(5));
					incomesReport.setIncomesInThirdMonth(resultSet.getFloat(6));
					incomesReportEntities.add(incomesReport);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to IncomesReport entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to IncomesReport entity");
		}
		return incomesReportEntities.isEmpty() ? null : incomesReportEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ComplaintsReport} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ComplaintsReport} entity.
	 * @return An {@link List} of {@link ComplaintsReport} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToComplaintsReportEntities(ResultSet resultSet) {
		ArrayList<IEntity> complaintsReportEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ComplaintsReport complaintReport = new ComplaintsReport();
				try {
					complaintReport.setShopManagerId(resultSet.getInt(1));
					Date mysqlDate = resultSet.getDate(2);
					if(mysqlDate != null) {
					complaintReport.setYear(new java.util.Date(mysqlDate.getTime()));}
					else {
						complaintReport.setYear(null);
					}
					complaintReport.setQuarter(resultSet.getInt(3));
					complaintReport.setNumberOfComplaintsFirstMonth(resultSet.getInt(4));
					complaintReport.setNumberOfComplaintsSecondMonth(resultSet.getInt(5));
					complaintReport.setNumberOfComplaintsThirdMonth(resultSet.getInt(6));
					complaintsReportEntities.add(complaintReport);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to ComplaintReport entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ComplaintReport entity");
		}
		return complaintsReportEntities.isEmpty() ? null : complaintsReportEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link Complaint} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link Complaint} entity.
	 * @return An {@link List} of {@link Complaint} entity if the resolving succeed,
	 *         and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToComplaintEntities(ResultSet resultSet) {
		ArrayList<IEntity> complaintsEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Complaint complaint = new Complaint();
				try {
					complaint.setId(resultSet.getInt(1));
					complaint.setCostumerId(resultSet.getInt(2));
					complaint.setShopManagerId(resultSet.getInt(3));
					Date mysqlDate = resultSet.getDate(4);
					if(mysqlDate!= null) {
					complaint.setCreationDate(new java.util.Date(mysqlDate.getTime()));}
					else {
						complaint.setCreationDate(null);
					}
					complaint.setComplaint(resultSet.getString(5));
					complaint.setSummary(resultSet.getString(6));
					complaint.setOpened(resultSet.getString(7).equals("0") ? false : true);
					complaintsEntities.add(complaint);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Complaint entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Complaint entity");
		}
		return complaintsEntities.isEmpty() ? null : complaintsEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link Item} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link Item} entity.
	 * @return An {@link List} of {@link Item} entity. if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToItemEntities(ResultSet resultSet) {

		ArrayList<IEntity> itemEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Item item = new Item();
				try {
					item.setId(resultSet.getInt(1));
					item.setName(resultSet.getString(2));
					item.setType(Enum.valueOf(ProductType.class, resultSet.getString(3)));
					item.setPrice(resultSet.getFloat(4));
					Blob blob = resultSet.getBlob(5);
					if (blob != null) {
						InputStream inputStream = blob.getBinaryStream();
						item.setImage(inputStream);
					}
					item.setDomainColor(resultSet.getString(6));
					itemEntities.add(item);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Item entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Item entity");
		}

		return itemEntities.isEmpty() ? null : itemEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link Costumer} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link Costumer} entity.
	 * @return An {@link List} of {@link Costumer} entity if the resolving succeed,
	 *         and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToCostumerEntities(ResultSet resultSet) {

		ArrayList<IEntity> costumerEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Costumer costumer = new Costumer();
				try {
					costumer.setId(resultSet.getInt(1));
					costumer.setUserName(resultSet.getString(2));
					costumer.setBalance(resultSet.getFloat(3));
					costumerEntities.add(costumer);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Costumer entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Costumer entity");
		}
		return costumerEntities.isEmpty() ? null : costumerEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link User} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link User} entity.
	 * @return An {@link List} of {@link User} entity if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToUserEntities(ResultSet resultSet) {

		ArrayList<IEntity> userEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				User user = new User();
				try {
					user.setUserName(resultSet.getString(1));
					user.setPassword(resultSet.getString(2));
					user.setEmail(resultSet.getString(3));
					user.setPrivilege(Enum.valueOf(UserPrivilege.class, resultSet.getString(4)));
					user.setStatus(Enum.valueOf(UserStatus.class, resultSet.getString(5)));
					userEntities.add(user);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to UserEntity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to User entity");
		}
		return userEntities.isEmpty() ? null : userEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link Reservation} entity.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link Reservation} entity.
	 * @return An {@link List} of {@link Reservation} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> ResultSetToReservationEntities(ResultSet resultSet) {

		ArrayList<IEntity> reservationEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Reservation reservation = new Reservation();
				try {
					reservation.setId(resultSet.getInt(1));
					reservation.setCostumerId(resultSet.getInt(2));
					reservation.setShopManagerId(resultSet.getInt(3));
					reservation.setCreditCard(resultSet.getString(4));
					reservation.setType(Enum.valueOf(ReservationType.class, resultSet.getString(5)));
					reservation.setNumberOfItems(resultSet.getInt(6));
					reservation.setPrice(resultSet.getFloat(7));
					reservation.setBlessingCard(resultSet.getString(8));
					Date mysqlDate = resultSet.getDate(9);
					if(mysqlDate != null) {
					reservation.setDeliveryDate(new java.util.Date(mysqlDate.getTime()));}
					else {
						reservation.setDeliveryDate(null);
					}
					reservation.setDeliveryType(Enum.valueOf(ReservationDeliveryType.class, resultSet.getString(10)));
					reservation.setDeliveryAddress(resultSet.getString(11));
					reservation.setDeliveryPhone(resultSet.getString(12));
					reservation.setDeliveryName(resultSet.getString(13));
					reservationEntities.add(reservation);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Reservation entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Reservation entity");
		}
		return reservationEntities.isEmpty() ? null : reservationEntities;
	}

}
