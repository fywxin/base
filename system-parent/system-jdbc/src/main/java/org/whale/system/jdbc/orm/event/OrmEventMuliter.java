package org.whale.system.jdbc.orm.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

@Component
public class OrmEventMuliter {
	
	@Autowired(required=false)
	private CopyOnWriteArrayList<OrmEventListener> listeners;
	
	private Map<Class<? extends OrmEvent>, List<OrmEventListener>> cache 
				= new ConcurrentHashMap<Class<? extends OrmEvent>, List<OrmEventListener>>();
	
	public void addListener(OrmEventListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(OrmEventListener listener){
		listeners.remove(listener);
	}
	
	public void multicater(OrmEvent event){
		List<OrmEventListener> matchListeners = this.getListeners(event);
		if(matchListeners != null && matchListeners.size() > 0){
			for(OrmEventListener listener : matchListeners){
				listener.onEvent(event);
			}
		}
	}
	
	private List<OrmEventListener> getListeners(OrmEvent event){
		if(!cache.containsKey(event.getClass())){
			List<OrmEventListener> temp = new ArrayList<OrmEventListener>();
			if(listeners == null){
				listeners = new CopyOnWriteArrayList<OrmEventListener>();
			}
			if(listeners.size() > 0){
				for(OrmEventListener listener : listeners){
					if(listener.match(event)){
						temp.add(listener);
					}
				}
			}
			//排序
			Collections.sort(temp, new OrderComparator());
			cache.put(event.getClass(), temp);
		}
		return cache.get(event.getClass());
	}
}
