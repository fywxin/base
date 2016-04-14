package org.whale.system.base;

public class Query implements Iquery {
	
	private String sql;
	
	private Object[] args;
	
	private Class<?> clazz;
	
	public static Query newQuery(String sql, Object... args){
		return new Query(sql, args);
	}
	
	public Query(String sql, Object... args){
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

	public Query setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

}
