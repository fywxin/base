package org.whale.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.base.BaseDao;
import org.whale.system.base.BaseService;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.DictDao;
import org.whale.system.dao.DictItemDao;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;

@Service
public class DictService extends BaseService<Dict, Long> {

	@Autowired
	private DictDao dictDao;
	@Autowired
	private DictItemDao dictItemDao;
	@Autowired
	private DictCacheService dictCacheService;
	
	@Override
	public void delete(Long dictId){
		this.dictDao.delete(dictId);
		
		DictItem dictItem = new DictItem();
		dictItem.setDictId(dictId);
		this.dictItemDao.deleteBy(dictItem);
	}
	
	public Dict getByDictCode(String dictCode){
		if(Strings.isBlank(dictCode))
			return null;
		return this.dictDao.getByDictCode(dictCode.trim());
	}
	
	@Override
	public BaseDao<Dict, Long> getDao() {
		return dictDao;
	}
	
}
