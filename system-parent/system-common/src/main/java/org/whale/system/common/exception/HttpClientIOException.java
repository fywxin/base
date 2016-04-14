package org.whale.system.common.exception;

/**
 * 
 * @author Administrator
 *
 */
public class HttpClientIOException extends HttpClientException {
	private static final long serialVersionUID = 403802348091231L;

	public HttpClientIOException(String msg){
		super(msg);
	}
	
	public HttpClientIOException(Throwable cause){
		super(cause);
	}

	public HttpClientIOException(String msg, Throwable cause){
		super(msg, cause);
	}
}
