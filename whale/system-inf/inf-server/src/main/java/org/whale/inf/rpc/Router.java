package org.whale.inf.rpc;

import java.util.List;
import java.util.Map;

public interface Router extends Comparable<Router> {
	
	Map<String, Object> getConf();

	<T> List<Invoker<T>> route(List<Invoker<T>> invokers, Invocation invocation, Map<String, Object> conf) throws RpcException;
}
