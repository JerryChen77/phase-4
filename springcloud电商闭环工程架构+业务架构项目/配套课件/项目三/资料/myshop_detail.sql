/*
SQLyog Ultimate v12.5.0 (64 bit)
MySQL - 5.7.25-log : Database - my-shop-v3
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`my-shop-v3` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `my-shop-v3`;

/*Table structure for table `t_product` */

DROP TABLE IF EXISTS `t_product`;

CREATE TABLE `t_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pname` varchar(1024) DEFAULT NULL,
  `price` bigint(20) DEFAULT NULL,
  `pimage` varchar(1024) DEFAULT NULL,
  `cid` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `flag` tinyint(4) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `created_user` bigint(20) DEFAULT NULL,
  `updated_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Data for the table `t_product` */

insert  into `t_product`(`id`,`pname`,`price`,`pimage`,`cid`,`status`,`flag`,`created_time`,`updated_time`,`created_user`,`updated_user`) values 
(3,'华为NOVA7',2699,'',1,NULL,NULL,NULL,NULL,NULL,NULL),
(4,'华为NOVA8',2599,'',1,NULL,NULL,NULL,NULL,NULL,NULL),
(12,'华为NOVA12',22222,'http://192.168.2.177:8888/group1/M00/00/00/wKgCsV87SoeAdV5OAACwZ__3Qcs784.jpg',1,1,NULL,NULL,NULL,NULL,NULL),
(14,'华为NOVA15',1555,'http://192.168.2.177:8888/group1/M00/00/00/wKgCsV87eoqAF-BNAACcBopizlw985.jpg',1,1,NULL,NULL,NULL,NULL,NULL),
(15,'华为NOVA33',333,'',1,1,NULL,NULL,NULL,NULL,NULL),
(16,'华为NOVA33',333,'',1,1,NULL,NULL,NULL,NULL,NULL),
(17,'华为NOVA33',333,'',1,1,NULL,NULL,NULL,NULL,NULL),
(18,'华为NOVA33',333,'',1,1,NULL,NULL,NULL,NULL,NULL),
(19,'华为NOVA28',123123,'',1,1,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `t_product_desc` */

DROP TABLE IF EXISTS `t_product_desc`;

CREATE TABLE `t_product_desc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pdesc` text,
  `flag` tinyint(4) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `created_user` bigint(20) DEFAULT NULL,
  `updated_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `t_product_desc` */

insert  into `t_product_desc`(`id`,`pdesc`,`flag`,`created_time`,`updated_time`,`created_user`,`updated_user`) values 
(9,'henzan@!!!!',NULL,NULL,NULL,NULL,NULL),
(10,'gui!!!!',NULL,NULL,NULL,NULL,NULL),
(11,'好看！！！',NULL,NULL,NULL,NULL,NULL),
(12,'henmen!!!!',NULL,NULL,NULL,NULL,NULL),
(13,'',NULL,NULL,NULL,NULL,NULL),
(14,'<p><img src=\"http://192.168.2.177:8888/group1/M00/00/00/wKgCsV87epKADkcQAACwZ__3Qcs884.jpg\" style=\"max-width:100%;\"><br></p>',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `t_product_type` */

DROP TABLE IF EXISTS `t_product_type`;

CREATE TABLE `t_product_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) DEFAULT NULL,
  `cname` varchar(1024) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `created_user` bigint(20) DEFAULT NULL,
  `updated_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Data for the table `t_product_type` */

insert  into `t_product_type`(`id`,`pid`,`cname`,`flag`,`created_time`,`updated_time`,`created_user`,`updated_user`) values 
(1,0,'手机',NULL,NULL,NULL,NULL,NULL),
(2,0,'家用电器',NULL,NULL,NULL,NULL,NULL),
(3,1,'华为',NULL,NULL,NULL,NULL,NULL),
(4,1,'小米',NULL,NULL,NULL,NULL,NULL),
(5,2,'电视',NULL,NULL,NULL,NULL,NULL),
(6,2,'冰箱',NULL,NULL,NULL,NULL,NULL),
(7,3,'P系列',NULL,NULL,NULL,NULL,NULL),
(8,3,'NOVA系列',NULL,NULL,NULL,NULL,NULL),
(9,3,'荣耀系列',NULL,NULL,NULL,NULL,NULL),
(10,4,'红米系列',NULL,NULL,NULL,NULL,NULL),
(11,4,'NOTE系列',NULL,NULL,NULL,NULL,NULL),
(12,5,'TCL',NULL,NULL,NULL,NULL,NULL),
(13,5,'三星',NULL,NULL,NULL,NULL,NULL),
(14,6,'海尔',NULL,NULL,NULL,NULL,NULL),
(15,6,'西门子',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `uname` varchar(24) DEFAULT NULL,
  `password` char(60) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `phone` varchar(18) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `flag1` tinyint(4) DEFAULT NULL,
  `level` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `t_user` */

insert  into `t_user`(`uid`,`uname`,`password`,`email`,`phone`,`create_time`,`update_time`,`status`,`flag`,`flag1`,`level`) values 
(3,NULL,'$2a$10$yQWPgQbcW0yVYh1tmCjCUe.pk5X1.U6sXZSVdOSaBMyXsNX7809Bi',NULL,'18667135181',NULL,NULL,NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
