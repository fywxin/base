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
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.roleId=?");
		
		return this.query(strb.toString(), roleId);
	}
	
	public List<UserRole> getByUserId(Long userId){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.userId=?");
		
		return this.query(strb.toString(), userId);
	}
	
	public List<Role> queryRoleByUserId(Long userId){
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT ").append(this.ormContext.getOrmTable(Role.class).getSqlColPrexs()).append(" ")
			.append("FROM sys_role t, sys_user_role ur ")
			.append("WHERE t.roleId = ur.roleId ")
			.append("    AND ur.userId = ? ")
			.append(" order by t.roleId ");
		
		return this.queryOther(Role.class, strb.toString(), userId);
		
		//return this.jdbcTemplate.query(strb.toString(), new Object[]{userId}, this.ormContext.getRowMapper(Role.class));
	}
	
	public List<User> queryUsersByRoleId(Long roleId){
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT ").append(this.ormContext.getOrmTable(User.class).getSqlColPrexs()).append(" ")
			.append("FROM sys_user t, sys_user_role ur ")
			.append("WHERE t.userId = ur.userId ")
			.append("    AND ur.roleId = ? ")
			.append(" order by t.userId ");
		
		return this.queryOther(User.class, strb.toString(), roleId);
		
		//return this.jdbcTemplate.query(strb.toString(), new Object[]{roleId}, this.ormContext.getRowMapper(User.class));
	}
	
	public List<User> queryUsersByRoleCode(String roleCode){
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT ").append(this.ormContext.getOrmTable(User.class).getSqlColPrexs()).append(" ")
			.append("FROM sys_user t, sys_user_role ur, sys_role r ")
			.append("WHERE t.userId = ur.userId ")
			.append("    AND ur.roleId = r.roleId ")
			.append("    AND r.roleCode = ?")
			.append(" order by t.userId ");
		
		return this.queryOther(User.class, strb.toString(), roleCode);
		
		//return this.jdbcTemplate.query(strb.toString(), new Object[]{roleCode}, this.ormContext.getRowMapper(User.class));
	}
	
	public void deleteByRoleId(Long roleId){
		UserRole ur = new UserRole();
		ur.setRoleId(roleId);
		
		this.deleteBy(ur);
		
//		String sql = "delete from "+this.getTableName()+" where roleId=?";
//		this.jdbcTemplate.update(sql, roleId);
	}
	
	public void deleteByUserId(Long userId){
		UserRole ur = new UserRole();
		ur.setUserId(userId);
		
		this.deleteBy(ur);
//		String sql = "delete from "+this.getTableName()+" where userId=?";
//		this.jdbcTemplate.update(sql, userId);
	}
}
