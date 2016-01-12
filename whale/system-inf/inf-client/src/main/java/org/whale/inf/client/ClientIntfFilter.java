package org.whale.inf.client;

import org.springframework.core.PriorityOrdered;

/**
 * 客户端自定义处理器
 * 
 * @author wjs
 * 2015年11月8日 上午12:20:02
 */
public interface ClientIntfFilter extends PriorityOrdered {
	
	/**
	 * 执行任务前调用
	 * @param clientContext
	 */
	public void before(ClientContext clientContext);
	
	/**
	 * 执行HTTP REQUEST 请求时调用
	 * @param clientContext
	 */
	public void onReqest(ClientContext clientContext);
	
	/**
	 * 执行任务完成后调用
	 * @param clientContext
	 */
	public void after(ClientContext clientContext);
	
	/**
	 * 
	 * @param clientContext
	 */
	public void exception(ClientContext clientContext, Exception e);
	
}
