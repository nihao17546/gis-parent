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
  `groupId` int(11) NOT NULL COMMENT '所属要客组ID',
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



