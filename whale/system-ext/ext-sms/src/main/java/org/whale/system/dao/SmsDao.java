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
		String sql = this.sqlHead()+"where t.status = ? "+this.sqlOrder();
		
		if(size != null && size > 0){
			sql+=" LIMIT 0, "+size;
		}
		
		return this.query(sql, Sms.STATUS_SEND_WAITing);
	}
	
}