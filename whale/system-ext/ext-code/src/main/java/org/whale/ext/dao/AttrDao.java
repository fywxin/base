package org.whale.ext.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.ext.domain.Attr;
import org.whale.system.base.BaseDao;

@Repository
public class AttrDao extends BaseDao<Attr, Long> {
	
	public List<Attr> getByDomainId(Long domainId){
		StringBuilder strb = this.getSqlHead();
		strb.append(" AND t.domainId = ? ORDER BY t.inOrder");
		
		return this.query(strb.toString(), domainId);
	}
}
