package org.whale.system.server.youboo.context;

import org.whale.system.server.youboo.domain.AppSession;
import org.whale.system.server.youboo.domain.ClientVersion;

public class YoubooContext {
	
	public static final String THREAD_KEY = "infContext";
	
	private boolean isLoginKey;
	
	private String uri;

	private String reqSecure;
	
	private String resSecure;
	
	private String signKey;
	
	private AppSession appSession;
	
	private ClientVersion clientVersion;
	
	private ReqParam reqParam;
	
	private Object argument;
	
	private String reqStr;
	
	private String resStr;

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

	public AppSession getAppSession() {
		return appSession;
	}

	public void setAppSession(AppSession appSession) {
		this.appSession = appSession;
	}

	public ClientVersion getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(ClientVersion clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Object getArgument() {
		return argument;
	}

	public void setArgument(Object argument) {
		this.argument = argument;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
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

	
	public boolean getIsLoginKey() {
		return isLoginKey;
	}

	public void setIsLoginKey(boolean isLoginKey) {
		this.isLoginKey = isLoginKey;
	}

	@Override
	public String toString() {
		return "InfContext [isLoginKey=" + isLoginKey + ", uri=" + uri
				+ ", reqSecure=" + reqSecure + ", resSecure=" + resSecure
				+ ", signKey=" + signKey + ", appSession=" + appSession
				+ ", clientVersion=" + clientVersion + ", reqParam=" + reqParam
				+ ", argument=" + argument + ", reqStr=" + reqStr + ", resStr="
				+ resStr + "]";
	}
	
	
}
