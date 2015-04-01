package org.whale.inf.rpc;

import java.util.Map;

/**
 * 代理工厂
 * @author Administrator
 *
 */
public interface ProxyFactory {

	<T> T getProxy(Invoker<T> invoker) throws RpcException;
	
	<T> Invoker<T> getInvoker(T proxy, Class<T> type, Map<String, Object> conf) throws RpcException;
	
}
