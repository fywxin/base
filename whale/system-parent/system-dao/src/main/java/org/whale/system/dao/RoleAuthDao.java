package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Query;
import org.whale.system.domain.RoleAuth;

@Repository
public class RoleAuthDao extends BaseDao<RoleAuth, Long> {

	public List<RoleAuth> getByRoleId(Long roleId){
		
		return this.queryBy(Query.newQuery(RoleAuth.class).addEq("roleId", roleId));
	}
		
	public List<RoleAuth> getByAuthId(Long authId){
		
		return this.queryBy(Query.newQuery(RoleAuth.class).addEq("authId", authId));
	}
	
	public void deleteByRoleId(Long roleId){
		
		this.deleteBy(Query.newQuery(RoleAuth.class).addEq("roleId", roleId));
	}
	
	public void deleteByAuthId(Long authId){
		
		this.deleteBy(Query.newQuery(RoleAuth.class).addEq("authId", authId));
	}
}
