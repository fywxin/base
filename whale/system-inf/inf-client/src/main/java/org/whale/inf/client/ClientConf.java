package org.whale.inf.client;

/**
 * 客户端应用配置
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:35:26
 */
public class ClientConf {
	
	//应用ID， 服务端分配
	private String appId;
	
	//应用签名字符串， 服务端分配
	private String signKey;
	
	//服务端主机地址
	private String serverHost;
	
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
	
	

}
