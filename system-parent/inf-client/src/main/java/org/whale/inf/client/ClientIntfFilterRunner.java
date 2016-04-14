package org.whale.inf.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;


public class ClientIntfFilterRunner implements InitializingBean{

	@Autowired(required=false)
	private List<ClientIntfFilter> filters;
	
	public void exeBefore(ClientContext clientContext){
		if(filters != null){
			for(ClientIntfFilter filter : filters){
				filter.before(clientContext);
			}
		}
	}
	
	public void exeOnRequest(ClientContext clientContext){
		if(filters != null){
			for(ClientIntfFilter filter : filters){
				filter.onReqest(clientContext);
			}
		}
	}
	
	public void exeAfter(ClientContext clientContext) {
		if(filters != null){
			for(int i=filters.size()-1; i>=0; i--){
				filters.get(i).after(clientContext);
			}
		}
	}
	
	public void exeException(ClientContext clientContext, Exception e) {
		if(filters != null){
			for(int i=filters.size()-1; i>=0; i--){
				filters.get(i).exception(clientContext, e);
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
