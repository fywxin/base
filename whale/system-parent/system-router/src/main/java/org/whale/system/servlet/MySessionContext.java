package org.whale.system.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.whale.system.base.UserContext;
import org.whale.system.common.constant.SysConstant;


public class MySessionContext {
    private static Map<String, HttpSession> mymap = new ConcurrentHashMap<String, HttpSession>();

    public static void addSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
    }

    public static void delSession(HttpSession session) {
        if (session != null) {
            mymap.remove(session.getId());
        }
    }

    public static HttpSession getSession(String jsessionid) {
        if (jsessionid == null) 
        	return null;
        return (HttpSession) mymap.get(jsessionid);
    }
    
    public static Collection<HttpSession> getAllHttpSessions(){
    	return mymap.values();
    }

    public static List<HttpSession> getSessionByUserId(Long userId){
    	if(mymap == null || mymap.size() < 1)
    		return null;
    	UserContext uc = null;
    	List<HttpSession> sessions = null;
    	for(HttpSession session : getAllHttpSessions()){
    		uc = (UserContext) session.getAttribute(SysConstant.USER_CONTEXT_KEY);
    		if(uc != null && uc.getUserId().equals(userId)){
    			if(sessions == null)
    				sessions = new ArrayList<HttpSession>();
    			sessions.add(session);
    		}
    	}
    	return sessions;
    }
}
