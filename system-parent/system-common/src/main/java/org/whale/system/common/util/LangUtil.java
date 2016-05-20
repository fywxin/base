package org.whale.system.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 常用工具类
 *
 * @author wjs
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
							ReflectionUtil.writeField(field, obj, ((String)val).trim(), true);
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
							}else if(field.getType().toString().equals("class java.lang.Boolean")){
								ReflectionUtil.writeField(field, obj, false, true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("trim 异常",e);
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
