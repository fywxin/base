package org.whale.system.dao;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Cmd;
import org.whale.system.domain.Dict;


@Repository
public class DictDao extends BaseDao<Dict, Long> {

	public Dict getByDictCode(String dictCode){
		
		return this.getBy(Cmd.newCmd(Dict.class).and("dictCode", dictCode));
	}
}
