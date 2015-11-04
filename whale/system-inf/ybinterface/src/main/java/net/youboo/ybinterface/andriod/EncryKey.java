package net.youboo.ybinterface.andriod;

public class EncryKey {
	
	//登录加密密钥
	private String loginKey;
	//用户名加密
	private String userNameKey;
	
	private String signKey;

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public String getUserNameKey() {
		return userNameKey;
	}

	public void setUserNameKey(String userNameKey) {
		this.userNameKey = userNameKey;
	}
}
