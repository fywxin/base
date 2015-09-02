package org.whale.system.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Query;
import org.whale.system.domain.Role;
import org.whale.system.domain.User;
import org.whale.system.domain.UserRole;

@Repository
public class UserRoleDao extends BaseDao<UserRole, Long> {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;

	public List<UserRole> getByRoleId(Long roleId){
		
		return this.queryBy(Query.newQuery(UserRole.class).addEq("roleId", roleId));
	}
	
	public List<UserRole> getByUserId(Long userId){
		
		return this.queryBy(Query.newQuery(UserRole.class).addEq("userId", userId));
	}
	
	final String queryRoleByUserId_SQL = "SELECT t.* FROM sys_role t, sys_user_role ur WHERE t.roleId = ur.roleId  AND ur.userId = ?  order by t.roleId";
	public List<Role> queryRoleByUserId(Long userId){
		return this.roleDao.query(queryRoleByUserId_SQL, userId);
		//return this.queryOther(Role.class, queryRoleByUserId_SQL, userId);
	}
	
	final String queryUsersByRoleId_SQL = "SELECT t.* FROM sys_user t, sys_user_role ur WHERE t.userId = ur.userId AND ur.roleId = ?  order by t.userId";
	public List<User> queryUsersByRoleId(Long roleId){
		return this.userDao.query(queryUsersByRoleId_SQL, roleId);
		//return this.queryOther(User.class, queryUsersByRoleId_SQL, roleId);
	}
	
	final String queryUsersByRoleCode_SQL = "SELECT t.* FROM sys_user t, sys_user_role ur, sys_role r WHERE t.userId = ur.userId  AND ur.roleId = r.roleId AND r.roleCode = ? order by t.userId";
	public List<User> queryUsersByRoleCode(String roleCode){
		return this.userDao.query(queryUsersByRoleCode_SQL, roleCode);
		//return this.queryOther(User.class, queryUsersByRoleCode_SQL, roleCode);
	}
	
	public void deleteByRoleId(Long roleId){
		
		this.deleteBy(Query.newQuery(UserRole.class).addEq("roleId", roleId));
	}
	
	public void deleteByUserId(Long userId){
		
		this.deleteBy(Query.newQuery(UserRole.class).addEq("userId", userId));
	}
}
