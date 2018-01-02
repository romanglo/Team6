DROP SCHEMA IF EXISTS `zer-li`;

CREATE SCHEMA `zer-li`;

USE `zer-li`;

CREATE TABLE items (
	iId INT NOT NULL AUTO_INCREMENT,
	iName VARCHAR(20) NOT NULL,
	iType VARCHAR(20) NOT NULL,
	iPrice FLOAT NOT NULL,
	iImage BLOB NULL DEFAULT NULL,
	iDomainColor VARCHAR(20) NULL DEFAULT NULL,
	iDeleted BIT(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (iId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users (
	uUserName VARCHAR(20) NOT NULL,
	uPassword VARCHAR(20) NOT NULL,
	uEmail VARCHAR(40) NOT NULL,
	uPrivilege VARCHAR(20) NOT NULL,
	uStatus VARCHAR(12) NOT NULL DEFAULT 'Disconnected' ,
	PRIMARY KEY (uUserName)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE costumers (
  cId INT NOT NULL AUTO_INCREMENT,
  uUserName VARCHAR(20) NOT NULL,
  cCreditCard VARCHAR(16) NULL DEFAULT NULL,
  cCostumerSubscription VARCHAR(7) NULL DEFAULT 'None',
  cRefund FLOAT NOT NULL DEFAULT 0,
  INDEX uUserName_idx (uUserName ASC),
  PRIMARY KEY (uUserName),
  UNIQUE INDEX cId_UNIQUE (cId ASC),
  CONSTRAINT uUserName FOREIGN KEY (uUserName) REFERENCES users (uUserName) ON DELETE CASCADE ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE reservations (
  rId INT NOT NULL AUTO_INCREMENT,
  cId INT NOT NULL,
  rType VARCHAR(7) NULL DEFAULT 'Open',
  rItems VARCHAR(100) NULL DEFAULT NULL,
  rPrice FLOAT NOT NULL DEFAULT 0,
  PRIMARY KEY (rId, cId),
  INDEX cId_idx (cId ASC),
  CONSTRAINT cId FOREIGN KEY (cId) REFERENCES costumers (cId) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES items WRITE;

INSERT INTO items (iName,iType,iPrice,iDomainColor) VALUES 
('Rose','Flower',1.0,'red'),
('Sunflower','Flower',2.0,'yellow'),
('Lily','Flower',3.0,'white'),
('Anemone','Flower',4.0,'red'),
('Aconite','Flower',5.0, 'purple'),
('Balloon Flower','Flower',6.0,'purple'),
('Canterbury Bells','Flower',7.0,'pink'),
('Dusty Miller','Flower',8.0,'white'),
('Epimedium','Flower',9.0,'yellow'),
('Fennel','Flower',10.0,'white'),
('Gaillardia','Flower',11.0,'yellow'),
('Hepatica','Flower',12.0,'purple'),
('Iris','Flower',13.0,'purple'),
('Lavender','Flower',14.0,'purple'),
('Marigold','Flower',15.0,'orange'),
('Orchid','Flower',16.0,'pink');

LOCK TABLES users WRITE;

INSERT INTO users (uUserName,uPassword,uEmail,uPrivilege) VALUES 
('companyemployee','companyemployee','companyemployee@local','CompanyEmployee'),
('shopmanager','shopmanager','shopmanager@local','ShopManager'),
('chainmanager','chainmanager','chainmanager@local','ChainManager'),
('administrator','administrator','administrator@local','Administrator'),
('shopemployee','shopemployee','shopemployee@local','ShopEmployee'),
('costumerservice','costumerservice','costumerservice@local','CostumerService'),
('costumer','costumer','costumer@local','Costumer'),
('servicespecialist','servicespecialist','servicespecialist@local','ServiceSpecialist');

LOCK TABLES costumers WRITE;

INSERT INTO costumers (uUserName) VALUES
('costumer');

UNLOCK TABLES;         
