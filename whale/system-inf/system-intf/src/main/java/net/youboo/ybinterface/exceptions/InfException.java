package net.youboo.ybinterface.exceptions;

import net.youboo.ybinterface.constant.ErrorCode;

public class InfException extends RuntimeException{
	private String code;
	
	private static final long serialVersionUID = 1L;
	
	

	public InfException(String message) {
		super(message);
	}
	/**
	 * 业务异常
	 */
	public InfException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InfException(String code, String message) {
		super(message);
		this.code = code;
	}

	
	public InfException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public InfException(ErrorCode error){
		super(error.getDescripter());
		this.code = error.getCode();
	}
	
	public InfException(ErrorCode error , Throwable cause){
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
