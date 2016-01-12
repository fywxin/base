package org.whale.system.auth.domain;

import java.util.HashMap;
import java.util.HashSet;
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
	//建立类与权限的关系
	public static final Set<Class<?>> CONTROLLER_Set= new HashSet<Class<?>>();

	private String authCode;
	
	private String authName;
	//类#方法
	private Set<String> methods;
	//所属类
	private Class<?> controller;
	
	
	
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
		CONTROLLER_Set.add(auth.getController());
	}
	
	public static AuthBean get(String key){
		return AUTH_HOLDER.get(key);
	}
	
	public static void clear(){
		CONTROLLER_Set.clear();
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
		return "[" + authCode + " | " + authName+ " | "+controller.getName()+ "]";
	}

	public Class<?> getController() {
		return controller;
	}

	public void setController(Class<?> controller) {
		this.controller = controller;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authCode == null) ? 0 : authCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthBean other = (AuthBean) obj;
		if (authCode == null) {
			if (other.authCode != null)
				return false;
		} else if (!authCode.equals(other.authCode))
			return false;
		return true;
	}
	
	
}
