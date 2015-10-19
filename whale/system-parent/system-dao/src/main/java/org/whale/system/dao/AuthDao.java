package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Cmd;
import org.whale.system.domain.Auth;

@Repository
public class AuthDao extends BaseDao<Auth, Long> {
	

	public List<Auth> getByMenuId(Long menuId){
		
		return this.query(Cmd.newCmd(Auth.class).eq("menuId", menuId));
	}
	
	String getByRoleId_SQL = "SELECT a.* FROM sys_role_auth ra, sys_auth a WHERE ra.roleId = ? AND a.authId = ra.authId ";
	public List<Auth> getByRoleId(Long roleId){
		
		return this.query(getByRoleId_SQL, roleId);
	}
	
	public List<Auth> getByAuthIds(List<Long> authIds){
		
		return this.query(this.cmd().and("authId", "in", authIds));
	}
	
	public Auth getByAuthCode(String authCode){
		
		return this.get(this.cmd().eq("authCode", authCode));
	}
	
	/**
	 * 获取当前用户的权限数据
	 * @param userId
	 * @return
	 */
	final String getByUserId_SQL = "SELECT t.* FROM sys_auth t,"
			+ "(SELECT ra.authId FROM sys_role_auth ra, sys_role r WHERE r.status=1 and r.roleId=ra.roleId and ra.roleId in(SELECT ur.roleId FROM sys_user_role ur WHERE ur.userId =?)) ura "
			+ "WHERE ura.authId = t.authId ";
	public List<Auth> getByUserId(Long userId){
		
		return this.query(getByUserId_SQL, userId);
	}
}
