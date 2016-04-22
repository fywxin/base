package org.whale.system.common.exception;

/**
 * 用户未登入系统
 *
 * @author wjs
 * 2014年9月6日-下午1:29:01
 */
public class NotLoginException extends BusinessException {

	private static final long serialVersionUID = 23231L;

	public NotLoginException() {
		super("未登录");
	}
	
	/**
	 * 用户未登入系统
	 */
	public NotLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 用户未登入系统
	 */
	public NotLoginException(String message) {
		super(message);
	}
}
