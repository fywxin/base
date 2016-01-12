package org.whale.system.common.exception;

/**
 * 乐观锁更新 脏数据异常
 * 
 * @author wjs
 * @date 2015年7月17日 下午4:42:26
 */
public class StaleObjectStateException extends OrmException {
	
	private static final long serialVersionUID = 1L;

	public StaleObjectStateException(String msg){
		super(msg);
	}

	public StaleObjectStateException(String msg, Throwable cause){
		super(msg, cause);
	}
}