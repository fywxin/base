package org.whale.system.jdbc.orm.event;

import java.util.EventListener;

import org.springframework.core.PriorityOrdered;

public interface OrmEventListener extends EventListener, PriorityOrdered {

	void onEvent(OrmEvent event);
	
	boolean match(OrmEvent event);
}

