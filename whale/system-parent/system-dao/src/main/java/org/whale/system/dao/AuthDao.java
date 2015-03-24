package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Page;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.Auth;

@Repository
public class AuthDao extends BaseDao<Auth, Long> {
	
	@Override
	protected void createPageSql(Page page) {
		StringBuilder strb = new StringBuilder();
		strb.append(" FROM ").append(this.getOrmTable().getTableDbName()).append(" t")
			.append(" WHERE 1=1 ");
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
		
	}

	public List<Auth> getByMenuId(Long menuId){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.menuId= ?").append(menuId);
		
		return this.query(strb.toString(), menuId);
	}
	
	public List<Auth> getByRoleId(Long roleId){
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT a.* FROM sys_role_auth ra, ").append(this.getTableName()).append(" a ")
			.append("WHERE ra.roleId = ? ")
			.append("AND a.authId = ra.authId");
		
		return this.query(strb.toString(), roleId);
	}
	
	public List<Auth> getByAuthIds(List<Long> authIds){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.authId in(").append(LangUtil.joinIds(authIds)).append(")");
		
		return this.query(strb.toString());
	}
	
	public Auth getByAuthCode(String authCode){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.authCode=?");
		
		return this.getObject(strb.toString(), authCode);
	}
	
	/**
	 * 获取当前用户的权限数据
	 * @param userId
	 * @return
	 */
	final String getByUserId_SQL = "SELECT t.* FROM sys_auth t,"
			+ "(SELECT ra.authId FROM sys_role_auth ra, sys_role r WHERE r.status=1 and r.roleId=ra.roleId and ra.roleId in(SELECT ur.roleId FROM sys_user_role ur WHERE ur.userId =?)) ura "
			+ "WHERE ura.authId = t.authId ";
	public List<Auth> getByUserId(Long userId){
		
		return this.query(getByUserId_SQL, userId);
	}
}
