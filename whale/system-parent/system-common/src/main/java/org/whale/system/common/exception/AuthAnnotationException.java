package org.whale.system.common.exception;

/**
 * @Auth 权限定义异常
 * 
 * @author 王金绍
 * @date 2014年12月23日 下午9:38:16
 */
public class AuthAnnotationException extends SysException {

	private static final long serialVersionUID = 23231L;
	
	/**
	 * 权限定义异常
	 */
	public AuthAnnotationException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * 权限定义异常
	 */
	public AuthAnnotationException(String message) {
		super(message);
	}
}
