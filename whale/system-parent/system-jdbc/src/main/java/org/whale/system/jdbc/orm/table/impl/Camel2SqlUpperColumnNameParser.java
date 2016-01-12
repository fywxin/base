package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.table.ParseColumnName;
import org.whale.system.jdbc.util.OrmUtil;

/**
 * 驼峰规则转下划线大写 
 * 	 userName USER_NAME
 * 
 * @author wjs
 * 2015年10月13日 下午11:09:18
 */
@Component
public class Camel2SqlUpperColumnNameParser implements ParseColumnName {

	@Override
	public String getDbColName(String columnAttr) {
		return OrmUtil.camel2SqlUpper(columnAttr);
	}

}
