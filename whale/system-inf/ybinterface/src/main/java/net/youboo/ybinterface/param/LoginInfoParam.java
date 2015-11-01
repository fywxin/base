package net.youboo.ybinterface.param;

import java.io.Serializable;

/**
 * 用户登录
 * 
 * @author 王金绍
 * 2015年11月1日 下午4:36:24
 */
public class LoginInfoParam implements Serializable{

	private static final long serialVersionUID = 1384272389L;

	private String userName;
	
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
