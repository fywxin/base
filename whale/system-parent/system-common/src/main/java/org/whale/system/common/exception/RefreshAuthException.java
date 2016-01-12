package org.whale.system.common.exception;

/**
 * 系统正在刷新权限缓存异常
 *
 * @author 王金绍
 * 2014年9月19日-下午5:09:48
 */
public class RefreshAuthException extends SysException {

	private static final long serialVersionUID = 23231L;
	
	public RefreshAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public RefreshAuthException(String message) {
		super(message);
	}
}
