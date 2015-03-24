package org.whale.system.jdbc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.whale.system.jdbc.orm.entry.OrmColumn;

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
	
//-----------------------------------------------------------------------------------------
	
	public static List<Object> getColumnValues(Object obj, List<OrmColumn> list){
		List<Object> args = new ArrayList<Object>(list.size());
		for(OrmColumn col : list){
			Object val = AnnotationUtil.getFieldValue(obj, col.getField());
			args.add(val);
		}
		return args;
	}

	
	public static void setColumnValue(Object obj, OrmColumn col, Object value){
		if(java.sql.Types.VARCHAR == col.getType()){
			AnnotationUtil.setFieldValue(obj, col.getField(), value);
			return ;
		}
		if(java.sql.Types.BIGINT == col.getType()){
			BigDecimal bg = (BigDecimal)value;
			if(bg != null)
				AnnotationUtil.setFieldValue(obj, col.getField(), bg.longValue());
			return ;
		}
		//不能用java.sql.Date 否则 时分秒 的值为 0
		if(java.sql.Types.DATE == col.getType()){
			Time time = (Time)value;
			if(time != null){
				Date date = new Date();
				date.setTime(time.getTime());
				AnnotationUtil.setFieldValue(obj, col.getField(),date);
				return ;
			}
		}
		if(java.sql.Types.INTEGER == col.getType()){
			BigDecimal bg = (BigDecimal)value;
			if(bg != null)
				AnnotationUtil.setFieldValue(obj, col.getField(), bg.intValue());
			return ;
		}
		if(java.sql.Types.DOUBLE == col.getType()){
			BigDecimal bg = (BigDecimal)value;
			if(bg != null)
				AnnotationUtil.setFieldValue(obj, col.getField(), bg.doubleValue()) ;
			return ;
		}
		if(java.sql.Types.FLOAT == col.getType()){
			BigDecimal bg = (BigDecimal)value;
			if(bg != null)
				AnnotationUtil.setFieldValue(obj, col.getField(), bg.floatValue());
			return ;
		}
		if(java.sql.Types.BOOLEAN == col.getType()){
			AnnotationUtil.setFieldValue(obj, col.getField(), (Boolean)value);
			return ;
		}
		if(java.sql.Types.CLOB == col.getType()){
			Type type = col.getField().getType();
			String t = type.toString();
			if("class java.lang.String".equals(t)){
				AnnotationUtil.setFieldValue(obj, col.getField(), value);
				return ;
			}
		}
		if(java.sql.Types.SMALLINT == col.getType()){
			BigDecimal bg = (BigDecimal)value;
			if(bg != null)
				AnnotationUtil.setFieldValue(obj, col.getField(), bg.shortValue());
			return ;
		}
		if(java.sql.Types.TINYINT == col.getType()){
			AnnotationUtil.setFieldValue(obj, col.getField(), (Byte)value);
			return ;
		}
		if(java.sql.Types.DISTINCT == col.getType()){
			AnnotationUtil.setFieldValue(obj, col.getField(), (BigDecimal)value);
			return ;
		}
		if(java.sql.Types.TIME == col.getType() || java.sql.Types.TIMESTAMP == col.getType()){
			Timestamp time = (Timestamp)value;
			if(time != null){
				Date date = new Date();
				date.setTime(time.getTime());
				AnnotationUtil.setFieldValue(obj, col.getField(),date);
				return ;
			}
		}
		AnnotationUtil.setFieldValue(obj, col.getField(), value);
	}
}
