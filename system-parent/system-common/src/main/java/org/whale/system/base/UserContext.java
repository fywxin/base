package org.whale.system.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.whale.system.common.exception.NotLoginException;
import org.whale.system.common.util.ThreadContext;

public class UserContext implements Serializable{

	private static final long serialVersionUID = -349058234001L;
	
	public static UserContext get(){
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if (uc == null){
			throw new NotLoginException();
		}
		return uc;
	}

	public static UserContext getOrNull(){
		return (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
	}

	public static String userName(){
		return get().getUserName();
	}

	public static String userNameOrNull(){
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if (uc == null){
			return null;
		}
		return uc.getUserName();
	}

	public static Long userId(){
		return get().getUserId();
	}

	public static Long userIdOrNull(){
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if (uc == null){
			return null;
		}
		return uc.getUserId();
	}

	public static String realName(){
		return get().getRealName();
	}

	public static String realNameOrNull(){
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if (uc == null){
			return null;
		}
		return uc.getRealName();
	}

	public static boolean isAdmin(){
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if (uc == null){
			return false;
		}
		return uc.isSuperAdmin();
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
