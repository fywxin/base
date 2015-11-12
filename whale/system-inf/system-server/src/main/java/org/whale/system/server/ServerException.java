package org.whale.system.server;

import org.whale.system.common.exception.BaseException;
import org.whale.system.inf.ErrorCode;

/**
 * 服务端异常
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:18:05
 */
public class ServerException extends BaseException{
	private static final long serialVersionUID = 832478231L;
	
	private String code;
	
	public ServerException(String message) {
		super(message);
	}
	
	public ServerException(Throwable cause) {
		super(cause);
	}
	/**
	 * 业务异常
	 */
	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServerException(String code, String message) {
		super(message);
		this.code = code;
	}

	
	public ServerException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public ServerException(ErrorCode error){
		super(error.getDescripter());
		this.code = error.getCode();
	}
	
	public ServerException(ErrorCode error , Throwable cause){
		super(error.getDescripter(), cause);
		this.code = error.getCode();
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
