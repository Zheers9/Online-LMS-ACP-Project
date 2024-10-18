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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `Course_ID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `description` text,
  `Credit` int NOT NULL DEFAULT '0',
  `Instructor_ID` int DEFAULT NULL,
  `Catagory_Name` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Course_ID`),
  KEY `Instructor_ID` (`Instructor_ID`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`Instructor_ID`) REFERENCES `user` (`user_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Physics 101','Basic physics concepts.',3,16,'Physics','2024-10-17 10:14:13'),(2,'Advanced Physics','In-depth physics principles.',4,16,'Physics','2024-10-17 10:14:13'),(8,'Intro to Java','Basic Java programming concepts.',3,22,'Programming & IT','2024-10-17 14:14:47'),(9,'Advanced Python','In-depth exploration of Python.',4,22,'Programming & IT','2024-10-17 14:14:47'),(10,'Web Development','Full-stack web development.',3,22,'Programming & IT','2024-10-17 14:14:47'),(11,'Data Structures','Fundamental data structures in computer science.',3,22,'Programming & IT','2024-10-17 14:14:47'),(12,'Algorithms','Introduction to algorithms and problem-solving.',4,22,'Programming & IT','2024-10-17 14:14:47'),(13,'Intro to Photoshop','Learn the basics of Adobe Photoshop.',3,23,'Graphic Design','2024-10-17 14:14:47'),(14,'Advanced Illustrator','Expert techniques in Adobe Illustrator.',4,23,'Graphic Design','2024-10-17 14:14:47'),(15,'Typography Basics','Foundations of typography and font design.',2,23,'Graphic Design','2024-10-17 14:14:47'),(16,'Color Theory','Understanding color schemes and applications.',3,23,'Graphic Design','2024-10-17 14:14:47'),(17,'Logo Design','Creating effective and creative logos.',3,23,'Graphic Design','2024-10-17 14:14:47'),(18,'Calculus 101','Introduction to calculus concepts.',3,24,'Mathematics','2024-10-17 14:14:47'),(19,'Linear Algebra','Study of vector spaces and matrices.',4,24,'Mathematics','2024-10-17 14:14:47'),(20,'Statistics for Data Science','Statistical methods for data analysis.',3,24,'Mathematics','2024-10-17 14:14:47'),(21,'Probability Theory','Basic principles of probability.',3,24,'Mathematics','2024-10-17 14:14:47'),(22,'Discrete Mathematics','Study of discrete structures in math.',4,24,'Mathematics','2024-10-17 14:14:47'),(23,'Basic French','Introduction to French language.',3,25,'Language Learning','2024-10-17 14:14:47'),(24,'Advanced Spanish','Advanced concepts in Spanish language.',4,25,'Language Learning','2024-10-17 14:14:47'),(25,'Conversational English','Improving everyday English communication.',3,25,'Language Learning','2024-10-17 14:14:47'),(26,'German for Beginners','Basic German language skills.',3,25,'Language Learning','2024-10-17 14:14:47'),(27,'Mandarin Chinese 101','Introduction to Mandarin Chinese.',4,25,'Language Learning','2024-10-17 14:14:47'),(28,'Physics 101','Basic physics concepts.',3,26,'Science','2024-10-17 14:14:47'),(29,'Advanced Chemistry','Exploring advanced chemistry principles.',4,26,'Science','2024-10-17 14:14:47'),(30,'Introduction to Biology','Study of living organisms.',3,26,'Science','2024-10-17 14:14:47'),(31,'Environmental Science','Understanding environmental systems.',3,26,'Science','2024-10-17 14:14:47'),(32,'Astronomy Basics','Exploring the universe and celestial bodies.',4,26,'Science','2024-10-17 14:14:47'),(33,'Intro to Human Anatomy','Understanding the human body.',3,27,'Health and Medicine','2024-10-17 14:14:47'),(34,'Pharmacology','Study of drugs and their effects.',4,27,'Health and Medicine','2024-10-17 14:14:47'),(35,'Clinical Procedures','Overview of common clinical practices.',3,27,'Health and Medicine','2024-10-17 14:14:47'),(36,'Medical Ethics','Ethical principles in medical practice.',3,27,'Health and Medicine','2024-10-17 14:14:47'),(37,'Emergency Medicine','Handling emergency medical situations.',4,27,'Health and Medicine','2024-10-17 14:14:47'),(38,'Electrical Engineering Basics','Introduction to electrical engineering.',3,28,'Engineering','2024-10-17 14:14:47'),(39,'Mechanical Engineering Principles','Study of mechanical systems.',4,28,'Engineering','2024-10-17 14:14:47'),(40,'Civil Engineering Concepts','Introduction to civil engineering.',3,28,'Engineering','2024-10-17 14:14:47'),(41,'Thermodynamics','Study of heat and temperature.',3,28,'Engineering','2024-10-17 14:14:47'),(42,'Robotics 101','Introduction to robotics and automation.',4,28,'Engineering','2024-10-17 14:14:47'),(43,'Business Strategy','Overview of business strategies.',3,29,'Business and Management','2024-10-17 14:14:47'),(44,'Financial Accounting','Study of accounting principles.',4,29,'Business and Management','2024-10-17 14:14:47'),(45,'Marketing Management','Managing marketing strategies.',3,29,'Business and Management','2024-10-17 14:14:47'),(46,'Human Resource Management','Principles of HR management.',3,29,'Business and Management','2024-10-17 14:14:47'),(47,'Entrepreneurship','Building and managing startups.',4,29,'Business and Management','2024-10-17 14:14:47'),(48,'Public Speaking','Mastering public speaking skills.',3,30,'Personal Development','2024-10-17 14:14:47'),(49,'Time Management','Effective time management techniques.',4,30,'Personal Development','2024-10-17 14:14:47'),(50,'Emotional Intelligence','Developing emotional intelligence.',3,30,'Personal Development','2024-10-17 14:14:47'),(51,'Leadership Skills','Developing leadership qualities.',3,30,'Personal Development','2024-10-17 14:14:47'),(52,'Building Confidence','Boosting self-confidence and self-esteem.',4,30,'Personal Development','2024-10-17 14:14:47'),(53,'Digital Marketing','Understanding online marketing techniques.',3,31,'Marketing','2024-10-17 14:14:47'),(54,'Brand Management','Managing brand image and identity.',4,31,'Marketing','2024-10-17 14:14:47'),(55,'SEO & SEM','Mastering search engine optimization and marketing.',3,31,'Marketing','2024-10-17 14:14:47'),(56,'Social Media Marketing','Leveraging social media platforms for marketing.',3,31,'Marketing','2024-10-17 14:14:47'),(57,'Content Marketing','Creating compelling marketing content.',4,31,'Marketing','2024-10-17 14:14:47');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
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
