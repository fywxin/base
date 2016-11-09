package org.whale.system.jdbc.readwrite.router;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.whale.system.spring.SpringContextHolder;

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
	public String doSelect(List<DataSource> readDsList, List<String> readDsKeyList) {
		DataSource dataSource = signMap.get(DynamicDataSourceDecision.getFromClass());
		if(dataSource != null)
			return readDsKeyList.get(readDsList.indexOf(dataSource));
			
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
		return pollRouter.doSelect( readDsList, readDsKeyList);
	}

	public Map<String, DataSource> getSignMap() {
		return signMap;
	}

	public void setSignMap(Map<String, DataSource> signMap) {
		this.signMap = signMap;
	}

}
