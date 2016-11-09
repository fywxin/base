package org.whale.system.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程上下文
 * TODO ab测试是否存在内存溢出
 * @author wjs
 * 2014年9月17日-下午12:02:35
 */
public class ThreadContext {
	
	/**创建时间 */
	public static final String KEY_OPT_CONTEXT = "optContext";
	/**创建时间 */
	public static final String KEY_USER_CONTEXT = "userContext";
	/**链前日志 */
	public static final String KEY_LOG_PREX = "log_prex";
	/**创建时间 */
	public static final String KEY_LOG_CREATE_TIME = "log_createTime";
	/**创建时间 */
	public static final String KEY_LOG_PREX_TIME = "log_prexTime";
	/**创建时间 */
	public static final String KEY_LOG_URI = "log_uri";
	/**日志：客户端IP地址 */
	public static final String KEY_LOG_IP = "log_ip";
	/**日志：@Log desc()模板 {} 对应参数 */
	public static final String KEY_LOG_DATAS = "log_datas";
	/**创建时间 */
	public static final String KEY_REQUEST = "request";
	/**创建时间 */
	public static final String KEY_RESPONSE = "response";
	/**是否从页面进入 */
	public static final String KEY_FROM_WEB = "from_web";
	
	public static final String KEY_CLIENT_CONTEXT = "client_context";
	
	public static final String KEY_SERVER_CONTEXT = "server_context";
	/**动态数据源KEY */
	public static final String DYNAMIC_DS_READ_MODE_FLAG = "dynamicDsRead";
	/**事务方法，强制走主库标志Boolean */
	public static final String DYNAMIC_DS_TRANS_FOCUS_WRITE_FLAG = "transWriteFlag";


	private static final ThreadLocal<ThreadContext> LOCAL = new ThreadLocal<ThreadContext>() {
		@Override
		protected ThreadContext initialValue() {
			return new ThreadContext();
		}
	};
	
	public static ThreadContext getContext() {
	    return LOCAL.get();
	}
	
	public static void removeContext() {
	    LOCAL.remove();
	}
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public Map<String, Object> put(String key, Object val){
		map.put(key, val);
		return map;
	}
	
	public Object get(String key){
		return map.get(key);
	}
	
	public String getStr(String key){
		return map.get(key) == null ? null : map.get(key).toString();
	}
	
	public Integer getInteger(String key){
		return map.get(key) == null ? null : Integer.parseInt(map.get(key).toString());
	}
	
	public Long getLong(String key){
		return map.get(key) == null ? null : Long.parseLong(map.get(key).toString());
	}
	
	public Boolean getBoolean(String key){
		return map.get(key) == null ? null : (Boolean)map.get(key);
	}
	
	public void remove(String key){
		map.remove(key);
	}
}
