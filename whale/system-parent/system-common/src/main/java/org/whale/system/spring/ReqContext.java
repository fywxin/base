package org.whale.system.spring;

public class ReqContext {

	private String reqSecure;
	
	private String resSecure;
	
	private ReqParam reqParam;

	public String getReqSecure() {
		return reqSecure;
	}

	public void setReqSecure(String reqSecure) {
		this.reqSecure = reqSecure;
	}

	public String getResSecure() {
		return resSecure;
	}

	public void setResSecure(String resSecure) {
		this.resSecure = resSecure;
	}

	public ReqParam getReqParam() {
		return reqParam;
	}

	public void setReqParam(ReqParam reqParam) {
		this.reqParam = reqParam;
	}
	
	
}
