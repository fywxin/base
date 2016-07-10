package org.whale.system.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * properties配置文件工具类
 *
 * @author wjs
 * 2014年9月6日-下午1:32:29
 */
public class PropertiesUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	/**缓存 */
	private static Map<String, String> confMap = new HashMap<String, String>();
	
	private PropertiesUtil(){}
	
	static{
		Map<String, String> map = readConf("conf.properties");
		if(map != null && map.keySet().size() > 0)
			confMap.putAll(map);
	}
	
	/**
	 * 
	 *功能说明: 读取配置文件信息返回Map
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:36:17
	 *@param resourcesPaths
	 *@return Map<String,String>
	 *
	 */
	public static Map<String, String> readConf(String... resourcesPaths){
		if(resourcesPaths == null)
			return null;
		Properties properties = loadProperties(resourcesPaths);
		Map<String, String> map = new HashMap<String, String>();
		Object value = null;
		for(Object key : properties.keySet()){
			if(key != null){
				value = properties.get(key);
				if(value == null){
					map.put(key.toString(), null);
				}else{
					map.put(key.toString(), value.toString());
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 *功能说明: 加载properties配置文件
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:36:45
	 *@param resourcesPaths
	 *@return Properties
	 *
	 */
	public static Properties loadProperties(String... resourcesPaths) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Properties props = new Properties();
		
		for (String location : resourcesPaths) {
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				logger.error("加载配置文件错误路径:" + location, ex);
			} finally {
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						logger.error("文件流关闭出现异常:", e);
					}
				}
			}
		}
		return props;
	}
	
	/**
	 * 
	 *功能说明: 添加配置集合
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:42:11
	 *@param map void
	 *
	 */
	public static void addAll(Map<String, String> map){
		if(confMap == null){
			confMap = map;
		}else{
			if(map != null && map.keySet().size() > 0){
				confMap.putAll(map);
			}
		}
	}
	
	/**
	 * 
	 *功能说明: 配置文件是否包含key的配置项
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:37:04
	 *@param key
	 *@return boolean
	 *
	 */
	public static boolean containKey(String key){
		if(confMap == null || Strings.isBlank(key))
			return false;
		return confMap.keySet().contains(key);
	}

	/**
	 * 
	 *功能说明:取值
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:37:33
	 *@param key
	 *@return String
	 *
	 */
	public static String getValue(String key){
		if(confMap == null || Strings.isBlank(key))
			return null;
		return confMap.get(key);
	}
	
	/**
	 * 
	 *功能说明: 取值，如果值为null，则使用默认值
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:38:09
	 *@param key
	 *@param defaultVal
	 *@return String
	 *
	 */
	public static String getValue(String key, String defaultVal){
		String val = getValue(key);
		if(val == null)
			val = defaultVal;
		return val;
	}
	
	public static Integer getValueInt(String key){
		String val = getValue(key);
		if(Strings.isBlank(val))
			return null;
		try{
			return Integer.parseInt(val);
		}catch(Exception e){
			return null;
		}
	}
	
	public static Integer getValueInt(String key, Integer defaultVal){
		Integer val = getValueInt(key);
		
		if(val == null)
			return defaultVal;
		return val;
	}
	
	public static Long getValueLong(String key){
		String val = getValue(key);
		if(Strings.isBlank(val))
			return null;
		try{
			return Long.parseLong(val);
		}catch(Exception e){
			return null;
		}
	}
	
	public static Long getValueLong(String key, Long defaultVal){
		Long val = getValueLong(key);
		
		if(val == null)
			return defaultVal;
		return val;
	}
	
	public static Boolean getValueBoolean(String key){
		return getValueBoolean(key, null);
	}
	
	public static Boolean getValueBoolean(String key, Boolean defaultVal){
		String val = getValue(key);
		if(val == null)
			return defaultVal;
		return "true".equalsIgnoreCase(val);
	}
	
	/**
	 * 
	 *功能说明:获取所有的配置项key集合
	 *创建人: wjs
	 *创建时间:2013-4-26 下午2:39:34
	 *@return Set<String>
	 *
	 */
	public static Set<String> getKeys(){
		if(confMap == null)
			return null;
		return confMap.keySet();
	}
}
