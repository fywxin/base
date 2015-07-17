package org.whale.system.common.exception;

/**
 * 客户端接口异常
 * 
 * @author 王金绍
 * @date 2015年7月16日 下午2:59:34
 */
public class InfClientException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public InfClientException(String msg){
		super(msg);
	}
	
	public InfClientException(Throwable cause){
		super(cause);
	}

	public InfClientException(String msg, Throwable cause){
		super(msg, cause);
	}

}
