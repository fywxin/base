package org.whale.system.base;

public class Find implements Iquery {
	
	private String sql;
	
	private Object[] args;
	
	private Class<?> clazz;
	
	public static Find newQuery(String sql, Object... args){
		return new Find(sql, args);
	}
	
	public Find(String sql, Object... args){
		this.sql = sql;
		this.args = args;
	}

	@Override
	public String getSql(SqlType sqlType) {
		return sql;
	}

	@Override
	public Object[] getArgs() {
		return args;
	}

	@Override
	public Class<?> rsClazz() {
		return clazz;
	}

	public Find setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

}
