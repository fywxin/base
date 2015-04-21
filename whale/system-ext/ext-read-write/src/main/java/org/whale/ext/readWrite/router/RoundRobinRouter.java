package org.whale.ext.readWrite.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

/**
 * 轮询路由选择
 * 
 * 存在慢的提供者累积请求问题
 * @author Administrator
 *
 */
public class RoundRobinRouter extends AbstractRouter {
	
	private AtomicInteger counter = new AtomicInteger(1);

	@Override
	public int doSelect(DataSource writeDataSource, List<DataSource> readDataSources, List<String> readDataSourceNames) {
		int index = counter.incrementAndGet() % readDataSources.size();
        if(index < 0) {
            index = - index;
        }

        return index;
	}

}
