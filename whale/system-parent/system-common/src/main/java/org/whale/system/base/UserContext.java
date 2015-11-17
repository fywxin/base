package org.whale.system.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.whale.system.common.util.ThreadContext;

public class UserContext implements Serializable{

	private static final long serialVersionUID = -349058234001L;
	
	public static UserContext get(){
		return (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
	}
	
	public static void set(UserContext uc){
		ThreadContext.getContext().put(ThreadContext.KEY_USER_CONTEXT, uc);
	}

	private Long userId;
	
	private String userName;
	
	private String realName;
	
	private String sessionId;
	
	private String ip;
	
	private Date loginTime;
	
	private Integer userType;
	
	private Map<String, Object> customDatas = new HashMap<String, Object>();
	
	private boolean isSuperAdmin;
	

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Map<String, Object> getCustomDatas() {
		return customDatas;
	}

	public void setCustomDatas(Map<String, Object> customDatas) {
		this.customDatas = customDatas;
	}
	
}
