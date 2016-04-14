package org.whale.system.common.exception;

/**
 * 禁止访问
 *
 * @author wjs
 * 2014年9月6日-下午1:28:54
 */
public class ForbidVisitException extends BusinessException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 禁止访问
	 */
	public ForbidVisitException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 禁止访问
	 */
	public ForbidVisitException(String message) {
		super(message);
	}
}
