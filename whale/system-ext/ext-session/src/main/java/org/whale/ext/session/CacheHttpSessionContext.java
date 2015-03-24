package org.whale.ext.session;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class CacheHttpSessionContext implements HttpSessionContext {

	@Override
	public Enumeration<String> getIds() {
		return new StringEnumeration(CacheSessionStoreImpl.getThis().getSessionIds().iterator());
	}

	@Override
	public HttpSession getSession(String id) {
		return CacheSessionStoreImpl.getThis().getSession(id);
	}

}
