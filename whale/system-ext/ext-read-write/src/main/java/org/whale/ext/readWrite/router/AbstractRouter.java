package org.whale.ext.readWrite.router;

import javax.sql.DataSource;

/**
 * 抽象路由选择模板
 * 
 * @author 王金绍
 *
 */
public abstract class AbstractRouter implements Router{

	@Override
	public DataSource route(DataSource writeDataSource, DataSource[] readDataSources, String[] readDataSourceNames) {
		if(readDataSources == null || readDataSources.length == 0)
			return writeDataSource;
		if(readDataSources.length == 1)
			return readDataSources[0];
		return doSelect(writeDataSource, readDataSources, readDataSourceNames);
	}

	public abstract DataSource doSelect(DataSource writeDataSource, DataSource[] readDataSources, String[] readDataSourceNames);
}
