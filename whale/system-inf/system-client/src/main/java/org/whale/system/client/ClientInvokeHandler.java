package org.whale.system.client;

/**
 * 客户端自定义处理器
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:20:02
 */
public interface ClientInvokeHandler {
	
	public void beforeCall(ClientContext clientContext);
	
	public void afterCall(ClientContext clientContext);

}
