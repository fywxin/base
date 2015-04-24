package org.whale.ext.session;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.util.SpringContextHolder;

/**
 * session缓存保存器
 *
 * @author 王金绍
 * @date 2015年1月16日 上午10:22:07
 */
@Component
public class CacheSessionStoreImpl implements ICacheSessionStore {
	
	public static final String SESSION_CACHE_NAME = "SES_NaMe";

	@Autowired
	private ICacheService<CacheSession> cacheService;

	@Override
	public void putSession(String id, CacheSession session, Integer expTime) {
		this.cacheService.put(SESSION_CACHE_NAME, id, session, expTime * 60000);
	}

	@Override
	public CacheSession getSession(String id) {
		return (CacheSession)this.cacheService.get(SESSION_CACHE_NAME, id);
	}

	@Override
	public void removeSession(String id) {
		this.cacheService.del(SESSION_CACHE_NAME, id);
	}

	@Override
	public Set<String> getSessionIds() {
		return this.cacheService.getKeys(SESSION_CACHE_NAME);
	}

	public void setCacheService(ICacheService<CacheSession> cacheService) {
		this.cacheService = cacheService;
	}
	
	/**
	 * 获取当前对象
	 * @date 2015年1月16日 上午10:22:00
	 */
	public static ICacheSessionStore getThis(){
		return SpringContextHolder.getBean(CacheSessionStoreImpl.class);
	}
}
