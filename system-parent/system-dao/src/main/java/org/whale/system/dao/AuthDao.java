package org.whale.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Cmd;
import org.whale.system.domain.Auth;

@Repository
public class AuthDao extends BaseDao<Auth, String> {
	

	public List<Auth> getByMenuId(Long menuId){
		
		return this.query(Cmd.newCmd(Auth.class).eq("menuId", menuId));
	}
	
	String getByRoleId_SQL = "SELECT a.* FROM sys_role_auth ra, sys_auth a WHERE ra.roleId = ? AND a.authCode = ra.authCode ";
	public List<Auth> getByRoleId(Long roleId){
		
		return this.query(getByRoleId_SQL, roleId);
	}
	
	
	/**
	 * 获取用户权限编码集合
	 * 
	 * @param userId
	 * @return
	 */
	final String getByUserId_SQL = "SELECT DISTINCT(ra.authCode) as authCode FROM sys_role_auth ra where ra.roleId in(SELECT ur.roleId FROM sys_user_role ur where ur.userId = ?)";
	public List<String> queryAuthCodeByUserId(Long userId){
		List<Map<String, Object>> authCodes = this.queryForList(getByUserId_SQL, userId);
		if(authCodes == null || authCodes.size() < 1){
			return null;
		}
		List<String> rs = new ArrayList<String>(authCodes.size());
		for(Map<String, Object> map : authCodes){
			rs.add((String)map.get("authCode"));
		}
		return rs;
	}
	
	final String SQL = "SELECT t.* FROM sys_auth t where t.authCode in( SELECT DISTINCT(ra.authCode) as authCode FROM sys_role_auth ra where ra.roleId in(SELECT ur.roleId FROM sys_user_role ur where ur.userId = ?))";
	public List<Auth> queryByUserId(Long userId){
		return this.query(SQL, userId);
	}
}
