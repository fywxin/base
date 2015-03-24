package org.whale.system.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.whale.system.common.util.Strings;

/**
 * 分页对象
 *
 * @author 王金绍
 * 2014年9月6日-下午1:49:23
 */
public class Page implements Serializable{
	private static final long serialVersionUID = -4710867631907261303L;

	private int pageSize = 20;
	
	private int pageNo = 1;
	
	private long total;
	//返回结果
	private List<Map<String, Object>> datas;
	//分页sql
	private String sql;
	//总记录数sql
	private String countSql;
	/** 查询条件 */
	private Map<String, Object> param = new HashMap<String, Object>();
	/** 参数*/
	private List<Object> args = new ArrayList<Object>();
	//排序字段 空则默认按id排序
	private List<String> orderColumn = new LinkedList<String>();
	//升降序
	private List<Boolean> orderAsc = new LinkedList<Boolean>();

	
	/**
	 * 添加参数
	 * @param obj
	 */
	public void addArg(Object obj){
		if(obj == null) return ;
		args.add(obj);
	}
	
	/**
	 * 添加查询条件
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value){
		this.param.put(key, value);
	}
	
	/**
	 * 获取查询条件值
	 * @param key
	 * @return
	 */
	public Object getParam(String key){
		return param.get(key);
	}
	
	/**
	 * 获取查询条件字符串值
	 * @param key
	 * @return
	 */
	public String getParamStr(String key) {
		Object val = this.getParam(key);
		if(val == null)
			return null;
		return val.toString();
	}
	
	/**
	 * 获取查询条件字符串值
	 * @param key
	 * @return
	 */
	public Integer getParamInteger(String key) {
		Object val = this.getParam(key);
		if(val == null)
			return null;
		try{
			return Integer.parseInt(val.toString());
		}catch(Exception e){
			
		}
		return null;
	}
	
	public Long getParamLong(String key) {
		Object val = this.getParam(key);
		if(val == null)
			return null;
		try{
			return Long.parseLong(val.toString());
		}catch(Exception e){
			
		}
		return null;
	}
	
	public Boolean getParamBoolean(String key) {
		return (Boolean) this.getParam(key);
	}
	
	/**
	 * 添加排序
	 * @param column
	 * @param asc
	 */
	public void addOrderBy(String column, boolean asc){
		if(Strings.isBlank(column))
			return ;
		orderColumn.add(column);
		orderAsc.add(asc ? Boolean.TRUE : Boolean.FALSE);
	}
	
	
	//----------------------------------------------------get set----------------------------------------
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getCountSql() {
		return countSql;
	}
	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}
	public List<Object> getArgs() {
		return args;
	}
	public List<Map<String, Object>> getDatas() {
		return datas;
	}
	public void setDatas(List<Map<String, Object>> datas) {
		this.datas = datas;
	}
	public Map<String, Object> getParam() {
		return param;
	}
	public List<String> getOrderColumn() {
		return orderColumn;
	}
	public List<Boolean> getOrderAsc() {
		return orderAsc;
	}
	
}
