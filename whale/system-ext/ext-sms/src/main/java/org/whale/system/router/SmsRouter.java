package org.whale.system.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.common.exception.SysException;
import org.whale.system.service.SmsService;
import org.whale.system.domain.Sms;


@Controller
@RequestMapping("/sms")
public class SmsRouter extends BaseRouter {
	
	@RequestMapping("/goList")
	public ModelAndView goList(){
		return new ModelAndView("system/sms/sms_list");
	}
	
	@RequestMapping("/doList")
	@ResponseBody
	public Page doList(Sms sms){
		Page page = this.newPage();
		page.newCmd(Sms.class).eq("smsType", sms.getSmsType()).like("content", sms.getContent());
		
		SmsService.queryPage(page);
		
		return page;
	}
	
	@RequestMapping("/goSave")
	public ModelAndView goSave(){
		
		return new ModelAndView("system/sms/sms_save");
	}
	
	
	@RequestMapping("/goView")
	public ModelAndView goView(Long id){
		Sms sms = SmsService.get(id);
		if(sms == null){
			throw new SysException("查找不到 短信 id="+id);
		}
		
		return new ModelAndView("system/sms/sms_view")
				.addObject("item", sms);
	}
	
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(Sms sms){
		SmsService.sendSms(sms);
		return Rs.success();
	}
	
}