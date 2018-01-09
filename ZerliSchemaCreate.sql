UNLOCK TABLES;         

DROP SCHEMA IF EXISTS `zer-li`;

CREATE SCHEMA `zer-li`;

USE `zer-li`;

CREATE TABLE users (
	uUserName VARCHAR(20),
	uPassword VARCHAR(20) NOT NULL,
	uEmail VARCHAR(40) NOT NULL,
	uPrivilege VARCHAR(20) NOT NULL,
	uStatus VARCHAR(12) NOT NULL DEFAULT 'Disconnected',
	PRIMARY KEY (uUserName)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE costumers (
  cId INT AUTO_INCREMENT,
  uUserName VARCHAR(20),
  cBalance FLOAT NOT NULL DEFAULT 0,
  PRIMARY KEY (cId),
  UNIQUE INDEX uUserName_UNIQUE (uUserName ASC),
  FOREIGN KEY (uUserName) REFERENCES users (uUserName) ON DELETE CASCADE ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_managers (
  smId INT AUTO_INCREMENT,
  uUserName VARCHAR(20) NOT NULL,
  FOREIGN KEY (uUserName) REFERENCES users (uUserName) ON DELETE CASCADE ON UPDATE NO ACTION,
  UNIQUE INDEX uUserName_UNIQUE (uUserName ASC),
  PRIMARY KEY (smId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE costumers_in_shops (
	cId INT NOT NULL,
	smId INT NOT NULL,
	csCostumerSubscription VARCHAR(7) NULL DEFAULT 'None',
	FOREIGN KEY (cId) REFERENCES costumers (cId) ON DELETE CASCADE ON UPDATE NO ACTION,
	FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
	PRIMARY KEY (cId,smId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_employees (
  seId INT AUTO_INCREMENT,
  uUserName VARCHAR(20),
  smId INT,
  FOREIGN KEY (uUserName) REFERENCES users (uUserName) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
  UNIQUE INDEX uUserName_UNIQUE (uUserName ASC),
  PRIMARY KEY (seId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE items (
	iId INT AUTO_INCREMENT,
	iName VARCHAR(20) NOT NULL,
	iType VARCHAR(20) NOT NULL,
	iPrice FLOAT NOT NULL,
	iImage BLOB NULL DEFAULT NULL,
	iDomainColor VARCHAR(20) NULL DEFAULT NULL,
	iDeleted BIT(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (iId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE reservations (
  rId INT AUTO_INCREMENT,
  cId INT,
  smId INT,
  rCreditCard VARCHAR(16) NULL DEFAULT NULL,
  rType VARCHAR(7) NULL DEFAULT 'Open',
  rNumberOfItems INT NOT NULL DEFAULT 0,
  rPrice FLOAT NOT NULL DEFAULT 0,
  rBlessingCard VARCHAR(100) NULL DEFAULT NULL,
  rDeliveryDate DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  rDeliveryType VARCHAR(9) NOT NULL DEFAULT 'None',
  rDeliveryAddress VARCHAR(50) NULL DEFAULT NULL,
  rDeliveryPhone VARCHAR(10) NULL DEFAULT NULL,
  rDeliveryName VARCHAR(20) NULL DEFAULT NULL,
  FOREIGN KEY (cId) REFERENCES costumers (cId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (rId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE items_in_reservations(
  rId INT,
  iId INT,
  iName VARCHAR(20) NOT NULL,
  irQuantity INT NOT NULL DEFAULT 1,
  irPrice FLOAT NOT NULL,
  FOREIGN KEY (rId) REFERENCES reservations (rId) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (iId) REFERENCES items (iId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (rId, iId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE items_in_shops(
  smId INT,
  iId INT,
  isDiscountedPrice FLOAT NOT NULL,  
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (iId) REFERENCES items (iId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (smId, iId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE complaints (
  coId INT NOT NULL AUTO_INCREMENT,
  cId INT NOT NULL,
  smId Int NOT NULL,
  coDate DATE NOT NULL DEFAULT '0000-00-00',
  coComplaint VARCHAR(200) NOT NULL,
  coSummary VARCHAR(200) NULL DEFAULT NULL,
  coOpened BIT(1) NOT NULL DEFAULT 1,
  FOREIGN KEY (cId) REFERENCES costumers (cId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (coId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE surveys (
  suId INT AUTO_INCREMENT,
  suQuestion1 VARCHAR(50) NOT NULL,
  suQuestion2 VARCHAR(50) NOT NULL,
  suQuestion3 VARCHAR(50) NOT NULL,
  suQuestion4 VARCHAR(50) NOT NULL,
  suQuestion5 VARCHAR(50) NOT NULL,
  suQuestion6 VARCHAR(50) NOT NULL,
  PRIMARY KEY (suId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE surveys_in_shops (
  ssId INT AUTO_INCREMENT,
  suId INT NOT NULL,
  smId INT NOT NULL,
  saStartDate DATE NOT NULL DEFAULT '0000-00-00',
  saAnswer1 INT NOT NULL DEFAULT 0,
  saAnswer2 INT NOT NULL DEFAULT 0,
  saAnswer3 INT NOT NULL DEFAULT 0,
  saAnswer4 INT NOT NULL DEFAULT 0,
  saAnswer5 INT NOT NULL DEFAULT 0,
  saAnswer6 INT NOT NULL DEFAULT 0,
  saNumberOfAnswers INT NOT NULL DEFAULT 0,
  saSummary VARCHAR(500) NULL DEFAULT NULL, 
  saClosed BIT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (suId) REFERENCES surveys (suId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (ssId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE complaints_reports (
 smId INT NOT NULL,
 crYear YEAR NOT NULL DEFAULT '0000', 
 crQuarter INT NOT NULL DEFAULT 0,
 crMonth1 INT NOT NULL DEFAULT 0,
 crMonth2 INT NOT NULL DEFAULT 0,
 crMonth3 INT NOT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
 PRIMARY KEY(smId,crYear,crQuarter)
);

CREATE TABLE surveys_reports (
 smId INT NOT NULL,
 srYear YEAR NOT NULL DEFAULT '0000', 
 srQuarter FLOAT NOT NULL DEFAULT 0,
 srAnswer1 FLOAT NOT NULL DEFAULT 0,
 srAnswer2 FLOAT NOT NULL DEFAULT 0,
 srAnswer3 FLOAT NOT NULL DEFAULT 0,
 srAnswer4 FLOAT NOT NULL DEFAULT 0,
 srAnswer5 FLOAT NOT NULL DEFAULT 0,
 srAnswer6 FLOAT NOT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
 PRIMARY KEY(smId,srYear,srQuarter)
);

CREATE TABLE incomes_reports (
 smId INT NOT NULL,
 irYear YEAR NOT NULL DEFAULT '0000', 
 irQuarter INT NOT NULL DEFAULT 0,
 irMonth1 FLOAT NOT NULL DEFAULT 0,
 irMonth2 FLOAT NOT NULL DEFAULT 0,
 irMonth3 FLOAT NOT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
 PRIMARY KEY(smId,irYear,irQuarter)
);

CREATE TABLE reservations_reports (
 smId INT NOT NULL,
 rrYear YEAR NOT NULL DEFAULT '0000', 
 rrQuarter INT NOT NULL DEFAULT 0,
 rrMonth1_Flower INT NOT NULL DEFAULT 0,
 rrMonth1_FlowerPot INT NOT NULL DEFAULT 0,
 rrMonth1_FlowerArrangement INT NOT NULL DEFAULT 0,
 rrMonth1_BridalBouquet INT NOT NULL DEFAULT 0,
 rrMonth2_Flower INT NOT NULL DEFAULT 0,
 rrMonth2_FlowerPot INT NOT NULL DEFAULT 0,
 rrMonth2_FlowerArrangement INT NOT NULL DEFAULT 0,
 rrMonth2_BridalBouquet INT NOT NULL DEFAULT 0,
 rrMonth3_Flower INT NOT NULL DEFAULT 0,
 rrMonth3_FlowerPot INT NOT NULL DEFAULT 0,
 rrMonth3_FlowerArrangement INT NOT NULL DEFAULT 0,
 rrMonth3_BridalBouquet INT NOT NULL DEFAULT 0, 
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
 PRIMARY KEY(smId,rrYear,rrQuarter)
); 

--
-- Create views:
--

CREATE VIEW catalog AS
SELECT iId, iName, iType, iPrice, iImage, iDomainColor 
FROM items
WHERE iDeleted = 0;

--
-- Create procedures:
--

DELIMITER //

CREATE PROCEDURE getShopCatalog( shop_manager_id INT )
BEGIN  
	
	(SELECT catalog.iId, catalog.iName, catalog.iType,
	catalog.iPrice,discounted.isDiscountedPrice,	catalog.iImage, catalog.iDomainColor 
	FROM catalog , (SELECT * FROM items_in_shops where smId = shop_manager_id) AS discounted 
	WHERE catalog.iId = discounted.iId)
	UNION
	(SELECT catalog.iId, catalog.iName, catalog.iType,
	catalog.iPrice, 0 , catalog.iImage, catalog.iDomainColor 
	FROM catalog WHERE catalog.iId NOT IN 
    (SELECT items_in_shops.iId FROM items_in_shops where smId = shop_manager_id))
    ORDER BY iID;
END; //

CREATE PROCEDURE getShopSurveyAverage( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE start_month INT;
	DECLARE end_month INT;
   
	IF (quarter = 1) THEN 
		SET start_month = 1;
		SET end_month = 3;
	ELSEIF (quarter = 2) THEN
	    SET start_month = 4;
		SET end_month = 6;
	ELSEIF (quarter = 3) THEN
	    SET start_month = 7;
		SET end_month = 9;
	ELSE
	    SET start_month = 10;
		SET end_month = 12;
	END IF;

	SELECT shop_manager_id AS 'Shop ID', in_year AS 'Year', quarter AS 'Quarter',
			saAnswer1 / saNumberOfAnswers AS 'First Question Average',
			saAnswer2 / saNumberOfAnswers AS 'Second Question Average', 
			saAnswer3 / saNumberOfAnswers AS 'Third Question Average',
			saAnswer4 / saNumberOfAnswers AS 'Fourth Question Average',
			saAnswer5 / saNumberOfAnswers AS 'Fifth Question Average', 
			saAnswer6 / saNumberOfAnswers AS 'Sixth Question Average'
	FROM surveys_in_shops WHERE 
	            smId = shop_manager_id AND YEAR(saStartDate) = in_year AND
	            MONTH(saStartDate) >= start_month AND MONTH(saStartDate) <= end_month;
END; //

CREATE PROCEDURE getShopNumberOfComplaints( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE first_month INT;
	DECLARE second_month INT;
	DECLARE third_month INT;
   
	IF (quarter = 1) THEN 
		SET first_month = 1;
		SET second_month = 2;
		SET third_month = 3;
	ELSEIF (quarter = 2) THEN
	    SET first_month = 4;
	    SET second_month = 5;
		SET third_month = 6;
	ELSEIF (quarter = 3) THEN
	    SET first_month = 7;
	    SET second_month = 8;
		SET third_month = 9;
	ELSE
	    SET first_month = 10;
	    SET second_month = 11;
		SET third_month = 12;
	END IF;

	SELECT shop_manager_id AS 'Shop ID', in_year AS 'Year', quarter AS 'Quarter',
		   (SELECT COUNT(*) FROM complaints WHERE smId = shop_manager_id 
			AND YEAR(rDeliveryDate) = in_year AND MONTH(cDate) = first_month) AS 'First Month',
           (SELECT COUNT(*) FROM complaints WHERE smId = shop_manager_id
			AND YEAR(rDeliveryDate) = in_year AND MONTH(cDate) = second_month) AS 'Second Month',
           (SELECT COUNT(*) FROM complaints WHERE smId = shop_manager_id
			AND YEAR(rDeliveryDate) = in_year AND MONTH(cDate) = third_month) AS 'Third Month'
           FROM complaints;
		    		   
END; //

CREATE PROCEDURE getShopIncomes( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE first_month INT;
	DECLARE second_month INT;
	DECLARE third_month INT;
   
	IF (quarter = 1) THEN 
		SET first_month = 1;
		SET second_month = 2;
		SET third_month = 3;
	ELSEIF (quarter = 2) THEN
	    SET first_month = 4;
	    SET second_month = 5;
		SET third_month = 6;
	ELSEIF (quarter = 3) THEN
	    SET first_month = 7;
	    SET second_month = 8;
		SET third_month = 9;
	ELSE
	    SET first_month = 10;
	    SET second_month = 11;
		SET third_month = 12;
	END IF;

	SELECT shop_manager_id AS 'Shop ID', in_year AS 'Year', quarter AS 'Quarter',
		   (SELECT SUM(rPrice) FROM reservations WHERE smId = shop_manager_id AND
           rType = 'Closed' AND YEAR(rDeliveryDate) = in_year AND MONTH(rDeliveryDate) = first_month) AS 'First Month',
           (SELECT SUM(rPrice) FROM reservations WHERE smId = shop_manager_id AND
           rType = 'Closed' AND YEAR(rDeliveryDate) = in_year AND MONTH(rDeliveryDate) = second_month) AS 'Second Month',
           (SELECT SUM(rPrice) FROM reservations WHERE smId = shop_manager_id AND
           rType = 'Closed' AND YEAR(rDeliveryDate) = in_year AND MONTH(rDeliveryDate) = third_month) AS 'Third Month'
           FROM reservations;
		    		   
END; //

CREATE PROCEDURE getShopReservations( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE start_month INT;
	DECLARE end_month INT;
   
	IF (quarter = 1) THEN 
		SET start_month = 1;
		SET end_month = 3;
	ELSEIF (quarter = 2) THEN
	    SET start_month = 4;
		SET end_month = 6;
	ELSEIF (quarter = 3) THEN
	    SET start_month = 7;
		SET end_month = 9;
	ELSE
	    SET start_month = 10;
		SET end_month = 12;
	END IF;

	
	SELECT shop_manager_id AS 'Shop ID', in_year AS 'Year', quarter AS 'Quarter', 
		(
			SELECT SUM(ItemsInShopReservation.irQuantity) 
            FROM(
				SELECT JoinedTables.irQuantity, JoinedTables.iType 
                FROM(
						SELECT reservations.smId, reservations.rDeliveryDate, reservations.rType ,items_in_reservations.irQuantity, items.iType 
                        FROM reservations 
						INNER JOIN items_in_reservations ON reservations.rId = items_in_reservations.rId
						INNER JOIN items ON items_in_reservations.iId = items.iId
					) AS JoinedTables 
				WHERE 
					JoinedTables.smId = shop_manager_id AND YEAR(JoinedTables.rDeliveryDate) = in_year AND
                    JoinedTables.rType = 'Closed' AND
					MONTH(JoinedTables.rDeliveryDate) >= start_month AND MONTH(JoinedTables.rDeliveryDate) <= end_month
				) AS ItemsInShopReservation
			WHERE ItemsInShopReservation.iType = 'Flower'
		) AS 'Flowers',
        (
			SELECT SUM(ItemsInShopReservation.irQuantity) 
            FROM(
				SELECT JoinedTables.irQuantity, JoinedTables.iType 
                FROM(
						SELECT reservations.smId, reservations.rDeliveryDate, reservations.rType ,items_in_reservations.irQuantity, items.iType 
                        FROM reservations 
						INNER JOIN items_in_reservations ON reservations.rId = items_in_reservations.rId
						INNER JOIN items ON items_in_reservations.iId = items.iId
					) AS JoinedTables 
				WHERE 
					JoinedTables.smId = shop_manager_id AND YEAR(JoinedTables.rDeliveryDate) = in_year AND
                    JoinedTables.rType = 'Closed' AND
					MONTH(JoinedTables.rDeliveryDate) >= start_month AND MONTH(JoinedTables.rDeliveryDate) <= end_month
				) AS ItemsInShopReservation
			WHERE ItemsInShopReservation.iType = 'FlowerPot'
		) AS 'FlowerPots',
        (
			SELECT SUM(ItemsInShopReservation.irQuantity) 
            FROM(
				SELECT JoinedTables.irQuantity, JoinedTables.iType 
                FROM(
						SELECT reservations.smId, reservations.rDeliveryDate, reservations.rType ,items_in_reservations.irQuantity, items.iType 
                        FROM reservations 
						INNER JOIN items_in_reservations ON reservations.rId = items_in_reservations.rId
						INNER JOIN items ON items_in_reservations.iId = items.iId
					) AS JoinedTables 
				WHERE 
					JoinedTables.smId = shop_manager_id AND YEAR(JoinedTables.rDeliveryDate) = in_year AND
                    JoinedTables.rType = 'Closed' AND
					MONTH(JoinedTables.rDeliveryDate) >= start_month AND MONTH(JoinedTables.rDeliveryDate) <= end_month
				) AS ItemsInShopReservation
			WHERE ItemsInShopReservation.iType = 'BridalBouquet'
		) AS 'BridalBouquets',
        (
			SELECT SUM(ItemsInShopReservation.irQuantity) 
            FROM(
				SELECT JoinedTables.irQuantity, JoinedTables.iType 
                FROM(
						SELECT reservations.smId, reservations.rDeliveryDate, reservations.rType ,items_in_reservations.irQuantity, items.iType 
                        FROM reservations 
						INNER JOIN items_in_reservations ON reservations.rId = items_in_reservations.rId
						INNER JOIN items ON items_in_reservations.iId = items.iId
					) AS JoinedTables 
				WHERE 
					JoinedTables.smId = shop_manager_id AND YEAR(JoinedTables.rDeliveryDate) = in_year AND
                    JoinedTables.rType = 'Closed' AND
					MONTH(JoinedTables.rDeliveryDate) >= start_month AND MONTH(JoinedTables.rDeliveryDate) <= end_month
				) AS ItemsInShopReservation
			WHERE ItemsInShopReservation.iType = 'FlowerArrangement'
		) AS 'FlowerArrangements'
    	FROM items_in_reservations GROUP BY shop_manager_id;
        
END; //

--
-- Create triggers:
--

CREATE TRIGGER update_item_trigger
AFTER UPDATE ON items FOR EACH ROW
BEGIN
	IF (NEW.iDeleted = 1) THEN
	
		DELETE FROM items_in_shops WHERE items_in_shops.iId = NEW.iId;
	
	ELSEIF (OLD.iPrice != NEW.iPrice) THEN 
	
		DELETE FROM items_in_shops WHERE items_in_shops.iId = NEW.iId AND items_in_shops.isDiscountedPrice > NEW.iPrice;
	
	END IF;
	
END; //

CREATE TRIGGER insert_surveys_in_shops
BEFORE INSERT ON surveys_in_shops FOR EACH ROW
BEGIN
    IF (NEW.saStartDate = '0000-00-00') THEN 
        SET NEW.saStartDate = NOW();
    END IF;
END; //

CREATE TRIGGER update_surveys_in_shops
AFTER UPDATE ON surveys_in_shops FOR EACH ROW
BEGIN -- FIX IT!
    IF (NEW.saSummary != NULL AND NEW.saClosed = 0) THEN 
        UPDATE surveys_in_shops SET saClosed = 1 WHERE ssId = New.ssId;
    END IF;
END; //

CREATE TRIGGER insert_complaint
BEFORE INSERT ON complaints FOR EACH ROW
BEGIN
    IF (NEW.coDate = '0000-00-00') THEN 
        SET NEW.coDate = NOW();
    END IF;
END; //


CREATE TRIGGER update_complaint
AFTER UPDATE ON complaints FOR EACH ROW
BEGIN -- FIX IT!
    IF (NEW.coSummary != NULL AND NEW.coOpened = 1) THEN 
        UPDATE complaints SET coOpened = 0 WHERE coId = New.coId;
    END IF;
END; //

CREATE TRIGGER insert_reservation
BEFORE INSERT ON reservations FOR EACH ROW
BEGIN
    IF (NEW.rDeliveryDate = '0000-00-00 00:00:00') THEN 
        SET NEW.rDeliveryDate = DATE_ADD(NOW(), INTERVAL 3 HOUR);
    END IF;
END; //

DELIMITER ;

--
-- Insert data to tables:
--

LOCK TABLES items WRITE;
INSERT INTO items (iName,iType,iPrice,iDomainColor) VALUES 
('Rose','Flower',9.0,'red'),
('Sunflower','Flower',15.0,'yellow'),
('Lily','Flower',3.0,'white'),
('Anemone','Flower',5.0,'red'),
('Aconite','Flower',5.0, 'purple'),
('Balloon Flower','Flower',78.0,'purple'),
('Canterbury Bells','Flower',12.0,'pink'),
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

LOCK TABLES shop_managers WRITE;
INSERT INTO shop_managers (uUserName) VALUES
('shopmanager');

LOCK TABLES shop_employees WRITE;
INSERT INTO shop_employees (uUserName,smId) VALUES
('shopemployee',1);

LOCK TABLES items_in_shops WRITE;
INSERT INTO items_in_shops (smId,iId,isDiscountedPrice) VALUES 
(1,1,5),
(1,2,5),
(1,10,5);

LOCK TABLES costumers WRITE;
INSERT INTO costumers (uUserName) VALUES
('costumer');

LOCK TABLES costumers_in_shops WRITE;
INSERT INTO costumers_in_shops (cId,smId) VALUES
(1,1);

LOCK TABLES surveys WRITE;
INSERT INTO surveys (suQuestion1,suQuestion2,suQuestion3,suQuestion4,suQuestion5,suQuestion6) VALUES
('Question 1','Question 2', 'Question 3', 'Question 4', 'Question 5', 'Question 6');

LOCK TABLES surveys_in_shops WRITE;
INSERT INTO surveys_in_shops (smId,suId,saAnswer1,saAnswer2,saAnswer3,saAnswer4,saAnswer5,saAnswer6,saNumberOfAnswers) VALUES
(1,1,4,4,4,4,4,4,2);

LOCK TABLES reservations WRITE;
INSERT INTO reservations (cId, smId,rCreditCard, rType, rNumberOfItems, rPrice, rBlessingCard, rDeliveryType, rDeliveryAddress, rDeliveryPhone, rDeliveryName) VALUES 
(1,1,'1234123412341234','Closed',2,10, 'Happy Birthday', 'Immediate','Ort Braude','049981111','Dolev');

LOCK TABLES items_in_reservations WRITE;
INSERT INTO items_in_reservations (rId, iId, iName, irQuantity, irPrice) VALUES 
(1,4,'Anemone',1,5),
(1,5,'Aconite',1,5);

LOCK TABLES complaints WRITE;
INSERT INTO complaints (cId,smId ,coComplaint ) VALUES 
(1,1,'The received product is not as orders.');

UNLOCK TABLES;         
