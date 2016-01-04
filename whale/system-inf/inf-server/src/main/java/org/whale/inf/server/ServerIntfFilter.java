package org.whale.inf.server;

import org.springframework.core.PriorityOrdered;

/**
 * 
 * @author Administrator
 *
 */
public interface ServerIntfFilter extends PriorityOrdered {

	public void beforeReq(ServerContext serverContext);
	
	public void afterReq(ServerContext serverContext);
	
	public void beforeResp(ServerContext serverContext);
	
	public void afterResp(ServerContext serverContext);
	
	public void exception(ServerContext serverContext, Exception e);
}
