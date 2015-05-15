package org.whale.system.service;

import org.whale.system.base.Page;
import org.whale.system.domain.Log;
import org.whale.system.rpc.LogRpc;

public class LogServiceAdapter {
	
	private LogRpc logRpc;

	public Page queryLogPage(Page page) {
		return this.logRpc.queryLogPage(page);
	}

	public Log get(String objId) {
		return this.logRpc.get(objId);
	}

	public void setLogRpc(LogRpc logRpc) {
		this.logRpc = logRpc;
	}
	
	
}
