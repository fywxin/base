package org.whale.system.common.constant;

/**
 * 系统常量
 *
 * @author 王金绍
 * 2014年9月6日-下午1:29:30
 */
public final class SysConstant {
	//状态
	/**逻辑删除 */
	public static final int STATUS_DEL = 0;
	/**正常状态 */
	public static final int STATUS_NORMAL = 1;
	/**禁用状态 */
	public static final int STATUS_UNUSE = 2;
	
	
	/**验证码key */
	public static final String VERITY_CODE_KEY = "S-cYou-Ver_CoDe";
	/**用户上下文key */
	public static final String USER_CONTEXT_KEY = "USeR_CoNTEXT-KeY";
	/**默认密码 */
	public static final String USER_DEFAULT_PASSWORD = "111111";
	
	/**逻辑true */
	public static final String LOGIC_TRUE = "true";
	/**逻辑false */
	public static final String LOGIC_FALSE = "false";
	
	public static Integer MAX_LRU_CACHE_SIZE = 20000;
	
	//是否需要验证码开关
	//public static boolean VERITY_CODE_FLAG = false;
	
	//缓存KEY
	//缓存激活总开关，可通过配置文件修改，默认开启
	public static boolean CACHE_FLAG = true;
	/**字典key */
	public static final String CACHE_DICT_KEY = "DI_";
	/**权限key */
	public static final String CACHE_AUTH_KEY = "AR_";
	/**用户权限key */
	public static final String CACHE_USER_AUTH_KEY = "UA_";
	
	
	/* 告警相关常量  -- start*/
	public static final int MESSAGE_SOURCE_LOG = 1;//消息来源--日志采编
	public static final int MESSAGE_SOURCE_STATUS = 2;//消息来源-状态采编
	
	public static final int FAULT_TYPE_BUSINESS_EXCEPTION = 1;//故障类型--业务异常
	public static final int FAULT_TYPE_ERROR = 2;//故障类型--错误
	public static final int FAULT_TYPE_CHECK_STATUS_WARNING = 3;//故障类型--状态检查警告
	/* 告警相关常量 -- end */
	
	/**是否正在刷新权限 */
	public static final Boolean isRefreshAuth = false;
}
