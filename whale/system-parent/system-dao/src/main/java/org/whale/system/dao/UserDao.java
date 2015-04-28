package org.whale.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Page;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.User;

@Repository
public class UserDao extends BaseDao<User, Long> {
	
	public List<User> getByDeptId(Long deptId){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.deptId=? order by t.userId");
		
		return this.query(strb.toString(), deptId);
	}

	public User getByUserName(String userName){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.userName=?");
		
		return this.getObject(strb.toString(), userName);
	}
	
	final String findAuthIds_SQL ="select ra.authId "+
			"from sys_user_role ur, sys_role_auth ra, sys_role r "+
			"where ur.userId=? "+
			"and ur.roleId = ra.roleId "+
			"and r.roleId = ra.roleId "+
			"and r.status = 1";
	public List<Long> findAuthIds(Long userId){
		
		List<Map<String, Object>> list = this.queryForList(findAuthIds_SQL, userId);
		if(list == null || list.size() < 1)
			return null;
		
		List<Long> rs = new ArrayList<Long>(list.size());
		for(Map<String, Object> map : list){
			rs.add(Long.parseLong(map.get("authId").toString()));
		}
		return rs;
	}

	@Override
	protected void createPageSql(Page page) {
		StringBuilder strb = new StringBuilder();
		strb.append(" FROM ").append(getOrmTable().getTableDbName()).append(" t WHERE t.isAdmin=0 ");
		if(Strings.isNotBlank(page.getParamStr("userName"))){
			strb.append(" AND t.userName like ?");
			page.addArg("%"+page.getParamStr("userName")+"%");
		}
		if(Strings.isNotBlank(page.getParamStr("realName"))){
			strb.append(" AND t.realName like ?");
			page.addArg("%"+page.getParamStr("realName")+"%");
		}
		if(page.getParamInteger("deptId") != null){
			strb.append(" AND t.deptId=?");
			page.addArg(page.getParamInteger("deptId"));
		}
		
		page.setCountSql("SELECT count(1) "+strb.toString());
		page.setSql("SELECT t.*, (select d.deptName from sys_dept d where d.id = t.deptId) as deptName "+strb.toString()+" ORDER BY t.userId");
	}
	
	
}
