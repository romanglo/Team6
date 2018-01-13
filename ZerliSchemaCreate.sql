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
  cId INT NOT NULL,
  smId Int NOT NULL,
  coDate DATE  NULL DEFAULT NULL,
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
  ssStartDate DATE NULL DEFAULT NULL,
  ssAnswer1 INT NOT NULL DEFAULT 0,
  ssAnswer2 INT NOT NULL DEFAULT 0,
  ssAnswer3 INT NOT NULL DEFAULT 0,
  ssAnswer4 INT NOT NULL DEFAULT 0,
  ssAnswer5 INT NOT NULL DEFAULT 0,
  ssAnswer6 INT NOT NULL DEFAULT 0,
  ssNumberOfAnswers INT NOT NULL DEFAULT 0,
  ssSummary VARCHAR(500) NULL DEFAULT NULL, 
  ssClosed BIT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (suId) REFERENCES surveys (suId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (ssId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE complaints_reports (
 smId INT NOT NULL,
 crYear YEAR, 
 crQuarter INT NOT NULL DEFAULT 0,
 crMonth1 INT NOT NULL DEFAULT 0,
 crMonth2 INT NOT NULL DEFAULT 0,
 crMonth3 INT NOT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
 PRIMARY KEY(smId,crYear,crQuarter)
);

CREATE TABLE surveys_reports (
 smId INT NOT NULL,
 srYear YEAR, 
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
 irYear YEAR, 
 irQuarter INT NOT NULL DEFAULT 0,
 irMonth1 FLOAT NOT NULL DEFAULT 0,
 irMonth2 FLOAT NOT NULL DEFAULT 0,
 irMonth3 FLOAT NOT NULL DEFAULT 0,
 FOREIGN KEY (smId) REFERENCES shop_managers (smId) ON DELETE NO ACTION ON UPDATE NO ACTION,
 PRIMARY KEY(smId,irYear,irQuarter)
);

CREATE TABLE reservations_reports (
 smId INT NOT NULL,
 rrYear YEAR , 
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
			ssAnswer1 / ssNumberOfAnswers AS 'First Question Average',
			ssAnswer2 / ssNumberOfAnswers AS 'Second Question Average', 
			ssAnswer3 / ssNumberOfAnswers AS 'Third Question Average',
			ssAnswer4 / ssNumberOfAnswers AS 'Fourth Question Average',
			ssAnswer5 / ssNumberOfAnswers AS 'Fifth Question Average', 
			ssAnswer6 / ssNumberOfAnswers AS 'Sixth Question Average'
	FROM surveys_in_shops WHERE 
	            smId = shop_manager_id AND YEAR(ssStartDate) = in_year AND
	            MONTH(ssStartDate) >= start_month AND MONTH(ssStartDate) <= end_month;
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

--- CREATE TRIGGER insert_item_trigger
--- BEFORE INSERT ON items FOR EACH ROW
--- BEGIN
--- 	IF (NEW.iImage = NULL) THEN
--- 	
--- 		SET NEW.iImage = '\״\\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342\\0C			\r\r2!!22222222222222222222222222222222222222222222222222ְ\0\0P\0P\"\0\"\0\ִ\0\0\0\0\0\0\0\0\0\0\0	\n\ִ\0µ\0\0\0}\0!1AQa\"q2‘¡#B±ֱR\ׁנ$3br‚	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzƒ„…†‡ˆ‰’“”•–—˜™¢£₪¥¦§¨©×²³´µ¶·¸¹÷\ֲ\ֳ\ִ\ֵ\ֶ\ַ\ָ\ֹ\ֺ\ׂ\׃\װ\ױ\ײ\׳\״\\\ב\ג\ד\ה\ו\ז\ח\ט\י\ךסעףפץצקרשת\ִ\0\0\0\0\0\0\0\0	\n\ִ\0µ\0\0w\0!1AQaq\"2B‘¡±ֱ	#3Rנbr\ׁ\n$4\ב%ס\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz‚ƒ„…†‡ˆ‰’“”•–—˜™¢£₪¥¦§¨©×²³´µ¶·¸¹÷\ֲ\ֳ\ִ\ֵ\ֶ\ַ\ָ\ֹ\ֺ\ׂ\׃\װ\ױ\ײ\׳\״\\\ג\ד\ה\ו\ז\ח\ט\י\ךעףפץצקרשת\\0\0\0\0?\0קת(×—w~Oָ˜.G‎ף@\0ET\׃\\\ַ\<€u×2j27¡=ת¦I$’rORj\ִRL¡‰\n‡¿ZQEF\׳3±ֹ•\0\זu9¿\גsW†›\חr}°(:lX;]ֱקֱ (¨c\װd^Cפ5z˜\ח‡‘\װµ5„±ע¿:u×°$Aֱ (­ת*¥¥ע>\0}U÷QEEs7‘	~§ ץI$’rORj\ז£&\י•>Qתע)–ש“\מ?u9{P0¢*axJ\ן\2*k;¯!¶?ת²*׃–%2:פ>†±e‰ב£ת\׀EV\ם\זU§¦hתE¥¼r\ֽeq5”“\\ָNI‘»²ƒ\־\ׂA¿\ֲyf₪}2%נצ£§\ך\Z—\"‘§E»]80\הf)(”\r×\אy ®\ַQ^U/-×]s\ַסץ\ךkd–+Xco:eEY%·{\ֻ`t\ֹ\ח-\0QE`A׀¶›ֿ„?C׀z־¿‡ֻp¯\ֿ\ד§I¶fCCץ\ה\׀0¢*¦\u!?\\ַ\ו\ֵ]\׃Ty.\\ֻc‏uJ\יv]H3sשףU<?\ג¨iק\ֿoe1’ף¡¾Sq\—=7lg\״פ4\0QE/ץ+½ֱקקצy71y{hlfE‚\טMs\ִ”³kvW2\ֶgk\י-´‡`‹ף‰De¨]ס·L‘»“:\כ½F\ַPM6\׀Z¥: ףUd”עUC™ `”\0rֲ¡׃®פֻ­r\ג\-2(ex.|<\יvS‚*“G=pp(¢\גץ/\ֻe¥h»ן¡‚\ך\ג\׀\\\ֻ$ˆ[q\״6©US€\ּzס€­k¡·ס÷\הGe~צ¶\יd/]¡E–\זpw-T†\0©_›†$9¥\ׁuK\ri,₪±\׃\־K«7”m\n>Hd„8³ַ \ג©ר‚m\"\ֲ\y¦AvKH\י—`0^W½\'I´(¢’\rzRkk;\ֽF\גH’\ג;¶µµ†	\ZX\ּ#,“\ה.€z€p:\ם7\ֿ‏ֿ‹\ם?jףy\צ¯/\ּ\ךzש_\'ON\״\ֿ9®KZװ´(l#:—v³+ָcAjF¹V“\0\הl\בGa[Z±§\Ceo§Yky£¸tDR*°2₪|‚₪ƒ@‚(­IG’Cc‏UJױ¶]FGק±שסT¼_\ג\ׁ~\ַ\\ײV¸\\א½\ֲB\0M£«	ש\ַ\ױ&‹;\\ֵkq$>CIף„ףA †Sƒ‘ƒַ­\n(¢¥ס%„—¶Rֱ\0gk„עּ¡w;ס‘\ֹ{u¬‎+D·׃µ{+‹K‹ˆRhZ\'•¥V€ֲ\ה\ל\ֳ*®:`u7‰\בd=zƒ\טk‚¬Tph\0¢)t\ֿֵ₪\\\\ZL¾d\ֺR\װ<¶\ױ3”pJ\ןv8\ָ\א(ם‚\ֱv:|:h°¸÷†k	\דw™\הV\ם \״Nׁ¼\ֹP0N}«b\־נ8\ָ~n€ץvQ\\]¯‚56\ֻM‹Lס\f³†XZo±+שעo¬\״ ~U+xR\ט\ֺo5]V[ש\ׂ?-^\0צ¥F\ג\ּHGֱ\'\ו\0pƒ\כ]}\0QE\חֻ jV¢\׳3[Zש\ׁ\ִ\f‘²‡{\ם\\0>€t\ַ7\ם\'y½´\F¿%¥\״i\\גi-R_1¥([ֲ \ּ`€\ֿvצ\װ\ִ\ֶD»=‡נ\ױx\ו’,\לb¹8 aEV£₪ksµ¡$\0LµYck°§\ןֺm\־6{\ײן‡­na·Qyuצ¹\ד4\Xq$\ד\ו)€` d“[Vנˆ!Tz“\ךh\0¢*Z«wh&׃‰‏=V¨ AEV)S†\״ױˆo¥ˆ8ui\ֻsy\לGQTd\׃\\d\ֶב‡¡\א\׀0¢*Eװ£#חצ\ז†װ£ה‰ק\ג×5ֲ˜\ֿ\אsB\\0ָˆ‏<PES¦½–PW…S\״w×ך¥OaWc\׃\\\א\ָ\ב}‡&¯Ep‘y\מOS@QP\ZFק\זC\0ױ×( AEW\';
--- 
--- 	END IF;
--- 	
--- END; //
 
 
--- CREATE TRIGGER before_update_item_trigger
--- BEFORE UPDATE ON items FOR EACH ROW
--- BEGIN
--- 	IF (NEW.iDeleted = 1) THEN
--- 	
--- 		SET NEW.iImage = NULL;
--- 	
--- 	ELSEIF (NEW.iImage = NULL) THEN 
--- 	
--- 		SET NEW.iImage = '\״\\0C\0		\n\r\Z\Z $.\' \",#(7),01444\'9=82<.342\\0C			\r\r2!!22222222222222222222222222222222222222222222222222ְ\0\0P\0P\"\0\"\0\ִ\0\0\0\0\0\0\0\0\0\0\0	\n\ִ\0µ\0\0\0}\0!1AQa\"q2‘¡#B±ֱR\ׁנ$3br‚	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzƒ„…†‡ˆ‰’“”•–—˜™¢£₪¥¦§¨©×²³´µ¶·¸¹÷\ֲ\ֳ\ִ\ֵ\ֶ\ַ\ָ\ֹ\ֺ\ׂ\׃\װ\ױ\ײ\׳\״\\\ב\ג\ד\ה\ו\ז\ח\ט\י\ךסעףפץצקרשת\ִ\0\0\0\0\0\0\0\0	\n\ִ\0µ\0\0w\0!1AQaq\"2B‘¡±ֱ	#3Rנbr\ׁ\n$4\ב%ס\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz‚ƒ„…†‡ˆ‰’“”•–—˜™¢£₪¥¦§¨©×²³´µ¶·¸¹÷\ֲ\ֳ\ִ\ֵ\ֶ\ַ\ָ\ֹ\ֺ\ׂ\׃\װ\ױ\ײ\׳\״\\\ג\ד\ה\ו\ז\ח\ט\י\ךעףפץצקרשת\\0\0\0\0?\0קת(×—w~Oָ˜.G‎ף@\0ET\׃\\\ַ\<€u×2j27¡=ת¦I$’rORj\ִRL¡‰\n‡¿ZQEF\׳3±ֹ•\0\זu9¿\גsW†›\חr}°(:lX;]ֱקֱ (¨c\װd^Cפ5z˜\ח‡‘\װµ5„±ע¿:u×°$Aֱ (­ת*¥¥ע>\0}U÷QEEs7‘	~§ ץI$’rORj\ז£&\י•>Qתע)–ש“\מ?u9{P0¢*axJ\ן\2*k;¯!¶?ת²*׃–%2:פ>†±e‰ב£ת\׀EV\ם\זU§¦hתE¥¼r\ֽeq5”“\\ָNI‘»²ƒ\־\ׂA¿\ֲyf₪}2%נצ£§\ך\Z—\"‘§E»]80\הf)(”\r×\אy ®\ַQ^U/-×]s\ַסץ\ךkd–+Xco:eEY%·{\ֻ`t\ֹ\ח-\0QE`A׀¶›ֿ„?C׀z־¿‡ֻp¯\ֿ\ד§I¶fCCץ\ה\׀0¢*¦\u!?\\ַ\ו\ֵ]\׃Ty.\\ֻc‏uJ\יv]H3sשףU<?\ג¨iק\ֿoe1’ף¡¾Sq\—=7lg\״פ4\0QE/ץ+½ֱקקצy71y{hlfE‚\טMs\ִ”³kvW2\ֶgk\י-´‡`‹ף‰De¨]ס·L‘»“:\כ½F\ַPM6\׀Z¥: ףUd”עUC™ `”\0rֲ¡׃®פֻ­r\ג\-2(ex.|<\יvS‚*“G=pp(¢\גץ/\ֻe¥h»ן¡‚\ך\ג\׀\\\ֻ$ˆ[q\״6©US€\ּzס€­k¡·ס÷\הGe~צ¶\יd/]¡E–\זpw-T†\0©_›†$9¥\ׁuK\ri,₪±\׃\־K«7”m\n>Hd„8³ַ \ג©ר‚m\"\ֲ\y¦AvKH\י—`0^W½\'I´(¢’\rzRkk;\ֽF\גH’\ג;¶µµ†	\ZX\ּ#,“\ה.€z€p:\ם7\ֿ‏ֿ‹\ם?jףy\צ¯/\ּ\ךzש_\'ON\״\ֿ9®KZװ´(l#:—v³+ָcAjF¹V“\0\הl\בGa[Z±§\Ceo§Yky£¸tDR*°2₪|‚₪ƒ@‚(­IG’Cc‏UJױ¶]FGק±שסT¼_\ג\ׁ~\ַ\\ײV¸\\א½\ֲB\0M£«	ש\ַ\ױ&‹;\\ֵkq$>CIף„ףA †Sƒ‘ƒַ­\n(¢¥ס%„—¶Rֱ\0gk„עּ¡w;ס‘\ֹ{u¬‎+D·׃µ{+‹K‹ˆRhZ\'•¥V€ֲ\ה\ל\ֳ*®:`u7‰\בd=zƒ\טk‚¬Tph\0¢)t\ֿֵ₪\\\\ZL¾d\ֺR\װ<¶\ױ3”pJ\ןv8\ָ\א(ם‚\ֱv:|:h°¸÷†k	\דw™\הV\ם \״Nׁ¼\ֹP0N}«b\־נ8\ָ~n€ץvQ\\]¯‚56\ֻM‹Lס\f³†XZo±+שעo¬\״ ~U+xR\ט\ֺo5]V[ש\ׂ?-^\0צ¥F\ג\ּHGֱ\'\ו\0pƒ\כ]}\0QE\חֻ jV¢\׳3[Zש\ׁ\ִ\f‘²‡{\ם\\0>€t\ַ7\ם\'y½´\F¿%¥\״i\\גi-R_1¥([ֲ \ּ`€\ֿvצ\װ\ִ\ֶD»=‡נ\ױx\ו’,\לb¹8 aEV£₪ksµ¡$\0LµYck°§\ןֺm\־6{\ײן‡­na·Qyuצ¹\ד4\Xq$\ד\ו)€` d“[Vנˆ!Tz“\ךh\0¢*Z«wh&׃‰‏=V¨ AEV)S†\״ױˆo¥ˆ8ui\ֻsy\לGQTd\׃\\d\ֶב‡¡\א\׀0¢*Eװ£#חצ\ז†װ£ה‰ק\ג×5ֲ˜\ֿ\אsB\\0ָˆ‏<PES¦½–PW…S\״w×ך¥OaWc\׃\\\א\ָ\ב}‡&¯Ep‘y\מOS@QP\ZFק\זC\0ױ×( AEW\';
--- 	
--- 	END IF;
--- 	
--- END; //

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
        
	ELSE
    
		SET NEW.csCreditCard = NULL;
		SET NEW.csStartSubscriptionDate = NULL;
        
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
        
	ELSE
    
		SET NEW.csCreditCard = NULL;
		SET NEW.csStartSubscriptionDate = NULL;
        
    END IF;
END; //

CREATE TRIGGER insert_surveys_in_shops_trigger
BEFORE INSERT ON surveys_in_shops FOR EACH ROW
BEGIN
    IF (NEW.ssStartDate = '0000-00-00' OR NEW.ssStartDate IS NULL) THEN 
        SET NEW.ssStartDate = NOW();
    END IF;
END; //

CREATE TRIGGER update_surveys_in_shops_trigger
BEFORE UPDATE ON surveys_in_shops FOR EACH ROW
BEGIN 
    IF ( NEW.ssSummary IS NOT NULL AND NEW.ssClosed = 0) THEN 
		SET NEW.ssClosed = 1;
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
		ELSEIF (NEW.uPrivilege = 'ShopManager') THEN
			DELETE FROM shop_managers WHERE shop_managers.uUserName = NEW.uUserName;
		END IF;
		
		IF (NEW.uPrivilege = 'Costumer') THEN 
			INSERT INTO costumers (uUserName) VALUES (NEW.uUserName);
		ELSEIF (NEW.uPrivilege = 'ShopEmployee') THEN
			INSERT INTO shop_employees (uUserName) VALUES (NEW.uUserName);
		ELSEIF (NEW.uPrivilege = 'ShopManager') THEN
			INSERT INTO shop_managers (uUserName) VALUES (NEW.uUserName);
		END IF;
	
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
UPDATE shop_managers SET smName = 'Kramiel' WHERE smId = 2;
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
INSERT INTO surveys (suQuestion1,suQuestion2,suQuestion3,suQuestion4,suQuestion5,suQuestion6) VALUES
('Question 1','Question 2', 'Question 3', 'Question 4', 'Question 5', 'Question 6');

LOCK TABLES surveys_in_shops WRITE;
INSERT INTO surveys_in_shops (smId,suId,ssAnswer1,ssAnswer2,ssAnswer3,ssAnswer4,ssAnswer5,ssAnswer6,ssNumberOfAnswers) VALUES
(1,1,4,4,4,4,4,4,2);

LOCK TABLES reservations WRITE;
INSERT INTO reservations (cId, smId,rCreditCard, rType, rNumberOfItems, rPrice, rBlessingCard, rDeliveryType, rDeliveryAddress, rDeliveryPhone, rDeliveryName) VALUES 
(1,1,'1234123412341234','Closed',2,10, 'Happy Birthday', 'Immidiate','Ort Braude','049981111','Dolev');

LOCK TABLES items_in_reservations WRITE;
INSERT INTO items_in_reservations (rId, iId, iName, irQuantity, irPrice) VALUES 
(1,4,'Anemone',1,5),
(1,5,'Aconite',1,5);

LOCK TABLES complaints WRITE;
INSERT INTO complaints (cId,smId ,coComplaint ) VALUES 
(1,1,'The received product is not as orders.');

LOCK TABLES complaints_reports WRITE;
INSERT INTO complaints_reports (smId ,crYear, crQuarter, crMonth1, crMonth2, crMonth3) VALUES 
(1,'2017',1,0,0,1),
(1,'2018',1,1,2,3),
(1,'2018',2,4,5,6),
(1,'2018',3,7,8,9),
(1,'2018',4,10,11,12),
(2,'2017',1,1,1,0),
(2,'2018',1,12,11,10),
(2,'2018',2,9,8,7),
(2,'2018',3,6,5,3),
(2,'2018',4,3,2,1);

LOCK TABLES surveys_reports WRITE;
INSERT INTO surveys_reports (smId ,srYear, srQuarter, srAnswer1,srAnswer2,srAnswer3,srAnswer4,srAnswer5,srAnswer6) VALUES 
(1,'2017',1,0,0,1,1,1,1),
(1,'2018',1,1,2,3,1,1,1),
(1,'2018',2,4,5,6,1,1,1),
(1,'2018',3,7,8,9,1,1,1),
(1,'2018',4,10,11,12,1,1,1),
(2,'2017',1,1,1,0,1,1,1),
(2,'2018',1,12,11,10,1,1,1),
(2,'2018',2,9,8,7,1,1,1),
(2,'2018',3,6,5,3,1,1,1),
(2,'2018',4,3,2,1,1,1,1);

LOCK TABLES incomes_reports WRITE;
INSERT INTO incomes_reports (smId ,irYear, irQuarter, irMonth1, irMonth2, irMonth3) VALUES 
(1,'2017',1,0,0,1),
(1,'2018',1,5246,2,3),
(1,'2018',2,4,1341,6),
(1,'2018',3,100,8,9),
(1,'2018',4,10,11,12),
(2,'2017',2,1,4124,0),
(2,'2018',1,1768,11,10),
(2,'2018',2,9,6363,7),
(2,'2018',3,6,5,857875),
(2,'2018',4,3,2,1);

LOCK TABLES reservations_reports WRITE;
INSERT INTO reservations_reports (smId ,rrYear, rrQuarter,rrMonth1_Flower, rrMonth1_FlowerPot, rrMonth1_FlowerArrangement,rrMonth1_BridalBouquet,rrMonth2_Flower,rrMonth2_FlowerPot,rrMonth2_FlowerArrangement,rrMonth2_BridalBouquet, rrMonth3_Flower, rrMonth3_FlowerPot ,rrMonth3_FlowerArrangement,rrMonth3_BridalBouquet) VALUES 
(1,'2017',1,0,0,1,1,0,0,1,1,0,0,1,1),
(1,'2018',1,1,2,3,1,1,1,0,0,1,1,0,0),
(1,'2018',2,4,5,6,1,1,1,0,0,1,1,0,0),
(1,'2018',3,7,8,9,1,1,1,0,0,1,1,0,0),
(1,'2018',4,1,1,2,1,1,1,0,0,1,1,0,0),
(2,'2017',1,1,1,0,1,1,1,0,0,1,1,0,0),
(2,'2018',1,1,1,0,1,1,1,0,0,1,1,0,9),
(2,'2018',2,9,8,7,1,1,1,0,0,1,1,0,0),
(2,'2018',3,6,5,3,1,1,1,0,0,1,1,0,0),
(2,'2018',4,3,2,1,1,1,1,0,0,1,1,0,0);

UNLOCK TABLES;         

