package org.whale.system.common.exception;

/**
 * 短信异常
 *
 * @author 王金绍
 * 2014年10月15日-下午5:54:22
 */
public class SmsException extends SysException {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信异常
	 */
	public SmsException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * 短信异常
	 */
	public SmsException(String message) {
		super(message);
	}
}
