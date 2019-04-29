CREATE DATABASE demo;

USE demo;

DROP TABLE IF EXISTS `hi_sys_sale`;
CREATE TABLE `hi_sys_sale`  (
  `id_sale` char(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sd_saletp` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cd` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `na` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `des` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `py` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wb` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zj` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `instr` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fg_active` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dt_create` datetime(0) NULL DEFAULT NULL,
  `dt_modify` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id_sale`) USING BTREE,
  INDEX `Index_soft_cd`(`cd`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `hi_sys_sale_fu`;
CREATE TABLE `hi_sys_sale_fu`  (
  `id_salefu` char(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id_sale` char(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id_soft` char(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id_fu` char(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id_salefu`) USING BTREE,
  INDEX `Index_soft_cd`(`id_soft`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;