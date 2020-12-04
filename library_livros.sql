-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.14-MariaDB

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
-- Table structure for table `livros`
--

DROP TABLE IF EXISTS `livros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `livros` (
  `id_livro` int(5) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) DEFAULT NULL,
  `autores` varchar(30) DEFAULT NULL,
  `edicao` int(3) NOT NULL DEFAULT 1,
  `editora` varchar(30) DEFAULT NULL,
  `nome_livro` varchar(50) DEFAULT NULL,
  `ano` int(4) DEFAULT NULL,
  `user_que_alugou` varchar(30) DEFAULT NULL,
  `dia_que_alugou` datetime DEFAULT NULL,
  `user_que_reservou` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id_livro`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `livros`
--

LOCK TABLES `livros` WRITE;
/*!40000 ALTER TABLE `livros` DISABLE KEYS */;
INSERT INTO `livros` VALUES (21,'9788532523051','J. K. Rowling',3,'Scholastic ','Harry Potter',2010,NULL,NULL,NULL),(22,'9783161484106','Sun Tzu',1,'Novo Seculo','A Arte da Guerra',1997,NULL,NULL,NULL),(26,'9788546187235','Arthur Schopenhauer',2,'Edipro','As Dores do Mundo',1950,'matheuss','2020-12-03 00:00:00',NULL),(27,'9784244214215','Sofia',2,'Rasbro','O Mundo de Sofia',2001,'mathiass','2020-12-02 00:00:00','matheuss'),(28,'9783221521515','Thomas Bulfinch',1,'Agir','Mitologia Grega',2007,'matheuss','2020-12-03 00:00:00',NULL),(29,'9784212421412','Augusto Cury',3,'Planeta','O Colecionador de Lágrimas',2012,NULL,NULL,'luiz');
/*!40000 ALTER TABLE `livros` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-03  0:28:43
