DROP SCHEMA IF EXISTS `zer-li`;

CREATE SCHEMA `zer-li`;

USE `zer-li`;

CREATE TABLE items (
	iId INT NOT NULL AUTO_INCREMENT,
	iName VARCHAR(20) NOT NULL,
	iType VARCHAR(20) NOT NULL,
	iPrice FLOAT NOT NULL,
	iImage BLOB,
	PRIMARY KEY (iId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users (
	uUserName VARCHAR(20) NOT NULL,
	uPassword VARCHAR(20) NOT NULL,
	uEmail VARCHAR(40) NOT NULL,
	uPrivilege VARCHAR(20) NOT NULL,
	uConnected VARCHAR(12) NOT NULL DEFAULT 'disconnected' ,
	PRIMARY KEY (uUserName)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES items WRITE;

INSERT INTO items (iName,iType,iPrice) VALUES ('Rose','Flower',1.0),('Sunflower','Flower',2.0),('Lily','Flower',3.0),('Anemone','Flower',4.0),('Aconite','Flower',5.0),('Balloon Flower','Flower',6.0),('Canterbury Bells','Flower',7.0),('Dusty Miller','Flower',8.0),('Epimedium','Flower',9.0),('Fennel','Flower',10.0),('Gaillardia','Flower',11.0),('Hepatica','Flower',12.0),('Iris','Flower',13.0),('Lavender','Flower',14.0),('Marigold','Flower',15.0),('Orchid','Flower',16.0);

LOCK TABLES users WRITE;

INSERT INTO users (uUserName,uPassword,uEmail,uPrivilege) VALUES ('companyemployee','companyemployee','companyemployee@local','companyemployee'),('shopmanager','shopmanager','shopmanager@local','shopmanager'),('chainmanager','chainmanager','chainmanager@local','chainmanager'),('administrator','administrator','administrator@local','administrator'),('shopemployee','shopemployee','shopemployee@local','shopemployee'),('costumerservice','costumerservice','costumerservice@local','costumerservice'),('costumer','costumer','costumer@local','costumer'),('servicespecialist','servicespecialist','servicespecialist@local','servicespecialist');

UNLOCK TABLES;
