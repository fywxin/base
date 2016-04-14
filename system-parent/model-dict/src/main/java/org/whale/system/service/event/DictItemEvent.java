package org.whale.system.service.event;

import org.springframework.core.PriorityOrdered;
import org.whale.system.base.BaseCrudEvent;
import org.whale.system.domain.DictItem;

/**
 * @author wjs
 * @date 2013-9-18 下午3:10:09 
 */
public interface DictItemEvent extends BaseCrudEvent<DictItem>, PriorityOrdered{

	static final int BEFORE_SETSTATUS = 7;
	
	static final int AFTER_SETSTATUS = 8;
    
    /**
     * 在设置用户状态之前调用这个方法
     * @param dictItem 用户对象
     */
    void onBeforeSetStatus(DictItem dictItem, Integer status);

    /**
     * 在设置用户状态之后调用这个方法
     * @param dictItem 用户对象
     */
    void onAfterSetStatus(DictItem dictItem);
}

