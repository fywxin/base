package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.RoleAuth;

@Repository
public class RoleAuthDao extends BaseDao<RoleAuth, Long> {

	public List<RoleAuth> getByRoleId(Long roleId){
		
		return this.query(this.cmd().eq("roleId", roleId));
	}
		
	public List<RoleAuth> getByAuthId(Long authId){
		
		return this.query(this.cmd().eq("authId", authId));
	}
	
	public void deleteByRoleId(Long roleId){
		
		this.delete(this.cmd().eq("roleId", roleId));
	}
	
	public void deleteByAuthId(Long authId){
		
		this.delete(this.cmd().eq("authId", authId));
	}
}
