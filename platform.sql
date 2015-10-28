/*
Navicat MySQL Data Transfer

Source Server         : fywx
Source Server Version : 50539
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 50539
File Encoding         : 65001

Date: 2015-10-28 18:59:35
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
INSERT INTO `sys_auth` VALUES ('AUTH_DEL', '7', '删除权限');
INSERT INTO `sys_auth` VALUES ('AUTH_LIST', '7', '查询权限');
INSERT INTO `sys_auth` VALUES ('AUTH_SAVE', '7', '新增权限');
INSERT INTO `sys_auth` VALUES ('AUTH_UPDATE', '7', '修改权限');
INSERT INTO `sys_auth` VALUES ('DEPT_DEL', '10', '删除部门');
INSERT INTO `sys_auth` VALUES ('DEPT_LIST', '10', '查询部门');
INSERT INTO `sys_auth` VALUES ('DEPT_SAVE', '10', '新增部门');
INSERT INTO `sys_auth` VALUES ('DEPT_UPDATE', '10', '修改部门');
INSERT INTO `sys_auth` VALUES ('DICT_DEL', '6', '删除字典');
INSERT INTO `sys_auth` VALUES ('DICT_LIST', '6', '查询字典');
INSERT INTO `sys_auth` VALUES ('DICT_SAVE', '6', '新增字典');
INSERT INTO `sys_auth` VALUES ('DICT_UPDATE', '6', '修改字典');
INSERT INTO `sys_auth` VALUES ('ITEM_DEL', null, '删除元素');
INSERT INTO `sys_auth` VALUES ('ITEM_LIST', null, '查询元素');
INSERT INTO `sys_auth` VALUES ('ITEM_SAVE', null, '新增元素');
INSERT INTO `sys_auth` VALUES ('ITEM_STATUS', null, '启禁元素');
INSERT INTO `sys_auth` VALUES ('ITEM_UPDATE', null, '修改元素');
INSERT INTO `sys_auth` VALUES ('ITEM_VIEW', null, '查看元素');
INSERT INTO `sys_auth` VALUES ('LOG_LIST', '11', '日志查询');
INSERT INTO `sys_auth` VALUES ('MENU_DEL', '5', '删除菜单');
INSERT INTO `sys_auth` VALUES ('MENU_LIST', '5', '查询菜单');
INSERT INTO `sys_auth` VALUES ('MENU_SAVE', '5', '新增菜单');
INSERT INTO `sys_auth` VALUES ('MENU_UPDATE', '5', '修改菜单');
INSERT INTO `sys_auth` VALUES ('ROLE_AUTH', '4', '分配权限');
INSERT INTO `sys_auth` VALUES ('ROLE_DEL', '4', '删除角色');
INSERT INTO `sys_auth` VALUES ('ROLE_LIST', '4', '查询角色');
INSERT INTO `sys_auth` VALUES ('ROLE_SAVE', '4', '新增角色');
INSERT INTO `sys_auth` VALUES ('ROLE_STATUS', '4', '启禁角色');
INSERT INTO `sys_auth` VALUES ('ROLE_UPDATE', '4', '修改角色');
INSERT INTO `sys_auth` VALUES ('USER_DEL', '3', '删除用户');
INSERT INTO `sys_auth` VALUES ('USER_LIST', '3', '查询用户');
INSERT INTO `sys_auth` VALUES ('USER_PASS_REST', '3', '重置密码');
INSERT INTO `sys_auth` VALUES ('USER_ROLE', '3', '分配角色');
INSERT INTO `sys_auth` VALUES ('USER_SAVE', '3', '新增用户');
INSERT INTO `sys_auth` VALUES ('USER_STATUS', '3', '启禁用户');
INSERT INTO `sys_auth` VALUES ('USER_UPDATE', '3', '修改用户');
INSERT INTO `sys_auth` VALUES ('USER_VIEW', '3', '查看用户');

-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `deptName` varchar(64) NOT NULL COMMENT '部门名称',
  `deptCode` varchar(32) NOT NULL COMMENT '部门编码',
  `orderNo` int(4) DEFAULT NULL COMMENT '排序',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `pid` bigint(10) NOT NULL COMMENT '父id',
  `deptTel` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `deptAddr` varchar(128) DEFAULT NULL COMMENT '联系地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '公司', 'ROOT', '1', null, '0', '155555555', null);

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `dictId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dictName` varchar(64) NOT NULL,
  `dictCode` varchar(32) NOT NULL,
  `dictType` tinyint(4) NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`dictId`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '系统配置', 'DICT_SYS_CONF', '0', '系统公共配置', '1');
INSERT INTO `sys_dict` VALUES ('2', '豁免地址', 'DICT_FREE_URL', '0', '不会被权限系统拦截验证', '1');
INSERT INTO `sys_dict` VALUES ('4', '文件上传', 'DICT_FILE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('5', '短信', 'SMS', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('6', '短信返回状态', 'SMS_STATUS_CODE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('9', '公告展示位置', 'DICT_NOTICE_PLACE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('10', '操盘类型', 'DICT_MASTER_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('11', '在线留言类型', 'DICT_ONLINE_MSG', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('12', '配资展示位置', 'DICT_FINANCING_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('13', '婚姻状况', 'DICT_MARITAL_STATUS', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('14', '最高学历', 'DICT_EDUCATION', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('15', '所属行业', 'DICT_INDUSTRY', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('16', '职位', 'DICT_POSITION', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('17', '月收入', 'DICT_INCOME', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('18', '新闻类型', 'DICT_NEWS_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('19', '公告类型', 'DICT_NOTICE_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('20', '资金种类', 'DICT_AMOUNT_KIND', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('21', '计算费用的方式DICT', 'DICT_CHARGING_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('22', '结算费用的周期', 'DICT_CHARGING_CYCLE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('24', '规则类型', 'DICT_RULES_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('25', '应收应付款状态', 'DICT_RECE_PAY_STATUS', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('26', '收付款方式', 'DICT_EXCHANGE_MODE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('27', '资金类型', 'DICT_AMOUNT_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('28', '业务类型', 'DICT_BUSINESS_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('29', '风险等级', 'DICT_RISK_LEVEL', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('30', '业务介绍人提成方式', 'DICT_EXTRACT_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('31', '客户类型', 'DICT_CUSTOMER_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('32', '账户分类', 'DICT_ACCOUNT_CATEGORY', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('33', '证券公司', 'DICT_SECURITIES_COMPANY', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('34', '账号类型', 'DICT_ACCOUNT_TYPE', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('35', '银行名称', 'DICT_BANK_ID', '0', '', '1');
INSERT INTO `sys_dict` VALUES ('36', '配置单来源', 'DICT_FINANCING_SOURCE', '0', '', '1');

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
  `status` tinyint(4) DEFAULT NULL,
  `orderNo` smallint(6) DEFAULT NULL,
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
INSERT INTO `sys_dict_item` VALUES ('26', '9', '首行展示', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('27', '9', '首页首行', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('28', '9', '轮播', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('29', '10', '天天发', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('30', '10', '股票配资', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('31', '11', '意见反馈', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('32', '11', '天天发问题', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('33', '11', '股票配资问题', '3', '3', null, '', '2', '3');
INSERT INTO `sys_dict_item` VALUES ('34', '11', '功能使用问题', '4', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('35', '11', '其他问题', '5', '5', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('36', '12', '首页配资', '1', '123', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('37', '12', '天天发', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('38', '12', '股票配资', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('40', '13', '未婚', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('41', '13', '已婚', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('42', '13', '离异', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('44', '14', '硕士', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('45', '14', '大学', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('46', '14', '高中', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('47', '14', '初中', '4', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('48', '14', '小学', '5', '5', null, '', '2', '5');
INSERT INTO `sys_dict_item` VALUES ('50', '15', '金融业', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('51', '15', '服务业', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('52', '15', '信息产业', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('53', '15', '制造业', '4', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('54', '15', '传播业', '5', '5', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('55', '15', '教育', '6', '6', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('56', '15', '政府机构', '7', '7', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('57', '15', '医疗保健', '8', '8', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('58', '15', '房地产', '9', '9', null, '', '1', '9');
INSERT INTO `sys_dict_item` VALUES ('59', '15', '其他', '10', '10', null, '', '1', '10');
INSERT INTO `sys_dict_item` VALUES ('60', '16', '学生', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('61', '16', '职员22', '2', '2', null, '', '1', '22');
INSERT INTO `sys_dict_item` VALUES ('62', '16', '经理', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('63', '16', '专业人士222', '4', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('64', '16', '公务员', '5', '5', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('65', '16', '私营主', '6', '6', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('66', '16', '待业', '7', '7', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('67', '16', '退休', '8', '8', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('68', '16', '其他', '9', '9', null, '', '1', '9');
INSERT INTO `sys_dict_item` VALUES ('69', '17', '6000以上', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('70', '17', '4001-6000', '2', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('71', '17', '2001-4000', '3', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('72', '17', '1001-2000', '4', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('73', '17', '501-1000', '5', '5', null, '', '1', '5');
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
INSERT INTO `sys_dict_item` VALUES ('105', '18', '今日头条', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('106', '19', '文字链接', '1', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('109', '20', '现金', 'AMOUNT_KIND_CASH', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('110', '20', '包户', 'AMOUNT_KIND_WRAP_ACCOUNT', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('111', '20', '公司资金', 'AMOUNT_KIND_COMPANY', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('113', '21', '月头先付', 'CHARGING_TYPE_MONTH_BEFORE', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('114', '21', '月末后付', 'CHARGING_TYPE_MONTH_AFTER', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('115', '22', '按日结', 'CHARGING_CYCLE_DAY', '1', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('116', '22', '按月结', 'CHARGING_CYCLE_MONTH', '2', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('117', '22', '按季度结', 'CHARGING_CYCLE_QUARTER', '3', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('118', '22', '按半年结', 'CHARGING_CYCLE_HALF_YEAR', '4', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('119', '22', '按年结', 'CHARGING_CYCLE_YEAR', '5', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('122', '24', '普通费用', 'RULES_TYPE_NORMAL', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('123', '24', '管理费', 'RULES_TYPE_MANAGEMENT', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('124', '25', '待合并', 'RECE_PAY_STATUS_TO_MERGED', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('125', '25', '未处理1222', 'RECE_PAY_STATUS_UNPROCESSED', '212f', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('126', '25', '部分转账', 'RECE_PAY_STATUS_HALF_PROCESSED', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('127', '25', '全部转账', 'RECE_PAY_STATUS_PROCESSED', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('128', '26', '现金', 'EXCHANGE_MODE_CASH', '1', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('129', '26', '银行转账', 'EXCHANGE_MODE_BANK_TRANSFER', '2', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('130', '26', '邮寄汇款', 'EXCHANGE_MODE_POST', '3', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('131', '26', '财务调账', 'EXCHANGE_MODE_FINANCE', '4', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('132', '26', '交易账户划拨', 'EXCHANGE_MODE_DEAL_ACCOUNT', '5', null, '', '1', '9');
INSERT INTO `sys_dict_item` VALUES ('133', '27', '存融资款', 'AMOUNT_TYPE_DEPOSIT', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('134', '27', '退融资款', 'AMOUNT_TYPE_REFUND', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('135', '27', '资方收息', 'AMOUNT_TYPE_INVESTOR_ACCRUAL', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('136', '27', '初始保证金', 'AMOUNT_TYPE_INIT_CASH_DEPOSIT', '21', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('137', '27', '补保证金', 'AMOUNT_TYPE_ADD_CASH_DEPOSIT', '22', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('138', '27', '退保证金', 'AMOUNT_TYPE_RETURN_CASH_DEPOSIT', '23', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('139', '27', '配资利息', 'AMOUNT_TYPE_BORROWER_ACCRUAL', '24', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('140', '27', '盈利提取', 'AMOUNT_TYPE_PROFIT_EXTRACT', '25', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('141', '27', '交易佣金返还', 'AMOUNT_TYPE_RETURN_POUNDAGE', '26', null, '', '1', '9');
INSERT INTO `sys_dict_item` VALUES ('142', '27', '业务提成', 'AMOUNT_TYPE_EXTRACT', '27', null, '', '1', '10');
INSERT INTO `sys_dict_item` VALUES ('143', '27', '其他支出', 'AMOUNT_TYPE_OTHER_EXPENSES', '28', null, '', '1', '11');
INSERT INTO `sys_dict_item` VALUES ('144', '28', '股票配资2', 'BUSINESS_TYPE_STOCK', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('145', '28', '期货配资', 'BUSINESS_TYPE_FUTURES', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('146', '28', '天天发', 'BUSINESS_TYPE_DAY', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('147', '28', '实盘大赛', 'BUSINESS_TYPE_GAME', '4', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('148', '28', '免费体验', 'BUSINESS_TYPE_FREE', '5', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('149', '29', '低', 'RISK_LEVEL_LOW', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('150', '29', '中', 'RISK_LEVEL_MIDDLE', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('151', '29', '高', 'RISK_LEVEL_HIGH', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('152', '30', '一次性', 'EXTRACT_TYPE_DISPOSABLE', '1', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('153', '30', '按月', 'EXTRACT_TYPE_MONTH', '2', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('154', '31', '资方', 'CUSTOMER_TYPE_INVESTOR', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('155', '31', '盘方', 'CUSTOMER_TYPE_BORROWER', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('156', '32', '股票账户', 'ACCOUNT_CATEGORY_STOCK', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('157', '32', '证券账户', 'ACCOUNT_CATEGORY_SECURITIES', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('158', '33', '恒生证券', 'SECURITIES_COMPANY_HS', '1', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('159', '33', '国信证券', 'SECURITIES_COMPANY_GX', '2', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('160', '34', '独立账户', 'ACCOUNT_TYPE_INDEPENDENT', '1', null, '', '1', '5');
INSERT INTO `sys_dict_item` VALUES ('161', '34', '母账户', 'ACCOUNT_TYPE_PARENT', '2', null, '', '1', '6');
INSERT INTO `sys_dict_item` VALUES ('162', '34', '子账户', 'ACCOUNT_TYPE_CHILD', '3', null, '', '1', '7');
INSERT INTO `sys_dict_item` VALUES ('163', '35', '中国银行', 'BANK_ID_BOC', '1', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('164', '35', '中国工商银行', 'BANK_ID_ICBC', '2', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('165', '35', '中国建设银行', 'BANK_ID_CCB', '3', null, '', '1', '3');
INSERT INTO `sys_dict_item` VALUES ('166', '36', '直接客户', 'CUSTOMER_OF_DIRECT', '', null, '', '1', '1');
INSERT INTO `sys_dict_item` VALUES ('167', '36', '网站用户', 'CUSTOMER_OF_WEB', '', null, '', '1', '2');
INSERT INTO `sys_dict_item` VALUES ('176', '27', '配资支出', 'AMOUNT_TYPE_FINANCE_COST', '28', null, '', '1', '12');
INSERT INTO `sys_dict_item` VALUES ('177', '27', '配资撤回', 'AMOUNT_TYPE_FINANCE_INCOME', '28', null, '', '1', '13');
INSERT INTO `sys_dict_item` VALUES ('178', '5', '短信签名', 'SMS_SIGN', '【伯庸金融】', null, '', '1', '8');
INSERT INTO `sys_dict_item` VALUES ('180', '12', 'wew23', 'qweq', 'qwe23', null, '', '1', '4');
INSERT INTO `sys_dict_item` VALUES ('181', '16', 'qwe', 'qwe', 'qwe', null, '', '1', '23');
INSERT INTO `sys_dict_item` VALUES ('182', '1', '代码模板路径', 'FTL_PATH', 'E:\\github\\hplus\\base\\whale\\system-ext\\ext-code\\src\\main\\resources\\templates\\game', '', '', '1', '7');

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
  `fileType` int(2) NOT NULL COMMENT '文件类型',
  `realFileName` varchar(64) NOT NULL COMMENT '原文件名',
  `fileSuffix` varchar(16) NOT NULL COMMENT '文件后缀名',
  `filePath` varchar(256) NOT NULL COMMENT '文件路径',
  `absolutePath` varchar(256) DEFAULT NULL COMMENT '绝对路径',
  `urlPath` varchar(256) NOT NULL COMMENT 'url访问地址',
  `saveWay` int(2) NOT NULL COMMENT '保存方式',
  `width` int(6) DEFAULT NULL COMMENT '图片宽度',
  `height` int(6) DEFAULT NULL COMMENT '图片高度',
  `fileSize` bigint(10) NOT NULL COMMENT '文件大小',
  `originalImgId` bigint(10) DEFAULT NULL COMMENT '原始图Id',
  `extInfo` varchar(512) DEFAULT NULL COMMENT '扩展信息',
  `creator` bigint(10) DEFAULT NULL COMMENT '创建人',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` varchar(64) NOT NULL,
  `appId` varchar(16) DEFAULT NULL,
  `opt` varchar(64) DEFAULT NULL,
  `cnName` varchar(128) DEFAULT NULL,
  `tableName` varchar(64) DEFAULT NULL,
  `uri` varchar(128) DEFAULT NULL,
  `sqlStr` varchar(512) DEFAULT NULL,
  `argStr` longtext,
  `rsStr` longtext,
  `ip` varchar(15) DEFAULT NULL,
  `createTime` bigint(15) DEFAULT NULL,
  `userName` varchar(32) DEFAULT NULL,
  `callOrder` int(4) unsigned DEFAULT NULL,
  `methodCostTime` int(10) unsigned DEFAULT NULL,
  `costTime` int(10) unsigned DEFAULT NULL,
  `rsType` int(2) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1446029894824system315', 'system', 'query', '短信', 'sys_sms', '短信扫描发送线程', 'SELECT t.id,t.content,t.smsType,t.toPhones,t.sendTime,t.encode,t.resStatus,t.resMsg,t.sid,t.overLengthIgnore,t.sendRealTime,t.retryTime,t.curRetryTime,t.createTime,t.recTime,t.status,t.bisExpInfo,t.originalId FROM sys_sms t  WHERE 1=1  AND t.status = ? ORDER BY t.id desc LIMIT 0, 10', '[1]', '[]', null, '1446028512528', null, '24', '60005', '1380104', '1');

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menuId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parentId` int(10) unsigned DEFAULT NULL,
  `menuName` varchar(64) DEFAULT NULL,
  `menuType` tinyint(4) DEFAULT NULL,
  `menuUrl` varchar(255) DEFAULT NULL,
  `inco` varchar(128) DEFAULT NULL,
  `openType` tinyint(4) DEFAULT NULL,
  `orderNo` int(6) DEFAULT NULL,
  `openState` tinyint(4) DEFAULT NULL,
  `isPublic` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`menuId`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('2', '0', '系统管理', '1', '', '', '1', '10', '1', '0');
INSERT INTO `sys_menu` VALUES ('3', '2', '用户管理', '3', '/user/goTree', '', '1', '2', '1', '0');
INSERT INTO `sys_menu` VALUES ('4', '2', '角色管理', '3', '/role/goList', '', '1', '3', '1', '0');
INSERT INTO `sys_menu` VALUES ('5', '2', '菜单管理', '3', '/menu/goList', '', '1', '4', '1', '0');
INSERT INTO `sys_menu` VALUES ('6', '2', '字典管理', '3', '/dict/goTree', '', '1', '7', '1', '0');
INSERT INTO `sys_menu` VALUES ('7', '2', '权限管理', '3', '/auth/goTree', '', '1', '5', '1', '0');
INSERT INTO `sys_menu` VALUES ('10', '2', '组织管理', '3', '/dept/goList', '', '1', '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('11', '2', '日志查看', '3', '/log/goList', '', '1', '8', '1', '0');
INSERT INTO `sys_menu` VALUES ('14', '2', '缓存管理', '3', '/goAdmin', '', '1', '11', '1', '0');
INSERT INTO `sys_menu` VALUES ('22', '2', '短信查看', '3', '/sms/goList', '', '1', '15', '1', '0');
INSERT INTO `sys_menu` VALUES ('23', '2', '文件上传', '3', '/fileInfo/goFileUpload', '', '1', '16', '1', '0');
INSERT INTO `sys_menu` VALUES ('24', '2', '图片上传', '3', '/fileInfo/goImgUpload', '', '1', '17', '1', '0');
INSERT INTO `sys_menu` VALUES ('27', '2', '在线用户', '3', '/goWhoAreOnline', '', '1', '12', '1', '0');
INSERT INTO `sys_menu` VALUES ('29', '2', '代码生产', '3', '/code/goList', '', '1', '12', '1', '0');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `roleId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roleName` varchar(16) DEFAULT NULL,
  `roleCode` varchar(64) DEFAULT NULL COMMENT '角色编码',
  `status` tinyint(4) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('50', '测试', 'test', '1', '');

-- ----------------------------
-- Table structure for `sys_role_auth`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_auth`;
CREATE TABLE `sys_role_auth` (
  `roleAuthId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roleId` int(10) unsigned DEFAULT NULL,
  `authCode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`roleAuthId`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
  `lastLoginTime` datetime DEFAULT NULL,
  `loginIp` varchar(64) DEFAULT NULL,
  `addOn` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `userType` tinyint(4) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `isAdmin` char(1) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `loginNum` int(11) DEFAULT NULL COMMENT '登录次数',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '8C24E4A28487E42FA6EE9E63ABC886A7A27432823050A5C377895734', '超级管理员', '0', '', '15059490712', '0', '2015-10-28 18:35:15', '127.0.0.1', '1', '1', '1', '1', '1', null, '9', '8');
INSERT INTO `sys_user` VALUES ('6', 'ls', '21218cca77804d2ba1922c33e0151105', '李四', '1', '', '15555555323', '0', null, '127.0.0.1', '', '1', '0', '', '0', null, '1', null);
INSERT INTO `sys_user` VALUES ('7', 'ww', '21218cca77804d2ba1922c33e0151105', '王五', '1', '', '15555555555', '0', null, '127.0.0.1', '', '1', '0', '', '0', null, '1', null);
INSERT INTO `sys_user` VALUES ('9', 'yedw', '21218cca77804d2ba1922c33e0151105', '东哥', '1', '', '15960108248', '0', null, '', '', '1', '0', '', '0', null, '1', null);

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `userRoleId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(10) unsigned DEFAULT NULL,
  `roleId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`userRoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
