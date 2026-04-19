-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com    Database: zysx_database_main
-- ------------------------------------------------------
-- Server version	8.0.11-TiDB-v7.5.2-serverless

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
-- Table structure for table `blood_oxygen_data`
--

DROP TABLE IF EXISTS `blood_oxygen_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_oxygen_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `oxygen_data` decimal(4,2) DEFAULT NULL COMMENT '血氧饱和度(%)',
  `record_time` timestamp NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_record_time` (`user_id`,`record_time`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blood_pressure_data`
--

DROP TABLE IF EXISTS `blood_pressure_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_pressure_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `systolic_bp` int(11) DEFAULT NULL COMMENT '收缩压',
  `diastolic_bp` int(11) DEFAULT NULL COMMENT '舒张压',
  `record_time` timestamp NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_record_time` (`user_id`,`record_time`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blood_sugar_data`
--

DROP TABLE IF EXISTS `blood_sugar_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_sugar_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `blood_data` decimal(5,2) DEFAULT NULL COMMENT '血糖值',
  `record_time` timestamp NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_record_time` (`user_id`,`record_time`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `daily_average_data`
--

DROP TABLE IF EXISTS `daily_average_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daily_average_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `data_type` enum('blood_pressure','blood_sugar','blood_oxygen','heart_rate','perfusion_index','sleep') NOT NULL,
  `average_value` decimal(8,2) NOT NULL,
  `record_date` date NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_type_date` (`user_id`,`data_type`,`record_date`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mac` varchar(50) NOT NULL COMMENT '设备的唯一标识',
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  UNIQUE KEY `equipment_pk_2` (`mac`),
  KEY `equipment_users_id_fk` (`user_id`),
  KEY `equipment_user_id_mac_index` (`user_id`,`mac`),
  CONSTRAINT `equipment_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30002 COMMENT='设备表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `heart_rate_data`
--

DROP TABLE IF EXISTS `heart_rate_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `heart_rate_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `heart_data` int(11) DEFAULT NULL COMMENT '心率(bpm)',
  `record_time` timestamp NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_record_time` (`user_id`,`record_time`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=60001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `monthly_average_data`
--

DROP TABLE IF EXISTS `monthly_average_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monthly_average_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `data_type` enum('blood_pressure','blood_sugar','blood_oxygen','heart_rate','blood_flow','sleep') NOT NULL,
  `average_value` decimal(8,2) NOT NULL,
  `record_month` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_type_year_month` (`user_id`,`data_type`,`record_month`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `perfusion_index_data`
--

DROP TABLE IF EXISTS `perfusion_index_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfusion_index_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `pi_data` decimal(5,2) DEFAULT NULL COMMENT '血流灌注指数',
  `record_time` timestamp NOT NULL,
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_record_time` (`user_id`,`record_time`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schedule_daily`
--

DROP TABLE IF EXISTS `schedule_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_daily` (
  `todo_id` bigint(20) NOT NULL COMMENT '代办ID，关联代办日程表的代办id',
  `end_time` time NOT NULL COMMENT '结束时刻（时分秒）',
  `location` varchar(100) DEFAULT NULL COMMENT '地点',
  `remarks` text DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`todo_id`) /*T![clustered_index] CLUSTERED */,
  CONSTRAINT `fk_1` FOREIGN KEY (`todo_id`) REFERENCES `zysx_database_main`.`todo_calendar` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='日常安排表（子表，关联代办日历）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schedule_medication`
--

DROP TABLE IF EXISTS `schedule_medication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_medication` (
  `todo_id` bigint(20) NOT NULL COMMENT '代办ID，关联代办日程表的代办id',
  `dosage` decimal(10,2) NOT NULL COMMENT '剂量',
  PRIMARY KEY (`todo_id`) /*T![clustered_index] CLUSTERED */,
  CONSTRAINT `fk_1` FOREIGN KEY (`todo_id`) REFERENCES `zysx_database_main`.`todo_calendar` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用药计划表（子表，关联代办日历）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sleep_data`
--

DROP TABLE IF EXISTS `sleep_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sleep_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `sleep_data` decimal(4,2) DEFAULT NULL COMMENT '睡眠时长(小时)',
  `record_time` datetime NOT NULL COMMENT '检测时间',
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_detection_date` (`user_id`,`record_time`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `todo_calendar`
--

DROP TABLE IF EXISTS `todo_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `todo_calendar` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代办id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `todo_type` enum('用药计划','日常安排') NOT NULL COMMENT '代办类型',
  `event_name` varchar(100) NOT NULL COMMENT '事件名称',
  `start_date` date NOT NULL COMMENT '开始日期（年月日）',
  `start_time` time NOT NULL COMMENT '开始时刻（时分秒）',
  `end_date` date NOT NULL COMMENT '结束时间（年月日）',
  `completed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '完成状态（0：未完成；1：已完成）',
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `fk_1` (`user_id`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001 COMMENT='代办日历表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `account` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gender` enum('male','female','other') DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `height` decimal(5,2) DEFAULT NULL COMMENT '身高(cm)',
  `weight` decimal(5,2) DEFAULT NULL COMMENT '体重(kg)',
  `past_medical_history` text DEFAULT NULL COMMENT '既往病史',
  `family_history` text DEFAULT NULL COMMENT '家族遗传病史',
  `allergy_history` text DEFAULT NULL COMMENT '过敏史',
  `surgical_history` text DEFAULT NULL COMMENT '手术史',
  `medical_compliance` text DEFAULT NULL COMMENT '用药医嘱',
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weekly_average_data`
--

DROP TABLE IF EXISTS `weekly_average_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `weekly_average_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `data_type` enum('blood_pressure','blood_sugar','blood_oxygen','heart_rate','blood_flow','sleep') NOT NULL,
  `average_value` decimal(8,2) NOT NULL,
  `start_date` tinyint(4) NOT NULL COMMENT '起始日期（包含当天）',
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */,
  KEY `idx_user_type_year_week` (`user_id`,`data_type`,`start_date`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `zysx_database_main`.`users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-26 10:05:37
