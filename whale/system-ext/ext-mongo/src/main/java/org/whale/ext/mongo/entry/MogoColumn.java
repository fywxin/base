package org.whale.ext.mongo.entry;

import org.whale.system.common.reflect.Acolumn;


public class MogoColumn {

	private Acolumn acolumn;
	
	//db字段名
	private String sqlName;
	//是否主键
	private boolean isId = false;
	//默认值
	private String defaultValue = null;
	
	
	public Acolumn getAcolumn() {
		return acolumn;
	}
	public void setAcolumn(Acolumn acolumn) {
		this.acolumn = acolumn;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public boolean isId() {
		return isId;
	}
	public void setId(boolean isId) {
		this.isId = isId;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
