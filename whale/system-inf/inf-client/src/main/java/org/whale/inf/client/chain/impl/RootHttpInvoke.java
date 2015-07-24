package org.whale.inf.client.chain.impl;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.inf.client.HttpInvokeService;
import org.whale.inf.client.RpcContext;
import org.whale.inf.client.chain.AbstractHttpInvoke;
import org.whale.inf.client.impl.DefaultHttpInvokeService;

@Component
public class RootHttpInvoke extends AbstractHttpInvoke implements InitializingBean{
	
	@Autowired(required=false)
	private List<HttpInvokeService> httpInvokeServices;
	@Autowired
	private DefaultHttpInvokeService defaultHttpInvokeService;

	@Override
	public void invoke(RpcContext rpcContext) {
		HttpInvokeService httpInvokeService = defaultHttpInvokeService;
		if(httpInvokeServices != null && httpInvokeServices.size() > 0){
			for(HttpInvokeService invokeService : httpInvokeServices){
				if(invokeService.match(rpcContext)){
					httpInvokeService = invokeService;
					break;
				}
			}
		}
		httpInvokeService.header(rpcContext);
		String body = httpInvokeService.encode(rpcContext.getArgs(), rpcContext.getParamTypes());
		rpcContext.setArgsStr(body);
		
		invokeNext(rpcContext);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(httpInvokeServices != null){
			httpInvokeServices.remove(defaultHttpInvokeService);
		}
	}

}
