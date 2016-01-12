package org.whale.system.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;


/**
 * session 监听器
 *
 * @author wjs
 * 2014年9月6日-下午3:11:03
 */
@Component
public class MySessionListener implements HttpSessionListener  {
	
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		MySessionContext.addSession(httpSessionEvent.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		MySessionContext.delSession(session);
	}


}
