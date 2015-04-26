package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.AuthDao;
import org.whale.system.dao.RoleAuthDao;
import org.whale.system.domain.Auth;
import org.whale.system.domain.RoleAuth;
import org.whale.system.jdbc.IOrmDao;

@Service
public class AuthService extends BaseService<Auth, Long> {
	
	@Autowired
	private AuthDao authDao;
	@Autowired
	private RoleAuthDao roleAuthDao;

	@Override
	public void delete(Long authId) {
		if(authId == null){
			return ;
		}
		//this.roleAuthDao.deleteByAuthId(authId);
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setAuthId(authId);
		this.roleAuthDao.deleteBy(roleAuth);
		
		//this.resourceDao.deleteByAuthId(authId);
		
		this.authDao.delete(authId);
	}

	@Override
	public Auth get(Long authId) {
		if(authId == null)
			return null;
		return this.authDao.get(authId);
	}
	
	public Auth getByAuthCode(String authCode, boolean loadResource){
		if(Strings.isBlank(authCode))
			return null;
		return this.authDao.getByAuthCode(authCode);
	}
	
	public List<Auth> getByMenuId(Long menuId){
		if(menuId == null)
			return null;
		return this.authDao.getByMenuId(menuId);
	}
	
	public List<Auth> getByRoleId(Long roleId){
		if(roleId == null)
			return null;
		return this.authDao.getByRoleId(roleId);
	}
	
	public List<Auth> getByAuthIds(List<Long> authIds){
		if(authIds == null || authIds.size() < 1)
			return null;
		return this.authDao.getByAuthIds(authIds);
	}
	
	public List<Auth> getByUserId(Long userId){
		if(userId == null){
			return null;
		}
		return this.authDao.getByUserId(userId);
	}
	

	@Override
	public IOrmDao<Auth, Long> getDao() {
		return authDao;
	}

}
