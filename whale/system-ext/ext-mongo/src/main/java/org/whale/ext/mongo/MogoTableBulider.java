package org.whale.ext.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.ext.mongo.entry.MogoTable;
import org.whale.system.common.reflect.Atable;
import org.whale.system.common.reflect.EntryContext;
import org.whale.system.common.reflect.TableBulider;

@Component
public class MogoTableBulider {
	
	@Autowired
	private TableBulider tableBulider;
	@Autowired
	private EntryContext entryContext;

	public MogoTable bulid(Class<?> clazz){
		Atable table = entryContext.getTable(clazz);
		if(table == null){
			table = this.tableBulider.parse(clazz);
			entryContext.putTable(clazz, table);
		}
		MogoTable mogoTable = new MogoTable();
		mogoTable.setTable(table);
		
		
		
		return new MogoTable();
	}
}
