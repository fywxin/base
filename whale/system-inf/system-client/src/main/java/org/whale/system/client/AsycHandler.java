package org.whale.system.client;

/**
 * 异步回调处理
 * 占不支持
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:43:34
 */
public interface AsycHandler {

	void onSuccess(ClientContext context);
	
	void onFail(ClientContext context, ClientException ex);
}
