-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: lms
-- ------------------------------------------------------
-- Server version	8.0.39

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
  `user_ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `credit` bigint DEFAULT '0',
  `role` int NOT NULL,
  `field` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_ID`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `user_chk_1` CHECK ((`role` in (0,1,2)))
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (13,'Sam Green','sam@student.com','ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',100,1,'Computer Science'),(14,'Lucy White','lucy@student.com','ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',50,0,'Economics'),(15,'Mark Blue','mark@student.com','ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',80,0,'Literature'),(16,'Prof. Kelly Black','kelly@instructor.com','c6ba91b90d922e159893f46c387e5dc1b3dc5c101a5a4522f03b987177a24a91',200,1,'Physics'),(17,'Prof. Amy Grey','amy@instructor.com','5efc2b017da4f7736d192a74dde5891369e0685d4d38f2a455b6fcdab282df9c',150,1,'Mathematics'),(18,'Admin John','admin@lms.com','f40c05434358a62bde28b7991e16f880d059d99adb2c20242cf2db46c197e322',0,2,NULL),(19,'zheer salam','zheer@gmail.com','1234',0,1,'soft eng'),(20,'tech test','tech@gmail.com','1234',0,2,'math'),(21,'zheer salam','zheersalam@gmail.com','eefe09c3a66a0a9cd8263a9f34fac689d473a9f0835d067d8ca9ba3de8cd35d5',0,1,'soft eng'),(22,'John Doe','john.doe@programming.com','password123',100,2,'Programming & IT'),(23,'Jane Smith','jane.smith@graphicdesign.com','password123',100,2,'Graphic Design'),(24,'Alan Turing','alan.turing@math.com','password123',100,2,'Mathematics'),(25,'Emily Johnson','emily.johnson@languages.com','password123',100,2,'Language Learning'),(26,'Marie Curie','marie.curie@science.com','password123',100,2,'Science'),(27,'Dr. House','dr.house@medicine.com','password123',100,2,'Health and Medicine'),(28,'Nikola Tesla','nikola.tesla@engineering.com','password123',100,2,'Engineering'),(29,'Warren Buffett','warren.buffett@business.com','password123',100,2,'Business and Management'),(30,'Tony Robbins','tony.robbins@personaldev.com','password123',100,2,'Personal Development'),(31,'Philip Kotler','philip.kotler@marketing.com','password123',100,2,'Marketing');
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

-- Dump completed on 2024-10-18 16:59:17
