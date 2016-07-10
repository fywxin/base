package org.whale.system.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.whale.system.base.Iquery.SqlType;
import org.whale.system.common.exception.SysException;

/**
 * 分页对象
 *
 * @author wjs 2014年9月6日-下午1:49:23
 */
public class Page implements Serializable {
	private static final long serialVersionUID = -4710867631907261303L;

	private int pageSize = 20;

	private int pageNo = 1;
	
	private Integer offset;
	
	/**
	 *  total >0 ，则不执行sql总数查询，直接返回用户设置的total值, 有两个好处： 1. 减少总数sql查询， 2.
	 *	有限度防止被恶意翻页（如爬虫） 
	 */
	private Long total;
	
	/** 返回结果 */
	private List<?> data;
	
	/** 分页sql */
	private String sql;
	
	/** 总记录数sql */
	private String countSql;
	
	/** 参数 */
	private List<Object> args = new ArrayList<Object>();
	
	/**动态拼接查询 */
	private transient Iquery q;
	
	/**返回结果data 的类型，默认为Map<String, Object> */
	private Class<?> dataClass;
	
	
	public Q newQ(Class<?> clazz){
		Q q = Q.newQ(clazz);
		this.setQ(q);
		return q;
	}

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
	public Page addArg(Object obj) {
		args.add(obj);
		return this;
	}
	
	private void exeCmd(){
		if(this.q == null){
			throw new SysException("Page 分页语句为空");
		}
		this.sql = this.q.getSql(SqlType.QUERY);
		this.countSql = this.q.getSql(SqlType.COUNT);
		for(Object obj : this.q.getArgs()){
			this.args.add(obj);
		}
		
	}

	// ----------------------------------------------------get set----------------------------------------

	public Iquery getQ() {
		return q;
	}

	public void setQ(Iquery q) {
		this.q = q;
	}

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

	public String sql() {
		if(sql == null){
			this.exeCmd();
		}
		return sql;
	}

	public Page setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public String countSql() {
		if(this.sql == null){
			this.exeCmd();
		}
		return countSql;
	}

	public Page setCountSql(String countSql) {
		this.countSql = countSql;
		return this;
	}

	public List<Object> args() {
		return args;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	

	public Class<?> getDataClass() {
		return dataClass;
	}

	public Page setDataClass(Class<?> dataClass) {
		this.dataClass = dataClass;
		return this;
	}

	@Override
	public String toString() {
		return "Page [\nsql   = " + sql + "\ncount = " + countSql + "\nargs  = "
				+ args + "\ndata = " + data  + "\n]";
	}
}
