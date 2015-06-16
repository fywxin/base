package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.common.util.LangUtil;
import org.whale.system.domain.Auth;

@Repository
public class AuthDao extends BaseDao<Auth, Long> {
	

	public List<Auth> getByMenuId(Long menuId){
		Auth auth = this.newT();
		auth.setMenuId(menuId);
		
		return this.query(auth);
	}
	
	String getByRoleId_SQL = "SELECT a.* FROM sys_role_auth ra, sys_auth a WHERE ra.roleId = ? AND a.authId = ra.authId ";
	public List<Auth> getByRoleId(Long roleId){
		
		return this.query(getByRoleId_SQL, roleId);
	}
	
	public List<Auth> getByAuthIds(List<Long> authIds){
		String sql = this.sqlHead()+" where t.authId in("+LangUtil.joinIds(authIds)+")"+this.sqlOrder();
		
		return this.query(sql);
	}
	
	public Auth getByAuthCode(String authCode){
		Auth auth = new Auth();
		auth.setAuthCode(authCode);
		
		return this.getObject(auth);
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
