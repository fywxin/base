package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.dao.AuthDao;
import org.whale.system.dao.RoleAuthDao;
import org.whale.system.domain.Auth;
import org.whale.system.jdbc.IOrmDao;

@Service
public class AuthService extends BaseService<Auth, String> {
	
	@Autowired
	private AuthDao authDao;
	@Autowired
	private RoleAuthDao roleAuthDao;

	public void transDelete(String[] authCodeS) {
		for(String authCode : authCodeS){
			this.roleAuthDao.deleteByAuthCode(authCode);
			this.authDao.delete(authCode);
		}
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
	
	public List<String> queryAuthCodeByUserId(Long userId){
		if(userId == null){
			return null;
		}
		return this.authDao.queryAuthCodeByUserId(userId);
	}
	
	public List<Auth> queryByUserId(Long userId){
		if(userId == null){
			return null;
		}
		return this.authDao.queryByUserId(userId);
	}

	@Override
	public IOrmDao<Auth, String> getDao() {
		return authDao;
	}

}
