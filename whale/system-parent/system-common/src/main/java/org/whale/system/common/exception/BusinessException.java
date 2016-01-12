package org.whale.system.common.exception;

/**
 * 业务异常
 *
 * @author wjs
 * 2014年9月29日-上午11:42:44
 */
public class BusinessException extends BaseException {

	private static final long serialVersionUID = 23231L;
	
	/**
	 * 业务异常
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 业务异常
	 */
	public BusinessException(String message) {
		super(message);
	}
	
	/**
	 * 优化性能
	 * http://www.blogjava.net/stone2083/archive/2010/07/09/325649.html
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
