package org.whale.system.server.adapter;

public class WspzContext {

	public static final String THREAD_KEY = "WspzContext";
	
	//请求uri地址
	private String uri;
	
	//请求客户端appid
	private String appId;
	
	//请求签名
	private String sign;
	
	//客户端分配的签名key
	private String signKey;
	
	//请求对象
	private Object argument;
	
	//请求字符串
	private String reqStr;
	
	//响应字符串
	private String resStr;
	
	//响应对象
	private Object rs;
	
	//流水号
	private String reqno;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public Object getArgument() {
		return argument;
	}

	public void setArgument(Object argument) {
		this.argument = argument;
	}

	public String getReqStr() {
		return reqStr;
	}

	public void setReqStr(String reqStr) {
		this.reqStr = reqStr;
	}

	public String getResStr() {
		return resStr;
	}

	public void setResStr(String resStr) {
		this.resStr = resStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Object getRs() {
		return rs;
	}

	public void setRs(Object rs) {
		this.rs = rs;
	}
	
	public String getReqno() {
		return reqno;
	}

	public void setReqno(String reqno) {
		this.reqno = reqno;
	}

	@Override
	public String toString() {
		return "WspzContext [uri=" + uri + ", appId=" + appId + ", sign="
				+ sign + ", signKey=" + signKey + ", argument=" + argument
				+ ", reqStr=" + reqStr + ", resStr=" + resStr + ", rs=" + rs
				+ "]";
	}
}
