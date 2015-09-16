package org.whale.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.WebUtil;
import org.whale.system.service.SmsService;
import org.whale.system.domain.Sms;


@Controller
@RequestMapping("/sms")
public class SmsController extends BaseController {
	
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/sms/sms_list");
	}
	
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, Sms sms){
		
		Page page = this.newPage(request);
		page.newCmd(Sms.class).and("smsType", sms.getSmsType()).like("content", sms.getContent());
		
		SmsService.queryPage(page);
		
		WebUtil.print(request, response, page);
	}
	
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response){
		
		return new ModelAndView("system/sms/sms_save");
	}
	
	
	@RequestMapping("/goView")
	public ModelAndView goView(HttpServletRequest request, HttpServletResponse response, Long id){
		Sms sms = SmsService.get(id);
		if(sms == null){
			throw new SysException("查找不到 短信 id="+id);
		}
		
		return new ModelAndView("system/sms/sms_view")
				.addObject("item", sms);
	}
	
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Sms sms){
		SmsService.sendSms(sms);
		WebUtil.printSuccess(request, response);
	}
	
}