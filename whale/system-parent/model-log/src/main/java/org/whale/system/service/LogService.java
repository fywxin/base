package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.base.IbaseDao;
import org.whale.system.base.Page;
import org.whale.system.dao.LogDao;
import org.whale.system.domain.Log;
import org.whale.system.rpc.LogRpc;

public class LogService extends BaseService<Log, String> implements LogRpc {
	
	@Autowired
	private LogDao logDao;

	@Override
	public IbaseDao<Log, String> getDao() {
		return this.logDao;
	}


	@Override
	public void save(List<Log> logs) {
		this.logDao.saveBatch(logs);
	}


	@Override
	public Page queryLogPage(Page page) {
		this.queryPage(page);
		return page;
	}

}
