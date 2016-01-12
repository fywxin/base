package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.table.ParseColumnName;
import org.whale.system.jdbc.util.OrmUtil;

/**
 * 驼峰规则转换器 驼峰转小写下划线
 * userName user_name
 * @author wjs
 * 2015年10月13日 下午11:18:44
 */
@Component
public class Camel2SqlLowerColumnNameParser implements ParseColumnName {

	@Override
	public String getDbColName(String columnAttr) {
		return OrmUtil.camel2SqlLower(columnAttr);
	}

}
