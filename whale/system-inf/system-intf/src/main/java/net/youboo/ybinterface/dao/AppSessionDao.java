package net.youboo.ybinterface.dao;

import net.youboo.ybinterface.domain.AppSession;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;

@Repository
public class AppSessionDao extends BaseDao<AppSession, Long> {

	public AppSession getBySessionId(String sessionId){
		return this.get(this.cmd().eq("sessionId", sessionId).eq("status", 1).and("deadTime", ">", System.currentTimeMillis()));
	}
}
