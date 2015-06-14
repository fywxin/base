package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.Role;
import org.whale.system.domain.User;
import org.whale.system.domain.UserRole;

@Repository
public class UserRoleDao extends BaseDao<UserRole, Long> {

	public List<UserRole> getByRoleId(Long roleId){
		UserRole userRole = this.newT();
		userRole.setRoleId(roleId);
		return this.getByRoleId(roleId);
		
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.roleId=?");
//		
//		return this.query(strb.toString(), roleId);
	}
	
	public List<UserRole> getByUserId(Long userId){
		UserRole userRole = this.newT();
		userRole.setRoleId(userId);
		return this.getByRoleId(userId);
		
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.userId=?");
//		
//		return this.query(strb.toString(), userId);
	}
	
	final String queryRoleByUserId_SQL = "SELECT t.* FROM sys_role t, sys_user_role ur WHERE t.roleId = ur.roleId  AND ur.userId = ?  order by t.roleId";
	public List<Role> queryRoleByUserId(Long userId){
		
		return this.queryOther(Role.class, queryRoleByUserId_SQL, userId);
	}
	
	final String queryUsersByRoleId_SQL = "SELECT t.* FROM sys_user t, sys_user_role ur WHERE t.userId = ur.userId AND ur.roleId = ?  order by t.userId";
	public List<User> queryUsersByRoleId(Long roleId){
		
		return this.queryOther(User.class, queryUsersByRoleId_SQL, roleId);
	}
	
	final String queryUsersByRoleCode_SQL = "SELECT t.* FROM sys_user t, sys_user_role ur, sys_role r WHERE t.userId = ur.userId  AND ur.roleId = r.roleId AND r.roleCode = ? order by t.userId";
	public List<User> queryUsersByRoleCode(String roleCode){
		
		return this.queryOther(User.class, queryUsersByRoleCode_SQL, roleCode);
	}
	
	public void deleteByRoleId(Long roleId){
		UserRole ur = this.newT();
		ur.setRoleId(roleId);
		
		this.deleteBy(ur);
		
//		String sql = "delete from "+this.getTableName()+" where roleId=?";
//		this.jdbcTemplate.update(sql, roleId);
	}
	
	public void deleteByUserId(Long userId){
		UserRole ur = this.newT();
		ur.setUserId(userId);
		
		this.deleteBy(ur);
//		String sql = "delete from "+this.getTableName()+" where userId=?";
//		this.jdbcTemplate.update(sql, userId);
	}
}
