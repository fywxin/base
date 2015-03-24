package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.table.ParseTableName;

/**
 * 驼峰规则转换器
 *
 * @author 王金绍
 * 2014年9月6日-下午2:00:16
 */
@Component
public class HumpTableNameParser implements ParseTableName {

	@Override
	public String getDbTableName(String tableName) {
		if(Strings.isBlank(tableName))return null;
		
		tableName = LangUtil.dump2SqlStyle(tableName);
		return "TB_"+tableName;
	}

	@Override
	public String getDbSequence(String tableName) {
		if(Strings.isBlank(tableName))return null;
		
		tableName = LangUtil.dump2SqlStyle(tableName);
		return "SEQ_"+tableName;
	}

}
