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

	private String login_name;
	
	private String password;
	
	/** 0: 家长版  1：教师版  2：园长版 */
	private Integer login_type;

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getLogin_type() {
		return login_type;
	}

	public void setLogin_type(Integer login_type) {
		this.login_type = login_type;
	}
	
	
}
