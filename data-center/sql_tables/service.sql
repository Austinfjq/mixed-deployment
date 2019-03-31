/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : aiops

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 26/01/2019 01:30:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service`  (
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `namespace` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `service_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cluster_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `owner_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `owner_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cpu_request` smallint(10) NULL DEFAULT NULL,
  `cpu_limit` smallint(10) NULL DEFAULT NULL,
  `mem_request` smallint(10) NULL DEFAULT NULL,
  `mem_limit` smallint(10) NULL DEFAULT NULL,
  `period` int(20) NULL DEFAULT NULL,
  `response_time` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` tinyint(5) NULL DEFAULT NULL,
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
