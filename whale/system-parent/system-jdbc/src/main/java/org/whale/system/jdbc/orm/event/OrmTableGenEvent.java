package org.whale.system.jdbc.orm.event;

import org.whale.system.jdbc.orm.entry.OrmTable;

public class OrmTableGenEvent extends OrmEvent {

	private static final long serialVersionUID = -34341L;
	

	private OrmTable ormTable;
	
	//创建OrmTable前
	private boolean beforeGen;
	//创建OrmTable后
	private boolean afterGen;
	
	public OrmTableGenEvent(Object source) {
		super(source);
	}
	
	public OrmTableGenEvent(Object source, OrmTable ormTable, boolean beforeGen, boolean afterGen) {
		super(source);
		this.ormTable = ormTable;
		this.beforeGen = beforeGen;
		this.afterGen = afterGen;
	}

	public OrmTable getOrmTable() {
		return ormTable;
	}

	public void setOrmTable(OrmTable ormTable) {
		this.ormTable = ormTable;
	}

	public boolean isBeforeGen() {
		return beforeGen;
	}

	public void setBeforeGen(boolean beforeGen) {
		this.beforeGen = beforeGen;
	}

	public boolean isAfterGen() {
		return afterGen;
	}

	public void setAfterGen(boolean afterGen) {
		this.afterGen = afterGen;
	}
	
	
}
