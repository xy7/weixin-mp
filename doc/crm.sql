/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : crm

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-09-17 17:55:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for card_class
-- ----------------------------
DROP TABLE IF EXISTS `card_class`;
CREATE TABLE `card_class` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `card_id` int(10) NOT NULL,
  `class_id` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for card_info
-- ----------------------------
DROP TABLE IF EXISTS `card_info`;
CREATE TABLE `card_info` (
  `card_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `company` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL COMMENT '职位',
  `tel` varchar(64) DEFAULT NULL COMMENT '电话',
  `tel2` varchar(255) DEFAULT NULL,
  `weixin` varchar(255) DEFAULT NULL,
  `qq` varchar(64) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `origin` varchar(64) DEFAULT NULL COMMENT '籍贯',
  `business_main` varchar(255) DEFAULT NULL COMMENT '主业',
  `business_secondary` varchar(255) DEFAULT NULL COMMENT '副业',
  `position_social` varchar(255) DEFAULT NULL COMMENT '社会职务',
  `resources_available` varchar(255) DEFAULT NULL COMMENT '现有资源',
  `resources_need` varchar(255) DEFAULT NULL COMMENT '需求资源',
  `platform` varchar(255) DEFAULT NULL COMMENT '平台',
  `company_address` varchar(255) DEFAULT NULL,
  `vip` int(10) NOT NULL DEFAULT '0' COMMENT 'vip等级',
  `openid` varchar(255) DEFAULT NULL COMMENT '微信用户订阅后对应的openid',
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for class_info
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(64) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for openid_info
-- ----------------------------
DROP TABLE IF EXISTS `openid_info`;
CREATE TABLE `openid_info` (
  `openid` varchar(255) NOT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `groupid` int(10) DEFAULT NULL,
  `headimgurl` varchar(255) DEFAULT NULL,
  `language` varchar(50) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sex` int(10) DEFAULT NULL,
  `subscribe` int(10) DEFAULT NULL,
  `subscribe_time` int(10) DEFAULT NULL,
  `tagid_list` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
