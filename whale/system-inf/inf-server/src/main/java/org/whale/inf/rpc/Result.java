package org.whale.inf.rpc;

/**
 * RPC结果
 * @author Administrator
 *
 */
public interface Result extends Attachment{

	Object getValue();
	
	Throwable getException();
	
	boolean hasException();
	
}
