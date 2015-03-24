package org.whale.ext.dao;

import org.springframework.stereotype.Repository;
import org.whale.ext.domain.Domain;
import org.whale.system.base.BaseDao;
@Repository
public class DomainDao extends BaseDao<Domain, Long> {

	public Domain getByName(String name){
		StringBuilder strb = this.getSqlHead();
		strb.append(" AND t.name = ?");
		
		return this.getObject(strb.toString(), name);
	}
	
	public Domain getByClazzName(String clazzName){
		StringBuilder strb = this.getSqlHead();
		strb.append(" AND t.clazzName = ?");
		
		return this.getObject(strb.toString(), clazzName);
	}
}
