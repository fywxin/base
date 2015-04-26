package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.RoleAuthDao;
import org.whale.system.dao.RoleDao;
import org.whale.system.dao.UserRoleDao;
import org.whale.system.domain.Role;
import org.whale.system.domain.RoleAuth;
import org.whale.system.domain.UserRole;
import org.whale.system.jdbc.IOrmDao;

@Service
public class RoleService extends BaseService<Role, Long> {
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleAuthDao roleAuthDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	
	@Override
	public void delete(Long roleId) {
		super.delete(roleId);
		
//		this.roleAuthDao.deleteByRoleId(roleId);
		RoleAuth ra= new RoleAuth();
		ra.setRoleId(roleId);
		this.roleAuthDao.deleteBy(ra);
		
//		this.userRoleDao.deleteByRoleId(roleId);
		UserRole ur = new UserRole();
		ur.setRoleId(roleId);
		this.userRoleDao.deleteBy(ur);
		
	}
	
	public void updateStatus(Long roleId, Integer status){
		Role role = this.get(roleId);
		if(role == null){
			throw new SysException("找不到角色ID=["+roleId+"]的对象");
		}
		role.setStatus(status);
		LangUtil.trim(role);
		this.roleDao.update(role);
	}
	
	public void saveRoleAuths(Long roleId, List<Long> authIds){
		if(roleId == null)
			throw new SysException("roleId == null");
		
		//this.roleAuthDao.deleteByRoleId(roleId);
		RoleAuth ra= new RoleAuth();
		ra.setRoleId(roleId);
		this.roleAuthDao.deleteBy(ra);
		
		if(authIds != null && authIds.size() > 0){
			RoleAuth roleAuth = null;
			for(Long authId : authIds){
				//TODO check duplicate
				roleAuth = new RoleAuth();
				roleAuth.setAuthId(authId);
				roleAuth.setRoleId(roleId);
				this.roleAuthDao.save(roleAuth);
			}
		}
	}

	public Role getByRoleCode(String roleCode){
		if(Strings.isBlank(roleCode))
			return null;
		return this.roleDao.getByRoleCode(roleCode.trim());
	}
	
	public List<Role> getByUserId(Long userId){
		if(userId == null)
			return null;
		return this.roleDao.getByUserId(userId);
	}

	@Override
	public IOrmDao<Role, Long> getDao() {
		return roleDao;
	}

}
