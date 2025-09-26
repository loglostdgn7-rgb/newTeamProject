-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: team
-- ------------------------------------------------------
-- Server version	8.4.6

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(20) NOT NULL,
  `password` char(60) DEFAULT NULL,
  `name` varchar(8) DEFAULT NULL,
  `postcode` int DEFAULT NULL,
  `road_address` varchar(255) DEFAULT NULL,
  `detail_address` varchar(255) DEFAULT NULL,
  `tel` char(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(10) DEFAULT NULL,
  `role` enum('USER','ADMIN') NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('loglost','$2a$10$BK6mSc18Twrc3lTfm62QTeGn5uSokRDoRMuKTvZY74E9g3y/85X3y','김영수',42006,'대구 수성구 달구벌대로 2315','국가기밀입니당','01050382706','loglost_dgn7@naver.com','테스터','ADMIN'),('tester','$2a$10$L/aFJ2iTzurRMUpZXk55bu7TlIE7Tc5Go0qD7roBvT5moq/EHoYNG','테스터',77777,'서울 강남구 어떤로 777','강남 어떤 아파트 7동 777동','01000000000','user@naver.com','테스트유저','USER'),('user1','$2a$10$L/aFJ2iTzurRMUpZXk55bu7TlIE7Tc5Go0qD7roBvT5moq/EHoYNG','김길동',11111,'1번 도로','1번 상세주소','01011111111','user1@example.com','유저1','USER'),('user2','$2a$10$L/aFJ2iTzurRMUpZXk55bu7TlIE7Tc5Go0qD7roBvT5moq/EHoYNG','성경김',22222,'2번 도로','2번 상세주소','01022222222','user2@example.com','유저2','USER'),('user3','$2a$10$L/aFJ2iTzurRMUpZXk55bu7TlIE7Tc5Go0qD7roBvT5moq/EHoYNG','정약욕',33333,'3번 도로','3번 상세주소','01033333333','user3@example.com','유저3','USER'),('user4','$2a$10$L/aFJ2iTzurRMUpZXk55bu7TlIE7Tc5Go0qD7roBvT5moq/EHoYNG','이순신',44444,'4번 도로','4번 상세주소','01044444444','user4@example.com','유저4','USER'),('user5','$2a$10$L/aFJ2iTzurRMUpZXk55bu7TlIE7Tc5Go0qD7roBvT5moq/EHoYNG','이김밥',55555,'5번 도로','5번 상세주소','01055555555','user5@example.com','유저5','USER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-26 17:36:34
