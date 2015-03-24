package org.whale.ext.session;

import java.util.Enumeration;
import java.util.Iterator;

public class StringEnumeration implements Enumeration<String> {

	private Iterator<String> iter;
	
	public StringEnumeration(){}
	
	public StringEnumeration(Iterator<String> iter){
		this.iter = iter;
	}
	
	@Override
	public boolean hasMoreElements() {
		 return this.iter.hasNext();
	}

	@Override
	public String nextElement() {
		return this.iter.next();
	}

	public void setIter(Iterator<String> iter) {
		this.iter = iter;
	}

}
