package org.whale.system.jdbc.readwrite.router;

import java.util.List;

import javax.sql.DataSource;

/**
 * 抽象路由选择模板
 * 
 * @author 王金绍
 *
 */
public abstract class AbstractRouter implements Router{

	@Override
	public String route(List<DataSource> readDsList, List<String> readDsKeyList) {
		if(readDsList == null || readDsList.size() == 0)
			return null;
		return doSelect(readDsList, readDsKeyList);
	}

	public abstract String doSelect(List<DataSource> readDsList, List<String> readDsKeyList);
}
