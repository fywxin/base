package org.whale.system.inf;

/**
 * 公共异常编码
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:37:35
 */
public enum ErrorCode {

	SUCCESS("0000", "处理成功"),
	INF_SYSTEM_ERROR("1000", "接口系统错误"),
	ENCRYPT_ERROR("1001", "密文错误"),
	AUTH_ERROR("1002", "权限错误"),
	DATA_ERROR("1003", "请求数据错误"),
	PARAM_ERROR("1004", "请求参数错误"),
	SIGN_ERROR("1005", "签名错误"),
	UNKNOW_ERROR("1006", "未知错误"),
	SESSION_INVAIAL("1010", "Session过期");
	
	private String code;
	private String descripter;

	private ErrorCode(String code, String descripter) {
		this.code = code;
		this.descripter = descripter;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescripter() {
		return this.descripter;
	}
}
