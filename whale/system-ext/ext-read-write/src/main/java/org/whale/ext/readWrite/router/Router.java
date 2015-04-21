package org.whale.ext.readWrite.router;

import javax.sql.DataSource;

/**
 * 路由选择
 * 
 * @author 王金绍
 *
 */
public interface Router {

	DataSource route(DataSource writeDataSource, DataSource[] readDataSources, String[] readDataSourceNames);
	
}
