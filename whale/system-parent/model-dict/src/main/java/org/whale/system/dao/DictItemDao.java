package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Cmd;
import org.whale.system.domain.DictItem;

@Repository
public class DictItemDao extends BaseDao<DictItem, Long> {
	
	public Integer getCurOrder(Long dictId){
		String sql = "select max(orderNo) from sys_dict_item where dictId=?";
		return this.queryForInt(sql, dictId);
	}
	
	public DictItem getByDictIdAndItemCode(Long dictId, String itemCode){
		
		return this.getBy(Cmd.newCmd(DictItem.class).and("dictId", dictId).and("itemCode", itemCode));
	}
	
	public List<DictItem> getByDictId(Long dictId){
//		String sql = this.sqlHead() + " where t.dictId = ? " + this.sqlOrder();
//		return this.query(sql, dictId);
		
		return this.queryBy(Cmd.newCmd(DictItem.class).and("dictId", dictId));
	}
}
