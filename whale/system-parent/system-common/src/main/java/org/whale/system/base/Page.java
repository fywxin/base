package org.whale.system.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.whale.system.common.util.Strings;

/**
 * 分页对象
 *
 * @author 王金绍 2014年9月6日-下午1:49:23
 */
public class Page implements Serializable {
	private static final long serialVersionUID = -4710867631907261303L;

	private int pageSize = 20;

	private int pageNo = 1;
	// total >0 ，则不执行sql总数查询，直接返回用户设置的total值, 有两个好处： 1. 减少总数sql查询， 2.
	// 有限度防止被恶意翻页（如爬虫）
	private Long total;
	// 返回结果
	private List<?> datas;
	// 分页sql
	private String sql;
	// 总记录数sql
	private String countSql;
	/** 参数 */
	private List<Object> args = new ArrayList<Object>();
	
	
	/** 查询条件 */
	private Map<String, Object> param = new HashMap<String, Object>();
	
	// 排序字段
	private List<Order> orders;

	public long getTotalPages() {
		if(total == null){
			return Long.MAX_VALUE;
		}
		if (this.total % this.pageSize == 0L) {
			return this.total / this.pageSize;
		}
		return (this.total / this.pageSize) + 1;
	}

	public boolean isPrevious() {
		return this.pageNo > 1;
	}

	public boolean isNext() {
		return this.pageNo < getTotalPages();
	}

	/**
	 * 添加参数
	 * 
	 * @param obj
	 */
	public void addArg(Object obj) {
		if (obj == null)
			return;
		args.add(obj);
	}

	/**
	 * 添加查询条件
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		this.param.put(key, value);
	}

	/**
	 * 获取查询条件值
	 * 
	 * @param key
	 * @return
	 */
	public Object getParam(String key) {
		return param.get(key);
	}

	/**
	 * 获取查询条件字符串值
	 * 
	 * @param key
	 * @return
	 */
	public String getParamStr(String key) {
		Object val = this.getParam(key);
		if (val == null)
			return null;
		return val.toString();
	}

	/**
	 * 获取查询条件字符串值
	 * 
	 * @param key
	 * @return
	 */
	public Integer getParamInteger(String key) {
		Object val = this.getParam(key);
		if (val == null)
			return null;
		try {
			return Integer.parseInt(val.toString());
		} catch (Exception e) {

		}
		return null;
	}

	public Long getParamLong(String key) {
		Object val = this.getParam(key);
		if (val == null)
			return null;
		try {
			return Long.parseLong(val.toString());
		} catch (Exception e) {

		}
		return null;
	}

	public Boolean getParamBoolean(String key) {
		return (Boolean) this.getParam(key);
	}

	/**
	 * 添加排序
	 * 
	 * @param column
	 * @param asc
	 */
	public void addOrderBy(String column, boolean asc) {
		if (Strings.isBlank(column))
			return;
		if(orders == null){
			orders = new ArrayList<Page.Order>();
		}
		orders.add(new Order(column, asc));
	}

	// ----------------------------------------------------get
	// set----------------------------------------

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

	public List<?> getDatas() {
		return datas;
	}

	public void setDatas(List<?> datas) {
		this.datas = datas;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public List<Order> getOrders() {
		return orders;
	}


	public static class Order{
		
		private boolean asc = true;
		
		private String col;
		
		public Order(String col, boolean asc){
			this.col = col;
			this.asc = asc;
		}

		public boolean isAsc() {
			return asc;
		}

		public void setAsc(boolean asc) {
			this.asc = asc;
		}

		public String getCol() {
			return col;
		}

		public void setCol(String col) {
			this.col = col;
		}
	}

}
