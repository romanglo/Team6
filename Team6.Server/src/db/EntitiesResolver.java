package db;

import java.sql.ResultSet;

import entities.IEntity;
import entities.ProductEntity;
import entities.ProductType;;

/**
 *
 * EntitiesResolver: A class that can resolve and create an instance of
 * {@link IEntity} from received element.
 * 
 * 
 */
public class EntitiesResolver {

	/**
	 * 
	 * TODO Roman: Auto-generated comment stub - Change it!
	 *
	 * @param resultSet
	 *            the data to resolve.
	 * @param expectedType
	 *            An object of the kind supposed to return.
	 * @return An instance of expected type if the resolving succeed,
	 *         <code>null</code> if does not.
	 * @throws NullPointerException
	 *             if one of the received parameters is null.
	 */
	public static IEntity resolveResultSet(ResultSet resultSet, IEntity expectedType) throws NullPointerException {
		if (resultSet == null) {
			throw new NullPointerException("ResultSet is null!");
		}
		if (expectedType == null) {
			throw new NullPointerException("IEntity is null!");
		}
		IEntity returnEntity = null;
		if (expectedType instanceof ProductEntity) {
			returnEntity = ResultSetToProduct(resultSet);
		}
		return returnEntity;

	}

	private static IEntity ResultSetToProduct(ResultSet resultSet) {
		try {
			int id;
			String name;
			ProductType productType;
			if (resultSet.next()) {
				id = resultSet.getInt(1);
				name = resultSet.getString(2);
				String productTypeString = resultSet.getString(3);
				productType = Enum.valueOf(ProductType.class, productTypeString);
				return new ProductEntity(id, name, productType);
			}
		} catch (Exception ignored) {

		}
		return null;
	}
}
