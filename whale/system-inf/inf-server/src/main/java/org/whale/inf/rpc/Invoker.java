package org.whale.inf.rpc;

/**
 * 服务执行者
 * 
 * @author Administrator
 */
public interface Invoker<T> extends Node{
	
	Result invoke(Invocation invocation) throws RpcException;
	
	Class<T> getInterface();
}
