package org.whale.system.common.constant;

/**
 * 字典常量
 *
 * @author wjs
 * 2014年9月6日-下午1:29:18
 */
public class DictConstant {
	
	//--------------------------------------------系统配置-------------------------------------
	
	public static final String DICT_SYS_CONF = "DICT_SYS_CONF";
	/** 单用户登录 */
	public static final String DICT_TIEM_ONLY_SINGLE_LOGIN = "ONLY_SINGLE_LOGIN";
	/** 刷新权限 */
	public static final String DICT_ITEM_FLUSH_AUTH = "ITEM_FLUSH_AUTH";
	/** 部门根节点*/
	public static final String DICT_ITEM_DEPT_ROOT = "ITEM_DEPT_ROOT";
	/** 用户自动登录 */
	public static final String DICT_AUTO_LOGIN_FLAG = "AUTO_LOGIN_FLAG";
	
	//--------------------------------------------代码-------------------------------------
	public static final String DICT_CODE = "DICT_CODE";
	/** 模板文件路径 */
	public static final String DICT_ITEM_FTL_PATH = "ITEM_FTL_PATH";
	/** 代码存储路径 */
	public static final String DICT_ITEM_CODE_DOMAIN_PATH = "ITEM_CODE_DOMAIN_PATH";
	
	public static final String DICT_ITEM_CODE_DAO_PATH = "ITEM_CODE_DAO_PATH";
	
	public static final String DICT_ITEM_CODE_SERVICE_PATH = "ITEM_CODE_SERVICE_PATH";
	
	public static final String DICT_ITEM_CODE_CONTROLLER_PATH = "ITEM_CODE_CONTROLLER_PATH";
	/** 代码存储路径 */
	public static final String DICT_ITEM_CODE_JSP_PATH = "ITEM_CODE_JSP_PATH";
	/** 全局包名称，可以被实体包名称覆盖 */
	public static final String DICT_ITEM_PACKAGE = "ITEM_PACKAGE";
	
	//--------------------------------------------文件上传-------------------------------------
	public static final String DICT_FILE = "DICT_FILE";
	/** 默认文件夹 */
	public static final String DICT_ITEM_FILE_DIR = "ITEM_FILE_DIR";
	/** 文件公共路径 */
	public static final String DICT_ITEM_FILE_PUB_DIRS = "ITEM_FILE_PUB_DIRS";
	
	/**linux 主机 IP */
	public static final String FILE_SERVER_HOST = "FILE_SERVER_HOST";
	/**文件服务器 SSH用户名 */
	public static final String FILE_SERVER_SSH_USER_NAME = "FILE_SERVER_SSH_USER_NAME";
	/**文件服务器 SSH登录密码 */
	public static final String FILE_SERVER_SSH_PASSWORD = "FILE_SERVER_SSH_PASSWORD";
	/**文件服务器 SSH登录端口 */
	public static final String FILE_SERVER_SSH_PORT = "FILE_SERVER_SSH_PORT";
	
	/**文件服务器 scp文件上传后的保存路径 */
	public static final String FILE_SERVER_DEPLOY_PATH = "FILE_SERVER_DEPLOY_PATH";
	/**文件服务器IP */
	public static final String FILE_SERVER_APACHE_HOST = "FILE_SERVER_APACHE_HOST";
}
