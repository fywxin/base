package org.whale.inf.client.chain;

import org.whale.inf.client.RpcContext;

public abstract class AbstractHttpInvoke implements HttpInvoke {
	
	private HttpInvoke httpInvoke;

	@Override
	public void setNextHttpInvoke(HttpInvoke httpInvoke) {
		this.httpInvoke = httpInvoke;
	}

	public void invokeNext(RpcContext context){
		if(httpInvoke != null){
			httpInvoke.invoke(context);
		}
	}
}
