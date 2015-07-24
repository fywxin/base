package org.whale.inf.client.chain;

import org.whale.inf.client.RpcContext;

public interface HttpInvoke {

	public void invoke(RpcContext rpcContext);
	
	public void setNextHttpInvoke(HttpInvoke httpInvoke);
}
