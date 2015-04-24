package org.whale.system.common.exception;

public class CacheException extends SysException {
	private static final long serialVersionUID = -121221L;

	public CacheException(String msg){
		super(msg);
	}

	public CacheException(String msg, Throwable cause){
		super(msg, cause);
	}
}
