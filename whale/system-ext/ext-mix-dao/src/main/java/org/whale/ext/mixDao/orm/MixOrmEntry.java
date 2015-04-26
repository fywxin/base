package org.whale.ext.mixDao.orm;

import org.whale.system.jdbc.orm.entry.OrmTable;

public class MixOrmEntry {
	
	public static final String MIX_ORM_KEY = "MixOrmEntry";

	private String cacheName;
	
	private int cacheTime;
	
	private OrmTable ormTable;

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	public OrmTable getOrmTable() {
		return ormTable;
	}

	public void setOrmTable(OrmTable ormTable) {
		this.ormTable = ormTable;
	}
	
	
}
