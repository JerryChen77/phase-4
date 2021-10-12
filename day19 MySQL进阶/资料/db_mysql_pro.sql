/*
 Navicat MySQL Data Transfer

 Source Server         : localserver
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : db_mysql_pro

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 02/09/2021 16:40:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(24) NOT NULL DEFAULT '' COMMENT '姓名',
  `age` int NOT NULL DEFAULT '0' COMMENT '年龄',
  `position` varchar(20) NOT NULL DEFAULT '' COMMENT '职位',
  `hire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',
  PRIMARY KEY (`id`),
  KEY `idx_name_age_position` (`name`,`age`,`position`) USING BTREE,
  KEY `idx_time` (`hire_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5941602 DEFAULT CHARSET=utf8 COMMENT='员工记录表';

-- ----------------------------
-- Table structure for t1
-- ----------------------------
DROP TABLE IF EXISTS `t1`;
CREATE TABLE `t1` (
  `id` int NOT NULL AUTO_INCREMENT,
  `a` int DEFAULT NULL,
  `b` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_a` (`a`)
) ENGINE=InnoDB AUTO_INCREMENT=20001 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t2
-- ----------------------------
DROP TABLE IF EXISTS `t2`;
CREATE TABLE `t2` (
  `id` int NOT NULL AUTO_INCREMENT,
  `a` int DEFAULT NULL,
  `b` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_a` (`a`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_account
-- ----------------------------
DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account` (
  `id` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `money` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tb_author
-- ----------------------------
DROP TABLE IF EXISTS `tb_author`;
CREATE TABLE `tb_author` (
  `id` int NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_book
-- ----------------------------
DROP TABLE IF EXISTS `tb_book`;
CREATE TABLE `tb_book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_book_author
-- ----------------------------
DROP TABLE IF EXISTS `tb_book_author`;
CREATE TABLE `tb_book_author` (
  `id` int NOT NULL,
  `book_id` int NOT NULL,
  `author_id` int NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_book_id_author_id` (`book_id`,`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_innodb_test
-- ----------------------------
DROP TABLE IF EXISTS `tb_innodb_test`;
CREATE TABLE `tb_innodb_test` (
  `id` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tb_innodb_test1
-- ----------------------------
DROP TABLE IF EXISTS `tb_innodb_test1`;
CREATE TABLE `tb_innodb_test1` (
  `id` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for tb_myisam_demo
-- ----------------------------
DROP TABLE IF EXISTS `tb_myisam_demo`;
CREATE TABLE `tb_myisam_demo` (
  `id` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Procedure structure for insert_emp
-- ----------------------------
DROP PROCEDURE IF EXISTS `insert_emp`;
delimiter ;;
CREATE PROCEDURE `db_mysql_pro`.`insert_emp`()
begin 
	declare i int; 
	set i=5941592; 
	while(i<=5941600)do 
        insert into employees(name,age,position) values(CONCAT('z'),i,'admin'); 
        set i=i+1; 
	end while; 
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insert_t1
-- ----------------------------
DROP PROCEDURE IF EXISTS `insert_t1`;
delimiter ;;
CREATE PROCEDURE `db_mysql_pro`.`insert_t1`()
begin 
	declare i int; 
	set i=1; 
	while(i<=10000)do 
        insert into t1(a,b) values(i,i); 
        set i=i+1; 
	end while; 
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for insert_t2
-- ----------------------------
DROP PROCEDURE IF EXISTS `insert_t2`;
delimiter ;;
CREATE PROCEDURE `db_mysql_pro`.`insert_t2`()
begin 
	declare i int; 
	set i=1; 
	while(i<=100)do 
        insert into t2(a,b) values(i,i); 
        set i=i+1; 
	end while; 
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
