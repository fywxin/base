package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.base.Cmd;
import org.whale.system.base.Page;
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
		this.roleAuthDao.deleteBy(Cmd.newCmd(RoleAuth.class).and("authId", authId));
		
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
	public void queryPage(Page page) {
		StringBuilder strb = new StringBuilder();
		strb.append(" FROM sys_auth t WHERE 1=1 ");
		if(Strings.isNotBlank(page.getParamStr("menuIds"))){
			strb.append(" AND t.menuId in(").append(page.getParam("menuIds")).append(")");
		}
		if(Strings.isNotBlank(page.getParamStr("authName"))){
			strb.append(" AND t.authName like ?");
			page.addArg("%"+page.getParamStr("authName").trim()+"%");
		}
		if(Strings.isNotBlank(page.getParamStr("authCode"))){
			strb.append(" AND t.authCode like ?");
			page.addArg("%"+page.getParamStr("authCode").trim()+"%");
		}
		
		page.setCountSql("SELECT count(1) "+strb.toString());
		page.setSql("SELECT t.*,(select m.menuName from sys_menu m where m.menuId = t.menuId) as menuName "+strb.toString()+" ORDER BY t.authId");
	
		this.roleAuthDao.queryPage(page);
	}

	@Override
	public IOrmDao<Auth, Long> getDao() {
		return authDao;
	}

}
