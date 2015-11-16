-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: eftech_oils
-- ------------------------------------------------------
-- Server version	5.6.21

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
-- Table structure for table `brakingfluids`
--

DROP TABLE IF EXISTS `brakingfluids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brakingfluids` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) DEFAULT NULL,
  `manufacturer` int(11) DEFAULT '0',
  `price` double DEFAULT '0',
  `fluidclass` int(11) DEFAULT '0',
  `BoilingTemperatureDry` double DEFAULT '0',
  `BoilingTemperatureWet` double DEFAULT '0',
  `Value` double DEFAULT '0',
  `photo` varchar(70) DEFAULT '',
  `Description` text,
  `Viscosity40` double DEFAULT '0',
  `Viscosity100` double DEFAULT '0',
  `Specification` text,
  `judgement` double NOT NULL,
  PRIMARY KEY (`id`,`judgement`),
  UNIQUE KEY `idBrakingFluids_UNIQUE` (`id`),
  KEY `manufacturer_idx` (`manufacturer`),
  KEY `price_idx` (`price`),
  KEY `fluidclass_idx` (`fluidclass`),
  CONSTRAINT `fluidclass` FOREIGN KEY (`fluidclass`) REFERENCES `fluidclass` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `manufacturer` FOREIGN KEY (`manufacturer`) REFERENCES `manufacturer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8 COMMENT='Braking fluids';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brakingfluids`
--

LOCK TABLES `brakingfluids` WRITE;
/*!40000 ALTER TABLE `brakingfluids` DISABLE KEYS */;
INSERT INTO `brakingfluids` VALUES (120,'Comma DOT4 1л',19,115000,2,260,155,1,'Fluid1.jpg','Универсальная синтетическая тормозная жидкость ДОТ4\nПрименима для всех гидравлических тормозных систем и систем сцепления, где требуется спецификация DOT3, DOT4, J1703.\nИдеальна для тормозных систем c ABS. Смешивается со всеми жидкостями DOT3, DOT4, SAE 1703.\n\nЗамена тормозной жидкости производится согласно рекомендациям автопроизводителей. Следить за чистотой жидкости, не допускать попадания воды, масла, грязи и пр. Гигроскопична – требует своевременной замены. Повреждает лакокрасочное покрытие.',1350,2.3,'FMVSS 116 DOT 4;\nISO 4925;\nSAE J-1704. ',4),(121,'Bosch DOT4 500мл',20,540000,2,300,10000,0.5,'Fluid10.jpg','Длительный срок службы и хранения:\r\n- жестяная упаковка предотвращающая гигроскопичность;\r\n- запатентованная природоохранная система переработки упаковки. ',0,0,'',0),(122,'TRW Brake Fluid DOT5.1 1л',21,160000,13,120,100,2,'Fluid3.jpg','Автомобили премиум класса, включая автомобили, оснащенные ABS. Состав жидкости обеспечивает максимальный уровень безопасности. Благодаря улучшенным вязкостным характеристикам при температуре -40°C, жидкость безопасна при использовании в самом холодном климате. ',0,0,'',5),(123,'Fuchs Maintain DOT 4 1л',22,185000,2,0,0,1,'Fluid4.jpg','MAINTAIN DOT 4 – это высокоэффективная тормозная жидкость для гидравлических тормозных системы легкового и коммерческого транспорта, а также для гидравлических приводов сцепления, если рекомендуется к применению тормозная жидкость.',0,0,'FMVSS NR. 116; DOT 3/DOT 4; ISO 4925 CLASS 3/4; SAE J 1703/J 1704; FORD M6C62-A; FORD M6C9103-A; NH 800 A; OPEL 19 42 421 ',0),(124,'Fuchs Maintain DOT 5.1 1л',22,185000,13,270,0,1,'Fluid5.jpg','MAINTAIN DOT 5.1 это высокооэффективная тормозная жидкость для гидравлических тормозных систем легкового и коммерческого транспорта, а также для гидравлических проводов сцепления. ',900,0,'FMVSS NR. 116 DOT 5.1; ISO 4925 CLASS 3/4/5.1; SAE J 1703/J 1704 ',0),(125,'Elf Frelub 650 DOT4 1л Elf Frelub 650 DOT4 1л',23,160000,12,260,180,1,'Fluid6.jpg','Не смешивать с минеральными гидравлическими жидкостями и не применять в гидропневматических подвесках и тормозах автомобилей типа Citroen. На синтетической основе. Обладает стабильностью эксплуатационных характеристик при экстремальных температурах, противопенной активностью и стойкостью к образованию отложений и смол. Предотвращает образование коррозии. ',0,300,'SAE J 1704; FMVSS 116 (DOT 4); DOT 4; DOT 5. ',0),(126,'Ford Super DOT 4 1л',24,240000,2,0,0,1,'Fluid7.jpg','Рекомендуемая замена при ТО.',0,0,'FMVSS 116;\nSAE J 1703\';\nSAE J 1704. ',0),(127,'Тосол-Синтез FELIX DOT4 0.91л',25,98150,2,250,155,0.91,'Fluid8.jpg','Стандартная тормозная жидкость класса DOT4. ',1650,0,'',0),(128,'Castrol Brake Fluid DOT 4 0.5л',26,126000,2,0,0,0.5,'Fluid9.jpg','Предназначено для применения во всех тормозных системах, особенно подверженных высокой нагрузке.\n- Тормозная жидкость с высокой температурой кипения, композиция которой включает в себя смесь сложных эфиров полиалкиленгликоля, сложного эфира бора, высокоэффективных присадок и ингибиторов.\n- Отличная устойчивость к экстремальным температурам.\n- Надежная защита тормозной\nсистемы от коррозии\nи образования паровых пробок. ',0,0,'SAE J1703;\nFMVSS 116 DOT 4. ',0),(129,'BP Brake Fluid DOT 0,5л',27,64300,2,270,170,0.5,'Fluid10.jpg','Синтетическая жидкость Brake Fluid DOT 4 не должна применяться в гидравлических системах, для которых рекомендуется использовать жидкости на минеральной основе. ',1250,2.3,'SAE J 1703; FMVSS № 116 DOT4; ISO 4925 ',5);
/*!40000 ALTER TABLE `brakingfluids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `country` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `country_idx` (`country`),
  CONSTRAINT `country` FOREIGN KEY (`country`) REFERENCES `country` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'Рога и копыта','glebas@tut.by','пер. Козлова, д.1',1),(2,'Снабсбытмонтажфанера','ss@yandex.ru','д. Гадюкино',1),(3,'Procter&Procter','pp@rambler.ru','Drezden',2);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='Countris';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'Беларусь'),(2,'Беларусь'),(3,'Германия'),(4,'США'),(5,'Япония'),(6,'Китай'),(7,'USA'),(8,'Canada'),(9,'Zimbabve'),(10,'Россия'),(11,'Великобритания'),(12,'Франция'),(13,'РФ'),(14,'<emptry>');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fluidclass`
--

DROP TABLE IF EXISTS `fluidclass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fluidclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='Class of bracking fluid';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fluidclass`
--

LOCK TABLES `fluidclass` WRITE;
/*!40000 ALTER TABLE `fluidclass` DISABLE KEYS */;
INSERT INTO `fluidclass` VALUES (1,'DOT3'),(2,'DOT4'),(3,'J1703'),(4,'SAE 1703'),(12,'DOT5'),(13,'DOT5.1');
/*!40000 ALTER TABLE `fluidclass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manufacturer`
--

DROP TABLE IF EXISTS `manufacturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manufacturer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `country` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `country_idx` (`country`),
  KEY `country_manufacturer_idx` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='Manufacturer of braking fluid';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manufacturer`
--

LOCK TABLES `manufacturer` WRITE;
/*!40000 ALTER TABLE `manufacturer` DISABLE KEYS */;
INSERT INTO `manufacturer` VALUES (1,'Toyota',6),(2,'XuXuXu',6),(3,'BMW',2),(4,'Наши жидкости',1),(12,'Procter&Gamble',0),(13,'Apple',8),(14,'ООО ВыхтехКруг',10),(15,'ДойчеМоторен',3),(16,'Дрозды Инкорпорейтед',1),(17,'ИП Тояма',5),(18,'Буш и братья Inc',7),(19,'Comma',11),(20,'Bosch',3),(21,'TRW',3),(22,'Fuchs',3),(23,'Elf',12),(24,'Ford',4),(25,' FELIX',13),(26,'Castrol',4),(27,'BP',11);
/*!40000 ALTER TABLE `manufacturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prices`
--

DROP TABLE IF EXISTS `prices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `price` double DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prices`
--

LOCK TABLES `prices` WRITE;
/*!40000 ALTER TABLE `prices` DISABLE KEYS */;
INSERT INTO `prices` VALUES (1,'2010-12-20 15:00:00',100),(2,'2009-01-20 14:00:00',50),(3,'2011-09-20 15:00:00',70);
/*!40000 ALTER TABLE `prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'saler'),(3,'dealer'),(4,'ROLE_PRODUCT'),(5,'ROLE_PRICE'),(6,'ROLE_DISTR'),(7,'ROLE_OFFER'),(8,'ROLE_OFFERPRICE');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `login` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`email`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `idusers_UNIQUE` (`id`),
  KEY `fk_users_roles_idx` (`role`),
  CONSTRAINT `fk_users_roles` FOREIGN KEY (`role`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='System users';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin Иван','admin@eftech.by','admin','111',1),(2,'login1','saler@eftech.by','login1','111',4),(3,'login2','dealer@eftech.by','login2','111',5),(6,'login3','salesmanager@eftech.by','login3','111',6),(7,'login4','login5@eftech.by','login4','111',7),(8,'login5','','login5','111',8);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-24  0:55:19
