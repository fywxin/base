package org.whale.inf.rpc;

/**
 * 过滤器
 * @author Administrator
 *
 */
public interface Filter {

	/**
	 * <code>
	 * // before filter
     * Result result = invoker.invoke(invocation);
     * // after filter
     * return result;
     * </code>
	 * 
	 * @param invoker
	 * @param invocation
	 * @return
	 * @throws RpcException
	 */
	Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;
	
	Filter getNextFilter();
	
}
