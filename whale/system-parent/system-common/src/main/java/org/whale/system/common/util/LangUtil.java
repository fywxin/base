package org.whale.system.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常用工具类
 *
 * @author 王金绍
 * 2014年9月6日-下午1:30:20
 */
@SuppressWarnings("all")
public final class LangUtil {
	private static final Logger logger = LoggerFactory.getLogger(LangUtil.class);
	
	/**
     * 判断一个对象是否为空。它支持如下对象类型：
     * <ul>
     * <li>null : 一定为空
     * <li>数组
     * <li>集合
     * <li>Map
     * <li>其他对象 : 一定不为空
     * </ul>
     * 
     * @param obj
     *            任意对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        if (obj.getClass().isArray())
            return Array.getLength(obj) == 0;
        if (obj instanceof Collection<?>)
            return ((Collection<?>) obj).isEmpty();
        if (obj instanceof Map<?, ?>)
            return ((Map<?, ?>) obj).isEmpty();
        return false;
    }

	/**
	 * 将以多个ID组成的字符串，分割组装成List<Long> 
	 * 默认分割为","
	 * @param ids 多个ID组成的字符串
	 * @return
	 */
	public static List<Long> splitIds(String ids){
		return splitIds(ids, ",");
	}
	
	/**
	 * 将以多个ID组成的字符串，分割组装成List<Long> 
	 * @param ids 多个ID组成的字符串
	 * @param splitStr 分割符
	 * @return
	 */
	public static List<Long> splitIds(String ids, String splitStr){
		if(Strings.isBlank(ids))
			return null;
		if(Strings.isBlank(splitStr)){
			splitStr = ",";
		}
		String[] idS = ids.split(splitStr);
		List<Long> list = new ArrayList<Long>(idS.length);
		for(String id : idS){
			if(Strings.isNotBlank(id))
				try {
					list.add(Long.valueOf(id.trim()));
				} catch (NumberFormatException e) {
					logger.error("ID转换成Long类型出错", e);
					throw new RuntimeException("ID转换成Long类型出错", e);
				}
		}
		return list;
	}
	
	/**
	 * 默认 "," 拼接ID列表 为字符串
	 * @param idList ID列表
	 * @return
	 */
	public static String joinIds(List<Long> idList){
		return joinIds(idList, ",");
	}
	
	/**
	 * 以拼接符 拼接ID列表 为字符串
	 * @param idList  ID列表
	 * @param joinStr  拼接符
	 * @return
	 */
	public static String joinIds(Collection<Long> idList, String joinStr){
		return joinList(idList, joinStr);
	}
	
	/**
	 * 默认 "," 拼接列表 为字符串
	 * @param list
	 * @return
	 */
	
	public static String joinList(Collection list){
		return joinIds(list, ",");
	}
	
	/**
	 * 以拼接符 拼接ID列表 为字符串
	 * @param list
	 * @param joinStr
	 * @return
	 */
	public static String joinList(Collection list, String joinStr){
		if(list == null || list.size()<1)
			return "";
		if(Strings.isBlank(joinStr)){
			joinStr = ",";
		}
		StringBuilder strb = new StringBuilder();
		for(Object obj : list){
			if(obj != null){
				strb.append(joinStr).append(obj.toString());
			}
		}
		if(strb.length() > 0)
			return strb.substring(joinStr.length());
		return "";
	}
	
	public static String joinEntityList(List<?> list, String attrName) {
		return joinEntityList(list, attrName, ",");
	}
	
	public static String joinEntityList(List<?> list, String attrName, String joinStr) {
		if(list == null || list.size()<1 || Strings.isBlank(attrName))
			return null;
		StringBuilder strb = new StringBuilder();
		for(Object obj : list){
			if(obj == null)
				continue;
			Field[] fields =  obj.getClass().getDeclaredFields();
			if(fields != null && fields.length > 0){
				for(Field field : fields){
					if(field.getName().equals(attrName)){
						Object val;
						try {
							val = ReflectionUtil.readField(field, obj, true);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							throw new RuntimeException("读取属性 ["+attrName+"] 值出错");
						}
						if(val != null && Strings.isNotBlank(val.toString()))
							strb.append(joinStr).append(val.toString().trim());
					}
				}
			}
		}
		if(strb.length() > joinStr.length()){
			return strb.substring(joinStr.length());
		}
		return strb.toString();
	}
	
	/**
	 * 获取对象列表中的ID集合
	 * @param list
	 * @return
	 */
	public static List<Long> getIdList(List<?> list){
		return getIdList(list, "id");
	}
	
	/**
	 * 获取对象列表中的ID集合
	 * @param list
	 * @return
	 */
	public static List<Long> getIdList(List<?> list, String idColName){
		if(list == null || list.size()<1 || Strings.isBlank(idColName))
			return null;
		List<Long> idList = new ArrayList<Long>(list.size());
		for(Object obj : list){
			if(obj == null)
				continue;
			Field[] fields =  obj.getClass().getDeclaredFields();
			if(fields != null && fields.length > 0){
				for(Field field : fields){
					if(field.getName().equals(idColName)){
						Object val;
						try {
							val = ReflectionUtil.readField(field, obj, true);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							throw new RuntimeException("读取属性 ["+idColName+"] 值出错");
						}
						if(val != null && Strings.isNotBlank(val.toString()))
							idList.add(Long.valueOf(val.toString()));
					}
				}
			}
		}
		return idList;
	}
	
	/**
	 * 通过反射将对象内所有字符串属性值 进行trim() 
	 * @param obj
	 * @return
	 */
	public static Object trim(Object obj){
		return trim(obj, null);
	}
	
	/**
	 * 通过反射将对象内所有字符串属性值 进行trim() 
	 * 暂时无法提供父对象属性的 trim(); 原因是未找到 子对象获取父对象
	 * @param obj
	 * @param escapeList 不需要trim()的属性列表
	 * @return
	 */
	public static Object trim(Object obj, List<String> escapeList){
		if(obj == null)
			return null;
		try {
			Field[] fields =  obj.getClass().getDeclaredFields();
			if(fields != null && fields.length > 0){
				for(Field field : fields){
					Object val = ReflectionUtil.readField(field, obj, true);
					if(field.getModifiers() < 15 && field.getType().toString().equals("class java.lang.String")){
						if(val != null){
							if(escapeList != null && escapeList.indexOf(field.getName()) != -1)
								continue;
							ReflectionUtil.writeField(field, obj, val.toString().trim(), true);
						}else{
							ReflectionUtil.writeField(field, obj, "", true);
						}
					}else if(field.getModifiers() < 15){
						if(val == null){
							if(field.getType().toString().equals("class java.lang.Integer")){
								ReflectionUtil.writeField(field, obj, 0, true);
							}else if(field.getType().toString().equals("class java.lang.Long")){
								ReflectionUtil.writeField(field, obj, 0L, true);
							}else if(field.getType().toString().equals("class java.lang.Double")){
								ReflectionUtil.writeField(field, obj, 0D, true);
							}else if(field.getType().toString().equals("class java.lang.Float")){
								ReflectionUtil.writeField(field, obj, 0F, true);
							}
						}
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	

    
    /**
     * 判断当前系统是否为Windows
     * 
     * @return true 如果当前系统为Windows系统
     */
    public static boolean isWin() {
        try {
            String os = System.getenv("OS");
            return os != null && os.indexOf("Windows") > -1;
        }
        catch (Throwable e) {
            return false;
        }
    }
    
    

    
    
    public static String bulidOptions(Map<String, Object> labelValMap, Object defVal){
    	return bulidOptions(labelValMap, defVal, true);
    }
    
    public static String bulidOptions(Map<String, Object> labelValMap, Object defVal, boolean hasEmptyOption){
    	StringBuilder strb = new StringBuilder();
    	if(hasEmptyOption){
    		strb.append("<option value=\"\" >--请选择--</option>");
    	}
    	
    	if(labelValMap != null && labelValMap.size() > 0){
    		for(Map.Entry<String, Object> entry : labelValMap.entrySet()){
    			strb.append("<option value=\"").append(entry.getValue()).append("\"");
    			if(defVal != null && entry.getValue().equals(defVal)){
    				strb.append(" selected=\"selected\"");
    			}
    			strb.append(" >").append(entry.getKey()).append("</option>");
    		}
    	}
    	
    	return strb.toString();
    }
	
    /**
     * 获取当前进程Id
     * @return
     * @date 2015年5月13日 下午1:47:57
     */
    public static int getPid() {  
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
        String name = runtime.getName(); // format: "pid@hostname"  
        try {  
            return Integer.parseInt(name.substring(0, name.indexOf('@')));  
        } catch (Exception e) {  
            return -1;  
        }  
    }  
}
