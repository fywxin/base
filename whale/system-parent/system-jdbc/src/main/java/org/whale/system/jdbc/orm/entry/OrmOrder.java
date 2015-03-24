package org.whale.system.jdbc.orm.entry;

public class OrmOrder {

	private int index;
	
	private boolean asc;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean getAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}
}
