package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.Role;

@Repository
public class RoleDao extends BaseDao<Role, Long> {

	public Role getByRoleCode(String roleCode){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.roleCode=?");
		
		return this.getObject(strb.toString(), roleCode);
	}
	
	final String getByUserId_SQL = "SELECT r.* FROM sys_user_role ur, sys_role r WHERE ur.userId = ? AND ur.roleId = r.roleId";
	public List<Role> getByUserId(Long userId){
		
		return this.query(getByUserId_SQL, userId);
	}
}
