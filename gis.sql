-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: 114.115.182.208    Database: gis_new
-- ------------------------------------------------------
-- Server version	5.6.40

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
-- Table structure for table `tb_building`
--

DROP TABLE IF EXISTS `tb_building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_building` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `streetId` int(11) NOT NULL COMMENT '所属物业街道ID',
  `longitude` double(9,6) NOT NULL COMMENT '经度',
  `latitude` double(9,6) NOT NULL COMMENT '纬度',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`name`),
  KEY `index_streetId` (`streetId`),
  KEY `index_lo_la` (`longitude`,`latitude`)
) ENGINE=InnoDB AUTO_INCREMENT=9290 DEFAULT CHARSET=utf8 COMMENT='建筑';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_center`
--

DROP TABLE IF EXISTS `tb_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `groupId` int(11) NOT NULL DEFAULT '0' COMMENT '所属要客组ID,0表示未关联任何要客组',
  `manager` varchar(100) DEFAULT NULL COMMENT '中心主任',
  `phone` varchar(50) DEFAULT NULL COMMENT '中心主任电话',
  `position` varchar(256) NOT NULL COMMENT '办公地点',
  `district` varchar(256) NOT NULL COMMENT '区县',
  `region` mediumtext NOT NULL COMMENT '坐标区域。json格式。格式要求：[[第1个点经度,第1个点纬度],[第2个点经度,第2个点纬度]...]。例：[[110.454702,21.263244],[110.45463,21.252599],[110.460451,21.251589],[110.468356,21.251589],[110.471087,21.258663],[110.470512,21.265467],[110.470512,21.265467]]',
  `loMax` double(9,6) NOT NULL COMMENT 'region字段中，计算出的所有坐标点中最大的经度',
  `loMin` double(9,6) NOT NULL COMMENT 'region字段中，计算出的所有坐标点中最小的经度',
  `laMax` double(9,6) NOT NULL COMMENT 'region字段中，计算出的所有坐标点中最大的纬度',
  `laMin` double(9,6) NOT NULL COMMENT 'region字段中，计算出的所有坐标点中最小的纬度',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `index_groupId` (`groupId`),
  KEY `index_lo_la` (`loMax`,`loMin`,`laMax`,`laMin`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='营销中心';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_config`
--

DROP TABLE IF EXISTS `tb_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expirationDateLimit` int(5) NOT NULL DEFAULT '0' COMMENT '业务到期提醒时间，单位：天',
  `mapSearchRegion` int(5) NOT NULL DEFAULT '0' COMMENT '地图搜索半径，单位：米',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_consumer`
--

DROP TABLE IF EXISTS `tb_consumer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_consumer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '名称',
  `buildingId` int(11) NOT NULL DEFAULT '0' COMMENT '所属建筑ID。0表示未关联建筑，此时定位以经纬度坐标为准，如果关联了建筑、就以建筑的经纬度坐标来定位',
  `floor` int(3) NOT NULL DEFAULT '1' COMMENT '楼层',
  `number` varchar(50) DEFAULT NULL COMMENT '门牌号',
  `position` varchar(100) DEFAULT NULL COMMENT '地址',
  `pic` mediumblob COMMENT '门头照',
  `category` varchar(200) DEFAULT NULL COMMENT '行业类别',
  `nature` varchar(200) DEFAULT NULL COMMENT '公司性质',
  `peopleNum` int(11) DEFAULT NULL COMMENT '公司人数',
  `linkman` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `operator` varchar(100) DEFAULT NULL COMMENT '现有业务运营商',
  `expenses` decimal(7,2) DEFAULT NULL COMMENT '现有业务资费',
  `expirationDate` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务到期时间。0表示未填写',
  `bandwidth` varchar(50) DEFAULT NULL COMMENT '带宽',
  `serviceType` int(2) DEFAULT NULL COMMENT '业务类型。1：专线产品，2：酒店产品，3：商务动力',
  `status` varchar(100) DEFAULT NULL COMMENT '公司状态',
  `legal` varchar(20) DEFAULT NULL COMMENT '法人',
  `lineNum` int(5) DEFAULT NULL COMMENT '专线条数，当serviceType为1时、必填',
  `lineType` varchar(100) DEFAULT NULL COMMENT '专线类型，当serviceType为1时、必填',
  `lineOpenDate` bigint(20) NOT NULL DEFAULT '0' COMMENT '专线开户时间，当serviceType为1时、必填。  0表示未填写',
  `lineStatus` varchar(100) DEFAULT NULL COMMENT '专线状态，当serviceType为1时、必填',
  `expensesName` varchar(100) DEFAULT NULL COMMENT '订购资费名称，当serviceType为2或3时、必填',
  `orderTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '订购时间，当serviceType为2或3时、必填。  0表示未填写',
  `memberRole` varchar(50) DEFAULT NULL COMMENT '成员角色，当serviceType为2或3时、必填',
  `memberRoleRealNum` varchar(100) DEFAULT NULL COMMENT '成员真实号码，当serviceType为2或3时、必填',
  `memberExpensesName` varchar(100) DEFAULT NULL COMMENT '成员侧资费名称，当serviceType为2或3时、必填',
  `groupCode` varchar(256) DEFAULT NULL COMMENT '集团代码',
  `groupGrade` varchar(50) DEFAULT NULL COMMENT '集团等级',
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '绑定到的客户经理ID。0表示未绑定',
  `type` int(2) NOT NULL COMMENT '建档类型。1：未建档，2：基础建档，3：有效建档',
  `longitude` double(9,6) NOT NULL DEFAULT '-999.000000' COMMENT '经度。如果关联了建筑、此时必须等于建筑的经度，如果是通过地图抓取的、此时等于抓取的经度，如果是通过excel导入创建的、此时等于-999、表示没有定位',
  `latitude` double(9,6) NOT NULL DEFAULT '-999.000000' COMMENT '纬度。如果关联了建筑、此时必须等于建筑的纬度，如果是通过地图抓取的、此时等于抓取的纬度，如果是通过excel导入创建的、此时等于-999、表示没有定位',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`),
  KEY `index_expiration` (`expirationDate`),
  KEY `index_buildingId` (`buildingId`),
  KEY `index_lo_la` (`longitude`,`latitude`),
  KEY `index_userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_group`
--

DROP TABLE IF EXISTS `tb_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `position` varchar(256) NOT NULL COMMENT '办公地点',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='要客组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_resource`
--

DROP TABLE IF EXISTS `tb_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `buildingId` int(11) NOT NULL DEFAULT '0' COMMENT '所属建筑ID。0表示未关联建筑，此时定位以经纬度坐标为准，如果关联了建筑、就以建筑的经纬度坐标来定位',
  `district` varchar(256) DEFAULT NULL COMMENT '区县',
  `cityName` varchar(100) DEFAULT NULL COMMENT '地市名称',
  `streetName` varchar(100) DEFAULT NULL COMMENT '街道',
  `villageName` varchar(100) DEFAULT NULL COMMENT '乡镇',
  `admStreetName` varchar(100) DEFAULT NULL COMMENT '道路/行政村',
  `zoneName` varchar(100) DEFAULT NULL COMMENT '片区/学校',
  `floor` int(6) NOT NULL DEFAULT '1' COMMENT '楼层',
  `number` varchar(50) DEFAULT NULL COMMENT '户号',
  `allPortCount` int(5) NOT NULL DEFAULT '0' COMMENT '总端口数',
  `idelPortCount` int(5) NOT NULL DEFAULT '0' COMMENT '空闲端口数',
  `sceneA` varchar(256) DEFAULT NULL COMMENT '用户场景一类',
  `sceneB` varchar(256) DEFAULT NULL COMMENT '用户场景二类',
  `overlayScene` varchar(256) DEFAULT NULL COMMENT '覆盖场景',
  `longitude` double(9,6) NOT NULL DEFAULT '-999.000000' COMMENT '经度。如果关联了建筑、此时必须等于建筑的经度，如果是通过地图抓取的、此时等于抓取的经度，如果是通过excel导入创建的、此时等于-999、表示没有定位',
  `latitude` double(9,6) NOT NULL DEFAULT '-999.000000' COMMENT '纬度。如果关联了建筑、此时必须等于建筑的纬度，如果是通过地图抓取的、此时等于抓取的纬度，如果是通过excel导入创建的、此时等于-999、表示没有定位',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `primaryId` varchar(100) NOT NULL COMMENT '外线ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_1` (`primaryId`),
  KEY `index_buildingId` (`buildingId`),
  KEY `index_lo_la` (`longitude`,`latitude`)
) ENGINE=InnoDB AUTO_INCREMENT=27661 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_statistic_center`
--

DROP TABLE IF EXISTS `tb_statistic_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_statistic_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `centerId` int(11) NOT NULL COMMENT '营销中心id',
  `notArchiveCount` int(10) NOT NULL DEFAULT '0' COMMENT '未建档数',
  `basicArchiveCount` int(10) NOT NULL DEFAULT '0' COMMENT '基础建档数',
  `effectiveArchiveCount` int(10) NOT NULL DEFAULT '0' COMMENT '有效建档数/完整建档数/已建档数',
  `wholePortCount` int(10) NOT NULL DEFAULT '0' COMMENT '总端口数',
  `usedPortCount` int(10) NOT NULL DEFAULT '0' COMMENT '已占用端口数',
  `specialLineRatio` float(5,4) NOT NULL DEFAULT '0.0000' COMMENT '专线比率',
  `hotelRatio` float(5,4) NOT NULL DEFAULT '0.0000' COMMENT '酒店比率',
  `businessRatio` float(5,4) NOT NULL DEFAULT '0.0000' COMMENT '商务动力比率',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32549 DEFAULT CHARSET=utf8 COMMENT='营销中心统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_statistic_user`
--

DROP TABLE IF EXISTS `tb_statistic_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_statistic_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL COMMENT '客户经理id',
  `basicArchiveCount` int(10) NOT NULL DEFAULT '0' COMMENT '基础建档数',
  `effectiveArchiveCount` int(10) NOT NULL DEFAULT '0' COMMENT '有效建档数/完整建档数/已建档数',
  `specialLineCount` int(10) NOT NULL DEFAULT '0' COMMENT '新建专线数量',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29420 DEFAULT CHARSET=utf8 COMMENT='客户经理业务统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_street`
--

DROP TABLE IF EXISTS `tb_street`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_street` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `position` varchar(256) NOT NULL COMMENT '地址',
  `type` int(2) NOT NULL COMMENT '类型。1：商务楼宇，2：综合聚类，3：工业园区，4：商业街道',
  `manager` varchar(45) NOT NULL COMMENT '物业负责人',
  `phone` varchar(45) NOT NULL COMMENT '物业电话',
  `pic` mediumblob COMMENT '照片',
  `remark` varchar(256) DEFAULT NULL COMMENT '物业备注',
  `competitor` text COMMENT '竞争对手，json格式。格式要求：[{"name":"","phone":""''}...]。例：[{"name":"对手1名称","phone":"15900000000"},{"name":"对手p名称","phone":"15800000000"}]',
  `longitude` double(9,6) NOT NULL DEFAULT '-999.000000' COMMENT '经度。根据物业街道下面的所有建筑的经纬度计算得出（所有建筑的中心坐标点的经度）。当该物业街道没有建筑时，默认-999',
  `latitude` double(9,6) NOT NULL DEFAULT '-999.000000' COMMENT '纬度。根据物业街道下面的所有建筑的经纬度计算得出（所有建筑的中心坐标点的纬度）。当该物业街道没有建筑时，默认-999',
  `ctime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_1` (`name`),
  KEY `index_lo_la` (`longitude`,`latitude`)
) ENGINE=InnoDB AUTO_INCREMENT=6360 DEFAULT CHARSET=utf8 COMMENT='物业街道';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `phone` varchar(50) NOT NULL COMMENT '电话',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `role` int(2) NOT NULL COMMENT '角色。1：管理员，2：要客组长，3：客户经理',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `centerId` int(11) NOT NULL DEFAULT '0' COMMENT '所属营销中心ID，0表示不关联',
  `groupId` int(11) NOT NULL DEFAULT '0' COMMENT '所属要客组ID。0表示不关联',
  `key` varchar(256) DEFAULT NULL COMMENT '手机序列号',
  `number` varchar(100) NOT NULL COMMENT '工号',
  `post` varchar(100) DEFAULT NULL COMMENT '职位',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_1` (`phone`),
  UNIQUE KEY `unique_2` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'gis_new'
--

--
-- Dumping routines for database 'gis_new'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-25 10:28:50
