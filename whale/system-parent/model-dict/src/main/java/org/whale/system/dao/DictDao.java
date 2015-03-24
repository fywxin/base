package org.whale.system.dao;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.Dict;


@Repository
public class DictDao extends BaseDao<Dict, Long> {

	public Dict getByDictCode(String dictCode){
		StringBuilder strb = this.getSqlHead();
		strb.append("and t.dictCode=?");
		return this.getObject(strb.toString(), dictCode);
	}
}
