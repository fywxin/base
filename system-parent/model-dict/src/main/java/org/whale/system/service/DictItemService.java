package org.whale.system.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;
import org.whale.system.base.BaseCrudEvent;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.DictDao;
import org.whale.system.dao.DictItemDao;
import org.whale.system.domain.DictItem;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.service.event.DictItemEvent;

@Service
public class DictItemService extends BaseService<DictItem, Long> {
	
	@Autowired
	private DictDao dictDao;
	@Autowired
	private DictItemDao dictItemDao;
	@Autowired(required = false)
	private List<DictItemEvent> list;
	
	private boolean sort = false;
	
	
	@Override
	public void save(DictItem dictItem) {
		this.fireEvent(dictItem, BaseCrudEvent.BEFORE_SAVE);
		super.save(dictItem);
		this.fireEvent(dictItem, BaseCrudEvent.AFTER_SAVE);
	}

	@Override
	public void update(DictItem dictItem) {
		this.fireEvent(dictItem, BaseCrudEvent.BEFORE_UPDATE);
		super.update(dictItem);
		this.fireEvent(dictItem, BaseCrudEvent.AFTER_UPDATE);
	}

	@Override
	public void delete(Long id) {
		DictItem dictItem = this.get(id);
		this.fireEvent(dictItem, BaseCrudEvent.BEFORE_DEL);
		super.delete(id);
		this.fireEvent(dictItem, BaseCrudEvent.AFTER_DEL);
	}

	public Integer getCurOrder(Long dictId){
		if(dictId == null)
			return null;
		Integer order = this.dictItemDao.getCurOrder(dictId);
		if(order == null)
			return 0;
		return order;
	}
	
	public DictItem getByDictIdAndItemCode(Long dictId, String itemCode) {
		if(dictId == null || Strings.isBlank(itemCode))
			return null;
		
		return this.dictItemDao.getByDictIdAndItemCode(dictId, itemCode);
	}
	
	public List<DictItem> getByDictId(Long dictId){
		if(dictId == null)
			return null;
		return this.dictItemDao.getByDictId(dictId);
	}

	@Override
	public IOrmDao<DictItem, Long> getDao() {
		return dictItemDao;
	}
	
	
	private void fireEvent(DictItem dictItem, int EventType){
		this.fireEvent(dictItem, EventType, null);
	}
	
	private void fireEvent(DictItem dictItem, int EventType, Object addOn){
		if(list == null || list.size() == 0)
			return ;
		if(!sort){
			Collections.sort(list, new OrderComparator());
			sort = true;
		}
		
		for(DictItemEvent dictItemEvent : list){
			switch (EventType) {
			case BaseCrudEvent.BEFORE_SAVE:
				dictItemEvent.onBeforeCreate(dictItem);
				break;
			case BaseCrudEvent.AFTER_SAVE:
				dictItemEvent.onAfterCreate(dictItem);
				break;
			case BaseCrudEvent.BEFORE_UPDATE:
				dictItemEvent.onBeforeUpdate(dictItem);
				break;
			case BaseCrudEvent.AFTER_UPDATE:
				dictItemEvent.onAfterUpdate(dictItem);
				break;
			case BaseCrudEvent.BEFORE_DEL:
				dictItemEvent.onBeforeDelete(dictItem);
				break;
			case BaseCrudEvent.AFTER_DEL:
				dictItemEvent.onAfterDelete(dictItem);
				break;
			case DictItemEvent.BEFORE_SETSTATUS:
				dictItemEvent.onBeforeSetStatus(dictItem, (Integer)addOn);
				break;
			case DictItemEvent.AFTER_SETSTATUS:
				dictItemEvent.onAfterSetStatus(dictItem);
				break;	
			default:
				break;
			}
		}
	}
}
