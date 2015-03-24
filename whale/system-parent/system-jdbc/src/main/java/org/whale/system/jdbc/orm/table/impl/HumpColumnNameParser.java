package org.whale.system.jdbc.orm.table.impl;

import org.springframework.stereotype.Component;
import org.whale.system.common.util.LangUtil;
import org.whale.system.jdbc.orm.table.ParseColumnName;


/**
 * 驼峰规则转换器
 *
 * @author 王金绍
 * 2014年9月6日-下午2:00:21
 */
@Component("humpColumnNameParser")
public class HumpColumnNameParser implements ParseColumnName {

	@Override
	public String getDbColName(String columnAttr) {
		return LangUtil.dump2SqlStyle(columnAttr);
	}

}
