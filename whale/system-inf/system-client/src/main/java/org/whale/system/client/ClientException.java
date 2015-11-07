package org.whale.system.client;

import org.whale.system.common.exception.BaseException;
import org.whale.system.inf.ErrorCode;

/**
 * 客户端异常
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:19:50
 */
public class ClientException extends BaseException {
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	public ClientException(String message) {
		super(message);
	}
	/**
	 * 业务异常
	 */
	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ClientException(String code, String message) {
		super(message);
		this.code = code;
	}

	
	public ClientException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public ClientException(ErrorCode error){
		super(error.getDescripter());
		this.code = error.getCode();
	}
	
	public ClientException(ErrorCode error , Throwable cause){
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
