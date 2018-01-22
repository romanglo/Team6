UNLOCK TABLES;  
       
SET SQL_SAFE_UPDATES = 0;

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
  smName VARCHAR(20) NULL DEFAULT 'Zer-Li',
  FOREIGN KEY (uUserName) REFERENCES users (uUserName) ON DELETE CASCADE ON UPDATE NO ACTION,
  UNIQUE INDEX uUserName_UNIQUE (uUserName ASC),
  PRIMARY KEY (smId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE costumers_in_shops (
	cId INT NOT NULL,
	smId INT NOT NULL,
	csCostumerSubscription VARCHAR(7) NULL DEFAULT 'None',
	csCreditCard VARCHAR(16) NULL DEFAULT NULL,
	csStartSubscriptionDate DATE NULL DEFAULT NULL,
	csCumulativePrice FLOAT NOT NULL DEFAULT 0,
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

CREATE TABLE costumer_service_employees (
  cseId INT AUTO_INCREMENT,
  uUserName VARCHAR(20),
  FOREIGN KEY (uUserName) REFERENCES users (uUserName) ON DELETE CASCADE ON UPDATE NO ACTION,
  UNIQUE INDEX uUserName_UNIQUE (uUserName ASC),
  PRIMARY KEY (cseId)
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
  rType VARCHAR(8) NULL DEFAULT 'Open',
  rNumberOfItems INT NOT NULL DEFAULT 0,
  rPrice FLOAT NOT NULL DEFAULT 0,
  rBlessingCard VARCHAR(100) NULL DEFAULT NULL,
  rDeliveryDate DATETIME NULL DEFAULT NULL,
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
  cId INT,
  smId INT,
  cseId INT,
  coDate DATE  NULL DEFAULT NULL,
  coComplaint VARCHAR(200) NOT NULL,
  coSummary VARCHAR(200) NULL DEFAULT NULL,
  coOpened BIT(1) NOT NULL DEFAULT 1,
  FOREIGN KEY (cId) REFERENCES costumers (cId) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (cseId) REFERENCES costumer_service_employees (cseId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (coId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE surveys (
  suId INT AUTO_INCREMENT,
  smId INT,
  suStartDate DATE NULL DEFAULT NULL,
  suEndDate DATE NULL DEFAULT NULL,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
  PRIMARY KEY (suId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE survey_results (
  srId INT AUTO_INCREMENT,
  suId INT,
  srEnterDate DATE NULL DEFAULT NULL,
  srAnswer1 INT NOT NULL,
  srAnswer2 INT NOT NULL,
  srAnswer3 INT NOT NULL,
  srAnswer4 INT NOT NULL,
  srAnswer5 INT NOT NULL,
  srAnswer6 INT NOT NULL,
  srSummary VARCHAR(500) NULL DEFAULT NULL, 
  FOREIGN KEY (suId) REFERENCES surveys (suId) ON DELETE CASCADE ON UPDATE NO ACTION,
  PRIMARY KEY (srId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE complaints_reports (
 smId INT,
 crYear YEAR, 
 crQuarter INT NOT NULL DEFAULT 0,
 crMonth1 INT NULL DEFAULT 0,
 crMonth2 INT NULL DEFAULT 0,
 crMonth3 INT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
 PRIMARY KEY(smId,crYear,crQuarter)
);

CREATE TABLE surveys_reports (
 smId INT,
 srYear YEAR, 
 srQuarter INT NOT NULL DEFAULT 0,
 srAnswer1 FLOAT NULL DEFAULT 0,
 srAnswer2 FLOAT NULL DEFAULT 0,
 srAnswer3 FLOAT NULL DEFAULT 0,
 srAnswer4 FLOAT NULL DEFAULT 0,
 srAnswer5 FLOAT NULL DEFAULT 0,
 srAnswer6 FLOAT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
 PRIMARY KEY(smId,srYear,srQuarter)
);

CREATE TABLE incomes_reports (
 smId INT,
 irYear YEAR, 
 irQuarter INT NOT NULL DEFAULT 0,
 irMonth1 FLOAT NULL DEFAULT 0,
 irMonth2 FLOAT NULL DEFAULT 0,
 irMonth3 FLOAT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
 PRIMARY KEY(smId,irYear,irQuarter)
);

CREATE TABLE reservations_reports (
 smId INT,
 rrYear YEAR , 
 rrQuarter INT NOT NULL DEFAULT 0,
 rrMonth1_Flower INT NULL DEFAULT 0,
 rrMonth1_FlowerPot INT NULL DEFAULT 0,
 rrMonth1_FlowerArrangement INT NULL DEFAULT 0,
 rrMonth1_BridalBouquet INT NULL DEFAULT 0,
 rrMonth2_Flower INT NULL DEFAULT 0,
 rrMonth2_FlowerPot INT NULL DEFAULT 0,
 rrMonth2_FlowerArrangement INT NULL DEFAULT 0,
 rrMonth2_BridalBouquet INT NULL DEFAULT 0,
 rrMonth3_Flower INT NULL DEFAULT 0,
 rrMonth3_FlowerPot INT NULL DEFAULT 0,
 rrMonth3_FlowerArrangement INT NULL DEFAULT 0,
 rrMonth3_BridalBouquet INT NULL DEFAULT 0, 
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
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

CREATE PROCEDURE checkCostumersSubsription()
BEGIN

	UPDATE costumers_in_shops SET 
		csCostumerSubscription='None', csCreditCard= NULL,
		csStartSubscriptionDate=NULL, csCumulativePrice =0 
		WHERE csCostumerSubscription = 'Monthly' AND DATE_ADD(csStartSubscriptionDate, INTERVAL 1 MONTH) > NOW();
		
	UPDATE costumers_in_shops SET 
		csCostumerSubscription='None', csCreditCard= NULL,
		csStartSubscriptionDate=NULL, csCumulativePrice =0 
		WHERE csCostumerSubscription = 'Yearly' AND DATE_ADD(csStartSubscriptionDate, INTERVAL 1 YEAR) > NOW();
		
END; //

CREATE PROCEDURE generateSurveysReport( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE end_month INT;
    
	SET end_month = quarter * 3;

	INSERT INTO surveys_reports (smId, srYear,srQuarter,srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6)   
		SELECT  shop_manager_id, in_year, quarter,
				sum(srAnswer1) / count(srAnswer1),
				sum(srAnswer2) / count(srAnswer2), 
				sum(srAnswer3) / count(srAnswer3),
				sum(srAnswer4) / count(srAnswer4),
				sum(srAnswer5) / count(srAnswer5), 
				sum(srAnswer6) / count(srAnswer6) 
		FROM 	(survey_results JOIN surveys ON survey_results.suId = surveys.suId) 
        WHERE 	smId = shop_manager_id AND YEAR(srEnterDate) = in_year AND
				MONTH(srEnterDate) >= (end_month - 2) AND MONTH(srEnterDate) <= end_month;
						
END; //

CREATE PROCEDURE generateComplaintsReport( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE end_month INT;
    
	SET end_month = quarter * 3;

	INSERT INTO complaints_reports (smId, crYear,crQuarter,crMonth1,crMonth2,crMonth3)   
		SELECT shop_manager_id, in_year, quarter,
			   (SELECT COUNT(*) FROM complaints WHERE smId = shop_manager_id 
					AND YEAR(coDate) = in_year AND MONTH(coDate) = (end_month - 2)),
			   (SELECT COUNT(*) FROM complaints WHERE smId = shop_manager_id
					AND YEAR(coDate) = in_year AND MONTH(coDate) = (end_month - 1)),
			   (SELECT COUNT(*) FROM complaints WHERE smId = shop_manager_id
					AND YEAR(coDate) = in_year AND MONTH(coDate) = end_month)
		FROM complaints;
		    		   
END; //

CREATE PROCEDURE generateIncomesReport( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
	DECLARE end_month INT;
    
	SET end_month = quarter * 3;

	INSERT INTO incomes_reports (smId, irYear,irQuarter,irMonth1,irMonth2,irMonth3)
		SELECT shop_manager_id, in_year, quarter,
			   (SELECT SUM(rPrice) FROM reservations WHERE smId = shop_manager_id AND
			   rType = 'Closed' AND YEAR(rDeliveryDate) = in_year AND MONTH(rDeliveryDate) = (end_month - 2)),
			   (SELECT SUM(rPrice) FROM reservations WHERE smId = shop_manager_id AND
			   rType = 'Closed' AND YEAR(rDeliveryDate) = in_year AND MONTH(rDeliveryDate) = (end_month - 1)),
			   (SELECT SUM(rPrice) FROM reservations WHERE smId = shop_manager_id AND
			   rType = 'Closed' AND YEAR(rDeliveryDate) = in_year AND MONTH(rDeliveryDate) = end_month)
	    FROM reservations;
		    		   
END; //

CREATE PROCEDURE generateReservationsReport( shop_manager_id INT , in_year YEAR , quarter INT)
BEGIN  
	
    DECLARE end_month INT;
   
	SET end_month = quarter * 3;

	DROP TABLE IF EXISTS ItemsInShopReservation;
	CREATE TABLE ItemsInShopReservation(irQuantity INT, iType VARCHAR(20), in_month INT);
	INSERT INTO ItemsInShopReservation(irQuantity,iType,in_month)
			SELECT JoinedTables.irQuantity, JoinedTables.iType ,MONTH(JoinedTables.rDeliveryDate) 
			FROM(
					SELECT reservations.smId, reservations.rDeliveryDate, reservations.rType ,items_in_reservations.irQuantity, items.iType 
					FROM reservations 
					INNER JOIN items_in_reservations ON reservations.rId = items_in_reservations.rId
					INNER JOIN items ON items_in_reservations.iId = items.iId
				) AS JoinedTables 
			WHERE 
					JoinedTables.smId = shop_manager_id AND YEAR(JoinedTables.rDeliveryDate) = in_year AND
                    JoinedTables.rType = 'Closed' AND
					MONTH(JoinedTables.rDeliveryDate) >= (end_month - 2) AND MONTH(JoinedTables.rDeliveryDate) <= end_month;

	INSERT INTO reservations_reports (smId,rrYear,rrQuarter,
									  rrMonth1_Flower,rrMonth1_FlowerPot,rrMonth1_FlowerArrangement,rrMonth1_BridalBouquet,
									  rrMonth2_Flower,rrMonth2_FlowerPot,rrMonth2_FlowerArrangement,rrMonth2_BridalBouquet,
								      rrMonth3_Flower,rrMonth3_FlowerPot,rrMonth3_FlowerArrangement,rrMonth3_BridalBouquet)
		SELECT shop_manager_id, in_year, quarter, 
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation 
				WHERE ItemsInShopReservation.iType = 'Flower' AND ItemsInShopReservation.in_month = (end_month -2)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'FlowerPot' AND ItemsInShopReservation.in_month = (end_month -2)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'BridalBouquet' AND ItemsInShopReservation.in_month = (end_month -2)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'FlowerArrangement' AND ItemsInShopReservation.in_month = (end_month -2)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation 
				WHERE ItemsInShopReservation.iType = 'Flower' AND ItemsInShopReservation.in_month = (end_month -1)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'FlowerPot' AND ItemsInShopReservation.in_month = (end_month -1)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'BridalBouquet' AND ItemsInShopReservation.in_month = (end_month -1)),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'FlowerArrangement' AND ItemsInShopReservation.in_month = (end_month -1)),
               (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation 
				WHERE ItemsInShopReservation.iType = 'Flower' AND ItemsInShopReservation.in_month = end_month),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'FlowerPot' AND ItemsInShopReservation.in_month = end_month),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'BridalBouquet' AND ItemsInShopReservation.in_month = end_month),
			   (SELECT SUM(ItemsInShopReservation.irQuantity) 
				FROM ItemsInShopReservation
				WHERE ItemsInShopReservation.iType = 'FlowerArrangement' AND ItemsInShopReservation.in_month = end_month)
		FROM items_in_reservations GROUP BY shop_manager_id;
			
	DROP TABLE ItemsInShopReservation;
    
END; //

--
-- Create triggers:
--

CREATE TRIGGER after_update_item_trigger
AFTER UPDATE ON items FOR EACH ROW
BEGIN
	IF (NEW.iDeleted = 1) THEN
	
		DELETE FROM items_in_shops WHERE items_in_shops.iId = NEW.iId;
	
	ELSEIF (OLD.iPrice != NEW.iPrice) THEN 
	
		DELETE FROM items_in_shops WHERE items_in_shops.iId = NEW.iId AND items_in_shops.isDiscountedPrice > NEW.iPrice;
	
	END IF;
	
END; //

CREATE TRIGGER insert_costumers_in_shops_trigger
BEFORE INSERT ON costumers_in_shops FOR EACH ROW
BEGIN
    IF (NEW.csCostumerSubscription != 'None') THEN 
        
        IF (NEW.csCreditCard IS NULL) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Costumer credit card could not be null when subscription exist.';
        END IF;
        
        IF (NEW.csStartSubscriptionDate = '0000-00-00' OR NEW.csStartSubscriptionDate IS NULL) THEN
			SET NEW.csStartSubscriptionDate = NOW();
        END IF;
        
		IF (NEW.csCumulativePrice != 0) THEN 
			SET NEW.csCumulativePrice = 0;
		END IF; 
	ELSE
    
		SET NEW.csCreditCard = NULL;
		SET NEW.csStartSubscriptionDate = NULL;
        SET NEW.csCumulativePrice = 0;
		
    END IF;
END; //

CREATE TRIGGER update_costumers_in_shops_trigger
BEFORE UPDATE ON costumers_in_shops FOR EACH ROW
BEGIN
    IF (NEW.csCostumerSubscription != 'None') THEN 
        
        IF (NEW.csCreditCard IS NULL) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Costumer credit card could not be null when subscription exist.';
        END IF;
        
        IF (NEW.csStartSubscriptionDate = '0000-00-00' OR NEW.csStartSubscriptionDate IS NULL) THEN
			SET NEW.csStartSubscriptionDate = NOW();
        END IF;
        
		IF (OLD.csCumulativePrice > NEW.csCumulativePrice) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The new cumulative price could not be lowest the old one.';
		END IF;
		
	ELSE
    
		SET NEW.csCreditCard = NULL;
		SET NEW.csStartSubscriptionDate = NULL;
        SET NEW.csCumulativePrice = 0;
		
    END IF;
END; //

CREATE TRIGGER insert_surveys_trigger
BEFORE INSERT ON surveys FOR EACH ROW
BEGIN
	IF (NEW.suEndDate = '0000-00-00' OR NEW.suEndDate IS NULL) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey end date could not be null or 0000-00-00.';
	END IF ;

    IF (NEW.suStartDate = '0000-00-00' OR NEW.suStartDate IS NULL) THEN 
        SET NEW.suStartDate = NOW();
    END IF;
	
	IF(NEW.suStartDate > NEW.suEndDate) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey end date must be after the start date.';
	END IF;
END; //

CREATE TRIGGER update_surveys_trigger
BEFORE UPDATE ON surveys FOR EACH ROW
BEGIN
	IF (NEW.suEndDate = '0000-00-00' OR NEW.suEndDate IS NULL OR NEW.suStartDate = '0000-00-00' OR NEW.suStartDate IS NULL) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey end date could not be null or 0000-00-00 at update operation.';
	END IF ;
	
	IF(NEW.suStartDate > NEW.suEndDate) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey end date must be after the start date.';
	END IF;
END; //

CREATE TRIGGER insert_survey_results_trigger
BEFORE INSERT ON survey_results FOR EACH ROW
BEGIN 
	IF (NEW.srEnterDate = '0000-00-00' OR NEW.srEnterDate IS NULL) THEN 
        SET NEW.srEnterDate = NOW();
    END IF;
	
	IF (NEW.srEnterDate > (SELECT surveys.suEndDate  FROM surveys WHERE NEW.suId = surveys.suId )) THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey enter date could not be after survey closing date';
    END IF;
	
	IF (NEW.srEnterDate < (SELECT surveys.suStartDate  FROM surveys WHERE NEW.suId = surveys.suId )) THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey enter date could not be before survey starting date';
    END IF;
END; //

CREATE TRIGGER update_survey_results_trigger
BEFORE UPDATE ON survey_results FOR EACH ROW
BEGIN 
    IF (NEW.srEnterDate != OLD.srEnterDate) THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey result enter date could not be change';
    END IF;
	
	IF (NEW.srEnterDate > (SELECT surveys.suEndDate  FROM surveys WHERE NEW.suId = surveys.suId )) THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey enter date could not be after survey closing date';
    END IF;
	
	IF (NEW.srEnterDate < (SELECT surveys.suStartDate  FROM surveys WHERE NEW.suId = surveys.suId )) THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Survey enter date could not be before survey starting date';
    END IF;
END; //

CREATE TRIGGER insert_complaint_trigger
BEFORE INSERT ON complaints FOR EACH ROW
BEGIN
    IF (NEW.coDate = '0000-00-00' OR NEW.coDate IS NULL) THEN 
        SET NEW.coDate = NOW();
    END IF;
END; //

CREATE TRIGGER update_complaint_trigger
AFTER UPDATE ON complaints FOR EACH ROW
BEGIN 
    IF (NEW.coSummary IS NOT NULL AND NEW.coOpened = 1) THEN 
        UPDATE complaints SET coOpened = 0 WHERE coId = New.coId;
    END IF;
END; //

CREATE TRIGGER insert_reservation_trigger
BEFORE INSERT ON reservations FOR EACH ROW
BEGIN
    IF (NEW.rDeliveryDate = '0000-00-00 00:00:00'  OR NEW.rDeliveryDate IS NULL) THEN 
        SET NEW.rDeliveryDate = DATE_ADD(NOW(), INTERVAL 3 HOUR);
    END IF;
END; //

CREATE TRIGGER insert_users_trigger
AFTER INSERT ON users FOR EACH ROW
BEGIN
    IF (NEW.uPrivilege = 'Costumer') THEN 
        INSERT INTO costumers (uUserName) VALUES (NEW.uUserName);
	ELSEIF (NEW.uPrivilege = 'ShopEmployee') THEN
        INSERT INTO shop_employees (uUserName) VALUES (NEW.uUserName);
	ELSEIF (NEW.uPrivilege = 'ShopManager') THEN
        INSERT INTO shop_managers (uUserName) VALUES (NEW.uUserName);
	ELSEIF (NEW.uPrivilege = 'CostumerService') THEN
        INSERT INTO costumer_service_employees (uUserName) VALUES (NEW.uUserName);
	END IF;
END; //
	
CREATE TRIGGER update_users_trigger
AFTER UPDATE ON users FOR EACH ROW
BEGIN
	IF(OLD.uPrivilege != NEW.uPrivilege) THEN
	
		IF(OLD.uPrivilege = 'Costumer') THEN
			DELETE FROM costumers WHERE costumers.uUserName = NEW.uUserName;
		ELSEIF (OLD.uPrivilege = 'ShopEmployee') THEN
			DELETE FROM shop_employees WHERE shop_employees.uUserName = NEW.uUserName;
		ELSEIF (OLD.uPrivilege = 'ShopManager') THEN
			DELETE FROM shop_managers WHERE shop_managers.uUserName = NEW.uUserName;
		ELSEIF (OLD.uPrivilege = 'CostumerService') THEN
			DELETE FROM costumer_service_employees WHERE costumer_service_employees.uUserName = NEW.uUserName;
		END IF;
		
		IF (NEW.uPrivilege = 'Costumer') THEN 
			INSERT INTO costumers (uUserName) VALUES (NEW.uUserName);
		ELSEIF (OLD.uPrivilege = 'ShopEmployee') THEN
			INSERT INTO shop_employees (uUserName) VALUES (NEW.uUserName);
		ELSEIF (OLD.uPrivilege = 'ShopManager') THEN
			INSERT INTO shop_managers (uUserName) VALUES (NEW.uUserName);
		ELSEIF (OLD.uPrivilege = 'CostumerService') THEN
			INSERT INTO costumer_service_employees (uUserName) VALUES (NEW.uUserName);
		END IF;
	
    END IF;
END; //

CREATE TRIGGER insert_complaints_reports_trigger
BEFORE INSERT ON complaints_reports FOR EACH ROW
BEGIN
    IF (NEW.crMonth1 IS NULL) THEN 
        SET NEW.crMonth1  = 0 ;
    END IF;
	IF (NEW.crMonth2 IS NULL) THEN 
        SET NEW.crMonth2  = 0 ;
    END IF;
	IF (NEW.crMonth3 IS NULL) THEN 
        SET NEW.crMonth3  = 0 ;
    END IF;
END; //

CREATE TRIGGER insert_surveys_reports_trigger
BEFORE INSERT ON surveys_reports FOR EACH ROW
BEGIN
    IF (NEW.srAnswer1 IS NULL) THEN 
        SET NEW.srAnswer1  = 0 ;
    END IF;
	IF (NEW.srAnswer2 IS NULL) THEN 
        SET NEW.srAnswer2  = 0 ;
    END IF;
	IF (NEW.srAnswer3 IS NULL) THEN 
        SET NEW.srAnswer3  = 0 ;
    END IF;
	IF (NEW.srAnswer4 IS NULL) THEN 
        SET NEW.srAnswer4  = 0 ;
    END IF;
	IF (NEW.srAnswer5 IS NULL) THEN 
        SET NEW.srAnswer5  = 0 ;
    END IF;
    IF (NEW.srAnswer6 IS NULL) THEN 
        SET NEW.srAnswer6  = 0 ;
    END IF;
END; //

CREATE TRIGGER insert_incomes_reports_trigger
BEFORE INSERT ON incomes_reports FOR EACH ROW
BEGIN
    IF (NEW.irMonth1 IS NULL) THEN 
        SET NEW.irMonth1  = 0 ;
    END IF;
	IF (NEW.irMonth2 IS NULL) THEN 
        SET NEW.irMonth2  = 0 ;
    END IF;
	IF (NEW.irMonth3 IS NULL) THEN 
        SET NEW.irMonth3  = 0 ;
    END IF;
END; //

CREATE TRIGGER insert_reservations_reports_trigger
BEFORE INSERT ON reservations_reports FOR EACH ROW
BEGIN
    IF (NEW.rrMonth1_Flower IS NULL) THEN 
        SET NEW.rrMonth1_Flower  = 0 ;
    END IF;
	IF (NEW.rrMonth1_FlowerPot IS NULL) THEN 
        SET NEW.rrMonth1_FlowerPot  = 0 ;
    END IF;
	IF (NEW.rrMonth1_FlowerArrangement IS NULL) THEN 
        SET NEW.rrMonth1_FlowerArrangement  = 0 ;
    END IF;
    IF (NEW.rrMonth1_BridalBouquet IS NULL) THEN 
        SET NEW.rrMonth1_BridalBouquet  = 0 ;
    END IF;
    
     IF (NEW.rrMonth2_Flower IS NULL) THEN 
        SET NEW.rrMonth2_Flower  = 0 ;
    END IF;
	IF (NEW.rrMonth2_FlowerPot IS NULL) THEN 
        SET NEW.rrMonth2_FlowerPot  = 0 ;
    END IF;
	IF (NEW.rrMonth2_FlowerArrangement IS NULL) THEN 
        SET NEW.rrMonth2_FlowerArrangement  = 0 ;
    END IF;
    IF (NEW.rrMonth2_BridalBouquet IS NULL) THEN 
        SET NEW.rrMonth2_BridalBouquet  = 0 ;
    END IF;
    
     IF (NEW.rrMonth3_Flower IS NULL) THEN 
        SET NEW.rrMonth3_Flower  = 0 ;
    END IF;
	IF (NEW.rrMonth3_FlowerPot IS NULL) THEN 
        SET NEW.rrMonth3_FlowerPot  = 0 ;
    END IF;
	IF (NEW.rrMonth3_FlowerArrangement IS NULL) THEN 
        SET NEW.rrMonth3_FlowerArrangement  = 0 ;
    END IF;
    IF (NEW.rrMonth3_BridalBouquet IS NULL) THEN 
        SET NEW.rrMonth3_BridalBouquet  = 0 ;
    END IF;
END; //

DELIMITER ;

--
-- Insert data to tables:
--

LOCK TABLES shop_managers WRITE;

LOCK TABLES shop_employees WRITE;

LOCK TABLES users WRITE;
INSERT INTO users (uUserName,uPassword,uEmail,uPrivilege) VALUES 
('companyemployee','companyemployee','companyemployee@local','CompanyEmployee'),
('shopmanager','shopmanager','shopmanager@local','ShopManager'),
('shopmanager2','shopmanager2','shopmanager@local','ShopManager'),
('shopmanager3','shopmanager3','shopmanager@local','ShopManager'),
('chainmanager','chainmanager','chainmanager@local','ChainManager'),
('administrator','administrator','administrator@local','Administrator'),
('shopemployee','shopemployee','shopemployee@local','ShopEmployee'),
('costumerservice','costumerservice','costumerservice@local','CostumerService'),
('costumer','costumer','costumer@local','Costumer'),
('servicespecialist','servicespecialist','servicespecialist@local','ServiceSpecialist');

UPDATE shop_employees SET smId = 1 WHERE uUserName = 'shopemployee';
UPDATE shop_managers SET smName = 'Haifa' WHERE  smId = 1;
UPDATE shop_managers SET smName = 'Karmiel' WHERE smId = 2;
UPDATE shop_managers SET smName = 'Akko' WHERE smId = 3;

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

LOCK TABLES items_in_shops WRITE;
INSERT INTO items_in_shops (smId,iId,isDiscountedPrice) VALUES 
(1,1,5),
(1,2,5),
(1,10,5);

LOCK TABLES costumers_in_shops WRITE;
INSERT INTO costumers_in_shops (cId,smId) VALUES
(1,1);

LOCK TABLES surveys WRITE;
INSERT INTO surveys (smId,suStartDate,suEndDate) VALUES
(1,'2017-01-01', '2017-01-31'),
(2,'2017-01-01', '2017-01-31'),
(3,'2017-01-01', '2017-01-31'),
(1,'2017-03-01', '2017-03-31'),
(2,'2017-03-01', '2017-03-31'),
(3,'2017-03-01', '2017-03-31');


LOCK TABLES survey_results WRITE;

INSERT INTO survey_results (suId,srEnterDate,srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES
(1,'2017-01-02',4,4,4,4,4,4),
(1,'2017-01-04',2,2,2,2,2,2),
(2,'2017-01-02',4,4,4,4,4,4),
(2,'2017-01-02',4,4,4,4,4,4),
(3,'2017-01-02',4,4,4,4,4,4),
(3,'2017-01-02',4,4,4,4,4,4),
(3,'2017-01-02',4,4,4,4,4,4),
(3,'2017-01-02',4,4,4,4,4,4);

LOCK TABLES reservations WRITE;
INSERT INTO reservations (cId, smId,rCreditCard, rType, rNumberOfItems, rPrice, rBlessingCard, rDeliveryType, rDeliveryAddress, rDeliveryPhone, rDeliveryName) VALUES 
(1,1,'1234123412341234','Closed',2,10, 'Happy Birthday', 'Immidiate','Ort Braude','049981111','Dolev');

LOCK TABLES items_in_reservations WRITE;
INSERT INTO items_in_reservations (rId, iId, iName, irQuantity, irPrice) VALUES 
(1,4,'Anemone',1,5),
(1,5,'Aconite',1,5);

LOCK TABLES complaints WRITE;
INSERT INTO complaints (cId,smId,cseId ,coComplaint ) VALUES 
(1,1,1,'The received product is not as orders.');

LOCK TABLES complaints_reports WRITE;
INSERT INTO complaints_reports (smId ,crYear, crQuarter, crMonth1, crMonth2, crMonth3) VALUES 
(1,'2017',1,0,0,1),
(2,'2017',1,1,1,0);

LOCK TABLES surveys_reports WRITE;
INSERT INTO surveys_reports (smId ,srYear, srQuarter, srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES 
(1,'2017',1,0,0,1,1,1,1),
(2,'2017',1,1,1,0,1,1,1);

LOCK TABLES incomes_reports WRITE;
INSERT INTO incomes_reports (smId ,irYear, irQuarter, irMonth1, irMonth2, irMonth3) VALUES 
(1,'2017',1,0,0,1),
(2,'2017',2,1,4124,0);

LOCK TABLES reservations_reports WRITE;
INSERT INTO reservations_reports (smId ,rrYear, rrQuarter,rrMonth1_Flower, rrMonth1_FlowerPot, rrMonth1_FlowerArrangement,rrMonth1_BridalBouquet,rrMonth2_Flower,rrMonth2_FlowerPot,rrMonth2_FlowerArrangement,rrMonth2_BridalBouquet, rrMonth3_Flower, rrMonth3_FlowerPot ,rrMonth3_FlowerArrangement,rrMonth3_BridalBouquet) VALUES 
(1,'2017',1,0,0,1,1,0,0,1,1,0,0,1,1),
(2,'2017',1,1,1,0,1,1,1,0,0,1,1,0,0);

UNLOCK TABLES;