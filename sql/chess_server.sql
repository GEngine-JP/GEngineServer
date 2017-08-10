/*
Navicat MySQL Data Transfer

Source Server         : xiaomo2017(106.15.188.160)
Source Server Version : 50717
Source Host           : 106.15.188.160:3306
Source Database       : chess_server

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-08-10 17:35:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for p_user
-- ----------------------------
DROP TABLE IF EXISTS `p_user`;
CREATE TABLE `p_user` (
  `id` bigint(20) NOT NULL,
  `login_name` varchar(64) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `platform_id` tinyint(4) DEFAULT NULL,
  `gm_level` int(4) DEFAULT NULL,
  `id_number` varchar(18) DEFAULT NULL,
  `register_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_user
-- ----------------------------
INSERT INTO `p_user` VALUES ('379841971683329', 'xiaomo', '1', '1', '1', null, '1500961227');
INSERT INTO `p_user` VALUES ('379876258545665', '2', '1', '1', '1', null, '1501484403');
INSERT INTO `p_user` VALUES ('379876272046083', '123', '1', '1', '1', null, '1501484609');
INSERT INTO `p_user` VALUES ('379876574298113', '234', '1', '1', '1', null, '1501489221');
