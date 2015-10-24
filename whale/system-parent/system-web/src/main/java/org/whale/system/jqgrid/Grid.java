package org.whale.system.jqgrid;

import java.io.Serializable;
import java.util.List;

import org.whale.system.base.Page;
import org.whale.system.common.util.WebUtil;

public class Grid implements Serializable {
	private static final long serialVersionUID = -47107631907261303L;

	private Long total;
	
	private Long page;
	
	private Long records;
	
	private List<?> rows;
	
	public static Page newPage(){
		int pageNo = fetInt("page", 1);
		int pageSize = fetInt("rows", 20);
		if(pageSize < 1)
			pageSize = 20;
		if(pageSize > 100)
			pageSize = 20;
		if(pageNo < 1)
			pageNo = 1;
		Page page = new Page();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		WebUtil.getRequest().setAttribute("page", page);
		return page;
	}
	
	public static Grid grid(Page page){
		Grid grid = new Grid();
		grid.setPage(new Long(page.getPageNo()));
		grid.setRecords(page.getTotal());
		grid.setRows(page.getData());
		grid.setTotal(page.getTotalPages());
		
		return grid;
	}
	
	private static Integer fetInt(String key, Integer defVal){
		String obj = WebUtil.getRequest().getParameter(key);
		if(obj == null || "".equals(obj.trim()))
			return defVal;
		
		try {
			return Integer.parseInt(obj);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Long getRecords() {
		return records;
	}

	public void setRecords(Long records) {
		this.records = records;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}
