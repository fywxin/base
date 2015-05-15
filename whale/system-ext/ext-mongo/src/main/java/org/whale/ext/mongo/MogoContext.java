package org.whale.ext.mongo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.ext.mongo.entry.MogoTable;

@Component
public class MogoContext {
	
	private Map<Class<?>, MogoTable> tables = new HashMap<Class<?>, MogoTable>();
	
	private Object lock = new Object();
	
	@Autowired
	private MogoTableBulider mogoTableBulider;

	public MogoTable get(Class<?> clazz){
		MogoTable table = tables.get(clazz);
		if(table == null){
			synchronized (lock) {
				if(table == null){
					table = this.mogoTableBulider.bulid(clazz);
					if(table != null){
						tables.put(clazz, table);
					}
				}
			}
		}
		return table;
	}
}
