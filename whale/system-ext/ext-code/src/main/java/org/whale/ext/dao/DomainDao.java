package org.whale.ext.dao;

import org.springframework.stereotype.Repository;
import org.whale.ext.domain.Domain;
import org.whale.system.base.BaseDao;
@Repository
public class DomainDao extends BaseDao<Domain, Long> {

	public Domain getByName(String name){
		Domain t = this.newT();
		t.setName(name);
		
		return this.getObject(t);
	}
	
	public Domain getByClazzName(String clazzName){
		Domain t = this.newT();
		t.setClazzName(clazzName);
		
		return this.getObject(t);
	}
}
