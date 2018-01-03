package db;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import entities.CostumerEntity;
import entities.CostumerSubscription;
import entities.IEntity;
import entities.ItemEntity;
import entities.ProductType;
import entities.ReservationDeliveryType;
import entities.ReservationEntity;
import entities.ReservationType;
import entities.UserEntity;
import entities.UserPrivilege;
import entities.UserStatus;
import javafx.scene.image.Image;
import logger.LogManager;
import utilities.ImageUtilities;

/**
 *
 * EntitiesResolver: A class that can resolve and create an {@link List} of
 * instances of {@link IEntity} from received element.
 * 
 */
public class EntitiesResolver {

	private static Logger s_logger = null;

	private static void loggerLazyLoading() {
		if (s_logger == null) {
			s_logger = LogManager.getLogger();
		}
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ItemEntity}.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link ItemEntity}.
	 * @return An {@link List} of {@link UserEntity} if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	public static List<IEntity> ResultSetToItemEntities(ResultSet resultSet) {
		loggerLazyLoading();

		if (resultSet == null) {
			s_logger.warning("EntitiesResolver received null ResultSet parameter");
			return null;
		}

		ArrayList<IEntity> itemEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				try {
					int id = resultSet.getInt(1);
					String name = resultSet.getString(2);
					ProductType productType = Enum.valueOf(ProductType.class, resultSet.getString(3));
					float price = resultSet.getFloat(4);
					Blob blob = resultSet.getBlob(5);
					String domainColor = resultSet.getString(6);
					ItemEntity toAdd;
					if (blob != null) {
						InputStream inputStream = blob.getBinaryStream();
						Image image = ImageUtilities.InputStreamToImage(inputStream, s_logger);
						toAdd = new ItemEntity(id, name, productType, (double) price, domainColor, image);
					} else {
						toAdd = new ItemEntity(id, name, productType, (double) price, domainColor, null);
					}
					itemEntities.add(toAdd);
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to ItemEntity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to ItemEntity");
		}

		return itemEntities.isEmpty() ? null : itemEntities;

	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link CostumerEntity}.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link CostumerEntity}.
	 * @return An {@link List} of {@link CostumerEntity} if the resolving succeed,
	 *         and <code>null</code> if did not.
	 */
	public static List<IEntity> ResultSetToCostumerEntities(ResultSet resultSet) {
		loggerLazyLoading();

		if (resultSet == null) {
			s_logger.warning("EntitiesResolver received null ResultSet parameter");
			return null;
		}

		ArrayList<IEntity> costumerEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				try {
					int id = resultSet.getInt(1);
					String creditCard = resultSet.getString(3);
					CostumerSubscription costumerSubscription = CostumerSubscription.valueOf(resultSet.getString(4));
					float refund = resultSet.getFloat(5);
					costumerEntities.add(new CostumerEntity(id, creditCard, costumerSubscription, (double) refund));
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to CostumerEntity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to CostumerEntity");
		}
		return costumerEntities.isEmpty() ? null : costumerEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link UserEntity}.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link UserEntity}.
	 * @return An {@link List} of {@link UserEntity} if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	public static List<IEntity> ResultSetToUserEntities(ResultSet resultSet) {
		loggerLazyLoading();

		if (resultSet == null) {
			s_logger.warning("EntitiesResolver received null ResultSet parameter");
			return null;
		}

		ArrayList<IEntity> userEntities = new ArrayList<>();
		int failedResolve = 0;
		try {
			while (resultSet.next()) {
				try {
					String username = resultSet.getString(1);
					String password = resultSet.getString(2);
					String email = resultSet.getString(3);

					UserPrivilege userPrivilege = Enum.valueOf(UserPrivilege.class, resultSet.getString(4));
					UserStatus userStatus = Enum.valueOf(UserStatus.class, resultSet.getString(5));

					userEntities.add(new UserEntity(username, password, email, userPrivilege, userStatus));
				} catch (Exception ignored) {
					failedResolve++;
				}
			}
		} catch (SQLException e) {
			s_logger.warning("Failed to resolve an ResultSet to UserEntity, exception:" + e.getMessage());
			return null;
		}

		if (failedResolve != 0) {
			s_logger.warning("Failed to resolve " + failedResolve + " rows to UserEntity");
		}
		return userEntities.isEmpty() ? null : userEntities;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link ReservationEntity}.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link UserEntity}.
	 * @param costumerId
	 *            if the resolving succeed this array in place 0 will contain the
	 *            costumer ID.
	 * @param itemsList
	 *            if the resolving succeed this parameter will contain a
	 *            {@link List} with {@link ItemEntity} IDs.
	 * @return A {@link ReservationEntity} if the resolving succeed, and
	 *         <code>null</code> if did not.
	 */
	public static IEntity ResultSetToReservationEntities(ResultSet resultSet, String[] costumerId,
			List<String> itemsList) {
		loggerLazyLoading();

		if (resultSet == null) {
			s_logger.warning("EntitiesResolver received null ResultSet parameter");
			return null;
		}

		IEntity reservationEntity = null;

		try {
			int reservationId = resultSet.getInt(1);
			costumerId[0] = resultSet.getString(2);
			ReservationType reservationType = Enum.valueOf(ReservationType.class, resultSet.getString(3));

			String itemsString = resultSet.getString(4);
			String[] items = itemsString.split(",");
			itemsList.addAll(Arrays.asList(items));
			float price = resultSet.getFloat(5);
			Date mysqlDate = resultSet.getDate(6);
			java.util.Date javaDate = new java.util.Date(mysqlDate.getTime());
			ReservationDeliveryType deliveryType = Enum.valueOf(ReservationDeliveryType.class, resultSet.getString(7));
			String BlessingCard = resultSet.getString(8);
			reservationEntity = new ReservationEntity(reservationId, reservationType, (double) price, javaDate,
					deliveryType, BlessingCard);
		} catch (Exception e) {
			s_logger.warning("Failed to resolve an ResultSet to ReservationEntity, exception:" + e.getMessage());
		}
		return reservationEntity;
	}

	/**
	 * The method received {@link ResultSet} and resolve it to {@link List} of
	 * {@link UserEntity}.
	 * 
	 * @param <TData>
	 *            The expected {@link IEntity} that the {@link ResultSet} contains.
	 *
	 * @param resultSet
	 *            A {@link ResultSet} which will be resolved in it to {@link List}
	 *            of {@link UserEntity}.
	 * @param expectedType
	 *            An {@link Class} of the expected {@link IEntity} that the
	 *            {@link ResultSet} contains.
	 * @return An {@link List} of {@link UserEntity} if the resolving succeed, and
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
		if (expectedType.equals(UserEntity.class)) {
			returningList = ResultSetToUserEntities(resultSet);
		} else if (expectedType.equals(ItemEntity.class)) {
			returningList = ResultSetToItemEntities(resultSet);
		} else if (expectedType.equals(CostumerEntity.class)) {
			returningList = ResultSetToCostumerEntities(resultSet);
		}

		return returningList;
	}
}
