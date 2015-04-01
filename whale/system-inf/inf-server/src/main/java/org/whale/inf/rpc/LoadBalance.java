package org.whale.inf.rpc;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡
 * @author Administrator
 *
 */
public interface LoadBalance {

	<T> Invoker<T> select(List<Invoker<T>> invokers, Invocation invocation, Map<String, Object> conf) throws RpcException;
}
