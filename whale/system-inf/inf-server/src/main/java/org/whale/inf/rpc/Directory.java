package org.whale.inf.rpc;

import java.util.List;

/**
 * 资源目录
 * @author Administrator
 *
 */
public interface Directory<T> extends Node {

	Class<T> getInterface();
	
	/**
	 * 获取所有满足条件的执行者
	 * @param invocation
	 * @return
	 * @throws RpcException
	 */
	List<Invoker<T>> list(Invocation invocation) throws RpcException;
}
