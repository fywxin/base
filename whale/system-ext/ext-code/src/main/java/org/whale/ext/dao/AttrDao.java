package org.whale.ext.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.ext.domain.Attr;
import org.whale.system.base.BaseDao;

@Repository
public class AttrDao extends BaseDao<Attr, Long> {
	
	public List<Attr> getByDomainId(Long domainId){
		Attr attr = this.newT();
		attr.setDomainId(domainId);
		
		return this.query(attr);
	}
}
