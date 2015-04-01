package org.whale.inf.rpc;

public interface Cluster {

	<T> Invoker<T> join(Directory<T> directory) throws RpcException;
	
}
