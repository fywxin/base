package org.whale.system.auth.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.whale.system.common.exception.AuthAnnotationException;
import org.whale.system.common.util.Strings;

/**
 * 权限标签实体
 * 
 * @author 王金绍
 * @date 2014年12月23日 下午9:50:26
 */
public class AuthBean {
	
	//系统注册的权限 <authCode, AuthBean>
	public static final Map<String, AuthBean> AUTH_HOLDER = new HashMap<String, AuthBean>();

	private String authCode;
	
	private String authName;
	//类#方法
	private Set<String> methods;
	
	/**
	 * 放入权限实体
	 * @param auth
	 * @date 2014年12月23日 下午9:50:19
	 */
	public static void put(AuthBean auth){
		if(Strings.isBlank(auth.getAuthCode())){
			throw new AuthAnnotationException("@Auth异常:权限编码不能为空method["+auth.getMethods()+"]");
		}
		
		AuthBean other = AUTH_HOLDER.get(auth.getAuthCode().trim());
		if(other == null){
			AUTH_HOLDER.put(auth.getAuthCode().trim(), auth);
		}else{
			//TODO 检查方法范围是否static private 等
			if(auth.getMethods() == null || auth.getMethods().size() < 1){
				throw new AuthAnnotationException("@Auth异常:权限编码["+auth.getAuthCode()+"] 对应方法不能为空！");
			}
			if(Strings.isBlank(other.getAuthName())){
				other.setAuthName(auth.getAuthName());
			}
			
			for(String method : auth.getMethods()){
				if(!other.getMethods().contains(method)){
					other.getMethods().add(method);
				}
			}
		}
	}
	
	public static AuthBean get(String key){
		return AUTH_HOLDER.get(key);
	}
	
	public static void clear(){
		AUTH_HOLDER.clear();
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Set<String> getMethods() {
		return methods;
	}

	public void setMethods(Set<String> methods) {
		this.methods = methods;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	@Override
	public String toString() {
		return "AuthBean [authCode=" + authCode + ", authName=" + authName
				+ ", methods=" + methods + "]";
	}
	
}
