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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `imp_uid` varchar(255) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `merchant_uid` varchar(255) NOT NULL,
  `order_name` varchar(45) NOT NULL,
  `order_price` int unsigned NOT NULL,
  `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_status` enum('PENDING','PREPARING','SHIPPED','DELIVERED') NOT NULL DEFAULT 'PENDING',
  `order_request` varchar(255) DEFAULT NULL,
  `buyer_name` varchar(10) NOT NULL,
  `buyer_addr` varchar(255) NOT NULL,
  `buyer_tel` varchar(11) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (5,'imp_844809359240','loglost','ORD_0f35f29b-1112-4132-8664-3b20cb98019c','드리프터 이즈 제트',62000,'2025-09-21 20:57:05','PENDING',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(6,'imp_950252808950','loglost','ORD_02db93bd-75e4-4036-b7a9-0440d5f27c09','테니스 스트라이프 헤어밴드 외 1건',137000,'2025-09-21 21:21:07','PENDING',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(7,'imp_052940657295','loglost','ORD_e9974716-5e3b-4f39-8235-656dfdee6d74','레이플라이드 v2 외 1건',568000,'2025-09-21 21:32:37','PENDING',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(8,'imp_975829656382','loglost','ORD_3b1ddde3-3317-4791-b5bc-dd93335ab565','휠라 에샤페 레이스 외 2건',282000,'2025-09-21 22:20:44','PENDING',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(9,'imp_677861775933','loglost','ORD_7a594f91-cffe-46fd-ae3a-f4062449744f','드리프터 튜브쏭 볼드',81000,'2025-09-22 00:51:15','PENDING','금1톤이랑 같이 주세요','김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(10,'imp_667854101957','loglost','ORD_8049dddc-41f7-4b0c-be21-4d71d901ec1d','휠라 F-조거 24 외 5건',505000,'2025-09-22 20:01:09','PENDING','','김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-24 19:18:22
