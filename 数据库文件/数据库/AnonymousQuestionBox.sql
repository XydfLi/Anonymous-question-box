-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: AnonymousQuestionBox
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `accountName` char(30) NOT NULL,
  `password` char(40) NOT NULL,
  `identity` tinyint(4) NOT NULL DEFAULT '3',
  `mailbox` char(40) DEFAULT NULL,
  `avatar` char(45) DEFAULT '1.jpg',
  `questionBoxStatus` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`accountName`),
  UNIQUE KEY `mailbox` (`mailbox`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('admin','1f06ba75d893fee79cb138162b80348f',1,'xydflxy@sina.com','1.jpg',1),('普通用户','1255a5e69d2984cafc69f761bb1ea15e',2,'690054845@qq.com','1.jpg',1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `action`
--

DROP TABLE IF EXISTS `action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `action` (
  `accountName` char(30) NOT NULL,
  `blacklist` tinyint(4) NOT NULL DEFAULT '0',
  `tipOff` tinyint(4) NOT NULL DEFAULT '2',
  `questionId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`accountName`,`questionId`),
  KEY `questionId` (`questionId`),
  CONSTRAINT `action_ibfk_1` FOREIGN KEY (`accountName`) REFERENCES `account` (`accountName`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `action_ibfk_2` FOREIGN KEY (`questionId`) REFERENCES `questionbox` (`questionId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `action`
--

LOCK TABLES `action` WRITE;
/*!40000 ALTER TABLE `action` DISABLE KEYS */;
/*!40000 ALTER TABLE `action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answerbox`
--

DROP TABLE IF EXISTS `answerbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answerbox` (
  `questionId` int(10) unsigned NOT NULL,
  `accountName` char(30) NOT NULL,
  `answerContent` text,
  `answerTime` datetime NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `deleted` tinyint(4) NOT NULL DEFAULT '2',
  PRIMARY KEY (`questionId`,`accountName`),
  KEY `accountName` (`accountName`),
  CONSTRAINT `answerbox_ibfk_1` FOREIGN KEY (`questionId`) REFERENCES `questionbox` (`questionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `answerbox_ibfk_2` FOREIGN KEY (`accountName`) REFERENCES `account` (`accountName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answerbox`
--

LOCK TABLES `answerbox` WRITE;
/*!40000 ALTER TABLE `answerbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `answerbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ips`
--

DROP TABLE IF EXISTS `ips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ips` (
  `accountName` char(30) NOT NULL,
  `IP` char(25) NOT NULL,
  `count` smallint(5) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`accountName`,`IP`),
  CONSTRAINT `ips_ibfk_1` FOREIGN KEY (`accountName`) REFERENCES `account` (`accountName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ips`
--

LOCK TABLES `ips` WRITE;
/*!40000 ALTER TABLE `ips` DISABLE KEYS */;
/*!40000 ALTER TABLE `ips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `noticeId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountName` char(30) NOT NULL,
  `content` text,
  `releaseTime` datetime NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`noticeId`),
  KEY `accountName` (`accountName`),
  CONSTRAINT `notice_ibfk_1` FOREIGN KEY (`accountName`) REFERENCES `account` (`accountName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionbox`
--

DROP TABLE IF EXISTS `questionbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionbox` (
  `questionId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountName` char(30) NOT NULL,
  `questionContent` text,
  `questionTime` datetime NOT NULL,
  PRIMARY KEY (`questionId`),
  KEY `accountName` (`accountName`),
  CONSTRAINT `questionbox_ibfk_1` FOREIGN KEY (`accountName`) REFERENCES `account` (`accountName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionbox`
--

LOCK TABLES `questionbox` WRITE;
/*!40000 ALTER TABLE `questionbox` DISABLE KEYS */;
INSERT INTO `questionbox` VALUES (1,'admin','空提问','2000-01-01 00:00:00');
/*!40000 ALTER TABLE `questionbox` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-04 21:06:50
