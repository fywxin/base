package org.whale.system.addon;

import org.whale.system.base.BaseDao;

/**
 * 执行BaseDao方法上下文
 * 
 * @author 王金绍
 * 2014年9月17日-上午11:39:27
 */
@SuppressWarnings("all")
public class OptContext {

	private String methodName;
	
	private Object arg;
	
	private BaseDao baseDao;
	
	private int order;
	
	public OptContext(){}
	
	public OptContext(String methodName, Object arg, BaseDao baseDao){
		this.methodName = methodName;
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

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
}
