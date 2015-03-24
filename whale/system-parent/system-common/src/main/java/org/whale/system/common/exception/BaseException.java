package org.whale.system.common.exception;

/**
 * 基础异常类
 *
 * @author 王金绍
 * 2014年9月18日-下午4:20:25
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 23231L;
	
	/**
	 * 系统异常
	 */
	public BaseException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * 系统异常
	 */
	public BaseException(String message) {
		super(message);
	}
	
	
}
