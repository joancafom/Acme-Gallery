start transaction;

drop database if exists `Acme-Gallery`;
create database `Acme-Gallery`;

use `Acme-Gallery`;

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
  on `Acme-Gallery`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Gallery`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Gallery
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_idt4b4u259p6vs4pyr9lax4eg` (`userAccount_id`),
  CONSTRAINT `FK_idt4b4u259p6vs4pyr9lax4eg` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (24,0,'824 Ashton Lane','fran@gmail.com','MALE','Fran','+34512565480','Vail',23);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aggroup`
--

DROP TABLE IF EXISTS `aggroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aggroup` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `creationMoment` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isClosed` bit(1) NOT NULL,
  `maxParticipants` int(11) DEFAULT NULL,
  `meetingDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creator_id` int(11) NOT NULL,
  `museum_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_pjan25oj0652kxui9r9hibggu` (`isClosed`,`containsTaboo`),
  KEY `FK_rvpl7qf0g2vbl7l69ge3ci6va` (`creator_id`),
  KEY `FK_qqjicja707am14qkb22qa0jwr` (`museum_id`),
  CONSTRAINT `FK_qqjicja707am14qkb22qa0jwr` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`),
  CONSTRAINT `FK_rvpl7qf0g2vbl7l69ge3ci6va` FOREIGN KEY (`creator_id`) REFERENCES `visitor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggroup`
--

LOCK TABLES `aggroup` WRITE;
/*!40000 ALTER TABLE `aggroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `aggroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `announcement` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `creationMoment` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_6tc3hm82q4jhkf9luyvm5bfbv` (`containsTaboo`,`creationMoment`),
  KEY `FK_p9u35snbda1unqd9dqojt9ojy` (`group_id`),
  CONSTRAINT `FK_p9u35snbda1unqd9dqojt9ojy` FOREIGN KEY (`group_id`) REFERENCES `aggroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcement`
--

LOCK TABLES `announcement` WRITE;
/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artwork`
--

DROP TABLE IF EXISTS `artwork`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artwork` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `isFinal` bit(1) NOT NULL,
  `isHighlight` bit(1) NOT NULL,
  `photograph` varchar(255) DEFAULT NULL,
  `remark` text,
  `title` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `exhibition_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_f41tfmt2dxpvk5n8gx2tntmn9` (`isHighlight`,`isFinal`),
  KEY `FK_ilkg9n1afaald54n8fefs1xeq` (`exhibition_id`),
  CONSTRAINT `FK_ilkg9n1afaald54n8fefs1xeq` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artwork`
--

LOCK TABLES `artwork` WRITE;
/*!40000 ALTER TABLE `artwork` DISABLE KEYS */;
/*!40000 ALTER TABLE `artwork` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parentCategory_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_foei036ov74bv692o5lh5oi66` (`name`),
  KEY `FK_3v34vcvwua46xp9jd0bj7rk78` (`parentCategory_id`),
  CONSTRAINT `FK_3v34vcvwua46xp9jd0bj7rk78` FOREIGN KEY (`parentCategory_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (25,0,'CATEGORY',NULL),(26,0,'Paintings',25),(27,0,'Sculptures',25),(28,0,'Other Arts',25),(29,0,'Contemporaine',26),(30,0,'Byzantine Painting',26),(31,0,'Renaissance',26),(32,0,'Baroque',26),(33,0,'Rococos',26),(34,0,'Mesopotamia',27),(35,0,'Egypt',27),(36,0,'Ancient Greece',27),(37,0,'Gothic',27),(38,0,'Modern Arts',28),(39,0,'Pop History',38),(40,0,'Abstract Art',38),(41,0,'Modern Style',38),(42,0,'Postmodern Era',38),(43,0,'Avant-Garde',38);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  `parentComment_id` int(11) DEFAULT NULL,
  `visitor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_l7clk1eoeeqatmxpchh3jjt1t` (`containsTaboo`),
  KEY `FK_q8dtm49uxgwbbmkhfkuwko4de` (`group_id`),
  KEY `FK_t4l8ri4kvik847hh7ei7y1l4u` (`parentComment_id`),
  KEY `FK_ofqf2f5j7kgoegs0h5mobkja2` (`visitor_id`),
  CONSTRAINT `FK_ofqf2f5j7kgoegs0h5mobkja2` FOREIGN KEY (`visitor_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_q8dtm49uxgwbbmkhfkuwko4de` FOREIGN KEY (`group_id`) REFERENCES `aggroup` (`id`),
  CONSTRAINT `FK_t4l8ri4kvik847hh7ei7y1l4u` FOREIGN KEY (`parentComment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `critic`
--

DROP TABLE IF EXISTS `critic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `critic` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rq8eilj98ork1yxlo7xggp5c8` (`userAccount_id`),
  CONSTRAINT `FK_rq8eilj98ork1yxlo7xggp5c8` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `critic`
--

LOCK TABLES `critic` WRITE;
/*!40000 ALTER TABLE `critic` DISABLE KEYS */;
/*!40000 ALTER TABLE `critic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `critique`
--

DROP TABLE IF EXISTS `critique`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `critique` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `description` text,
  `score` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `critic_id` int(11) NOT NULL,
  `exhibition_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m9k088l5lry20wnu8t83h8yh7` (`critic_id`),
  KEY `FK_et93j0955yeipr8b4bb2jernu` (`exhibition_id`),
  CONSTRAINT `FK_et93j0955yeipr8b4bb2jernu` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`),
  CONSTRAINT `FK_m9k088l5lry20wnu8t83h8yh7` FOREIGN KEY (`critic_id`) REFERENCES `critic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `critique`
--

LOCK TABLES `critique` WRITE;
/*!40000 ALTER TABLE `critique` DISABLE KEYS */;
/*!40000 ALTER TABLE `critique` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daypass`
--

DROP TABLE IF EXISTS `daypass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daypass` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `VAT` double DEFAULT NULL,
  `CVV` int(11) DEFAULT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `purchaseMoment` datetime DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `visitDate` datetime DEFAULT NULL,
  `exhibition_id` int(11) DEFAULT NULL,
  `museum_id` int(11) NOT NULL,
  `visitor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_b199skiv0y6u89be6y460nvcs` (`price`),
  KEY `FK_9xxn4eysa7pxw8pkyqc20dxcm` (`exhibition_id`),
  KEY `FK_ky5jb9g5p0lcoer9dgn2vpqs0` (`museum_id`),
  KEY `FK_8adrwf6gt8b1k5hep50cr57sf` (`visitor_id`),
  CONSTRAINT `FK_8adrwf6gt8b1k5hep50cr57sf` FOREIGN KEY (`visitor_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_9xxn4eysa7pxw8pkyqc20dxcm` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`),
  CONSTRAINT `FK_ky5jb9g5p0lcoer9dgn2vpqs0` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daypass`
--

LOCK TABLES `daypass` WRITE;
/*!40000 ALTER TABLE `daypass` DISABLE KEYS */;
/*!40000 ALTER TABLE `daypass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `director`
--

DROP TABLE IF EXISTS `director`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `director` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lyi14cqa3xj6jq8qtoe4txxmi` (`userAccount_id`),
  CONSTRAINT `FK_lyi14cqa3xj6jq8qtoe4txxmi` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `director`
--

LOCK TABLES `director` WRITE;
/*!40000 ALTER TABLE `director` DISABLE KEYS */;
/*!40000 ALTER TABLE `director` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exhibition`
--

DROP TABLE IF EXISTS `exhibition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exhibition` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `endingDate` datetime DEFAULT NULL,
  `isPrivate` bit(1) NOT NULL,
  `price` double DEFAULT NULL,
  `startingDate` datetime DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8iibj36b9i2vnlg3f8dlswvy8` (`ticker`),
  KEY `UK_t9dagydjkxbf64jehge13cadm` (`ticker`,`title`,`description`(767),`endingDate`,`startingDate`,`isPrivate`),
  KEY `FK_qbey1br2ew2c8mn3oijob26me` (`category_id`),
  KEY `FK_j8tp9kwexl25qg3oejuukotj` (`room_id`),
  CONSTRAINT `FK_j8tp9kwexl25qg3oejuukotj` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_qbey1br2ew2c8mn3oijob26me` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exhibition`
--

LOCK TABLES `exhibition` WRITE;
/*!40000 ALTER TABLE `exhibition` DISABLE KEYS */;
/*!40000 ALTER TABLE `exhibition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exhibition_websites`
--

DROP TABLE IF EXISTS `exhibition_websites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exhibition_websites` (
  `Exhibition_id` int(11) NOT NULL,
  `websites` varchar(255) DEFAULT NULL,
  KEY `FK_5m7fjjmxos4vyuuo8uoyx1usb` (`Exhibition_id`),
  CONSTRAINT `FK_5m7fjjmxos4vyuuo8uoyx1usb` FOREIGN KEY (`Exhibition_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exhibition_websites`
--

LOCK TABLES `exhibition_websites` WRITE;
/*!40000 ALTER TABLE `exhibition_websites` DISABLE KEYS */;
/*!40000 ALTER TABLE `exhibition_websites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guide`
--

DROP TABLE IF EXISTS `guide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ntsbh3t5dd6tw1ovubvnga7ye` (`userAccount_id`),
  CONSTRAINT `FK_ntsbh3t5dd6tw1ovubvnga7ye` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide`
--

LOCK TABLES `guide` WRITE;
/*!40000 ALTER TABLE `guide` DISABLE KEYS */;
/*!40000 ALTER TABLE `guide` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guide_exhibition`
--

DROP TABLE IF EXISTS `guide_exhibition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide_exhibition` (
  `guides_id` int(11) NOT NULL,
  `exhibitions_id` int(11) NOT NULL,
  KEY `FK_euhoglujt5dk5137toi0xa7je` (`exhibitions_id`),
  KEY `FK_4pnr3rc5sw61jkox9dlal9aek` (`guides_id`),
  CONSTRAINT `FK_4pnr3rc5sw61jkox9dlal9aek` FOREIGN KEY (`guides_id`) REFERENCES `guide` (`id`),
  CONSTRAINT `FK_euhoglujt5dk5137toi0xa7je` FOREIGN KEY (`exhibitions_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide_exhibition`
--

LOCK TABLES `guide_exhibition` WRITE;
/*!40000 ALTER TABLE `guide_exhibition` DISABLE KEYS */;
/*!40000 ALTER TABLE `guide_exhibition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incident`
--

DROP TABLE IF EXISTS `incident`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incident` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isChecked` bit(1) NOT NULL,
  `level` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `guide_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_i49k4urikwqolhaln27xkk63s` (`isChecked`,`level`),
  KEY `FK_1j64uo893d1j4vbuw0i3rd0o8` (`guide_id`),
  KEY `FK_hh3b0qxp9gh8lj1yiomcu5nyn` (`room_id`),
  CONSTRAINT `FK_hh3b0qxp9gh8lj1yiomcu5nyn` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_1j64uo893d1j4vbuw0i3rd0o8` FOREIGN KEY (`guide_id`) REFERENCES `guide` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incident`
--

LOCK TABLES `incident` WRITE;
/*!40000 ALTER TABLE `incident` DISABLE KEYS */;
/*!40000 ALTER TABLE `incident` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isAccepted` bit(1) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `sentMoment` datetime DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_cbp4o2bk8ony9dn5dshi7sox0` (`sentMoment`,`isAccepted`),
  KEY `FK_cen7765xfec0yqv28st3p50e7` (`group_id`),
  KEY `FK_epmv4ikdppx6m2h76wlwkca88` (`guest_id`),
  KEY `FK_k9wyr1cu15kwgf4yeeo4n5wl2` (`host_id`),
  CONSTRAINT `FK_k9wyr1cu15kwgf4yeeo4n5wl2` FOREIGN KEY (`host_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_cen7765xfec0yqv28st3p50e7` FOREIGN KEY (`group_id`) REFERENCES `aggroup` (`id`),
  CONSTRAINT `FK_epmv4ikdppx6m2h76wlwkca88` FOREIGN KEY (`guest_id`) REFERENCES `visitor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museum`
--

DROP TABLE IF EXISTS `museum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museum` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `slogan` varchar(255) DEFAULT NULL,
  `director_id` int(11) NOT NULL,
  `store_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dfopy3k5j29ufc6b8pro6qaji` (`identifier`),
  KEY `FK_139hun7wm4u7u36dkl37tnqlj` (`director_id`),
  KEY `FK_i53dhce858ylhtvn6486l3jvu` (`store_id`),
  CONSTRAINT `FK_i53dhce858ylhtvn6486l3jvu` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`),
  CONSTRAINT `FK_139hun7wm4u7u36dkl37tnqlj` FOREIGN KEY (`director_id`) REFERENCES `director` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museum`
--

LOCK TABLES `museum` WRITE;
/*!40000 ALTER TABLE `museum` DISABLE KEYS */;
/*!40000 ALTER TABLE `museum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museum_guide`
--

DROP TABLE IF EXISTS `museum_guide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museum_guide` (
  `Museum_id` int(11) NOT NULL,
  `guides_id` int(11) NOT NULL,
  KEY `FK_fcq5lt2oxtf42ngial6s8cpd0` (`guides_id`),
  KEY `FK_4li5ehoi1f8cosq9qj2walue4` (`Museum_id`),
  CONSTRAINT `FK_4li5ehoi1f8cosq9qj2walue4` FOREIGN KEY (`Museum_id`) REFERENCES `museum` (`id`),
  CONSTRAINT `FK_fcq5lt2oxtf42ngial6s8cpd0` FOREIGN KEY (`guides_id`) REFERENCES `guide` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museum_guide`
--

LOCK TABLES `museum_guide` WRITE;
/*!40000 ALTER TABLE `museum_guide` DISABLE KEYS */;
/*!40000 ALTER TABLE `museum_guide` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `store_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qn519odsge2mmd5htarooejeu` (`store_id`),
  CONSTRAINT `FK_qn519odsge2mmd5htarooejeu` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_pictures`
--

DROP TABLE IF EXISTS `product_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_pictures` (
  `Product_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_388qr0bkr4lmxmau40p2f3xpi` (`Product_id`),
  CONSTRAINT `FK_388qr0bkr4lmxmau40p2f3xpi` FOREIGN KEY (`Product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_pictures`
--

LOCK TABLES `product_pictures` WRITE;
/*!40000 ALTER TABLE `product_pictures` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `containsTaboo` bit(1) NOT NULL,
  `creationDate` datetime DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `museum_id` int(11) NOT NULL,
  `visitor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_q5lebl12rg0dyanw6oyl74c6k` (`creationDate`,`containsTaboo`),
  KEY `FK_effef8l1gye5hl4fwnqd0lhcy` (`museum_id`),
  KEY `FK_qev81awblncgi904knsb39gbl` (`visitor_id`),
  CONSTRAINT `FK_qev81awblncgi904knsb39gbl` FOREIGN KEY (`visitor_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_effef8l1gye5hl4fwnqd0lhcy` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `area` double DEFAULT NULL,
  `inRepair` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `museum_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_py85odw0bsu8uys2kt6m7i4kv` (`museum_id`),
  CONSTRAINT `FK_py85odw0bsu8uys2kt6m7i4kv` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsor`
--

DROP TABLE IF EXISTS `sponsor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_okfx8h0cn4eidh8ng563vowjc` (`userAccount_id`),
  CONSTRAINT `FK_okfx8h0cn4eidh8ng563vowjc` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsor`
--

LOCK TABLES `sponsor` WRITE;
/*!40000 ALTER TABLE `sponsor` DISABLE KEYS */;
/*!40000 ALTER TABLE `sponsor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsorship`
--

DROP TABLE IF EXISTS `sponsorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsorship` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `CVV` int(11) DEFAULT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `endingDate` datetime DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `startingDate` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `exhibition_id` int(11) NOT NULL,
  `sponsor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_5akbjhbo66na692br8fufcbx5` (`status`,`startingDate`,`endingDate`),
  KEY `FK_7url5id2hc7muicjogootxy6p` (`exhibition_id`),
  KEY `FK_e3idyfyffpufog3sjl3c2ulun` (`sponsor_id`),
  CONSTRAINT `FK_e3idyfyffpufog3sjl3c2ulun` FOREIGN KEY (`sponsor_id`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_7url5id2hc7muicjogootxy6p` FOREIGN KEY (`exhibition_id`) REFERENCES `exhibition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsorship`
--

LOCK TABLES `sponsorship` WRITE;
/*!40000 ALTER TABLE `sponsorship` DISABLE KEYS */;
/*!40000 ALTER TABLE `sponsorship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `systemconfiguration`
--

DROP TABLE IF EXISTS `systemconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `systemconfiguration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `VAT` double DEFAULT NULL,
  `tabooWords` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `systemconfiguration`
--

LOCK TABLES `systemconfiguration` WRITE;
/*!40000 ALTER TABLE `systemconfiguration` DISABLE KEYS */;
INSERT INTO `systemconfiguration` VALUES (44,0,0.21,'sex|sexo|viagra|cialis|porn');
/*!40000 ALTER TABLE `systemconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isLocked` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`),
  KEY `UK_mf45pwghc90f25v1rfqjyubdp` (`isLocked`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (23,0,'\0','21232f297a57a5a743894a0e4a801fc3','admin');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount_authorities`
--

DROP TABLE IF EXISTS `useraccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount_authorities`
--

LOCK TABLES `useraccount_authorities` WRITE;
/*!40000 ALTER TABLE `useraccount_authorities` DISABLE KEYS */;
INSERT INTO `useraccount_authorities` VALUES (23,'ADMINISTRATOR');
/*!40000 ALTER TABLE `useraccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor`
--

DROP TABLE IF EXISTS `visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `surnames` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2y6pheswvood223whbrhn006d` (`userAccount_id`),
  CONSTRAINT `FK_2y6pheswvood223whbrhn006d` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor`
--

LOCK TABLES `visitor` WRITE;
/*!40000 ALTER TABLE `visitor` DISABLE KEYS */;
/*!40000 ALTER TABLE `visitor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor_aggroup`
--

DROP TABLE IF EXISTS `visitor_aggroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitor_aggroup` (
  `participants_id` int(11) NOT NULL,
  `joinedGroups_id` int(11) NOT NULL,
  KEY `FK_sl1nryxkgt6nfi8oxhx0pwdxs` (`joinedGroups_id`),
  KEY `FK_9gdm0pi72wnmooabx800up9p7` (`participants_id`),
  CONSTRAINT `FK_9gdm0pi72wnmooabx800up9p7` FOREIGN KEY (`participants_id`) REFERENCES `visitor` (`id`),
  CONSTRAINT `FK_sl1nryxkgt6nfi8oxhx0pwdxs` FOREIGN KEY (`joinedGroups_id`) REFERENCES `aggroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor_aggroup`
--

LOCK TABLES `visitor_aggroup` WRITE;
/*!40000 ALTER TABLE `visitor_aggroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `visitor_aggroup` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-06 15:18:09
