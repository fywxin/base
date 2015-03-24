package org.whale.system.common.exception;

/**
 * 用户未登入系统
 *
 * @author 王金绍
 * 2014年9月6日-下午1:29:01
 */
public class NotLoginException extends BusinessException {

	private static final long serialVersionUID = 23231L;
	
	/**
	 * 用户未登入系统
	 */
	public NotLoginException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * 用户未登入系统
	 */
	public NotLoginException(String message) {
		super(message);
	}
}
