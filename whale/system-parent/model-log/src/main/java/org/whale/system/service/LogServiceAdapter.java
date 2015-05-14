package org.whale.system.service;

import org.whale.system.base.Page;
import org.whale.system.domain.Log;
import org.whale.system.rpc.LogRpc;

public class LogServiceAdapter {
	
	private LogRpc logRpc;

	public void queryPage(Page page) {
		this.logRpc.queryPage(page);
	}

	public Log get(String objId) {
		return this.logRpc.get(objId);
	}

	public void setLogRpc(LogRpc logRpc) {
		this.logRpc = logRpc;
	}
	
	
}
