package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.table.ParseTableName;

@Component("sameTableNameParser")
public class SameTableNameParser implements ParseTableName {

	@Override
	public String getDbTableName(String tableName) {
		return tableName;
	}

	@Override
	public String getDbSequence(String tableName) {
		// TODO Auto-generated method stub
		return "seq_"+tableName;
	}

}
