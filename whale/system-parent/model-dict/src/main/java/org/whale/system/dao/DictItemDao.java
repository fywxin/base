package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.DictItem;

@Repository
public class DictItemDao extends BaseDao<DictItem, Long> {
	
	public Integer getCurOrder(Long dictId){
		String sql = "select max(orderNo) from "+this.getTableName()+" where dictId=?";
		return this.jdbcTemplate.queryForInt(sql, dictId);
	}
	
	public DictItem getByDictIdAndItemCode(Long dictId, String itemCode){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.dictId = ? ")
			.append("and t.itemCode=?");
		
		return this.getObject(strb.toString(), dictId, itemCode);
	}
	
	public List<DictItem> getByDictId(Long dictId){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.dictId = ?");
		return this.query(strb.toString(), dictId);
	}
}
