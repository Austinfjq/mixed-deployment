SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for serviceload_mapping_podinstances
-- ----------------------------
DROP TABLE IF EXISTS `serviceload_mapping_podinstances`;
CREATE TABLE `serviceload_mapping_podinstances`  (
  `mapping_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `service_load` int(20) NOT NULL,
  `pod_instances` int(20) NOT NULL,
  PRIMARY KEY (`mapping_id`) USING BTREE,
  INDEX `service_id_foreign_key`(`service_id`) USING BTREE,
  CONSTRAINT `service_id_foreign_key` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
