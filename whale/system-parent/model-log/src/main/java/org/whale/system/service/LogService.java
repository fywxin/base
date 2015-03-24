package org.whale.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.base.BaseDao;
import org.whale.system.base.BaseService;
import org.whale.system.dao.LogDao;
import org.whale.system.domain.Log;

@Service
public class LogService extends BaseService<Log, Long> {
	
	@Autowired
	private LogDao logDao;

	@Override
	public BaseDao<Log, Long> getDao() {
		return this.logDao;
	}

}
