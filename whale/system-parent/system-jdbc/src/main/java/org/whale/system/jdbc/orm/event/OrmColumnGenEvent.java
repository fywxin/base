package org.whale.system.jdbc.orm.event;

import org.whale.system.jdbc.orm.entry.Acolumn;
import org.whale.system.jdbc.orm.entry.Atable;
import org.whale.system.jdbc.orm.entry.OrmColumn;

public class OrmColumnGenEvent extends OrmEvent {
	
	private static final long serialVersionUID = 233473981L;
	

	private Atable table;
	
	private Acolumn acolumn;
	
	private OrmColumn ormColumn;
	
	//创建OrmColumn前
	private boolean beforeGen;
	//创建OrmColumn后
	private boolean afterGen;
	
	public OrmColumnGenEvent(Object source) {
		super(source);
	}

	public OrmColumnGenEvent(Object source, Atable table, Acolumn acolumn,
			OrmColumn ormColumn, boolean beforeGen, boolean afterGen) {
		super(source);
		this.table = table;
		this.acolumn = acolumn;
		this.ormColumn = ormColumn;
		this.beforeGen = beforeGen;
		this.afterGen = afterGen;
	}

	public Atable getTable() {
		return table;
	}

	public void setTable(Atable table) {
		this.table = table;
	}

	public Acolumn getAcolumn() {
		return acolumn;
	}

	public void setAcolumn(Acolumn acolumn) {
		this.acolumn = acolumn;
	}

	public OrmColumn getOrmColumn() {
		return ormColumn;
	}

	public void setOrmColumn(OrmColumn ormColumn) {
		this.ormColumn = ormColumn;
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
