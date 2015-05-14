package org.whale.ext.mongo.entry.mogo;

import org.whale.ext.mongo.entry.common.Acolumn;

public class MogoColumn {

	private Acolumn acolumn;
	
	//db字段名
	private String sqlName;
	//是否主键
	private boolean isId = false;
	//主键是否自增，非自增主键，值由开发人员提供，如UUID
	private boolean idAuto = true;
	//是否唯一
	private boolean unique;
	//是否可更新
	private boolean updateAble;
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
	public boolean isIdAuto() {
		return idAuto;
	}
	public void setIdAuto(boolean idAuto) {
		this.idAuto = idAuto;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	public boolean isUpdateAble() {
		return updateAble;
	}
	public void setUpdateAble(boolean updateAble) {
		this.updateAble = updateAble;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
