package org.whale.inf.common;

/**
 * 接口运行时异常
 * 
 * @author wjs
 * @date 2015年12月1日 下午3:00:38
 */
public class InfException extends RuntimeException {

	private static final long serialVersionUID = 12323242L;	
	
	//错误编码
	private String code;
	
	//附加信息
	private Object data;
	
	public InfException(String message) {
		super(message);
	}
	
	public InfException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InfException(String code, String message){
		super(message);
		this.code = code;
	}
	
	public InfException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public InfException(ResultCode error){
		super(error.getMsg());
		this.code = error.getCode();
	}
	
	public InfException(ResultCode error, Throwable cause){
		super(error.getMsg(), cause);
		this.code = error.getCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
