package org.whale.inf.client;

import java.util.Map;

public interface HttpInvokeService {

	public boolean match(RpcContext rpcContext);
	
	public Map<String, String> header(RpcContext rpcContext);
	
	public String encode(Object[] args, Class<?>[] paramTypes);
	
	public Object decode(String datas);
}
