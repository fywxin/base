package org.whale.system.router;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.AuthAdmin;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.auth.scan.AuthBeanStore;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Rs;
import org.whale.system.base.UserContext;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.servlet.MySessionContext;

@Controller
public class AdminRouter extends BaseRouter{
	
	@Autowired
	private AuthBeanStore authBeanStore;
	@Autowired
	private UserAuthCacheService userAuthCacheService;
	@Autowired
	private DictCacheService dictCacheService;
	
	@AuthAdmin
	@RequestMapping("/goAdmin")
	public ModelAndView goAdmin(){
		
		return new ModelAndView("system/admin");
	}
	
	@AuthAdmin
	@ResponseBody
	@RequestMapping("/doFlushDictCache")
	public Rs doFlushDictCache(){
		this.dictCacheService.init(null);
		return Rs.success();
	}
	
	@AuthAdmin
	@ResponseBody
	@RequestMapping("/doFlushAuthCache")
	public Rs doFlushAuthCache(){
		this.userAuthCacheService.init(null);
		return Rs.success();
	}
	
	/**
	 * 刷新权限信息
	 * @param request
	 * @param response
	*/
	@AuthAdmin
	@ResponseBody
	@RequestMapping("/doFlushAuthUris")
	public Rs doFlushAuthUris(){
		String warn = this.authBeanStore.doStore();
		
		this.userAuthCacheService.init(null);
		return Rs.success(warn);
	}
	 
	
	/**
	 * 显示所有在线用户
	 * @param request
	 * @param response
	 */
	@AuthAdmin
	@SuppressWarnings("all")
	@RequestMapping("/goWhoAreOnline")
	public ModelAndView goWhoAreOnline() {

		return new ModelAndView("system/user/who_are_online")
				.addObject("mySessionId", this.getUserContext().getSessionId());
	}
	
	@AuthAdmin
	@ResponseBody
	@SuppressWarnings("all")
	@RequestMapping("/doWhoAreOnline")
	public Map<String, Object> doWhoAreOnline() {
		Collection<HttpSession> sessions = MySessionContext.getAllHttpSessions();
		
		//在线的用户名
		List<Map<String, Object>> users = new ArrayList<Map<String,Object>>(sessions.size());
		//登录的主机 userId - 登录的主机List
		Map<String, List<Map<String, Object>>> logins = new HashMap<String, List<Map<String,Object>>>();
		
		UserContext uc = null;
		Map<String, Object> tmp = null;
		for(HttpSession session : sessions){
			uc = (UserContext) session.getAttribute(SysConstant.USER_CONTEXT_KEY);
			if(uc == null)
				continue ;
//			if(isSearch && uc.getUserName().toLowerCase().indexOf(userName.trim().toLowerCase()) == -1)
//				continue;
			if(logins.size()>0 && logins.containsKey(uc.getUserName())){
				List<Map<String, Object>> userLogins = logins.get(uc.getUserName());
				tmp = new HashMap<String, Object>();
				tmp.put("ip", uc.getIp());
				tmp.put("loginTime", TimeUtil.formatTime(uc.getLoginTime(), TimeUtil.COMMON_FORMAT));
				tmp.put("sessionId", uc.getSessionId());
				userLogins.add(tmp);
			}else{
				List<Map<String, Object>> userLogins = new ArrayList<Map<String,Object>>();
				tmp = new HashMap<String, Object>();
				tmp.put("ip", uc.getIp());
				tmp.put("loginTime", TimeUtil.formatTime(uc.getLoginTime(), TimeUtil.COMMON_FORMAT));
				tmp.put("sessionId", uc.getSessionId());
				userLogins.add(tmp);
				logins.put(uc.getUserName(), userLogins);
				
				tmp = new HashMap<String, Object>();
				tmp.put("userId", uc.getUserId());
				tmp.put("userName", uc.getUserName());
				tmp.put("realName", uc.getRealName());
				
				users.add(tmp);
			}
		}
		
		Map<String, Object> user = new HashMap<String, Object>(6);
		user.put("datas", users);
		user.put("total", users.size());
		
		tmp = new HashMap<String, Object>(6);
		tmp.put("user", user);
		tmp.put("login", logins);
		
		return tmp;
	}
	
	/**
	 * 强制用户退出登录
	 * @param request
	 * @param response
	 * @param sessionId
	 */
	@AuthAdmin
	@ResponseBody
	@RequestMapping("/doForceLoginOut")
	public Rs doForceLoginOut(String sessionId){
		if(Strings.isBlank(sessionId)){
			return Rs.fail("请选择要强制退出的用户");
		}
		HttpSession session = MySessionContext.getSession(sessionId);
		if(session == null){
			return Rs.fail("未找到相应的登录信息");
		}
		session.invalidate();
		MySessionContext.delSession(session);
		
		return Rs.success();
	}
}
