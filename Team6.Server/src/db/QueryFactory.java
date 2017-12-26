package db;

import java.text.MessageFormat;

import entities.IEntity;
import entities.ProductEntity;
import entities.ProductType;

/**
 *
 * QueryGenerator: The class is a factory of queries for {@link IEntity} types.
 *
 */
public class QueryFactory {

	/**
	 * 
	 * The method generate update query.
	 *
	 * @param entity
	 *            The entity to update.
	 * @return String query, <code>null</<code> if something wrong.
	 */
	public static String generateUpdateEntityQuery(IEntity entity) {
		String result = null;
		if (entity instanceof ProductEntity) {
			result = generateUpdateEntityQuery((ProductEntity) entity);
		}
		return result;
	}

	private static String generateUpdateEntityQuery(ProductEntity productEntity) {
		int id = productEntity.getId();
		String name = productEntity.getName();
		ProductType productType = productEntity.getProductType();

		// Wrong details
		if (id < 1 || ((name == null || name.isEmpty()) && productType == null)) {
			return null;
		}

		// Update only product type
		if ((name == null || name.isEmpty()) && productType != null) {
			return MessageFormat.format("UPDATE products SET pType = \"{0}\" WHERE pId = {1} ;", productType.toString(),
					id);
		}

		// Update only name
		if (!(name == null || name.isEmpty()) && productType == null) {
			return MessageFormat.format("UPDATE products SET pName = \"{0}\" WHERE pId = {1} ;", name, id);
		}

		// Update all states
		return MessageFormat.format("UPDATE products SET pName = \"{0}\" , pType = \"{1}\" WHERE pId = {2} ;", name,
				productType.toString(), id);
	}

	/**
	 * 
	 * The method generate get specific <{@link IEntity} query.
	 *
	 * @param entity
	 *            The entity need to has the id of the entity to get.
	 * @return String query, <code>null</<code> if something wrong.
	 */
	public static String generateGetEntityQuery(IEntity entity) {
		String result = null;
		if (entity instanceof ProductEntity) {
			result = generateGetEntityQuery((ProductEntity) entity);
		}
		return result;
	}

	private static String generateGetEntityQuery(ProductEntity productEntity) {
		int id = productEntity.getId();

		// Wrong ID
		if (id < 1) {
			return null;
		}

		return MessageFormat.format("SELECT * FROM products WHERE pId = {0} ;", id);
	}

}
