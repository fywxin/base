package org.whale.inf.client.chain.impl;

import org.springframework.stereotype.Component;
import org.whale.inf.client.RpcContext;
import org.whale.inf.client.chain.AbstractHttpInvoke;

@Component
public class RootHttpInvoke extends AbstractHttpInvoke {

	@Override
	public void invoke(RpcContext context) {
		invokeNext(context);
	}

}
