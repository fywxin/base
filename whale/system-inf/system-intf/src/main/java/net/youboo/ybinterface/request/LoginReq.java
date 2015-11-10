package net.youboo.ybinterface.request;

import java.io.Serializable;

import org.whale.system.annotation.jdbc.Validate;

public class LoginReq implements Serializable {

	private static final long serialVersionUID = 23239893911L;

	private String loginName;
	
	private String password;
	
	/** 0: 家长版  1：教师版  2：园长版 */
	private Integer loginType;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
}
