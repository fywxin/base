package org.whale.system.common.exception;

/**
 * orm 异常
 *
 * @author 王金绍
 * 2014年9月6日-下午1:28:20
 */
public class OrmException extends BaseException {
	private static final long serialVersionUID = 1L;

	public OrmException(String msg){
		super(msg);
	}

	public OrmException(String msg, Exception cause){
		super(msg, cause);
	}
}
