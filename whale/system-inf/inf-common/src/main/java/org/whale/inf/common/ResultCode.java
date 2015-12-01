package org.whale.inf.common;

/**
 * 公共异常编码
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:37:35
 */
public enum ResultCode {

	SUCCESS("000", "success"),
	INF_SYSTEM_ERROR("100", "接口框架内部异常"),
	MUST_POST_ERROR("110", "非POST请求"),
	REQ_DATA_ERROR("120", "请求数据错误"),
	DATA_ENCODE_ERROR("130", "数据编码错误"),
	ENCRYPT_ERROR("140", "密文错误"),
	SIGN_ERROR("150", "签名错误"),
	AUTH_ERROR("160", "权限错误"),
	FIELD_VALID_ERROR("200", "字段参数校验错误"),//客户端需要处理
	SESSION_INVAIAL("300", "Session过期"),//客户端需要处理
	UNKNOW_ERROR("999", "未知错误");
	
	private String code;
	private String msg;
	
	
	private ResultCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return this.code;
	}

	public String getMsg() {
		return msg;
	}

}
