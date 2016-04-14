package org.whale.inf.server;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

@Component
public class ServerIntfFilterRunner implements InitializingBean {
	
	@Autowired(required=false)
	private List<ServerIntfFilter> filters;
	
	public void exeBeforeReq(ServerContext serverContext){
		if(filters != null){
			for(ServerIntfFilter filter : filters){
				filter.beforeReq(serverContext);
			}
		}
	}
	
	public void exeAfterReq(ServerContext serverContext){
		if(filters != null){
			for(ServerIntfFilter filter : filters){
				filter.afterReq(serverContext);
			}
		}
	}
	
	public void exeBeforeResp(ServerContext serverContext){
		if(filters != null){
			for(int i=filters.size()-1; i>=0; i--){
				filters.get(i).beforeResp(serverContext);
			}
		}
	}
	
	public void exeAfterResp(ServerContext serverContext){
		if(filters != null){
			for(int i=filters.size()-1; i>=0; i--){
				filters.get(i).afterResp(serverContext);
			}
		}
	}
	
	public void exeException(ServerContext serverContext, Exception e){
		if(filters != null){
			for(int i=filters.size()-1; i>=0; i--){
				filters.get(i).exception(serverContext, e);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(filters != null && filters.size() > 0){
			Collections.sort(filters, new OrderComparator());
		}
	}

}
