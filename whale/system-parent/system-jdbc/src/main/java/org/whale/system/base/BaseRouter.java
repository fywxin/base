package org.whale.system.base;

import javax.servlet.http.HttpServletRequest;

import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.NotLoginException;
import org.whale.system.common.util.WebUtil;

public abstract class BaseRouter {

	/**
	 * 获取分页信息
	 * @param request
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
	 * @param request
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