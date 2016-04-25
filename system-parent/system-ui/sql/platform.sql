/*
Navicat MySQL Data Transfer

Source Server         : fywx
Source Server Version : 50539
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 50539
File Encoding         : 65001

Date: 2015-11-29 12:19:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_attr`
-- ----------------------------
DROP TABLE IF EXISTS `sys_attr`;
CREATE TABLE `sys_attr` (
  `attrId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `domainId` int(11) unsigned NOT NULL COMMENT '所属实体Id',
  `name` varchar(64) NOT NULL COMMENT '字段名',
  `cnName` varchar(64) NOT NULL COMMENT '中文名',
  `sqlName` varchar(64) NOT NULL COMMENT '数据库名',
  `type` varchar(32) DEFAULT NULL COMMENT '字段类型',
  `dbType` varchar(64) DEFAULT NULL COMMENT '数据库类型',
  `isId` char(1) DEFAULT NULL COMMENT '是否ID',
  `isNull` char(1) DEFAULT NULL COMMENT '是否可空',
  `isEdit` char(1) DEFAULT NULL COMMENT '是否可更新',
  `isUnique` char(1) DEFAULT NULL COMMENT '是否唯一',
  `inList` char(1) DEFAULT NULL COMMENT '是否列表展示',
  `inForm` char(1) DEFAULT NULL COMMENT '是否表单展示',
  `inQuery` char(1) DEFAULT NULL COMMENT '是否查询条件',
  `queryType` varchar(32) DEFAULT NULL COMMENT '查询匹配方式',
  `formType` varchar(32) DEFAULT NULL COMMENT '表单类型',
  `dictName` varchar(64) DEFAULT NULL COMMENT '所属字典',
  `maxLength` int(11) DEFAULT NULL COMMENT '最大长度',
  `inOrder` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`attrId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_attr
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_auth`
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth`;
CREATE TABLE `sys_auth` (
  `authCode` varchar(64) NOT NULL,
  `menuId` int(10) unsigned DEFAULT NULL,
  `authName` varchar(16) NOT NULL,
  PRIMARY KEY (`authCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_auth
-- ----------------------------
INSERT INTO `sys_auth` VALUES ('auth:list', '7', '查询权限');
INSERT INTO `sys_auth` VALUES ('auth:update', '7', '修改权限');
INSERT INTO `sys_auth` VALUES ('dept:del', '10', '删除部门');
INSERT INTO `sys_auth` VALUES ('dept:list', '10', '查询部门');
INSERT INTO `sys_auth` VALUES ('dept:save', '10', '新增部门');
INSERT INTO `sys_auth` VALUES ('dept:update', '10', '修改部门');
INSERT INTO `sys_auth` VALUES ('dict:del', '6', '删除字典');
INSERT INTO `sys_auth` VALUES ('dict:list', '6', '查询字典');
INSERT INTO `sys_auth` VALUES ('dict:save', '6', '新增字典');
INSERT INTO `sys_auth` VALUES ('dict:update', '6', '修改字典');
INSERT INTO `sys_auth` VALUES ('dictItem:del', '6', '删除元素');
INSERT INTO `sys_auth` VALUES ('dictItem:list', '6', '查询元素');
INSERT INTO `sys_auth` VALUES ('dictItem:save', '6', '新增元素');
INSERT INTO `sys_auth` VALUES ('dictItem:status', '6', '启禁元素');
INSERT INTO `sys_auth` VALUES ('dictItem:update', '6', '修改元素');
INSERT INTO `sys_auth` VALUES ('log:list', '11', '日志查询');
INSERT INTO `sys_auth` VALUES ('menu:del', '5', '删除菜单');
INSERT INTO `sys_auth` VALUES ('menu:list', '5', '查询菜单');
INSERT INTO `sys_auth` VALUES ('menu:save', '5', '新增菜单');
INSERT INTO `sys_auth` VALUES ('menu:update', '5', '修改菜单');
INSERT INTO `sys_auth` VALUES ('role:auth', '4', '分配权限');
INSERT INTO `sys_auth` VALUES ('role:del', '4', '删除角色');
INSERT INTO `sys_auth` VALUES ('role:list', '4', '查询角色');
INSERT INTO `sys_auth` VALUES ('role:save', '4', '新增角色');
INSERT INTO `sys_auth` VALUES ('role:status', '4', '启禁角色');
INSERT INTO `sys_auth` VALUES ('role:update', '4', '修改角色');
INSERT INTO `sys_auth` VALUES ('user:del', '3', '删除用户');
INSERT INTO `sys_auth` VALUES ('user:list', '3', '查询用户');
INSERT INTO `sys_auth` VALUES ('user:restPassword', '3', '重置密码');
INSERT INTO `sys_auth` VALUES ('user:role', '3', '分配角色');
INSERT INTO `sys_auth` VALUES ('user:save', '3', '新增用户');
INSERT INTO `sys_auth` VALUES ('user:status', '3', '启禁用户');
INSERT INTO `sys_auth` VALUES ('user:update', '3', '修改用户');

-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `deptName` varchar(64) NOT NULL COMMENT '部门名称',
  `deptCode` varchar(32) NOT NULL COMMENT '部门编码',
  `orderNo` tinyint(4) DEFAULT NULL COMMENT '排序',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `pid` int(11) NOT NULL COMMENT '父id',
  `deptTel` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `deptAddr` varchar(128) DEFAULT NULL COMMENT '联系地址',
  `deptType` varchar(32) DEFAULT NULL COMMENT '部门类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '公司', 'ROOT', '1', '', '0', '155555555', '', '');

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `dictId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dictName` varchar(64) NOT NULL,
  `dictCode` varchar(32) NOT NULL,
  `dictType` tinyint(2) NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`dictId`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '系统配置', 'DICT_SYS_CONF', '0', '系统公共配置', '1');
INSERT INTO `sys_dict` VALUES ('4', '文件上传', 'DICT_FILE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('5', '短信', 'SMS', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('6', '短信返回状态', 'SMS_STATUS_CODE', '0', '', '1');

-- ----------------------------
-- Table structure for `sys_dict_item`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `dictItemId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dictId` int(10) unsigned NOT NULL,
  `itemName` varchar(64) NOT NULL,
  `itemCode` varchar(32) NOT NULL,
  `itemVal` varchar(1024) DEFAULT NULL,
  `itemValExt` varchar(1024) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `orderNo` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`dictItemId`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('1', '1', '开启登录验证', 'VERITY_CODE_FLAG', 'false', '', '1', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('2', '1', '前台照片目录', 'FRONT_PIC_URL', 'false', '1212', '1', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('3', '1', '单用户登录', 'ONLY_SINGLE_LOGIN', 'false', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('11', '1', '刷新权限', 'ITEM_FLUSH_AUTH', 'auto', null, '自动刷新 auto  手动刷新  hand', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('12', '1', '部门根节点名称', 'ITEM_DEPT_ROOT', '创智财富', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('13', '4', '默认文件夹', 'ITEM_FILE_DIR', 'upload', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('14', '4', '文件公共路径', 'ITEM_FILE_PUB_DIRS', 'file/', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('15', '1', '开启自动登录', 'AUTO_LOGIN_FLAG', 'true', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('83', '4', '默认文件夹', 'ITEM_FILE_DIR', 'upload', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('84', '4', '文件公共路径', 'ITEM_FILE_PUB_DIRS', 'file/', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('85', '1', '开启自动登录', 'AUTO_LOGIN_FLAG', 'true', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('86', '5', '短信发送地址', 'SMS_SEND_URL', 'http://116.213.72.20/SMSHttpService/send.aspx', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('87', '5', '短信接收回复地址', 'SMS_RECV_URL', 'http://116.213.72.20/SMSHttpService/getmsg.aspx', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('88', '5', '短信余额查询地址', 'SMS_MONEY_URL', 'http://116.213.72.20/SMSHttpService/Balance.aspx', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('89', '5', '用户名', 'SMS_UID', 'byjr3', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('90', '5', '用户密码', 'SMS_PWD', 'byjr3', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('91', '5', '短信线程一次发送记录数', 'SMS_SCAN_LIMIT', '10', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('92', '5', '短信线程扫描间隔', 'SMS_SCAN_INTERVAL', '60000', null, '毫秒', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('93', '6', '成送成功', '0', '', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('94', '6', '当前账号余额不足2', '1002', '', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('95', '6', '当前用户密码错误', '1001', '', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('97', '6', '手机号码格式不对', '-2', '', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('99', '6', '其他错误', '1004', '', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('100', '6', '预约时间格式不正确', '1005', '', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('178', '5', '短信签名', 'SMS_SIGN', '【伯庸金融】', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('182', '1', '代码模板路径', 'FTL_PATH', 'E:\\github\\platform2\\base\\whale\\system-ext\\ext-code\\src\\main\\resources\\templates\\educate', '', '', '1', '7');

-- ----------------------------
-- Table structure for `sys_domian`
-- ----------------------------
DROP TABLE IF EXISTS `sys_domian`;
CREATE TABLE `sys_domian` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domainName` varchar(64) NOT NULL COMMENT '实体名',
  `domainCnName` varchar(64) NOT NULL COMMENT '中文名',
  `domainSqlName` varchar(64) NOT NULL COMMENT '数据库',
  `pkgName` varchar(256) DEFAULT NULL COMMENT '包名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_domian
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_file_info`
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fileName` varchar(1024) NOT NULL COMMENT '文件名',
  `fileType` tinyint(2) NOT NULL COMMENT '文件类型',
  `realFileName` varchar(64) NOT NULL COMMENT '原文件名',
  `fileSuffix` varchar(16) NOT NULL COMMENT '文件后缀名',
  `filePath` varchar(256) NOT NULL COMMENT '文件路径',
  `absolutePath` varchar(256) DEFAULT NULL COMMENT '绝对路径',
  `urlPath` varchar(256) NOT NULL COMMENT 'url访问地址',
  `saveWay` tinyint(2) NOT NULL COMMENT '保存方式',
  `width` int(6) DEFAULT NULL COMMENT '图片宽度',
  `height` int(6) DEFAULT NULL COMMENT '图片高度',
  `fileSize` int(10) NOT NULL COMMENT '文件大小',
  `originalImgId` int(11) DEFAULT NULL COMMENT '原始图Id',
  `extInfo` varchar(512) DEFAULT NULL COMMENT '扩展信息',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `createTime` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_log_info`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_info`;
CREATE TABLE `sys_log_info` (
`id`  int(11) NOT NULL ,
`clazz`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用类' ,
`method`  varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用方法' ,
`module`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文模块名' ,
`opt`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作名' ,
`info`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志描述' ,
`params`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '方法参数' ,
`ip`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`userName`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前用户' ,
`costTime`  int(11) NOT NULL COMMENT '调用耗时' ,
`createTime`  bigint(14) NOT NULL COMMENT '创建时间' ,
`rs`  tinyint(4) NULL DEFAULT NULL COMMENT '结果类型 0：成功' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=COMPACT
;



-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menuId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parentId` int(10) unsigned DEFAULT NULL,
  `menuName` varchar(64) DEFAULT NULL,
  `menuType` tinyint(2) DEFAULT NULL,
  `menuUrl` varchar(512) DEFAULT NULL,
  `inco` varchar(64) DEFAULT NULL,
  `openType` tinyint(2) DEFAULT NULL,
  `orderNo` tinyint(4) DEFAULT NULL,
  `openState` tinyint(2) DEFAULT NULL,
  `publicFlag` bit(1) DEFAULT NULL,
  PRIMARY KEY (`menuId`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('2', '0', '系统管理', '1', '', '', '1', '10', '1', '');
INSERT INTO `sys_menu` VALUES ('3', '2', '用户管理', '3', '/user/goTree', '', '1', '2', '1', '');
INSERT INTO `sys_menu` VALUES ('4', '2', '角色管理', '3', '/role/goList', '', '1', '3', '1', '');
INSERT INTO `sys_menu` VALUES ('5', '2', '菜单管理', '3', '/menu/goList', '', '1', '4', '1', '');
INSERT INTO `sys_menu` VALUES ('6', '2', '字典管理', '3', '/dict/goTree', '', '1', '7', '1', '');
INSERT INTO `sys_menu` VALUES ('7', '2', '权限管理', '3', '/auth/goTree', '', '1', '5', '1', '');
INSERT INTO `sys_menu` VALUES ('10', '2', '组织管理', '3', '/dept/goList', '', '1', '1', '1', '');
INSERT INTO `sys_menu` VALUES ('11', '2', '日志查看', '3', '/log/goList', '', '1', '8', '1', '');
INSERT INTO `sys_menu` VALUES ('14', '2', '缓存管理', '3', '/goAdmin', '', '1', '11', '1', '');
INSERT INTO `sys_menu` VALUES ('22', '2', '短信查看', '3', '/sms/goList', '', '1', '15', '1', '');
INSERT INTO `sys_menu` VALUES ('23', '2', '文件上传', '3', '/fileInfo/goFileUpload', '', '1', '16', '1', '');
INSERT INTO `sys_menu` VALUES ('24', '2', '图片上传', '3', '/fileInfo/goImgUpload', '', '1', '17', '1', '');
INSERT INTO `sys_menu` VALUES ('27', '2', '在线用户', '3', '/goWhoAreOnline', '', '1', '12', '1', '');
INSERT INTO `sys_menu` VALUES ('29', '2', '代码生产', '3', '/code/goList', '', '1', '12', '1', '');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `roleId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roleName` varchar(16) DEFAULT NULL,
  `roleCode` varchar(64) DEFAULT NULL COMMENT '角色编码',
  `status` tinyint(2) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `canDelFlag` bit(1) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `sys_role_auth`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_auth`;
CREATE TABLE `sys_role_auth` (
  `roleAuthId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roleId` int(10) unsigned DEFAULT NULL,
  `authCode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`roleAuthId`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_auth
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_sms`
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms`;
CREATE TABLE `sys_sms` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(1024) NOT NULL COMMENT '短信内容',
  `smsType` int(11) NOT NULL COMMENT '短信类型',
  `sendTime` datetime DEFAULT NULL,
  `toPhones` varchar(1024) NOT NULL COMMENT '接收号码',
  `encode` varchar(1024) DEFAULT NULL COMMENT '自定义扩展号',
  `resStatus` varchar(1024) DEFAULT NULL COMMENT '返回状态',
  `resMsg` varchar(1024) DEFAULT NULL COMMENT '返回信息',
  `sid` varchar(1024) DEFAULT NULL COMMENT 'sid',
  `overLengthIgnore` char(1) DEFAULT NULL COMMENT '内容超70忽略',
  `sendRealTime` char(1) DEFAULT NULL COMMENT '是否实时发送',
  `retryTime` int(11) DEFAULT NULL COMMENT '重发次数',
  `curRetryTime` int(11) DEFAULT NULL COMMENT '当前发送次数',
  `recTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '消息状态 1.待发送， 2. 发送成功， 3. 发送失败 ',
  `bisExpInfo` varchar(1024) DEFAULT NULL COMMENT '异常信息',
  `originalId` int(10) unsigned DEFAULT NULL COMMENT '原始id，适应与短信内容超长分条发送',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_sms
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `userId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(16) NOT NULL COMMENT '用户名',
  `password` char(56) NOT NULL COMMENT '密码',
  `realName` varchar(32) DEFAULT NULL,
  `deptId` int(10) NOT NULL,
  `email` varchar(32) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `createUserId` int(10) unsigned DEFAULT NULL,
  `lastLoginTime` bigint(13) DEFAULT NULL,
  `loginIp` varchar(64) DEFAULT NULL,
  `addOn` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `userType` tinyint(4) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `adminflag` bit(1) DEFAULT NULL,
  `createTime` bigint(13) DEFAULT NULL,
  `loginNum` int(11) DEFAULT NULL COMMENT '登录次数',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '8C24E4A28487E42FA6EE9E63ABC886A7A27432823050A5C377895734', '超级管理员', '0', '', '15059490712', '0', '1448768807910', '127.0.0.1', '1', '1', '1', '1', '', null, '8');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `userRoleId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(10) unsigned DEFAULT NULL,
  `roleId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`userRoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
