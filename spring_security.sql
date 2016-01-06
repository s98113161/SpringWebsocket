/*
Navicat MySQL Data Transfer

Source Server         : Maria
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : spring_security

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2015-10-26 18:25:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('bill', '123456', '1');
INSERT INTO `users` VALUES ('ken', '123456', '1');
INSERT INTO `users` VALUES ('naveen', 'naveen', '1');
INSERT INTO `users` VALUES ('priya', 'priya', '1');
INSERT INTO `users` VALUES ('tom', '123456', '1');

-- ----------------------------
-- Table structure for `user_roles`
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `uni_username_role` (`role`,`username`),
  KEY `fk_username_idx` (`username`),
  CONSTRAINT `fk_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
INSERT INTO `user_roles` VALUES ('4', 'bill', 'ROLE_ADMIN');
INSERT INTO `user_roles` VALUES ('5', 'ken', 'ROLE_ADMIN');
INSERT INTO `user_roles` VALUES ('3', 'naveen', 'ROLE_USER');
INSERT INTO `user_roles` VALUES ('2', 'priya', 'ROLE_USER');
INSERT INTO `user_roles` VALUES ('6', 'tom', 'ROLE_USER');
