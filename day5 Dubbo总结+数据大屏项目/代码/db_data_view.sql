/*
 Navicat MySQL Data Transfer

 Source Server         : localserver
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : db_data_view

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 13/08/2021 17:20:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_worker
-- ----------------------------
DROP TABLE IF EXISTS `tb_worker`;
CREATE TABLE `tb_worker` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `age` int DEFAULT NULL COMMENT '年龄',
  `work_type` tinyint(1) DEFAULT NULL COMMENT '工种',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `flag` tinyint(1) DEFAULT NULL COMMENT '标签',
  `gmt_created` datetime DEFAULT NULL COMMENT '创建记录的时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新记录的时间',
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '1:删除；0:正常',
  `dep_id` bigint DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
