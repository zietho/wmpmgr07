#File-Encoding: UTF-
#Ansprechperson: Christian
#Version: 2012-11-19
#run with: mysql -u root --password=thailand --default-character-set=utf8 < setup_database.sql

#recreate database
DROP DATABASE IF EXISTS wienerhelden;
CREATE DATABASE wienerhelden;

#drop users if exist
DROP PROCEDURE IF EXISTS mysql.drop_user_if_exists ;
DELIMITER $$
CREATE PROCEDURE mysql.drop_user_if_exists(IN myusername VARCHAR(50), IN myhost VARCHAR(50))
BEGIN
  DECLARE foo BIGINT DEFAULT 0 ;
  SELECT COUNT(*)
  INTO foo
    FROM mysql.user
      WHERE User = myusername and  Host = myhost;
   IF foo > 0 THEN
      SET @SQL = (SELECT mResult FROM (SELECT GROUP_CONCAT("DROP USER ", "'", myusername, "'@'", myhost, "'") AS mResult) AS Q LIMIT 1);
      PREPARE STMT FROM @SQL;
      EXECUTE STMT;
      DEALLOCATE PREPARE STMT;

  END IF;
END ;$$
DELIMITER ;
CALL mysql.drop_user_if_exists('wienerhelden','localhost') ;
CALL mysql.drop_user_if_exists('webuser','localhost') ;
DROP PROCEDURE IF EXISTS mysql.drop_users_if_exists ;

#create users
GRANT USAGE ON *.* TO wienerhelden@localhost IDENTIFIED BY 'osterhase' REQUIRE NONE;
GRANT Select  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Insert  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Update  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Delete  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Create  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Drop  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Index  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Alter  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Create View  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Show view  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Execute  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Select  ON *.* TO wienerhelden@localhost;
GRANT Select  ON mysql.* TO wienerhelden@localhost;
GRANT Show databases  ON *.* TO wienerhelden@localhost;
GRANT References  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Create temporary tables  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Trigger  ON wienerhelden.* TO wienerhelden@localhost;
GRANT Lock tables  ON wienerhelden.* TO wienerhelden@localhost;
GRANT USAGE  ON wienerhelden.* TO wienerhelden@localhost WITH GRANT OPTION;
GRANT Shutdown  ON *.* TO wienerhelden@localhost;

GRANT USAGE ON *.* TO webuser@localhost IDENTIFIED BY 'webheroes1' REQUIRE NONE;
GRANT Select  ON wienerhelden.* TO webuser@localhost;
GRANT Insert  ON wienerhelden.* TO webuser@localhost;
GRANT Update  ON wienerhelden.* TO webuser@localhost;
GRANT Delete  ON wienerhelden.* TO webuser@localhost;
GRANT Show view  ON wienerhelden.* TO webuser@localhost;
GRANT Execute  ON wienerhelden.* TO webuser@localhost;

#create tables
CREATE TABLE wienerhelden.USER (
   ID INT UNSIGNED NOT NULL auto_increment,
   moderator BOOLEAN NOT NULL DEFAULT 0,
   trusted BOOLEAN NOT NULL DEFAULT 0,
   NICKNAME VARCHAR(20) UNIQUE NOT NULL,
   PASSWORD VARCHAR(100) NOT NULL,
   EMAIL VARCHAR(50),
   TEL VARCHAR(50),
   FIRSTNAME VARCHAR(50),
   LASTNAME VARCHAR(50),
   PICTURE VARCHAR(100),
   dateCreated TIMESTAMP DEFAULT NOW(),
   VERSION INT UNSIGNED,
   SALT VARCHAR(32),
  PRIMARY KEY (ID),
  UNIQUE (NICKNAME)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.ADDRESS (
   ID INT UNSIGNED NOT NULL auto_increment,
   name VARCHAR(50),
   street VARCHAR(100),
   zip INT,
   place VARCHAR(100),
   longitude DOUBLE,
   latitude DOUBLE,
   dateCreated TIMESTAMP DEFAULT NOW(),
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.USER_ADDRESS (
   ID INT UNSIGNED NOT NULL auto_increment,
   user_id INT UNSIGNED,
   address_id INT UNSIGNED,
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.CATEGORY (
   ID INT UNSIGNED NOT NULL auto_increment,
   userChoosable BOOLEAN NOT NULL DEFAULT 1,
   title VARCHAR(50),
   description VARCHAR(300),
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.SKILL (
   ID INT UNSIGNED NOT NULL auto_increment,
   title VARCHAR(100),
   description VARCHAR(300),
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.SOTIVITY (
   ID INT UNSIGNED NOT NULL auto_increment,
   user_id INT UNSIGNED,
   address_id INT UNSIGNED,
   publicVisible BOOLEAN NOT NULL DEFAULT 1,
   groupSotivity BOOLEAN NOT NULL DEFAULT 0,
   done BOOLEAN NOT NULL DEFAULT 0,
   title VARCHAR(50) NOT NULL,
   description VARCHAR(300),
   dateCreated TIMESTAMP DEFAULT NOW(),
   dateEnd TIMESTAMP,
   skills VARCHAR(300),
   duration INT UNSIGNED,
   facebookId VARCHAR(20),
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;
ALTER TABLE wienerhelden.SOTIVITY AUTO_INCREMENT = 3450;

CREATE TABLE wienerhelden.SOTIVITY_CATEGORY (
   ID INT UNSIGNED NOT NULL auto_increment,
   sotivity_id INT UNSIGNED,
   category_id INT UNSIGNED,
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.USER_CATEGORY (
   ID INT UNSIGNED NOT NULL auto_increment,
   user_id INT UNSIGNED,
   category_id INT UNSIGNED,
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.SOTIVITY_SKILL (
   ID INT UNSIGNED NOT NULL auto_increment,
   sotivity_id INT UNSIGNED,
   skill_id INT UNSIGNED,
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

#kein update, sondern immer neuen Eintrag bilden, für nächsten Status
#state=OFFER,ACCEPTED,DONE
CREATE TABLE wienerhelden.WORKFLOW (
   ID INT UNSIGNED NOT NULL auto_increment,
   user_id INT UNSIGNED,
   sotivity_id INT UNSIGNED,
   state VARCHAR(20) NOT NULL,
   comment VARCHAR(300),
   dateCreated TIMESTAMP DEFAULT NOW(),
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;

CREATE TABLE wienerhelden.THANKS (
   ID INT UNSIGNED NOT NULL auto_increment,
   user_id INT UNSIGNED,
   sotivity_id INT UNSIGNED,
   comment VARCHAR(300),
   dateCreated TIMESTAMP DEFAULT NOW(),
   VERSION INT UNSIGNED,
  PRIMARY KEY (ID)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT;


#fill tables
use wienerhelden
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (1,0,0,'hansi','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','hansi@gmail.xcom',null,'Hans','Huber',null,'2012-11-15 19:16:12');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (2,0,1,'chris','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','christian.fischer@student.tuwien.ac.at','06500000000','Christian','Fischer',null,'2012-11-15 19:14:23');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (3,0,1,'patrick','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','e0926061@student.tuwien.ac.at','06500000001','Patrick','Schuh',null,'2012-11-15 19:16:09');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (4,0,1,'matthias','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','e0925873@student.tuwien.ac.at','06500000002','Matthias','Sperl',null,'2012-11-15 19:17:17');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (5,0,1,'markus','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','markus.friedl.773@facebook.com','06500000003','Markus','Friedl',null,'2012-11-15 19:17:26');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (6,0,1,'thomas','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','thomas.ziegelbecker@student.tuwien.ac.at','06500000004','Thomas','Ziegelbecker',null,'2012-11-15 19:21:20');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (7,0,0,'michi','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','michael.dornkasch@XXinso.tuwien.ac.at','06500000005','Michael','Dornkasch',null,'2012-11-15 19:22:19');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (8,0,1,'oma','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','oma@omasonline.xcom','','Gertrude','Ganzer',null,'2012-11-15 19:25:21');
insert into `USER`(`id`,`moderator`,`trusted`,`NICKNAME`,`PASSWORD`,`SALT`,`EMAIL`,`TEL`,`FIRSTNAME`,`LASTNAME`,`PICTURE`,`dateCreated`) values (9,1,1,'moderator','tQzmQvyrkMcTAkcB11TN7HQPf9I=','69d6Fe2PqFU=','mod@mod.xcom','06500000000','Michael','Moderator',null,'2012-11-25 19:14:23');

insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (1,'Zu Hause','Pöchlarnstraße 1/10',1200,'Wien',48.232996,16.387535,'2012-10-17 19:02:32');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (2,'Zu Hause','Darwingasse 31/2/9',1020,'Wien',48.220906,16.385207,'2012-10-17 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (3,'Zu Hause','Paniglgasse 1',1040,'Wien',48.198463,16.370938,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (4,'Zu Hause','Gußhausstraße 5',1040,'Wien',48.198001,16.373819,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (5,'Zu Hause','Schwindgasse 1',1040,'Wien',48.198001,16.373819,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (6,'Zu Hause','Brucknerstraße 5',1040,'Wien',48.198963,16.374543,'2012-05-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (7,'Zu Hause','Gußhausstraße 3/1',1040,'Wien',48.198048,16.374135,'2012-08-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (8,'Zu Hause','Argentinierstraße 3',1040,'Wien',48.197233,16.371861,'2012-08-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (9,'Arbeit','Taubstummengasse 1',1040,'Wien',48.195373,16.372633,'2012-09-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (10,'Arbeit','Theobaldgasse 10',1040,'Wien',48.20078,16.360745,'2012-11-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (11,'Park','Resselpark',1010,'Wien',48.199807,16.370595,'2012-11-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (12,'Blutspendezentrale Rotes Kreuz','Wiedner Hauptstraße 32',1040,'Wien',48.195365,16.366927,'2012-09-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (13,'TU Wien','Karlsplatz 13',1040,'Wien',48.19898,16.36990,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (14,'Freihaus','Treitlstrasse',1040,'Wien',48.19981,16.36751,'2012-09-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (15,'Zu Hause','Walfischgasse',1010,'Wien',48.20325,16.37213,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (16,'Pfarre 1050','St.Florian 1050',1040,'Wien',48.18550, 16.36357,'2012-09-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (17,'Alois Drasche Park','Alois Drasche Park ',1010,'Wien',48.18646,16.36785,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (18,'Zu Hause','Walfischgasse',1010,'Wien',48.20325,16.37213,'2012-10-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (19,'Pfarre 1050','St.Florian 1050',1040,'Wien',48.18550, 16.36357,'2012-09-25 19:05:41');
insert into ADDRESS(id,name,street,zip,place,latitude,longitude,dateCreated) values (20,'Alois Drasche Park','Alois Drasche Park ',1010,'Wien',48.18646,16.36785,'2012-10-25 19:05:41');


insert into USER_ADDRESS(user_id,address_id) values (1,1);
insert into USER_ADDRESS(user_id,address_id) values (2,2);
insert into USER_ADDRESS(user_id,address_id) values (3,3);
insert into USER_ADDRESS(user_id,address_id) values (4,4);
insert into USER_ADDRESS(user_id,address_id) values (5,5);
insert into USER_ADDRESS(user_id,address_id) values (6,6);
insert into USER_ADDRESS(user_id,address_id) values (7,7);
insert into USER_ADDRESS(user_id,address_id) values (8,8);
insert into USER_ADDRESS(user_id,address_id) values (9,9);
insert into USER_ADDRESS(user_id,address_id) values (2,10);
   
insert into CATEGORY(id,userChoosable,title,description,version) values (1,1,'Haushaltshilfe','Hilfe zu Hause wie Wäsche waschen, Bügeln, Fenster putzen.',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (2,1,'Einkaufen','Besorgen von Lebensmitteln, Medikamenten oder einfachen Utensilien.',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (3,1,'Botengang','Überlieferung von Gegenständen.',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (4,1,'Beisammensein','Kartenspielen, Brettspiele, Teetrinken, gemütliches Beisammensein.',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (5,1,'Fernseherproblem','Sender einstellen, Probleme mit dem Gerät.',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (6,1,'Computerproblem','Kleinere Probleme mit der Bedienung des Gerätes.',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (7,0,'Blutspenden','Blutspenden',1);
insert into CATEGORY(id,userChoosable,title,description,version) values (8,0,'Park aufräumen','Park säubern von herumliegenden Gegenständen.',1);

insert into SKILL(id,title,description,version) values (1,'EDV Kenntnisse','allgemeine Beschreibung',1);
insert into SKILL(id,title,description,version) values (2,'handwerklich begabt','allgemeine Beschreibung',1);
insert into SKILL(id,title,description,version) values (3,'kann gut mit Kindern','allgemeine Beschreibung',1);
insert into SKILL(id,title,description,version) values (4,'kann gut mit älteren Personen','allgemeine Beschreibung',1);

insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3421,2,11,1,0,0,'Wäsche waschen','Hallo liebe Leute, Ich bräuchte bitte wieder Hilfe mit meiner Wäsche. Waschmaschine habe ich, traue mich aber nicht, sie alleine einzuschalten. Waschpulver sollte auch noch zu Genüge da sein. Aufhängen kann ich die Wäsche dann selber.','2012-12-02 12:25:00','2013-05-05 12:25:22',30,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3422,2,12,1,0,1,'Hilfe bei Einkauf','Sitze krank zu Hause und würde beim Einkaufen hilfe benötigen','2012-12-02 12:25:00','2012-12-31 12:25:22',120,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3423,2,13,0,0,0,'Umzugshilfe','Hallo Helfer da draußn, ich ziehe an den naechsten beiden Wochenenden um und wollte fragen ob jemand Lust und Zeit hat mir dabei zu helfen. Es wäre nur 1 Bezirk von meiner jetzigen Wohnsituation entfernt, also kein weiter weg und nur vom Mezanin in dem 1. Stock','2012-12-05 12:25:00','2013-04-04 12:25:22',240,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3424,2,14,0,0,0,'Begleitung beim Spaziergehen','Ich würde die nächste Woche gerne einmal spazieren gehen, hab mir aber eben erst den Fuss gebrochen, und es wäre mein  erster Versuch nach Abnahme des Gipses wieder einmal eine längere Strecke zu gehen. Könnte mich jemand begleiten?','2012-11-02 12:25:00','2012-12-31 12:25:22',60,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3425,4,15,1,0,0,'Renovierung meiner Wohnung','Ich hab das kommende Wochenende vor meine Wohnung zu renovieren, und meine Freunde habn leider keine Zeit. Kann mir jemand beim ausmalen meines Zimmers helfen, Es würde um circa 25 m² gehen die in weiß ausgestrichen werden. Eventuell würde ich noch Hilfe beim einkaufen brauchen - Das wäre dann einen Tag davor Und gebe deswegen noch bescheid','2012-10-02 12:25:00','2012-12-31 12:25:22',1200,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3426,5,16,1,0,0,'Hilfe beim Boden verlegen','Zur Zeit haben in unserer Wohnung einen alten Boden, weswegen wir gestern einen neuen Laminat Boden gekauft haben. Da wir noch nie einen verlegt haben wäre es toll wenn uns jemand dabei helfen könnte. Im Austausch bitten wir Kaffee, Kuchen und andere köstlichkeiten während der Arbeit an.  Bitte einfach bei uns melden. Würden uns über jegliche Hilfe sehr freuen','2012-12-05 12:25:00','2012-12-31 12:25:22',600,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3427,7,17,1,0,0,'Kochhilfe','Kann ich mich seit meinem Unfall nur schwer oder sehr eingeschränkt bewegen und Bräuchte Hilfe bei diversen Tätigkeiten wie schneiden, schälen und beim tragen von Töpfen. Es würde auch nicht lange dauern, Ich rechne mit ungefähr 2 Stunden. Ich würde mich sehr freuen meldet euch einfach!
Liebe Grüße Manfred','2012-12-04 12:25:00','2013-06-29 12:25:22',120,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3428,1,18,1,0,0,'Streichen meines Wohnzimmers','Würde gerne mein Wohnzimmer Wieder einmal streichen. Leider ist mein rechter Fuß lädiert, weswegen ich Hilfe Bei der Decke und den oberen Bereich der den Wänden brauchen würde. Der Termin ist nicht so wichtig, da bin ich flexibel.
Wäre toll wenn jemand helfen könnte freu mich schon auf eure Kontaktaufnahme.','2013-08-02 12:25:00','2012-12-31 12:25:22',180,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3429,9,19,1,1,0,'Park säubern','Hallo, also der Park - nahe Favoritenstrasse - schaut wieder einmal schrecklich aus und wollte hier einfach mal fragen wer hat Lust die Zeitungen, Flyer,etc. von dort vom letzten Event zu entfernen?','2012-12-02 12:25:00','2012-12-31 12:25:22',300,1);
insert into `sotivity`(`ID`,`user_id`,`address_id`,`publicVisible`,`groupSotivity`,`done`,`title`,`description`,`dateCreated`,`dateEnd`,`duration`,`VERSION`) values (3430,7,20,1,0,0,'Pflanzen anbauen','Rund um die Pfarre im 5ten koennte man gemeinsam den Bereich um die Kirche begrünen was hällt ihr davon?! Bitte um Vorschläge dann können wir das Event befüllen','2012-12-02 12:25:00','2013-12-31 12:25:22',240,1);

insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (1,3421,1,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (2,3422,2,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (3,3423,3,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (4,3424,4,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (5,3425,1,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (6,3426,1,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (7,3427,1,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (8,3428,1,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (9,3429,8,1);
insert into SOTIVITY_CATEGORY(id,sotivity_id,category_id,version) values (10,3430,8,1);

insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (1,3421,1,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (2,3422,2,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (3,3423,3,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (4,3424,4,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (5,3425,1,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (6,3426,2,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (7,3427,3,1);
insert into SOTIVITY_SKILL(id,sotivity_id,skill_id,version) values (8,3428,4,1);

insert into WORKFLOW(id,user_id,sotivity_id,state,comment,version) values (1,3,3421,'ACCEPTED','Hi, ich kann dir helfen!',1);
insert into WORKFLOW(id,user_id,sotivity_id,state,comment,version) values (2,4,3421,'OFFER','Ich bin mir nicht sicher, ob ichs schaff, aber ich probiers gerne ;)',1);
insert into WORKFLOW(id,user_id,sotivity_id,state,comment,version) values (3,2,3427,'OFFER','Will dir Helfen! Ich koche für mein LEEEEben gern.',1);
insert into WORKFLOW(id,user_id,sotivity_id,state,comment,version) values (4,1,3422,'ACCEPTED','Hallo, helfe dir gerne, gute Besserung!',1);

insert into THANKS(user_id,sotivity_id,comment) values (1,3422,'Danke dir vielmals für deine Hilfe, Hans!');
