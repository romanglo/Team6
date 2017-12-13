package db;

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
		if (id <= 1 || name == null || name.isEmpty() || productType == null) {
			return null;
		}

		// Update only product type
		if (name == null || name.isEmpty() && productType != null) {
			return "UPDATE product SET pType = \"" + productType.toString() + "\" WHERE pId = " + id + ';';
		}

		// Update only name
		if (!(name == null || name.isEmpty()) && productType == null) {
			return "UPDATE product SET  pName = \"" + name + "\" WHERE pId = " + id + ';';
		}

		// Update all states
		return "UPDATE product SET  pName = \"" + name + "\" , pType = \"" + productType.toString() + "\" WHERE pId = "
				+ id + ';';
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
		if (id <= 1) {
			return null;
		}
		return "SELECT * FROM product WHERE pId = " + id + ';';
	}
	
}
