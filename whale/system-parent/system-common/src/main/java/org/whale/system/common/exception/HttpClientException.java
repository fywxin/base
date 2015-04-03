package org.whale.system.common.exception;

public class HttpClientException extends BaseException {
	private static final long serialVersionUID = 4038022348091231L;

	public HttpClientException(String msg){
		super(msg);
	}
	
	public HttpClientException(Throwable cause){
		super(cause);
	}

	public HttpClientException(String msg, Throwable cause){
		super(msg, cause);
	}
}
