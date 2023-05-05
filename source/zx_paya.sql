/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : localhost:3306
 Source Schema         : mu_paya

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 07/01/2023 18:33:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for broker_message_log
-- ----------------------------
DROP TABLE IF EXISTS `broker_message_log`;
CREATE TABLE `broker_message_log`  (
  `message_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `try_count` int(5) NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `next_retry` datetime NULL DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of broker_message_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_customer_account
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_account`;
CREATE TABLE `t_customer_account`  (
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
-- Records of t_customer_account
-- ----------------------------
INSERT INTO `t_customer_account` VALUES ('1000000', NULL, '2023-01-05 22:35:55', 700.00, 3, '2023-01-05 22:35:51', '2023-01-07 00:00:00');

SET FOREIGN_KEY_CHECKS = 1;
