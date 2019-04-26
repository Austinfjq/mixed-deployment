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

 Date: 26/01/2019 01:31:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for forecastcell
-- ----------------------------
DROP TABLE IF EXISTS `forecastcell`;
CREATE TABLE `forecastcell`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` int(10) NOT NULL,
  `cell_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `forecasting_index` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time_interval` int(10) NOT NULL,
  `number_of_per_period` int(10) NOT NULL,
  `forecasting_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `model_params` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `forecasting_end_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
