package org.whale.system.server.youboo.dao;


import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.server.youboo.domain.AppSession;

@Repository
public class AppSessionDao extends BaseDao<AppSession, Long> {

	public AppSession getBySessionId(String sessionId){
		return this.get(this.cmd().eq("sessionId", sessionId).eq("status", 1).and("deadTime", ">", System.currentTimeMillis()));
	}
}
