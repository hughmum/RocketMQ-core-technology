/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : localhost:3306
 Source Schema         : mu_payb

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 07/01/2023 18:33:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_platform_account
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_account`;
CREATE TABLE `t_platform_account`  (
  `account_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date_time` datetime NULL DEFAULT NULL,
  `current_balance` decimal(15, 2) NULL DEFAULT NULL,
  `version` int(10) NULL DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_platform_account
-- ----------------------------
INSERT INTO `t_platform_account` VALUES ('platform001', '11', '2023-01-07 18:24:55', 300.00, 3, '2023-01-07 14:55:10', '2023-01-07 18:24:55');

SET FOREIGN_KEY_CHECKS = 1;
