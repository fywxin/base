package org.whale.system.jdbc.filter.impl;

import org.whale.system.base.BaseDao;
import org.whale.system.jdbc.IOrmDao;

/**
 * 执行BaseDao方法上下文
 * 
 * @author wjs
 * 2014年9月17日-上午11:39:27
 */
@SuppressWarnings("all")
public class OrmExeContext {

	private String methodName;
	
	private String sql;
	
	private Object arg;
	
	private IOrmDao baseDao;
	
	private int order;
	
	public OrmExeContext(String methodName, Object arg, IOrmDao baseDao){
		this.methodName = methodName;
		this.arg = arg;
		this.baseDao = baseDao;
	}
	
	public OrmExeContext(String methodName, String sql, Object arg, IOrmDao baseDao){
		this.methodName = methodName;
		this.sql = sql;
		this.arg = arg;
		this.baseDao = baseDao;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
	}

	public IOrmDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IOrmDao baseDao) {
		this.baseDao = baseDao;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	
}
