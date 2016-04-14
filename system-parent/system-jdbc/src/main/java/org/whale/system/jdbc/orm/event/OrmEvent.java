package org.whale.system.jdbc.orm.event;

import java.util.EventObject;

public class OrmEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param source 事件发生的源对象  this
	 */
	public OrmEvent(Object source) {
		super(source);
	}
}
