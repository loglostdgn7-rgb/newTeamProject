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
-- Table structure for table `review_defaults`
--

DROP TABLE IF EXISTS `review_defaults`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_defaults` (
  `review_Id` int NOT NULL,
  `image` longblob,
  `content` varchar(255) NOT NULL,
  `at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` varchar(20) NOT NULL,
  `product_id` int NOT NULL,
  `order_id` int DEFAULT NULL,
  PRIMARY KEY (`review_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_defaults`
--

LOCK TABLES `review_defaults` WRITE;
/*!40000 ALTER TABLE `review_defaults` DISABLE KEYS */;
INSERT INTO `review_defaults` VALUES (1,NULL,'제품이 생각보다 좋아요!','2025-07-01 00:00:00','user1',16,11),(2,NULL,'배송이 빨랐습니다.','2025-07-21 00:00:00','user1',27,11),(3,NULL,'포장이 깔끔했어요.','2025-07-13 00:00:00','user1',29,11),(4,NULL,'품질이 아주 만족스럽습니다.','2025-03-30 00:00:00','user1',87,11),(5,NULL,'다시 구매할 의향 있어요.','2024-11-21 00:00:00','user1',2,12),(6,NULL,'사이즈가 조금 작네요.','2024-12-20 00:00:00','user1',138,12),(7,NULL,'가성비가 최고입니다.','2025-07-01 00:00:00','user1',192,12),(8,NULL,'친구한테 추천했어요.','2025-07-01 00:00:00','user1',277,12),(9,NULL,'색상이 화면과 똑같습니다.','2025-07-01 00:00:00','user1',427,12),(10,NULL,'사용하기 편리합니다.','2025-07-01 00:00:00','user1',85,13),(11,NULL,'품질에 비해 가격이 비쌉니다.','2025-07-01 00:00:00','user1',96,13),(12,NULL,'예상보다 튼튼해요.','2025-07-01 00:00:00','user1',139,13),(13,NULL,'설명서가 이해하기 쉬웠습니다.','2025-07-01 00:00:00','user2',83,14),(14,NULL,'조립이 간단했어요.','2025-07-01 00:00:00','user2',93,14),(15,NULL,'디자인이 세련됐습니다.','2025-03-30 00:00:00','user2',102,14),(16,NULL,'조금 무겁습니다.','2025-03-30 00:00:00','user2',13,15),(17,NULL,'재구매 의사 있어요.','2025-03-30 00:00:00','user2',107,15),(18,NULL,'냄새가 약간 나네요.','2025-03-30 00:00:00','user2',110,15),(19,NULL,'선물용으로 좋습니다.','2025-03-30 00:00:00','user2',128,15),(20,NULL,'아이도 좋아해요.','2025-03-30 00:00:00','user2',54,16),(21,NULL,'제품이 생각보다 좋아요!','2025-07-01 00:00:00','user2',91,16),(22,NULL,'배송이 빨랐습니다.','2025-07-21 00:00:00','user2',221,16),(23,NULL,'포장이 깔끔했어요.','2025-07-13 00:00:00','user3',273,17),(24,NULL,'품질이 아주 만족스럽습니다.','2025-03-30 00:00:00','user3',281,17),(25,NULL,'다시 구매할 의향 있어요.','2024-11-21 00:00:00','user3',287,17),(27,NULL,'가성비가 최고입니다.','2025-07-01 00:00:00','user3',14,18),(28,NULL,'친구한테 추천했어요.','2025-07-01 00:00:00','user3',13,18),(29,NULL,'색상이 화면과 똑같습니다.','2025-07-01 00:00:00','user3',179,18),(30,NULL,'사용하기 편리합니다.','2025-07-01 00:00:00','user3',429,18),(31,NULL,'품질에 비해 가격이 비쌉니다.','2025-07-01 00:00:00','user3',438,18),(32,NULL,'예상보다 튼튼해요.','2025-07-01 00:00:00','user4',74,19),(34,NULL,'조립이 간단했어요.','2025-07-01 00:00:00','user4',29,20),(35,NULL,'디자인이 세련됐습니다.','2025-03-30 00:00:00','user4',73,20),(36,NULL,'조금 무겁습니다.','2025-03-30 00:00:00','user4',106,20),(38,NULL,'냄새가 약간 나네요.','2025-03-30 00:00:00','user4',57,21),(39,NULL,'선물용으로 좋습니다.','2025-03-30 00:00:00','user4',65,21),(40,NULL,'아이도 좋아해요.','2025-03-30 00:00:00','user4',105,21),(41,NULL,'제품이 생각보다 좋아요!','2025-07-01 00:00:00','user4',118,22),(42,NULL,'배송이 빨랐습니다.','2025-07-21 00:00:00','user4',119,22),(43,NULL,'포장이 깔끔했어요.','2025-07-13 00:00:00','user4',132,22),(44,NULL,'품질이 아주 만족스럽습니다.','2025-03-30 00:00:00','user5',13,24),(45,NULL,'다시 구매할 의향 있어요.','2024-11-21 00:00:00','user5',84,24),(46,NULL,'사이즈가 조금 작네요.','2024-12-20 00:00:00','user5',88,24),(47,NULL,'가성비가 최고입니다.','2025-07-01 00:00:00','user5',151,24),(48,NULL,'친구한테 추천했어요.','2025-07-01 00:00:00','user5',182,25),(49,NULL,'색상이 화면과 똑같습니다.','2025-07-01 00:00:00','user5',192,25),(50,NULL,'사용하기 편리합니다.','2025-07-01 00:00:00','user5',213,25);
/*!40000 ALTER TABLE `review_defaults` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-14  0:35:26
