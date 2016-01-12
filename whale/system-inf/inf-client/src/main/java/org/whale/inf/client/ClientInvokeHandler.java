package org.whale.inf.client;

/**
 * 客户端自定义处理器
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:20:02
 */
public interface ClientInvokeHandler {
	
	/**
	 * 执行任务前调用
	 * @param clientContext
	 */
	public void beforeCall(ClientContext clientContext);
	
	/**
	 * 执行HTTP REQUEST 请求时调用
	 * @param clientContext
	 */
	public void onReqest(ClientContext clientContext);
	
	/**
	 * 执行任务完成后调用
	 * @param clientContext
	 */
	public void afterCall(ClientContext clientContext);
	
}
