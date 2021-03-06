package db;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import entities.EntitiesEnums.CostumerSubscription;
import entities.EntitiesEnums.ProductType;
import entities.EntitiesEnums.ReservationDeliveryType;
import entities.EntitiesEnums.ReservationType;
import entities.EntitiesEnums.UserPrivilege;
import entities.EntitiesEnums.UserStatus;
import logger.LogManager;

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
	public static <TData extends IEntity> List<IEntity> resultSetToEntity(ResultSet resultSet,
			Class<TData> expectedType) {
		loggerLazyLoading();

		if (resultSet == null || expectedType == null) {
			s_logger.warning("EntitiesResolver received at least one null parameter");
			return null;
		}

		List<IEntity> returningList = null;
		if (expectedType.equals(User.class)) {
			returningList = resultSetToUserEntities(resultSet);
		} else if (expectedType.equals(Item.class)) {
			returningList = resultSetToItemEntities(resultSet);
		} else if (expectedType.equals(Costumer.class)) {
			returningList = resultSetToCostumerEntities(resultSet);
		} else if (expectedType.equals(Complaint.class)) {
			returningList = resultSetToComplaintEntities(resultSet);
		} else if (expectedType.equals(ComplaintsReport.class)) {
			returningList = resultSetToComplaintsReportEntities(resultSet);
		} else if (expectedType.equals(IncomesReport.class)) {
			returningList = resultSetToIncomesReportEntities(resultSet);
		} else if (expectedType.equals(ItemInReservation.class)) {
			returningList = resultSetToItemInReservationEntities(resultSet);
		} else if (expectedType.equals(ItemInShop.class)) {
			returningList = resultSetToItemInShopEntities(resultSet);
		} else if (expectedType.equals(Reservation.class)) {
			returningList = resultSetToReservationEntities(resultSet);
		} else if (expectedType.equals(ReservationsReport.class)) {
			returningList = resultSetToReservationsReportEntities(resultSet);
		} else if (expectedType.equals(ShopEmployee.class)) {
			returningList = resultSetToShopEmployeeEntities(resultSet);
		} else if (expectedType.equals(ShopManager.class)) {
			returningList = resultSetToShopManagerEntities(resultSet);
		} else if (expectedType.equals(Survey.class)) {
			returningList = resultSetToSurveyEntities(resultSet);
		} else if (expectedType.equals(SurveysReport.class)) {
			returningList = resultSetToSurveysReportEntities(resultSet);
		} else if (expectedType.equals(SurveyResult.class)) {
			returningList = resultSetToSurveyResultEntities(resultSet);
		} else if (expectedType.equals(ShopCostumer.class)) {
			returningList = resultSetToShopCostumerEntities(resultSet);
		} else if (expectedType.equals(CostumerServiceEmployee.class)) {
			returningList = resultSetToCostumerServiceEmployeeEntities(resultSet);
		}

		return returningList;
	}

	private static List<IEntity> resultSetToCostumerServiceEmployeeEntities(ResultSet resultSet) {
		ArrayList<IEntity> ShopCostumerEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				CostumerServiceEmployee costumerServiceEmployee = new CostumerServiceEmployee();
				try {
					costumerServiceEmployee.setId(resultSet.getInt(1));
					costumerServiceEmployee.setUserName(resultSet.getString(2));
					ShopCostumerEntities.add(costumerServiceEmployee);
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
	 * {@link ShopCostumer} entity that describes a shop catalog.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ShopCostumer} entity.
	 * @return An {@link List} of {@link ShopCostumer} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> resultSetToShopCostumerEntities(ResultSet resultSet) {
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
					shopCostumer.setCumulativePrice(resultSet.getFloat(6));
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
	 * {@link SurveyResult} entity that describes a shop catalog.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link SurveyResult} entity.
	 * @return An {@link List} of {@link SurveyResult} entity if the resolving
	 *         succeed, and <code>null</code> if did not.
	 */
	private static List<IEntity> resultSetToSurveyResultEntities(ResultSet resultSet) {
		ArrayList<IEntity> surveyResultsEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				SurveyResult surveyResult = new SurveyResult();
				try {
					surveyResult.setId(resultSet.getInt(1));
					surveyResult.setSurveyId(resultSet.getInt(2));
					Date sqlDate = resultSet.getDate(3);
					if (sqlDate != null) {
						surveyResult.setEnterDate(new java.util.Date(sqlDate.getTime()));
					} else {
						surveyResult.setEnterDate(null);
					}
					surveyResult.setFirstAnswer(resultSet.getInt(4));
					surveyResult.setSecondAnswer(resultSet.getInt(5));
					surveyResult.setThirdAnswer(resultSet.getInt(6));
					surveyResult.setFourthanswer(resultSet.getInt(7));
					surveyResult.setFifthAnswer(resultSet.getInt(8));
					surveyResult.setSixthAnswer(resultSet.getInt(9));
					surveyResult.setSummary(resultSet.getString(10));
					surveyResultsEntities.add(surveyResult);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to Shop Survey entity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to Survey Result entity.");
		}

		return surveyResultsEntities.isEmpty() ? null : surveyResultsEntities;
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
	private static List<IEntity> resultSetToSurveysReportEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToSurveyEntities(ResultSet resultSet) {

		ArrayList<IEntity> surveyEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Survey survey = new Survey();
				try {
					survey.setId(resultSet.getInt(1));
					survey.setManagerId(resultSet.getInt(2));
					Date mysqlDate = resultSet.getDate(3);
					if (mysqlDate != null) {
						survey.setStartDate(new java.util.Date(mysqlDate.getTime()));
					} else {
						survey.setStartDate(null);
					}
					mysqlDate = resultSet.getDate(4);
					if (mysqlDate != null) {
						survey.setEndDate(new java.util.Date(mysqlDate.getTime()));
					} else {
						survey.setEndDate(null);
					}
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
	private static List<IEntity> resultSetToShopManagerEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToShopEmployeeEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToReservationsReportEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToItemInShopEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToItemInReservationEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToIncomesReportEntities(ResultSet resultSet) {
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
	private static List<IEntity> resultSetToComplaintsReportEntities(ResultSet resultSet) {
		ArrayList<IEntity> complaintsReportEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				ComplaintsReport complaintReport = new ComplaintsReport();
				try {
					complaintReport.setShopManagerId(resultSet.getInt(1));
					Date mysqlDate = resultSet.getDate(2);
					if (mysqlDate != null) {
						complaintReport.setYear(new java.util.Date(mysqlDate.getTime()));
					} else {
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
	private static List<IEntity> resultSetToComplaintEntities(ResultSet resultSet) {
		ArrayList<IEntity> complaintsEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				Complaint complaint = new Complaint();
				try {
					complaint.setId(resultSet.getInt(1));
					complaint.setCostumerId(resultSet.getInt(2));
					complaint.setShopManagerId(resultSet.getInt(3));
					complaint.setCostumerServiceEmployeeId(resultSet.getInt(4));
					Date mysqlDate = resultSet.getDate(5);
					if (mysqlDate != null) {
						complaint.setCreationDate(new java.util.Date(mysqlDate.getTime()));
					} else {
						complaint.setCreationDate(null);
					}
					complaint.setComplaint(resultSet.getString(6));
					complaint.setSummary(resultSet.getString(7));
					complaint.setOpened(resultSet.getString(8).equals("0") ? false : true);
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
	private static List<IEntity> resultSetToItemEntities(ResultSet resultSet) {

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
	private static List<IEntity> resultSetToCostumerEntities(ResultSet resultSet) {

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
	private static List<IEntity> resultSetToUserEntities(ResultSet resultSet) {

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
					Blob blob = resultSet.getBlob(6);
					if (blob != null) {
						InputStream inputStream = blob.getBinaryStream();
						user.setImage(inputStream);
					}
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
	private static List<IEntity> resultSetToReservationEntities(ResultSet resultSet) {

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
					Timestamp mysqlDateTime = resultSet.getTimestamp(9);
					if (mysqlDateTime != null) {
						reservation.setDeliveryDate(new java.util.Date(mysqlDateTime.getTime()));
					} else {
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
