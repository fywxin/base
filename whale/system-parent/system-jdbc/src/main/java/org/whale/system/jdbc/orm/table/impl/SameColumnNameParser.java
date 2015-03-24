package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.table.ParseColumnName;

@Component("sameColumnNameParser")
public class SameColumnNameParser implements ParseColumnName {

	@Override
	public String getDbColName(String columnAttr) {
		return columnAttr;
	}

}
