package net.youboo.ybinterface.andriod;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReqParam implements Serializable{
	
	private static final long serialVersionUID = 3982374981L;
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private String appKey;
	
	private String method;
	
	private String session;
	
	private String timestamp;
	
	private String reqno;
	
	private String format;
	
	private String version;
	
	private String gzip;
	
	//是否加密
	private String encrypt;
	
	private String signMethod;
	
	private String sign;
	
	public ReqParam(){}
	
	public ReqParam(String appKey, String reqno, String version){
		this.appKey = appKey;
		this.timestamp = sdf.format(new Date());
		this.reqno = reqno;
		this.version = version;
	}
	
	public ReqParam(String appKey, String reqno, String version, String session) {
		this.appKey = appKey;
		this.timestamp = sdf.format(new Date());
		this.reqno = reqno;
		this.version = version;
		this.session = session;
	}
	
	public String formatUrlStr(String sign){
		StringBuilder strb = new StringBuilder();
		strb.append("app_key=").append(this.appKey)
			.append("&method=").append(this.method == null ? "" : this.method)
			.append("&session=").append(this.session == null ? "" : this.session)
			.append("&timestamp=").append(this.timestamp)
			.append("&reqno=").append(this.reqno)
			.append("&format=").append(this.format == null ? "" : this.format)
			.append("&version=").append(this.version)
			.append("&gzip=").append(this.gzip == null ? "" : gzip)
			.append("&encrypt=").append(this.encrypt == null ? "" : encrypt)
			.append("&sign_method=").append(this.signMethod == null ? "" : this.signMethod)
			.append("&sign=").append(sign);
		return strb.toString();
	}
	
	/**
	 * 获取接口参数待签名字符串
	 * @return
	 */
	public String buildSignStr(){
		StringBuilder strb = new StringBuilder();
		strb.append(appKey)
			.append(method == null ? "" : method)
			.append(session == null ? "" : session)
			.append(timestamp)
			.append(reqno)
			.append(format == null ? "" : format)
			.append(version)
			.append(gzip == null ? "" : gzip)
			.append(encrypt == null ? "" : encrypt)
			.append(signMethod == null ? "" : signMethod);
		return strb.toString();
	}

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

	@Override
	public String toString() {
		return "ReqParam [appKey=" + appKey + ", method=" + method
				+ ", session=" + session + ", timestamp=" + timestamp
				+ ", reqno=" + reqno + ", format=" + format + ", version="
				+ version + ", gzip=" + gzip + ", encrypt=" + encrypt
				+ ", signMethod=" + signMethod + ", sign=" + sign + "]";
	}
	
}
