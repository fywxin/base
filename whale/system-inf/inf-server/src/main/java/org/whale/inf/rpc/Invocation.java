package org.whale.inf.rpc;

/**
 * RPC 请求参数对象
 * 
 * @author Administrator
 *
 */
public interface Invocation extends Attachment{

	String getMethodName();
	
	Class<?>[] getParameterTypes();
	
	Object[] getArguments();
	
}
