package org.whale.system.jdbc.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Atable {

	/** table 实体名 */
	private String entityName;
	
	private Class<?> clazz;
	
	private Class<?> superClazz;
	
	private List<Acolumn> cols;
	
	private Atable parent;
	
	
	public List<Field> getFields(){
		List<Field> list = new ArrayList<Field>(cols.size());
		for(Acolumn col : cols){
			list.add(col.getField());
		}
		return list;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getSuperClazz() {
		return superClazz;
	}

	public void setSuperClazz(Class<?> superClazz) {
		this.superClazz = superClazz;
	}

	public List<Acolumn> getCols() {
		return cols;
	}

	public void setCols(List<Acolumn> cols) {
		this.cols = cols;
	}

	public Atable getParent() {
		return parent;
	}

	public void setParent(Atable parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "Atable [entityName=" + entityName + ", clazz=" + clazz
				+ ", superClazz=" + superClazz + ", cols=" + cols + ", parent="
				+ parent + "]";
	}
	
}
