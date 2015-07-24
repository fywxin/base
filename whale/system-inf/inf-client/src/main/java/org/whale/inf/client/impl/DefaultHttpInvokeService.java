package org.whale.inf.client.impl;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.whale.inf.client.HttpInvokeService;
import org.whale.inf.client.RpcContext;

@Component
public class DefaultHttpInvokeService implements HttpInvokeService{
	
	@Override
	public boolean match(RpcContext rpcContext) {
		return true;
	}

	public Map<String, String> header(RpcContext rpcContext){
		
		return null;
	}
	
	public String encode(Object[] args, Class<?>[] paramTypes){
		
		return "";
	}
	
	public Object decode(String datas){
		return null;
	}

	
}
