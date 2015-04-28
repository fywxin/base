package org.whale.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.auth.annotation.Auth;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Log;
import org.whale.system.service.LogService;

@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param roleName
	 * @param roleCode
	 * @return
	 */
	@Auth(code="LOG_LIST",name="日志查询")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){

		return new ModelAndView("system/log/log_list");
	}
	
	@Auth(code="LOG_LIST",name="日志查询")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, Log log, String startTime, String endTime){
		
		Page page = this.newPage(request);
		page.put("opt", log.getOpt());
		page.put("cnName", log.getCnName());
		page.put("tableName", log.getTableName());
		page.put("userName", log.getUserName());
		page.put("startTime", startTime);
		page.put("endTime", endTime);
		page.put("uri", log.getUri());
		page.put("rsType", log.getRsType());
		
		this.logService.queryPage(page);
		
		WebUtil.print(request, response, page);
//		return new ModelAndView("system/log/log_list")
//			.addObject("item", log)
//			.addObject("startTime", startTime)
//			.addObject("endTime", endTime);
	}
	
	@Auth(code="LOG_LIST",name="日志查询")
	@RequestMapping("/goView")
	public ModelAndView goView(HttpServletRequest request, HttpServletResponse response, Long id){
		return new ModelAndView("system/log/log_view").addObject("item", this.logService.get(id));
	}
}
