package net.youboo.ybinterface.service;

import net.youboo.ybinterface.dao.AppSessionDao;
import net.youboo.ybinterface.domain.AppSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.service.BaseService;

@Service
public class AppSessionService extends BaseService<AppSession, Long>{
	
	@Autowired
	private AppSessionDao appSessionDao;
	
	@Autowired(required=false)
	private ICacheService<AppSession> cacheService;
	
	/**
	 * 
	 * @param appSession
	 */
	public void save(AppSession appSession){
		this.appSessionDao.save(appSession);
		if(this.cacheService != null){
			this.cacheService.put(AppSession.CACHE_KEY, appSession.getSessionId(), appSession, AppSession.CACHE_EXPRIE_TIME);
		}
	}
	
	/**
	 * TODO 
	 * @param sessionId
	 * @return
	 */
	public AppSession getBySessionId(String sessionId){
		if(Strings.isBlank(sessionId)){
			return null;
		}
		AppSession appSession = null;
		if(this.cacheService != null){
			appSession = this.cacheService.get(AppSession.CACHE_KEY, sessionId);
		}
		if(appSession != null && appSession.getDeadTime() < System.currentTimeMillis() && new Integer(1).equals(appSession.getStatus())){
			return appSession;
		}else{
			appSession = this.appSessionDao.getBySessionId(sessionId);
			if(this.cacheService != null && appSession != null){
				this.cacheService.put(AppSession.CACHE_KEY, appSession.getSessionId(), appSession, AppSession.CACHE_EXPRIE_TIME);
			}
			return appSession;
		}
	}
	

	@Override
	public IOrmDao<AppSession, Long> getDao() {
		return appSessionDao;
	}

}
