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
	uImage BLOB NULL DEFAULT NULL,
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
  FOREIGN KEY (cId) REFERENCES costumers (cId) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
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
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (cseId) REFERENCES costumer_service_employees (cseId) ON DELETE CASCADE ON UPDATE NO ACTION,
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
		FROM complaints GROUP BY shop_manager_id;
		    		   
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
		ELSEIF (NEW.uPrivilege = 'ShopEmployee') THEN
			INSERT INTO shop_employees (uUserName) VALUES (NEW.uUserName);
		ELSEIF (NEW.uPrivilege = 'ShopManager') THEN
			INSERT INTO shop_managers (uUserName) VALUES (NEW.uUserName);
		ELSEIF (NEW.uPrivilege = 'CostumerService') THEN
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
('companyemployee2','companyemployee2','companyemployee2@local','CompanyEmployee'),
('companyemployee3','companyemployee3','companyemployee3@local','CompanyEmployee'),
('shopmanager','shopmanager','shopmanager@local','ShopManager'),
('shopmanager2','shopmanager2','shopmanager@local','ShopManager'),
('shopmanager3','shopmanager3','shopmanager@local','ShopManager'),
('shopmanager4','shopmanager4','shopmanager@local','ShopManager'),
('shopmanager5','shopmanager5','shopmanager@local','ShopManager'),
('chainmanager','chainmanager','chainmanager@local','ChainManager'),
('administrator','administrator','administrator@local','Administrator'),
('shopemployee','shopemployee','shopemployee@local','ShopEmployee'),
('moshe','123','moshe@local','ShopEmployee'),
('dolev','123','dolev@local','ShopEmployee'),
('kobe','123','kobe@local','ShopEmployee'),
('idan','123','idan@local','ShopEmployee'),
('gaya','123','gaya@local','ShopEmployee'),
('shahar','123','shahar@local','ShopEmployee'),
('sahar','123','sahar@local','ShopEmployee'),
('roman','123','roman@local','ShopEmployee'),
('petrick','123','petrick@local','ShopEmployee'),
('alis','123','alis@local','ShopEmployee'),
('ester','123','ester@local','ShopEmployee'),
('bob','123','bob@local','ShopEmployee'),
('tal','123','tal@local','ShopEmployee'),
('david','123','david@local','ShopEmployee'),
('ariel','123','ariel@local','ShopEmployee'),
('may','123','may@local','ShopEmployee'),
('sapir','123','sapir@local','ShopEmployee'),
('ofek','123','ofek@local','ShopEmployee'),
('reuven','123','reuven@local','ShopEmployee'),
('costumerservice','costumerservice','costumerservice@local','CostumerService'),
('costumer','costumer','costumer@local','Costumer'),
('jake','123','jake@gmail.com','Costumer'),
('adam','123','adam@gmail.com','Costumer'),
('kate','123','kate@gmail.com','Costumer'),
('josh','123','josh@gmail.com','Costumer'),
('drake','123','drake@gmail.com','Costumer'),
('specialist1','specialist1','specialist1@local','ServiceSpecialist');

UPDATE shop_employees SET smId = 1 WHERE uUserName = 'moshe';
UPDATE shop_employees SET smId = 1 WHERE uUserName = 'dolev';
UPDATE shop_employees SET smId = 1 WHERE uUserName = 'kobe';
UPDATE shop_employees SET smId = 1 WHERE uUserName = 'idan';
UPDATE shop_employees SET smId = 1 WHERE uUserName = 'gaya';
UPDATE shop_employees SET smId = 2 WHERE uUserName = 'shaha';
UPDATE shop_employees SET smId = 2 WHERE uUserName = 'sahar';
UPDATE shop_employees SET smId = 2 WHERE uUserName = 'roman';
UPDATE shop_employees SET smId = 2 WHERE uUserName = 'petri';
UPDATE shop_employees SET smId = 3 WHERE uUserName = 'alis';
UPDATE shop_employees SET smId = 3 WHERE uUserName = 'ester';
UPDATE shop_employees SET smId = 3 WHERE uUserName = 'bob';
UPDATE shop_employees SET smId = 3 WHERE uUserName = 'tal';
UPDATE shop_employees SET smId = 4 WHERE uUserName = 'david';
UPDATE shop_employees SET smId = 4 WHERE uUserName = 'ariel';
UPDATE shop_employees SET smId = 4 WHERE uUserName = 'may';
UPDATE shop_employees SET smId = 4 WHERE uUserName = 'sapir';
UPDATE shop_employees SET smId = 5 WHERE uUserName = 'ofek';
UPDATE shop_employees SET smId = 5 WHERE uUserName = 'reuven';
UPDATE shop_employees SET smId = 5 WHERE uUserName = 'moshe';
UPDATE shop_employees SET smId = 5 WHERE uUserName = 'dolev';
UPDATE shop_managers SET smName = 'Haifa' WHERE  smId = 1;
UPDATE shop_managers SET smName = 'Karmiel' WHERE smId = 2;
UPDATE shop_managers SET smName = 'Akko' WHERE smId = 3;
UPDATE shop_managers SET smName = 'Bat-Yam' WHERE smId = 4;
UPDATE shop_managers SET smName = 'Afula' WHERE smId = 5;

LOCK TABLES costumers_in_shops WRITE;
INSERT INTO costumers_in_shops (cId,smId) VALUES
(1,1),
(2,1),
(2,2),
(3,3),
(4,2),
(4,1),
(5,3),
(6,3);

TRUNCATE TABLE surveys;

LOCK TABLES surveys WRITE;
INSERT INTO surveys (smId,suStartDate,suEndDate) VALUES
(1,'2017-01-01', '2017-02-28'),
(2,'2017-01-01', '2017-02-28'),
(3,'2017-01-01', '2017-02-28'),
(4,'2017-01-01', '2017-02-28'),
(5,'2017-01-01', '2017-02-28'),
(1,'2017-03-01', '2017-05-31'),
(2,'2017-03-01', '2017-05-31'),
(3,'2017-03-01', '2017-05-31'),
(4,'2017-03-01', '2017-05-31'),
(5,'2017-03-01', '2017-05-31'),
(1,'2017-06-01', '2017-08-31'),
(2,'2017-06-01', '2017-08-31'),
(3,'2017-06-01', '2017-08-31'),
(4,'2017-06-01', '2017-08-31'),
(5,'2017-06-01', '2017-08-31'),
(1,'2017-09-01', '2017-11-30'),
(2,'2017-09-01', '2017-11-30'),
(3,'2017-09-01', '2017-11-30'),
(4,'2017-09-01', '2017-11-30'),
(5,'2017-09-01', '2017-11-30');


LOCK TABLES survey_results WRITE;

INSERT INTO survey_results (suId,srEnterDate,srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES
(1,'2017-01-01',10,8,7,6,5,4),
(1,'2017-01-02',2,3,4,6,9,9),
(1,'2017-01-03',1,2,3,6,9,8),
(1,'2017-01-04',4,3,3,2,2,2),
(1,'2017-01-05',7,7,8,3,1,2),
(1,'2017-01-06',2,9,8,7,6,6),
(1,'2017-01-07',2,3,5,7,8,1),
(1,'2017-01-08',8,3,4,9,10,10),
(1,'2017-01-09',10,10,10,6,3,2),
(1,'2017-01-10',1,1,1,2,3,4),
(2,'2017-01-11',4,3,2,1,8,9),
(2,'2017-01-12',4,10,4,10,4,10),
(2,'2017-01-13',3,8,4,2,9,1),
(2,'2017-01-14',3,3,3,1,2,5),
(2,'2017-01-15',9,7,1,2,9,8),
(2,'2017-01-16',1,2,3,4,5,6),
(2,'2017-01-17',7,8,9,9,8,7),
(2,'2017-01-18',10,9,3,4,2,4),
(2,'2017-01-19',3,9,9,4,1,4),
(2,'2017-01-20',9,5,1,4,4,2),
(3,'2017-01-21',7,7,7,4,7,10),
(3,'2017-01-22',9,8,4,4,2,10),
(3,'2017-01-23',10,3,4,4,2,4),
(3,'2017-01-24',9,8,7,2,1,10),
(3,'2017-01-25',8,7,10,1,2,4),
(3,'2017-01-26',5,4,5,6,5,7),
(3,'2017-01-27',9,10,10,9,8,7),
(3,'2017-01-28',8,2,8,1,8,1),
(3,'2017-01-29',4,7,2,4,1,9),
(3,'2017-01-30',4,8,10,4,2,8),
(4,'2017-02-01',4,1,5,4,4,7),
(4,'2017-02-02',4,4,6,4,9,6),
(4,'2017-02-03',1,4,2,4,4,2),
(4,'2017-02-04',8,7,4,3,2,1),
(4,'2017-02-05',9,8,7,7,8,9),
(4,'2017-02-06',5,4,3,3,4,5),
(4,'2017-02-07',2,4,6,8,10,1),
(4,'2017-02-08',8,3,5,7,9,10),
(4,'2017-02-09',7,8,4,4,10,4),
(4,'2017-02-10',6,2,8,10,4,6),
(5,'2017-02-11',4,4,10,8,2,4),
(5,'2017-02-12',4,10,4,4,8,7),
(5,'2017-02-13',10,4,2,3,4,8),
(5,'2017-02-14',9,3,9,2,1,6),
(5,'2017-02-15',9,1,2,6,2,6),
(5,'2017-02-16',9,9,9,9,9,9),
(5,'2017-02-17',4,2,7,4,8,8),
(5,'2017-02-18',2,4,4,8,4,1),
(5,'2017-02-19',4,2,8,4,7,4),
(5,'2017-02-20',7,8,4,4,4,7),
(6,'2017-04-01',1,2,3,4,5,6),
(6,'2017-04-02',8,7,6,4,1,1),
(6,'2017-04-03',9,8,7,4,1,2),
(6,'2017-04-04',6,7,7,8,8,8),
(6,'2017-04-05',3,3,2,7,9,8),
(6,'2017-04-06',8,1,2,3,4,4),
(6,'2017-04-07',8,7,5,3,2,9),
(6,'2017-04-08',2,7,6,2,1,1),
(6,'2017-04-09',1,1,1,3,2,9),
(6,'2017-04-10',9,9,9,8,7,6),
(7,'2017-04-11',2,2,4,2,1,9),
(7,'2017-04-12',4,1,4,1,4,1),
(7,'2017-04-13',7,2,6,5,2,9),
(7,'2017-04-14',7,7,7,9,1,5),
(7,'2017-04-15',9,7,1,2,9,8),
(7,'2017-04-16',8,8,2,1,9,9),
(7,'2017-04-17',3,2,1,1,2,3),
(7,'2017-04-18',1,1,7,6,8,6),
(7,'2017-04-19',7,1,1,6,9,6),
(7,'2017-04-20',1,5,9,6,6,8),
(8,'2017-04-21',3,3,3,6,3,1),
(8,'2017-04-22',1,2,6,6,8,1),
(8,'2017-04-23',1,9,9,6,5,4),
(8,'2017-04-24',9,2,3,8,9,1),
(8,'2017-04-25',2,3,1,9,8,6),
(8,'2017-04-26',5,5,5,5,5,5),
(8,'2017-04-27',1,1,1,1,2,3),
(8,'2017-04-28',2,8,2,10,2,10),
(8,'2017-04-29',6,3,8,6,9,10),
(8,'2017-04-30',6,2,10,6,8,2),
(9,'2017-05-01',6,9,5,6,6,3),
(9,'2017-05-02',4,3,2,6,1,4),
(9,'2017-05-03',9,6,8,6,5,8),
(9,'2017-05-04',2,3,6,7,8,9),
(9,'2017-05-05',1,7,3,3,2,10),
(9,'2017-05-06',2,6,6,8,8,10),
(9,'2017-05-07',7,5,1,5,1,7),
(9,'2017-05-08',2,7,5,3,1,10),
(9,'2017-05-09',7,8,4,4,10,4),
(9,'2017-05-10',6,2,8,10,4,6),
(10,'2017-05-11',4,4,10,8,2,4),
(10,'2017-05-12',4,10,4,4,8,7),
(10,'2017-05-13',10,4,2,3,4,8),
(10,'2017-05-14',9,3,9,2,1,6),
(10,'2017-05-15',9,1,2,6,2,6),
(10,'2017-05-16',9,9,9,9,9,9),
(10,'2017-05-17',4,2,7,4,8,8),
(10,'2017-05-18',2,4,4,8,4,1),
(10,'2017-05-19',4,2,8,4,7,4),
(10,'2017-05-20',7,8,4,4,4,7),
(11,'2017-07-01',9,9,9,9,9,9),
(11,'2017-07-02',9,7,7,7,7,7),
(11,'2017-07-03',9,7,1,2,9,8),
(11,'2017-07-04',8,8,2,1,9,9),
(11,'2017-07-05',3,2,1,1,2,3),
(11,'2017-07-06',1,1,7,6,8,6),
(11,'2017-07-07',7,1,1,6,9,6),
(11,'2017-07-08',1,5,9,6,6,8),
(11,'2017-07-09',3,3,3,6,3,1),
(11,'2017-07-10',1,2,6,6,8,1),
(12,'2017-07-11',1,9,9,6,5,4),
(12,'2017-07-12',9,2,3,8,9,1),
(12,'2017-07-13',2,3,1,9,8,6),
(12,'2017-07-14',5,5,5,5,5,5),
(12,'2017-07-15',1,1,1,1,2,3),
(12,'2017-07-16',2,8,2,10,2,10),
(12,'2017-07-17',10,8,7,6,5,4),
(12,'2017-07-18',2,3,4,6,9,9),
(12,'2017-07-19',1,2,3,6,9,8),
(12,'2017-07-20',4,3,3,2,2,2),
(13,'2017-07-21',7,7,8,3,1,2),
(13,'2017-07-22',2,9,8,7,6,6),
(13,'2017-07-23',2,3,5,7,8,1),
(13,'2017-07-24',8,3,4,9,10,10),
(13,'2017-07-25',10,10,10,6,3,2),
(13,'2017-07-26',1,1,1,2,3,4),
(13,'2017-07-27',1,1,1,1,2,3),
(13,'2017-07-28',2,7,5,3,1,10),
(13,'2017-07-29',7,8,4,4,10,4),
(13,'2017-07-30',6,2,8,10,4,6),
(14,'2017-08-01',4,4,10,8,2,4),
(14,'2017-08-02',4,10,4,4,8,7),
(14,'2017-08-03',10,4,2,3,4,8),
(14,'2017-08-04',9,3,9,2,1,6),
(14,'2017-08-05',9,1,2,6,2,6),
(14,'2017-08-06',9,9,9,9,9,9),
(14,'2017-08-07',4,2,7,4,8,8),
(14,'2017-08-08',2,4,4,8,4,1),
(14,'2017-08-09',2,2,4,2,1,9),
(14,'2017-08-10',4,1,4,1,4,1),
(15,'2017-08-11',7,2,6,5,2,9),
(15,'2017-08-12',7,7,7,9,1,5),
(15,'2017-08-13',9,7,1,2,9,8),
(15,'2017-08-14',8,8,2,1,9,9),
(15,'2017-08-15',3,2,1,1,2,3),
(15,'2017-08-16',1,1,7,6,8,6),
(15,'2017-08-17',7,1,1,6,9,6),
(15,'2017-08-18',1,5,9,6,6,8),
(15,'2017-08-19',3,3,3,6,3,1),
(15,'2017-08-20',1,2,6,6,8,1),
(16,'2017-10-01',9,8,7,4,3,2),
(16,'2017-10-02',9,2,2,1,3,9),
(16,'2017-10-03',9,8,2,7,9,1),
(16,'2017-10-04',7,7,3,2,1,9),
(16,'2017-10-05',2,2,2,2,2,2),
(16,'2017-10-06',2,1,3,6,8,9),
(16,'2017-10-07',8,1,3,6,8,9),
(16,'2017-10-08',10,8,7,6,5,4),
(16,'2017-10-09',2,3,4,6,9,9),
(16,'2017-10-10',1,2,3,6,9,8),
(17,'2017-10-11',4,3,3,2,2,2),
(17,'2017-10-12',7,7,8,3,1,2),
(17,'2017-10-13',2,9,8,7,6,6),
(17,'2017-10-14',2,3,5,7,8,1),
(17,'2017-10-15',1,1,1,1,2,3),
(17,'2017-10-16',2,8,7,9,2,10),
(17,'2017-10-17',10,8,7,6,5,4),
(17,'2017-10-18',2,3,4,6,9,9),
(17,'2017-10-19',1,2,3,6,9,8),
(17,'2017-10-20',4,3,3,2,2,2),
(18,'2017-10-21',7,7,8,3,1,2),
(18,'2017-10-22',2,9,8,9,6,6),
(18,'2017-10-23',2,3,5,7,8,1),
(18,'2017-10-24',8,3,4,9,10,10),
(18,'2017-10-25',10,10,10,6,3,2),
(18,'2017-10-26',1,1,2,2,3,4),
(18,'2017-10-27',1,1,1,1,2,3),
(18,'2017-10-28',2,7,5,3,1,10),
(18,'2017-10-29',7,3,4,4,10,4),
(18,'2017-10-30',6,2,8,10,4,6),
(19,'2017-11-01',4,4,10,8,2,4),
(19,'2017-11-02',4,10,4,4,8,7),
(19,'2017-11-03',10,4,2,3,4,8),
(19,'2017-11-04',9,3,9,2,1,6),
(19,'2017-11-05',9,1,2,6,2,6),
(19,'2017-11-06',9,9,9,9,9,9),
(19,'2017-11-07',4,2,7,4,8,8),
(19,'2017-11-08',2,4,1,8,9,1),
(19,'2017-11-09',2,2,4,2,1,9),
(19,'2017-11-10',4,1,2,1,3,1),
(20,'2017-11-11',7,2,6,5,2,9),
(20,'2017-11-12',7,7,7,9,1,5),
(20,'2017-11-13',9,7,6,2,9,8),
(20,'2017-11-14',8,8,2,1,9,9),
(20,'2017-11-15',3,2,6,1,2,3),
(20,'2017-11-16',1,1,7,6,8,6),
(20,'2017-11-17',7,1,2,6,9,6),
(20,'2017-11-18',1,5,9,6,6,8),
(20,'2017-11-19',3,3,7,6,3,1),
(20,'2017-11-20',1,2,9,2,8,1);


LOCK TABLES complaints WRITE;
INSERT INTO complaints (cId,smId,cseId ,coComplaint,coDate ) VALUES 
(1,1,1,'This is the worst customer service Ive ever encountered', '2017-01-19'),
(2,1,2,'This company seems to be an internet scam--I placed an order with them for same day delivery. The flowers were never delivered, and Ive called twice now with no response.', '2017-02-01'),
(3,1,3,'Horrible business practices and customer service', '2017-04-02'),
(4,1,1,' dont answer the phone or any feedback', '2017-05-06'),
(5,1,2,'I ordered a lovely arrangement from the website and what was received was nothing like the online picture.', '2017-07-29'),
(6,1,3,'We ordered bereavement flowers a week ago tomorrow and my credit card has been charged $125', '2017-08-21'),
(1,1,1,' cancelled my ORDER at 1:30 PM.', '2017-10-22'),
(2,1,2,'This is the worst customer service Ive ever encountered', '2017-11-15'),
(3,1,3,'poor quality. poor service. not professional. ', '2017-03-12'),
(4,1,1,'My orchid bouquet had so many holes and was half-wilted.', '2017-01-19'),
(5,2,2,' dont answer the phone or any feedback', '2017-01-01'),
(6,2,3,'We ordered bereavement flowers a week ago tomorrow and my credit card has been charged $125', '2017-04-02'),
(1,2,1,'The received product is not as orders.', '2017-12-06'),
(2,2,2,'This is the worst customer service Ive ever encountered', '2017-12-29'),
(3,2,3,'The received product is not as orders.', '2017-11-21'),
(4,2,1,'My orchid bouquet had so many holes and was half-wilted.', '2017-10-22'),
(5,2,2,' dont answer the phone or any feedback', '2017-09-15'),
(6,2,3,'The received product is not as orders.', '2017-09-12'),
(1,2,1,'This is the worst customer service Ive ever encountered', '2017-09-05'),
(2,2,2,'The received product is not as orders.', '2017-09-06'),
(3,3,3,'We ordered bereavement flowers a week ago tomorrow and my credit card has been charged $125', '2017-03-19'),
(4,3,1,'Horrible business practices and customer service', '2017-03-01'),
(5,3,2,'The received product is not as orders.', '2017-08-02'),
(6,3,3,'This is the worst customer service Ive ever encountered', '2017-02-06'),
(1,3,1,'The received product is not as orders.', '2017-01-29'),
(2,3,2,'My orchid bouquet had so many holes and was half-wilted.', '2017-01-21'),
(3,3,3,'The received product is not as orders.', '2017-01-22'),
(4,3,1,'Horrible business practices and customer service', '2017-03-15'),
(5,3,2,'The received product is not as orders.', '2017-09-12'),
(6,3,3,' dont answer the phone or any feedback', '2017-12-05'),
(1,4,1,'The received product is not as orders.', '2017-01-19'),
(2,4,2,'This is the worst customer service Ive ever encountered', '2017-02-01'),
(3,4,3,'We ordered bereavement flowers a week ago tomorrow and my credit card has been charged $125', '2017-03-02'),
(4,4,1,'My orchid bouquet had so many holes and was half-wilted.', '2017-05-06'),
(5,4,2,'The received product is not as orders.', '2017-07-29'),
(6,4,3,'We ordered bereavement flowers a week ago tomorrow and my credit card has been charged $125', '2017-09-21'),
(1,4,1,'The received product is not as orders.', '2017-10-22'),
(2,4,2,' dont answer the phone or any feedback', '2017-12-15'),
(3,4,3,'The received product is not as orders.', '2017-04-12'),
(4,4,1,'Horrible business practices and customer service', '2017-05-05'),
(5,5,2,'My orchid bouquet had so many holes and was half-wilted.', '2017-01-19'),
(6,5,3,'The received product is not as orders.', '2017-02-01'),
(1,5,1,'The received product is not as orders.', '2017-04-02'),
(2,5,2,'This is the worst customer service Ive ever encountered', '2017-05-06'),
(3,5,3,'The received product is not as orders.', '2017-07-29'),
(4,5,1,'My orchid bouquet had so many holes and was half-wilted.', '2017-08-21'),
(5,5,2,'The received product is not as orders.', '2017-10-22'),
(6,5,3,'We ordered bereavement flowers a week ago tomorrow and my credit card has been charged $125', '2017-11-15'),
(1,5,1,'The received product is not as orders.', '2017-06-12'),
(2,5,2,'My orchid bouquet had so many holes and was half-wilted.', '2017-12-05'),
(3,5,3,'The received product is not as orders.', '2017-02-18');

UNLOCK TABLES;