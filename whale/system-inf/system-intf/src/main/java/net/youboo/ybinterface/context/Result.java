package net.youboo.ybinterface.context;

import net.youboo.ybinterface.constant.ErrorCode;

public class Result<T> {
	
	public static final String SUCCESS_CODE = "0000";
	
	public static final String SUCCESS_MSG = "success";
	
	@SuppressWarnings("all")
	public static Result<?> success(){
		return new Result();
	}
	
	public static <M> Result<M> success(M data){
		Result<M> rs = new Result<M>();
		rs.setData(data);
		rs.setMessage("success");
		return rs;
	}
	
	public static <M> Result<M> fail(ErrorCode errorCode){
		Result<M> rs = new Result<M>();
		rs.setCode(errorCode.getCode());
		rs.setMessage(errorCode.getDescripter());
		return rs;
	}
	
	public static <M> Result<M> fail(String code, String message){
		Result<M> rs = new Result<M>();
		rs.setCode(code);
		rs.setMessage(message);
		return rs;
	}

	/** 成功 */
	private String code = SUCCESS_CODE;
	
	private String message = SUCCESS_MSG;
	
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result [" + code + ": " + message + "\ndata="
				+ data + "]";
	}
	
	
}
