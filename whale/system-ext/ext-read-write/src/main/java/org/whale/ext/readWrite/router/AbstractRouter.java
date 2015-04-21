package org.whale.ext.readWrite.router;

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
	public int route(DataSource writeDataSource, List<DataSource> readDataSources, List<String> readDataSourceNames) {
		if(readDataSources == null || readDataSources.size() == 0)
			return -1;
		if(readDataSources.size() == 1)
			return 0;
		return doSelect(writeDataSource, readDataSources, readDataSourceNames);
	}

	public abstract int doSelect(DataSource writeDataSource, List<DataSource> readDataSources, List<String> readDataSourceNames);
}
