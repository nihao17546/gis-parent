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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `tb_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `position` varchar(256) NOT NULL COMMENT '办公地点',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_1` (`name`)
) ENGINE=InnoD DEFAULT CHARSET=utf8 COMMENT='要客组';

CREATE TABLE `tb_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `groupId` `groupId` int(11) default '0' NOT NULL comment '所属要客组ID,0表示未关联任何要客组',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='营销中心';

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物业街道';

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
  KEY `index_lo&la` (`longitude`,`latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建筑';

CREATE TABLE `tb_consumer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '名称',
  `buildingId` int(11) NOT NULL COMMENT '所属建筑ID',
  `floor` varchar(20) DEFAULT NULL COMMENT '楼层',
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
  `expirationDate` bigint(20) DEFAULT NULL COMMENT '业务到期时间',
  `bandwidth` varchar(50) DEFAULT NULL COMMENT '带宽',
  `serviceType` int(2) DEFAULT NULL COMMENT '业务类型。1：专线产品，2：酒店产品，3：商务动力',
  `status` varchar(100) DEFAULT NULL COMMENT '公司状态',
  `legal` varchar(20) DEFAULT NULL COMMENT '法人',
  `lineNum` int(5) DEFAULT NULL COMMENT '专线条数',
  `lineType` varchar(100) DEFAULT NULL COMMENT '专线类型',
  `lineOpenDate` bigint(20) DEFAULT NULL COMMENT '专线开户时间',
  `lineStatus` varchar(100) DEFAULT NULL COMMENT '专线状态',
  `groupCode` varchar(256) DEFAULT NULL COMMENT '集团代码',
  `groupGrade` varchar(50) DEFAULT NULL COMMENT '集团等级',
  `userId` int(11) DEFAULT NULL COMMENT '绑定到的客户经理ID。0表示未绑定',
  `type` int(2) NOT NULL COMMENT '建档类型。1：未建档，2：基础建档，3：有效建档',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `buildingId` INT(11) NOT NULL DEFAULT 0 COMMENT '所属建筑ID',
  `district` VARCHAR(256) NULL COMMENT '区县',
  `floor` VARCHAR(20) NULL COMMENT '楼层',
  `number` VARCHAR(50) NULL COMMENT '户号',
  `allPortCount` INT(5) NOT NULL DEFAULT 0 COMMENT '总端口数',
  `idelPortCount` INT(5) NOT NULL DEFAULT 0 COMMENT '空闲端口数',
  `sceneA` VARCHAR(256) NULL COMMENT '用户场景一类',
  `sceneB` VARCHAR(256) NULL COMMENT '用户场景二类',
  `overlayScene` VARCHAR(256) NULL COMMENT '覆盖场景',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`));

ALTER TABLE `tb_resource`
CHANGE COLUMN `buildingId` `buildingId` INT(11) NOT NULL DEFAULT '0' COMMENT '所属建筑ID。0表示未关联建筑，此时定位以经纬度坐标为准，如果关联了建筑、就以建筑的经纬度坐标来定位' ,
ADD COLUMN `longitude` DOUBLE(9,6) NULL COMMENT '经度,当未关联建筑时,此字段不为空' AFTER `overlayScene`,
ADD COLUMN `latitude` DOUBLE(9,6) NULL COMMENT '纬度,当未关联建筑时,此字段不为空' AFTER `longitude`;

ALTER TABLE `tb_consumer`
CHANGE COLUMN `buildingId` `buildingId` INT(11) NOT NULL DEFAULT 0 COMMENT '所属建筑ID。0表示未关联建筑，此时定位以经纬度坐标为准，如果关联了建筑、就以建筑的经纬度坐标来定位' ,
ADD COLUMN `longitude` DOUBLE(9,6) NULL COMMENT '经度,当未关联建筑时,此字段不为空' AFTER `type`,
ADD COLUMN `latitude` DOUBLE(9,6) NULL COMMENT '纬度,当未关联建筑时,此字段不为空' AFTER `longitude`;

ALTER TABLE `tb_consumer`
CHANGE COLUMN `longitude` `longitude` DOUBLE(9,6) NOT NULL COMMENT '经度,当关联建筑时,此字段等于关联建筑的经度' ,
CHANGE COLUMN `latitude` `latitude` DOUBLE(9,6) NOT NULL COMMENT '纬度,当关联建筑时,此字段等于关联建筑的纬度' ;

ALTER TABLE `tb_resource`
CHANGE COLUMN `longitude` `longitude` DOUBLE(9,6) NOT NULL COMMENT '经度,当关联建筑时,此字段等于关联建筑的经度' ,
CHANGE COLUMN `latitude` `latitude` DOUBLE(9,6) NOT NULL COMMENT '纬度,当关联建筑时,此字段等于关联建筑的纬度' ;

create table `tb_config`(
   `mapSearchRegion` int(5) NOT NULL DEFAULT '0' COMMENT '地图搜索半径，单位：米',
   `expirationDateLimit` int(5) NOT NULL DEFAULT '0' COMMENT '业务到期提醒时间，单位：天'
)

alter table `tb_consumer`
add column `expensesName` varchar(100) NULL COMMENT '订购资费名称，当serviceType为2或3时、必填' after `lineStatus`,
add column `orderTime` bigint(20) NULL COMMENT '订购时间，当serviceType为2或3时、必填' after `expensesName`,
add column `memberRole` varchar(50) NULL COMMENT '成员角色，当serviceType为2或3时、必填' after `orderTime`,
add column `memberRoleRealNum` varchar(100) NULL COMMENT '成员真实号码，当serviceType为2或3时、必填' after `memberRole`,
add column `memberExpensesName` varchar(100) NULL COMMENT '成员侧资费名称，当serviceType为2或3时、必填' after `memberRoleRealNum`,
change `expenses` `expenses` decimal(7,2) NULL  comment '现有业务资费',
change `expirationDate` `expirationDate` bigint(20) NULL  comment '业务到期时间',
change `serviceType` `serviceType` int(2) NULL  comment '业务类型。1：专线产品，2：酒店产品，3：商务动力',
change `lineNum` `lineNum` int(5) NULL  comment '专线条数，当serviceType为1时、必填',
change `lineType` `lineType` varchar(100) character set utf8 collate utf8_general_ci NULL  comment '专线类型，当serviceType为1时、必填',
change `lineOpenDate` `lineOpenDate` bigint(20) NULL  comment '专线开户时间，当serviceType为1时、必填',
change `lineStatus` `lineStatus` varchar(100) character set utf8 collate utf8_general_ci NULL  comment '专线状态，当serviceType为1时、必填';

ALTER TABLE `tb_resource`
CHANGE COLUMN `longitude` `longitude` DOUBLE(9,6) NOT NULL DEFAULT -999.000000 COMMENT '经度。如果关联了建筑、此时必须等于建筑的经度，如果是通过地图抓取的、此时等于抓取的经度，如果是通过excel导入创建的、此时等于-999、表示没有定位' ,
CHANGE COLUMN `latitude` `latitude` DOUBLE(9,6) NOT NULL DEFAULT -999.000000 COMMENT '纬度。如果关联了建筑、此时必须等于建筑的纬度，如果是通过地图抓取的、此时等于抓取的纬度，如果是通过excel导入创建的、此时等于-999、表示没有定位' ;

ALTER TABLE `tb_consumer`
CHANGE COLUMN `longitude` `longitude` DOUBLE(9,6) NOT NULL DEFAULT -999.000000 COMMENT '经度。如果关联了建筑、此时必须等于建筑的经度，如果是通过地图抓取的、此时等于抓取的经度，如果是通过excel导入创建的、此时等于-999、表示没有定位' ,
CHANGE COLUMN `latitude` `latitude` DOUBLE(9,6) NOT NULL DEFAULT -999.000000 COMMENT '纬度。如果关联了建筑、此时必须等于建筑的纬度，如果是通过地图抓取的、此时等于抓取的纬度，如果是通过excel导入创建的、此时等于-999、表示没有定位' ;

CREATE TABLE `tb_statistic_center` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `centerId` INT(11) NOT NULL COMMENT '营销中心id',
  `notArchiveCount` INT(10) NOT NULL DEFAULT 0 COMMENT '未建档数',
  `basicArchiveCount` INT(10) NOT NULL DEFAULT 0 COMMENT '基础建档数',
  `effectiveArchiveCount` INT(10) NOT NULL DEFAULT 0 COMMENT '有效建档数/完整建档数/已建档数',
  `wholePortCount` INT(10) NOT NULL DEFAULT 0 COMMENT '总端口数',
  `usedPortCount` INT(10) NOT NULL DEFAULT 0 COMMENT '已占用端口数',
  `specialLineRatio` FLOAT(5,4) NOT NULL DEFAULT 0.0000 COMMENT '专线比率',
  `hotelRatio` FLOAT(5,4) NOT NULL DEFAULT 0.0000 COMMENT '酒店比率',
  `businessRatio` FLOAT(5,4) NOT NULL DEFAULT 0.0000 COMMENT '商务动力比率',
  `ctime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
COMMENT = '营销中心统计';

ALTER TABLE `tb_consumer`
ADD INDEX `index_expiration` (`expirationDate` ASC);

ALTER TABLE `tb_consumer`
CHANGE COLUMN `expirationDate` `expirationDate` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '业务到期时间。0表示未填写' ,
CHANGE COLUMN `lineOpenDate` `lineOpenDate` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '专线开户时间，当serviceType为1时、必填。  0表示未填写' ,
CHANGE COLUMN `orderTime` `orderTime` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '订购时间，当serviceType为2或3时、必填。  0表示未填写' ;

ALTER TABLE `tb_consumer`
ADD INDEX `index_buildingId` (`buildingId` ASC);

ALTER TABLE `tb_consumer`
ADD INDEX `index_lo_la` (`longitude` ASC, `latitude` ASC);

ALTER TABLE `tb_consumer`
CHANGE COLUMN `userId` `userId` INT(11) NOT NULL DEFAULT 0 COMMENT '绑定到的客户经理ID。0表示未绑定' ;

ALTER TABLE `tb_consumer`
ADD INDEX `index_userId` (`userId` ASC);

ALTER TABLE `tb_resource`
ADD INDEX `index_buildingId` (`buildingId` ASC),
ADD INDEX `index_lo_la` (`longitude` ASC, `latitude` ASC);

ALTER TABLE `tb_building`
DROP INDEX `index_lo&la` ,
ADD INDEX `index_lo_la` (`longitude` ASC, `latitude` ASC);

ALTER TABLE `tb_center`
ADD INDEX `index_groupId` (`groupId` ASC),
ADD INDEX `index_lo_la` (`loMax` ASC, `loMin` ASC, `laMax` ASC, `laMin` ASC);

CREATE TABLE `tb_statistic_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userId` INT(11) NOT NULL COMMENT '客户经理id',
  `basicArchiveCount` INT(10) NOT NULL DEFAULT '0' COMMENT '基础建档数',
  `effectiveArchiveCount` INT(10) NOT NULL DEFAULT '0' COMMENT '有效建档数/完整建档数/已建档数',
  `specialLineCount` INT(10) NOT NULL DEFAULT '0' COMMENT '新建专线数量',
  `ctime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='客户经理业务统计'

ALTER TABLE `tb_resource`
ADD COLUMN `cityName` VARCHAR(100) NULL COMMENT '地市名称' AFTER `district`,
ADD COLUMN `streetName` VARCHAR(100) NULL COMMENT '街道' AFTER `cityName`,
ADD COLUMN `villageName` VARCHAR(100) NULL COMMENT '乡镇' AFTER `streetName`,
ADD COLUMN `admStreetName` VARCHAR(100) NULL COMMENT '道路/行政村' AFTER `villageName`,
ADD COLUMN `zoneName` VARCHAR(100) NULL COMMENT '片区/学校' AFTER `admStreetName`;

ALTER TABLE `tb_resource`
CHANGE COLUMN `district` `district` VARCHAR(256) NOT NULL COMMENT '区县' ,
CHANGE COLUMN `cityName` `cityName` VARCHAR(100) NOT NULL COMMENT '地市名称' ,
CHANGE COLUMN `streetName` `streetName` VARCHAR(100) NOT NULL COMMENT '街道' ,
CHANGE COLUMN `villageName` `villageName` VARCHAR(100) NOT NULL COMMENT '乡镇' ,
CHANGE COLUMN `admStreetName` `admStreetName` VARCHAR(100) NOT NULL COMMENT '道路/行政村' ,
CHANGE COLUMN `zoneName` `zoneName` VARCHAR(100) NOT NULL COMMENT '片区/学校' ,
CHANGE COLUMN `floor` `floor` VARCHAR(20) NOT NULL COMMENT '楼层' ,
CHANGE COLUMN `number` `number` VARCHAR(50) NOT NULL COMMENT '户号' ,
CHANGE COLUMN `sceneA` `sceneA` VARCHAR(256) NOT NULL COMMENT '用户场景一类' ,
CHANGE COLUMN `sceneB` `sceneB` VARCHAR(256) NOT NULL COMMENT '用户场景二类' ,
CHANGE COLUMN `overlayScene` `overlayScene` VARCHAR(256) NOT NULL COMMENT '覆盖场景' ;

ALTER TABLE `tb_resource`
CHANGE COLUMN `district` `district` VARCHAR(256) NULL COMMENT '区县' ,
CHANGE COLUMN `cityName` `cityName` VARCHAR(100) NULL COMMENT '地市名称' ,
CHANGE COLUMN `streetName` `streetName` VARCHAR(100) NULL COMMENT '街道' ,
CHANGE COLUMN `villageName` `villageName` VARCHAR(100) NULL COMMENT '乡镇' ,
CHANGE COLUMN `admStreetName` `admStreetName` VARCHAR(100) NULL COMMENT '道路/行政村' ,
CHANGE COLUMN `zoneName` `zoneName` VARCHAR(100) NULL COMMENT '片区/学校' ,
CHANGE COLUMN `sceneA` `sceneA` VARCHAR(256) NULL COMMENT '用户场景一类' ,
CHANGE COLUMN `sceneB` `sceneB` VARCHAR(256) NULL COMMENT '用户场景二类' ,
CHANGE COLUMN `overlayScene` `overlayScene` VARCHAR(256) NULL COMMENT '覆盖场景' ;













