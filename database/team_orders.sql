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
  `order_status` enum('PENDING','PREPARING','SHIPPED','DELIVERED','CANCEL','EXCHANGE','REFUND') NOT NULL DEFAULT 'PENDING',
  `order_request` varchar(255) DEFAULT NULL,
  `buyer_name` varchar(10) NOT NULL,
  `buyer_addr` varchar(255) NOT NULL,
  `buyer_tel` varchar(11) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (5,'imp_844809359240','loglost','ORD_0f35f29b-1112-4132-8664-3b20cb98019c','드리프터 이즈 제트',62000,'2025-09-21 20:57:05','CANCEL',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(6,'imp_950252808950','loglost','ORD_02db93bd-75e4-4036-b7a9-0440d5f27c09','테니스 스트라이프 헤어밴드 외 1건',137000,'2025-09-21 21:21:07','PREPARING',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(7,'imp_052940657295','loglost','ORD_e9974716-5e3b-4f39-8235-656dfdee6d74','레이플라이드 v2 외 1건',568000,'2025-09-21 21:32:37','SHIPPED',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(8,'imp_975829656382','loglost','ORD_3b1ddde3-3317-4791-b5bc-dd93335ab565','휠라 에샤페 레이스 외 2건',282000,'2025-09-21 22:20:44','REFUND',NULL,'김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(9,'imp_677861775933','loglost','ORD_7a594f91-cffe-46fd-ae3a-f4062449744f','드리프터 튜브쏭 볼드',81000,'2025-09-22 00:51:15','REFUND','금1톤이랑 같이 주세요','김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(10,'imp_667854101957','loglost','ORD_8049dddc-41f7-4b0c-be21-4d71d901ec1d','휠라 F-조거 24 외 5건',505000,'2025-09-22 20:01:09','CANCEL','','김영수','대구 수성구 달구벌대로 2315국가기밀입니당','01050382706'),(11,'imp_769291584863','user1','ORD_bf4603df-3f80-4795-b546-cd385cbbf397','레이플라이드 TR 다이얼 외 3건',439000,'2025-09-26 00:31:48','PENDING','','김길동','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(12,'imp_326406019920','user1','ORD_421397bd-d604-41a8-b27d-3ac8efd965c7','드리프터 튜브쏭 볼드 외 4건',368000,'2025-09-26 00:33:46','SHIPPED','','김길동','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(13,'imp_946497203600','user1','ORD_81de8abe-f947-48a7-bc3c-01c1a6f08afd','테니스 스트라이프 아대 (2pcs) 외 2건',165000,'2025-09-26 00:35:12','DELIVERED','','김길동','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(14,'imp_820570267840','user2','ORD_aa402393-703c-4e51-bbe9-c003e7d1168e','테니스 베이직 헤어밴드 외 2건',153000,'2025-09-26 00:36:23','PENDING','','성경김','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(15,'imp_925624243029','user2','ORD_7bf30515-7102-4aaf-82e3-1f7f7c82a97f','휠라 토러스 v3 외 3건',171000,'2025-09-26 00:37:21','SHIPPED','','성경김','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(16,'imp_129685668814','user2','ORD_f62bffb7-946b-4b6b-8132-9df23bd6885f','드리프터 이즈 외 2건',159000,'2025-09-26 00:38:17','DELIVERED','','성경김','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(17,'imp_710788318643','user3','ORD_9f2a32a4-2d63-4298-8e07-92b8c2fa3a93','경량 스트레치 맨투맨 외 2건',260000,'2025-09-26 00:40:01','PENDING','','정약용','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(18,'imp_413881748600','user3','ORD_11b293c1-0d4d-4634-bacd-0c57a0cc8fda','타이니 럼블 v2 외 4건',311000,'2025-09-26 00:41:36','SHIPPED','','정약용','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(19,'imp_850914223871','user3','ORD_463ad542-c231-4f6e-9bea-83103b9610d1','퍼포먼스 2WAY 슬링백',62000,'2025-09-26 00:42:06','DELIVERED','','정약용','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(20,'imp_997013718166','user4','ORD_3f370fc5-d69f-4085-b971-c2b40c5a4eed','슬랜트샷 98/23 외 2건',166000,'2025-09-26 00:43:47','PENDING','','이순신','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(21,'imp_221077002193','user4','ORD_f3c4c189-ced4-4185-8256-aa0a61261ddf','드리프터 튜브 쏭 외 2건',100000,'2025-09-26 00:44:50','SHIPPED','','이순신','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(22,'imp_599134802859','user4','ORD_ada62c94-2c63-43ab-b696-855780baefe1','에센셜 중목양말 3매입 외 2건',84800,'2025-09-26 00:45:46','DELIVERED','','이순신','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(23,'imp_085238925542','user5','ORD_f716a6a5-c45e-4644-b325-fe482ec3a867','휠라 토러스 v3',82000,'2025-09-26 00:47:34','PENDING','','이김밥','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(24,'imp_747284300380','user5','ORD_96b0bff6-0795-4c30-a5cd-26a1a722df3c','헤리티지스트라이프 아대 (2pcs) 외 2건',293000,'2025-09-26 00:48:15','SHIPPED','','이김밥','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000'),(25,'imp_168354786289','user5','ORD_19e20f76-c87a-40d6-a2eb-517959171b26','경량 스트레치 져지 반바지 외 2건',180000,'2025-09-26 00:49:32','DELIVERED','','이김밥','서울 강남구 어떤로 777강남 어떤 아파트 7동 777동','01000000000');
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

-- Dump completed on 2025-09-26 17:36:34
