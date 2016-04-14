package org.whale.system.service.event;

import org.springframework.core.PriorityOrdered;
import org.whale.system.base.BaseCrudEvent;
import org.whale.system.domain.User;


/**
 * 用户事件监听接口
 * @author wjs
 * @date 2013-12-17 上午11:35:29 
 *
 */
public interface UserEvent extends BaseCrudEvent<User>, PriorityOrdered{
	
	static final int BEFORE_SETSTATUS = 7;
	
	static final int AFTER_SETSTATUS = 8;
    
    /**
     * 在设置用户状态之前调用这个方法
     * @param user 用户对象
     */
    void onBeforeSetStatus(User user, Integer status);

    /**
     * 在设置用户状态之后调用这个方法
     * @param user 用户对象
     */
    void onAfterSetStatus(User user);
}
