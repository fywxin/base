package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.Sms;

@Repository
public class SmsDao extends BaseDao<Sms, Long> {

	/**
	 * 获取待发送的短信记录
	 * @param size
	 * @return
	 */
	public List<Sms> queryWaitSendSmss(Integer size){
		StringBuilder strb = this.getSqlHead();
		strb.append("	AND t.status = ? ")
			.append(" ORDER BY t.sendRealTime desc, t.createTime desc");
		
		if(size != null && size > 0){
			strb.append(" LIMIT 0, ").append(size);
		}
		
		return this.query(strb.toString(), Sms.STATUS_SEND_WAITing);
	}
	
}