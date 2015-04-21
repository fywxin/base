package org.whale.ext.readWrite.router;

import java.util.Map;

import javax.sql.DataSource;

import org.whale.ext.readWrite.ReadWriteDataSourceDecision;
import org.whale.system.common.util.SpringContextHolder;

/**
 * 指定路由，比如userService的操作只能使用路由1， deptService的操作只能使用路由2，非指定情况下，使用最少活跃调用数规则
 * 
 * @author 王金绍
 */
public class SignRouter  extends AbstractRouter {
	
	private Map<String, DataSource> signMap;
	
	private RoundRobinRouter pollRouter;
	
	private Object lock = new Object();

	@Override
	public DataSource doSelect(DataSource writeDataSource, DataSource[] readDataSources, String[] readDataSourceNames) {
		DataSource dataSource = signMap.get(ReadWriteDataSourceDecision.getFromClass());
		if(dataSource != null)
			return dataSource;
			
		if(pollRouter == null){
			synchronized (lock) {
				if(pollRouter == null){
					try{
						pollRouter = SpringContextHolder.getBean(RoundRobinRouter.class);
					}catch(Exception e){
						pollRouter = new RoundRobinRouter();
					}
				}
			}
		}
		return pollRouter.route(writeDataSource, readDataSources, readDataSourceNames);
	}

	public Map<String, DataSource> getSignMap() {
		return signMap;
	}

	public void setSignMap(Map<String, DataSource> signMap) {
		this.signMap = signMap;
	}

}
