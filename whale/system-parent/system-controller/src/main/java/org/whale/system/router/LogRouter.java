package org.whale.system.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.domain.Log;
import org.whale.system.service.LogServiceAdapter;

@Controller
@RequestMapping("/log")
public class LogRouter extends BaseRouter {

	@Autowired
	private LogServiceAdapter LogServiceAdapter;
	
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
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(Log log, String startTime, String endTime){
		Page page = this.newPage();
		
		StringBuilder strb = new StringBuilder();
		StringBuilder param = new StringBuilder();
		
		strb.append("SELECT t.id, t.opt, t.cnName, t.tableName, t.ip, t.createTime, t.userName, t.uri, t.callOrder, t.methodCostTime, t.costTime, t.rsType ")
			.append("FROM sys_log t ")
			.append("WHERE 1=1 ");
		
		String opt = log.getOpt();
		if(Strings.isNotBlank(opt)){
			if("dll".equals(opt)){
				param.append("AND (t.opt like 'save%' OR t.opt like 'update%' OR t.opt like 'delete%') ");
			}else if("find".equals(opt)){
				param.append("AND (t.opt like 'get%' OR t.opt like 'query%') ");
			}else{
				param.append("AND t.opt = ? ");
				page.addArg(opt);
			}
		}
		
		if(Strings.isNotBlank(log.getCnName())){
			param.append("AND t.cnName like ? ");
			page.addArg("%"+log.getCnName().trim()+"%");
		}
		
		if(Strings.isNotBlank(log.getTableName())){
			param.append("AND t.tableName like ? ");
			page.addArg("%"+log.getTableName().trim()+"%");
		}
		
		if(Strings.isNotBlank(log.getUri())){
			param.append("AND t.uri like ? ");
			page.addArg("%"+log.getUri().trim()+"%");
		}
		
		if(Strings.isNotBlank(log.getUserName())){
			param.append("AND t.userName like ? ");
			page.addArg("%"+log.getUserName().trim()+"%");
		}
		
		if(Strings.isNotBlank(startTime)){
			param.append("AND t.createTime >= ? ");
			page.addArg(TimeUtil.parseTime(startTime).getTime());
		}
		
		if(Strings.isNotBlank(endTime)){
			param.append("AND t.createTime<=? ");
			page.addArg(TimeUtil.parseTime(endTime).getTime());
		}
		
		Integer rsType = log.getRsType();
		if(rsType != null){
			if(rsType == 11){
				param.append("AND t.rsType != 1 ");
			}else{
				param.append("AND t.rsType =? ");
				page.addArg(log.getRsType());
			}
		}
		if(Strings.isNotBlank(log.getAppId())){
			param.append("AND t.appId like ? ");
			page.addArg("%"+log.getAppId().trim()+"%");
		}
		
		if(log.getMethodCostTime() != null){
			param.append("AND t.methodCostTime >= ? ");
			page.addArg(log.getMethodCostTime());
		}
		if(log.getCostTime() != null){
			param.append("AND t.costTime >= ? ");
			page.addArg(log.getCostTime());
		}
	
		page.setCountSql("SELECT count(1) FROM sys_log t where 1=1 "+param.toString());
		page.setSql(strb.append(param.toString())+" ORDER BY t.createTime desc, t.callOrder asc");
		
		this.LogServiceAdapter.queryPage(page);
		
		return page;
	}
	
	@Auth(code="LOG_LIST",name="日志查询")
	@RequestMapping("/goView")
	public ModelAndView goView(String id){
		return new ModelAndView("system/log/log_view").addObject("item", this.LogServiceAdapter.get(id));
	}
}
