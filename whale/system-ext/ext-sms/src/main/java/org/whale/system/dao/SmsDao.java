package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Cmd;
import org.whale.system.domain.Sms;

@Repository
public class SmsDao extends BaseDao<Sms, Long> {

	/**
	 * 获取待发送的短信记录
	 * @param size
	 * @return
	 */
	public List<Sms> queryWaitSendSmss(Integer size){
		
		return this.query(Cmd.newCmd(Sms.class).eq("status", Sms.STATUS_SEND_WAITing).limit(0, size));
	}
	
}