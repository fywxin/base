package org.whale.system.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.NotLoginException;
import org.whale.system.common.util.WebUtil;

public abstract class BaseRouter {
	
	@InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    }

	/**
	 * 获取分页信息
	 * @return
	 */
	protected Page newPage(){
		HttpServletRequest request = WebUtil.getRequest();
		int pageNo = getInt(request, "page", 1);
		int pageSize = getInt(request, "limit", 20);
		if(pageSize < 1)
			pageSize = 20;
		if(pageSize > 100)
			pageSize = 20;
		if(pageNo < 1)
			pageNo = 1;
		Page page = new Page();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setOffset(getInt(request, "offset"));
		
		WebUtil.getRequest().setAttribute("page", page);
		return page;
	}

	/**
	 * 获取分页信息, 并设置返回类型为clazz
	 * @param clazz
	 * @return
	 */
	protected Page newPage(Class clazz){
		Page page = this.newPage();
		page.setDataClass(clazz);
		return page;
	}
	
	private Integer getInt(HttpServletRequest request, String key, Integer defVal){
		String obj = request.getParameter(key);
		if(obj == null || "".equals(obj.trim()))
			return defVal;
		
		try {
			return Integer.parseInt(obj);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	private Integer getInt(HttpServletRequest request, String key){
		String obj = request.getParameter(key);
		if(obj != null){
			try {
				return Integer.parseInt(obj);
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}
	
	/**
	 * 获取用户上下文信息
	 * @return
	 */
	public UserContext getUserContext(){
		UserContext uc = (UserContext)WebUtil.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
		if(uc == null){
			throw new NotLoginException("用户未登入，请重新登入");
		}
		return uc;
	}
	
	
}
