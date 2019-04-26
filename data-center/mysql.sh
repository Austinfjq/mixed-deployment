#!/bin/bash
HOSTNAME="10.10.102.25"
PORT="3306"
USERNAME="root"
PASSWORD="123456"
DBNAME="mixed_deployment"
create_service_table="SET NAMES utf8mb4;SET FOREIGN_KEY_CHECKS = 0;DROP TABLE IF EXISTS \`service\`;CREATE TABLE \`service\`  (\`service_id\` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\`namespace\` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\`service_name\` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\`cluster_ip\` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\`owner_type\` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\`owner_name\` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\`cpu_request\` smallint(10) NULL DEFAULT NULL,\`cpu_limit\` smallint(10) NULL DEFAULT NULL,\`mem_request\` smallint(10) NULL DEFAULT NULL,\`mem_limit\` smallint(10) NULL DEFAULT NULL,\`period\` int(20) NULL DEFAULT NULL,\`response_time\` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\`type\` tinyint(5) NULL DEFAULT NULL,PRIMARY KEY (\`service_id\`) USING BTREE) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;SET FOREIGN_KEY_CHECKS = 1;"
create_forecastcell_table="SET NAMES utf8mb4;SET FOREIGN_KEY_CHECKS = 0;DROP TABLE IF EXISTS \`forecastcell\`;CREATE TABLE \`forecastcell\`  (\`id\` int(10) NOT NULL AUTO_INCREMENT,\`type\` int(10) NOT NULL,\`cell_id\` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\`forecasting_index\` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\`time_interval\` int(10) NOT NULL,\`number_of_per_period\` int(10) NOT NULL,\`forecasting_model\` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\`model_params\` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\`forecasting_end_time\` datetime(0) NULL DEFAULT NULL,PRIMARY KEY (\`id\`) USING BTREE) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;SET FOREIGN_KEY_CHECKS = 1;"
create_db_sql="create database IF NOT EXISTS ${DBNAME}"
mysql -h${HOSTNAME}  -P${PORT}  -u${USERNAME} -p${PASSWORD} -e "${create_db_sql}"
mysql -h${HOSTNAME}  -P${PORT}  -u${USERNAME} -p${PASSWORD} -e "use ${DBNAME};${create_service_table};${create_forecastcell_table};"

