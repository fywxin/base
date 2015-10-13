package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.table.ParseTableName;
import org.whale.system.jdbc.util.OrmUtil;

/**
 * 驼峰规则转换器
 * 
 * @author 王金绍
 * 2015年10月13日 下午11:18:34
 */
@Component
public class CamelTableNameParser implements ParseTableName {

	@Override
	public String getDbTableName(String tableName) {
		if(Strings.isBlank(tableName))return null;
		
		tableName = OrmUtil.camel2SqlUpper(tableName);
		return "TB_"+tableName;
	}

	@Override
	public String getDbSequence(String tableName) {
		if(Strings.isBlank(tableName))return null;
		
		tableName = OrmUtil.camel2SqlUpper(tableName);
		return "SEQ_"+tableName;
	}

}
