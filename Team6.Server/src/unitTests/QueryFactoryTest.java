package unitTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import db.QueryFactory;
import entities.IEntity;
import entities.ProductEntity;
import entities.ProductType;

class QueryFactoryTest {

	@Test
	void getQueryTest() {
		IEntity entity = new ProductEntity(1,"TestFlower",ProductType.Flower);
		String generateGetEntityQuery = QueryFactory.generateGetEntityQuery(entity);
		assertTrue("SELECT * FROM product WHERE pId = 1 ;".equalsIgnoreCase(generateGetEntityQuery));
		
		entity =  new ProductEntity(1,ProductType.Flower);
		generateGetEntityQuery = QueryFactory.generateGetEntityQuery(entity);
		assertTrue("SELECT * FROM product WHERE pId = 1 ;".equalsIgnoreCase(generateGetEntityQuery));

		entity =  new ProductEntity(1,"TestFlower");
		generateGetEntityQuery = QueryFactory.generateGetEntityQuery(entity);
		assertTrue("SELECT * FROM product WHERE pId = 1 ;".equalsIgnoreCase(generateGetEntityQuery));
		
		entity =  new ProductEntity(-1);
		assertNull(QueryFactory.generateGetEntityQuery(entity));
	}

	@Test
	void updateQueryTest() {
		IEntity entity = new ProductEntity(1,"TestFlower",ProductType.Flower);
		String generateGetEntityQuery = QueryFactory.generateUpdateEntityQuery(entity);
		assertTrue("UPDATE product SET pName = \"TestFlower\" , pType = \"Flower\" WHERE pId = 1 ;".equalsIgnoreCase(generateGetEntityQuery));
		
		entity =  new ProductEntity(1,ProductType.Flower);
		generateGetEntityQuery = QueryFactory.generateUpdateEntityQuery(entity);
		assertTrue("UPDATE product SET pType = \"Flower\" WHERE pId = 1 ;".equalsIgnoreCase(generateGetEntityQuery));

		entity =  new ProductEntity(1,"TestFlower");
		generateGetEntityQuery = QueryFactory.generateUpdateEntityQuery(entity);
		assertTrue("UPDATE product SET pName = \"TestFlower\" WHERE pId = 1 ;".equalsIgnoreCase(generateGetEntityQuery));
		
		entity =  new ProductEntity(1);
		assertNull(QueryFactory.generateUpdateEntityQuery(entity));
		
		entity =  new ProductEntity(-1);
		assertNull(QueryFactory.generateUpdateEntityQuery(entity));
	}
}
