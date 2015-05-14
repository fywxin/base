package org.whale.ext.mongo.entry.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 反射常用对象缓存
 *
 * @author 王金绍
 * 2014年9月6日-下午1:45:02
 */
@Component("entryContext")
public class EntryContext {

	private Map<Class<?>, Atable> cache = new HashMap<Class<?>, Atable>();
	
	public Atable getTable(Class<?> clazz) {
		return cache.get(clazz);
	}
	
	public void putTable(Class<?> clazz, Atable table){
		this.cache.put(clazz, table);
	}
	
	public List<Acolumn> getColumns(Class<?> clazz) {
		Atable table = this.getTable(clazz);
		if(table == null) return null;
		return table.getCols();
	}
	
	public Acolumn getColumn(Class<?> clazz, String attrName){
		List<Acolumn> cols = this.getColumns(clazz);
		if(cols == null || cols.size() < 1) return null;
		for(Acolumn col : cols){
			if(col.getAttrName().equals(attrName))
				return col;
		}
		return null;
	}
	
	
}
