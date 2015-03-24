package org.whale.system.common.exception;

/**
 * 系统异常
 *
 * @author 王金绍
 * 2014年9月6日-下午1:28:45
 */
public class SysException extends BaseException {
	private static final long serialVersionUID = 23231L;
	
	/**
	 * 系统异常
	 */
	public SysException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * 系统异常
	 */
	public SysException(String message) {
		super(message);
	}
}
