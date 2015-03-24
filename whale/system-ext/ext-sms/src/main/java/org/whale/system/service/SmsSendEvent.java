package org.whale.system.service;

import org.springframework.core.PriorityOrdered;
import org.whale.system.domain.Sms;

/**
 * 短信发送时间
 *
 * @author 王金绍
 * 2014年10月16日-下午3:57:54
 */
public interface SmsSendEvent extends PriorityOrdered{

	/**
	 * 发送前事件
	 * @param sms
	 * @return
	 */
	boolean beforeSend(Sms sms);
	
	/**
	 * 发送完成事件
	 * @param sms
	 */
	void afterSend(Sms sms);
	
	/**
	 * 是否匹配
	 * @param sms
	 * @return
	 */
	boolean match(Sms sms);
	
}
