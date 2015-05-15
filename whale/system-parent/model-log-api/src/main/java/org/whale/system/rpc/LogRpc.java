package org.whale.system.rpc;

import java.util.List;

import org.whale.system.base.Page;
import org.whale.system.domain.Log;

public interface LogRpc {
	
	void save(List<Log> logs);
	
	Page queryLogPage(Page page);
	
	Log get(String objId);
}
