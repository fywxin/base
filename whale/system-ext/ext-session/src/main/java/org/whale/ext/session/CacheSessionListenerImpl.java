package org.whale.ext.session;

import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.common.util.SpringContextHolder;

@Component
public class CacheSessionListenerImpl {

	@Autowired(required=false)
	private List<HttpSessionListener> listeners;
	
	public void broadcastSessionCreated(HttpSessionEvent event){
		if(listeners != null && listeners.size() > 0){
			for(HttpSessionListener listener : listeners){
				listener.sessionCreated(event);
			}
		}
	}
	
	public void broadcastSessionDestroyed(HttpSessionEvent event){
		if(listeners != null && listeners.size() > 0){
			for(HttpSessionListener listener : listeners){
				listener.sessionDestroyed(event);
			}
		}
	}
	
	public void setListeners(List<HttpSessionListener> listeners) {
		this.listeners = listeners;
	}

	public static  CacheSessionListenerImpl getThis(){
		return SpringContextHolder.getBean(CacheSessionListenerImpl.class);
	}
}
