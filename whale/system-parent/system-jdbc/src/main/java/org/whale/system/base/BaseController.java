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

public abstract class BaseController {
	
	@InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    }

	/**
	 * 获取分页信息
	 * @param request
	 * @return
	 */
	protected Page newPage(HttpServletRequest request){
		int pageNo = WebUtil.getInt(request, "page", 1);
		int pageSize = WebUtil.getInt(request, "pageSize", 20);
		if(pageSize < 1)
			pageSize = 20;
		if(pageSize > 100)
			pageSize = 20;
		if(pageNo < 1)
			pageNo = 1;
		Page page = new Page();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		request.setAttribute("page", page);
		return page;
	}
	
	/**
	 * 获取用户上下文信息
	 * @param request
	 * @return
	 */
	public UserContext getUserContext(HttpServletRequest request){
		UserContext uc = (UserContext)request.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
		if(uc == null){
			throw new NotLoginException("用户未登入，请重新登入");
		}
		return uc;
	}
	
	
}
