package org.whale.system.jdbc.orm.entry;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Acolumn {
	//字段类型 
	private Class<?> attrType;
	//字段名
	private String attrName;
	//字段中文名
	private String cnName;
	//字段
	private Field field;
	
	
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public Class<?> getAttrType() {
		return attrType;
	}
	public void setAttrType(Class<?> attrType) {
		this.attrType = attrType;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	@Override
	public String toString() {
		return "[attrName=" + attrName + ", cnName=" + cnName + "]";
	}

}
