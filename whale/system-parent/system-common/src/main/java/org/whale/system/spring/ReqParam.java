package org.whale.system.spring;

import java.io.Serializable;

public class ReqParam implements Serializable{
	
	private static final long serialVersionUID = 3982374981L;
	
	private String appKey;
	
	private String method;
	
	private String session;
	
	private String timestamp;
	
	private String reqno;
	
	private String format;
	
	private String version;
	
	private String gzip;
	
	private String encrypt;
	
	private String signMethod;
	
	private String sign;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getReqno() {
		return reqno;
	}

	public void setReqno(String reqno) {
		this.reqno = reqno;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getGzip() {
		return gzip;
	}

	public void setGzip(String gzip) {
		this.gzip = gzip;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	
}
