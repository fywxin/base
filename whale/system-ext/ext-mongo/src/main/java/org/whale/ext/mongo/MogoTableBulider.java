package org.whale.ext.mongo;

import org.springframework.stereotype.Component;
import org.whale.ext.mongo.entry.mogo.MogoTable;

@Component
public class MogoTableBulider {

	public MogoTable bulid(Class<?> clazz){
		
		return new MogoTable();
	}
}
