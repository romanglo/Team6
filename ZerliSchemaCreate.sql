DROP SCHEMA IF EXISTS `zer-li`;

CREATE SCHEMA `zer-li`;

USE `zer-li`;

CREATE TABLE products (
	pId INT NOT NULL,
	pName VARCHAR(20) NOT NULL,
	pType VARCHAR(20) NOT NULL,
	PRIMARY KEY (pId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users (
	uName VARCHAR(10) NOT NULL,
	uPassword VARCHAR(10) NOT NULL,
	uPrivilege VARCHAR(20) NOT NULL,
	uConnected TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	PRIMARY KEY (uName)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES product WRITE;

INSERT INTO product VALUES (1,'Rose','Flower'),(2,'Sunflower','Flower'),(3,'Lily','Flower'),(4,'Anemone','Flower'),(5,'Aconite','Flower'),(6,'Balloon Flower','Flower'),(7,'Canterbury Bells','Flower'),(8,'Dusty Miller','Flower'),(9,'Epimedium','Flower'),(10,'Fennel','Flower'),(11,'Gaillardia','Flower'),(12,'Hepatica','Flower'),(13,'Iris','Flower'),(14,'Lavender','Flower'),(15,'Marigold','Flower'),(16,'Orchid','Flower');

INSERT INTO users VALUES ('admin','admin','administrator',0);

UNLOCK TABLES;
