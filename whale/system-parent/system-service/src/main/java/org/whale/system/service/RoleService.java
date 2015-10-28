package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.dao.RoleAuthDao;
import org.whale.system.dao.RoleDao;
import org.whale.system.dao.UserRoleDao;
import org.whale.system.domain.Role;
import org.whale.system.domain.RoleAuth;
import org.whale.system.jdbc.IOrmDao;

@Service
public class RoleService extends BaseService<Role, Long> {
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleAuthDao roleAuthDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	
	public void transDelete(List<Long> roleIds) {
		if(roleIds == null || roleIds.size() < 1){
			return ;
		}
		for(Long roleId : roleIds){
			this.delete(roleId);
			this.roleAuthDao.deleteByRoleId(roleId);
			this.userRoleDao.deleteByRoleId(roleId);
		}
		
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
	
	public void transSaveRoleAuths(Long roleId, String[] authCodes){
		if(roleId == null)
			throw new SysException("roleId == null");
		
		this.roleAuthDao.deleteByRoleId(roleId);
		
		if(authCodes != null && authCodes.length > 0){
			RoleAuth roleAuth = null;
			for(String authCode : authCodes){
				roleAuth = new RoleAuth();
				roleAuth.setAuthCode(authCode);
				roleAuth.setRoleId(roleId);
				this.roleAuthDao.save(roleAuth);
			}
		}
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
