package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.RoleAuth;

@Repository
public class RoleAuthDao extends BaseDao<RoleAuth, Long> {

	public List<RoleAuth> getByRoleId(Long roleId){
		RoleAuth roleAuth = this.newT();
		roleAuth.setRoleId(roleId);
		return this.query(roleAuth);
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.roleId=?");
//		
//		return this.query(strb.toString(), roleId);
	}
		
	public List<RoleAuth> getByAuthId(Long authId){
		RoleAuth roleAuth = this.newT();
		roleAuth.setAuthId(authId);
		return this.query(roleAuth);
//		
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.authId=?");
//		
//		return this.query(strb.toString(), authId);
	}
	
//	public void deleteByRoleId(Long roleId){
//		String sql = "delete from "+this.getTableName()+" where roleId=?";
//		this.jdbcTemplate.update(sql, roleId);
//	}
//	
//	public void deleteByAuthId(Long authId){
//		String sql = "delete from "+this.getTableName()+" where authId=?";
//		this.jdbcTemplate.update(sql, authId);
//	}
}
