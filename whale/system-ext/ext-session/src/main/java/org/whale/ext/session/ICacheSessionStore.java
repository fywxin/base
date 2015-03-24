package org.whale.ext.session;

import java.util.Set;

public interface ICacheSessionStore {

	void putSession(String id, CacheSession session, Integer expTime);
	
	CacheSession getSession(String id);
	
	void removeSession(String id);
	
	Set<String> getSessionIds();
}
