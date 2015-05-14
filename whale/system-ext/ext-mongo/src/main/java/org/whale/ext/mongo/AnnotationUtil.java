package org.whale.ext.mongo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtil {

	/**
	 * 
	 *功能说明: 获取字段的值
	 *创建人: 王金绍
	 *创建时间:2013-3-25 上午11:23:25
	 *@param obj
	 *@param field
	 *@return Object
	 *
	 */
	public static Object getFieldValue(Object obj, Field field) {
        Object fieldValue = null;
        try {
        	field.setAccessible(true);
            fieldValue = field.get(obj);
            if (fieldValue == null) return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }
	
	public static List<Object> getFieldValues(Object obj, List<Field> list){
		List<Object> args = new ArrayList<Object>(list.size());
		for(Field field : list){
			args.add(getFieldValue(obj, field));
		}
		return args;
	}
	
	public static void setFieldValue(Object obj, Field field, Object value) {
        try {
        	field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
	

}
