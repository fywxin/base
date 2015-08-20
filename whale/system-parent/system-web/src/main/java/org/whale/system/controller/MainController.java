package org.whale.system.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseController;
import org.whale.system.base.UserContext;
import org.whale.system.base.UserContextAccessor;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Dept;
import org.whale.system.domain.Menu;
import org.whale.system.domain.User;
import org.whale.system.service.DeptService;
import org.whale.system.service.MenuService;
import org.whale.system.service.UserService;
import org.whale.system.servlet.MySessionContext;

import com.alibaba.fastjson.JSON;

@Controller
public class MainController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private UserService userService;
	@Autowired(required=false)
	private List<UserContextAccessor> accessores;
	@Autowired
	private MenuService menuService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private DictCacheService dictCacheService;
	@Autowired
	private UserAuthCacheService userAuthCacheService;
	
	private Boolean sort = Boolean.FALSE;

	List<String> menuCss = new ArrayList<String>(3);
	{
		menuCss.add("nav");
		menuCss.add("nav nav-second-level");
		menuCss.add("nav nav-third-level");
		menuCss.add("nav nav-fourth-level");
		menuCss.add("nav nav-fifth-level");
	}
	
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param userName
	 * @param password
	 */
	@RequestMapping("/login")
	public void doLogin(HttpServletRequest request, HttpServletResponse response, String userName, String password, String encryptedPwd){
		logger.info("用户登录[{}/{}] ：ip[{}]",userName, password, WebUtil.getIp(request));
		if(Strings.isBlank(userName)){
			WebUtil.printFail(request, response, "用户名不能为空", errorCount(request));
			return ;
		}
		
		if(Strings.isBlank(password) && Strings.isBlank(encryptedPwd)){
			WebUtil.printFail(request, response, "密码不能为空", errorCount(request));
			return ;
		}
		
		Integer errorCount = (Integer)request.getSession().getAttribute("ERROR");
		String msg = null;
		if((Strings.isNotBlank(password) || Strings.isBlank(encryptedPwd)) 
				&& ((errorCount != null && errorCount >= 3) || SysConstant.LOGIC_TRUE.equals(dictCacheService.getItemValue(DictConstant.DICT_SYS_CONF, "VERITY_CODE_FLAG"))) 
				&& (msg = this.checkVerify(request)) != null){
			WebUtil.printFail(request, response, msg, errorCount(request));
			return ;
		}
		
		User user = this.userService.getByUserName(userName);
		if(user == null || user.getStatus() == SysConstant.STATUS_DEL){
			WebUtil.printFail(request, response, "用户名不存在", errorCount(request));
			return ;
		}
		if(user.getStatus() != SysConstant.STATUS_NORMAL){
			WebUtil.printFail(request, response, "用户已禁止登录，请联系管理员");
			return ;
		}
		if(Strings.isBlank(password)){
			if(SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"))){
				if(!encryptedPwd.equals(user.getPassword())){
					WebUtil.printFail(request, response, "请登录");
					return ;
				}
			}else{
				WebUtil.printFail(request, response, "密码不能为空", errorCount(request));
				return ;
			}
		}else{
			if(!this.userService.validPasswd(password, user.getPassword())){
				WebUtil.printFail(request, response, "密码错误", errorCount(request));
				return ;
			}
		}
		try {
			if(SysConstant.LOGIC_TRUE.equals(dictCacheService.getItemValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_TIEM_ONLY_SINGLE_LOGIN))){
				List<HttpSession> sessions = null;
				UserContext uc = null;
				if((sessions = MySessionContext.getSessionByUserId(user.getUserId())) != null && sessions.size() > 0) {
					uc = (UserContext) sessions.get(0).getAttribute(SysConstant.USER_CONTEXT_KEY);
					if(uc != null && !uc.getIp().equals(WebUtil.getIp(request))){
						WebUtil.printFail(request, response, "该帐号已于["+TimeUtil.formatTime(uc.getLoginTime(), TimeUtil.COMMON_FORMAT)+"] 在IP: ["+uc.getIp()+"] 登录，不允许重复登录!");
						return ;
					}else{
						for(HttpSession session : sessions){
							if(!session.getId().equals(request.getSession().getId())){
								session.invalidate();
								MySessionContext.delSession(session);
							}
						}
					}
				}
			}
			this.bind(request, user);
			request.getSession().removeAttribute("ERROR");
		} catch (Exception e) {
			logger.error("绑定用户上下文出现异常", e);
			this.clearUserContext(request);
			WebUtil.printFail(request, response, "绑定用户出现异常，请联系管理员");
			return ;
		}
		if(SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"))){
			WebUtil.printSuccess(request, response, Strings.isBlank(password) ? encryptedPwd : user.getPassword());
		}else{
			WebUtil.printSuccess(request, response);
		}
	}
	
	/**
	 * 验证验证码
	 * @param request
	 * @return 返回null表示成功
	 */
	private String checkVerify(HttpServletRequest request){
		String verifycode = request.getParameter("verifycode");
		if(Strings.isBlank(verifycode)){
			return "请输入验证码";
		}
		Object code = request.getSession().getAttribute(SysConstant.VERITY_CODE_KEY);
		if(code == null){
			return "登入超时，请重新登入";
		}
		if(!verifycode.equalsIgnoreCase(code.toString())){
			return "验证码错误";
		}
		this.clearVerify(request);
		return null;
	}
	
	/**
	 * 绑定用户数据
	 * @param request
	 * @param user
	 */
	private void bind(HttpServletRequest request, User user){
		this.bindUserContext(request, user);
		this.updateUserLoginInfo(request, user);
	}
	
	/**
	 * 绑定用户上下文
	 * @param request
	 * @param user
	 */
	private void bindUserContext(HttpServletRequest request, User user){
		UserContext uc = new UserContext();
		uc.setIp(WebUtil.getIp(request));
		uc.setLoginTime(new Date());
		uc.setRealName(user.getRealName());
		uc.setUserId(user.getUserId());
		uc.setUserName(user.getUserName());
		uc.setUserType(user.getUserType());
		if(!user.getIsAdmin()){
			Dept dept = this.deptService.get(user.getDeptId());
			if(dept == null){
				dept = new Dept();
				dept.setId(0L);
				dept.setDeptCode("ROOT");
				dept.setDeptName(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "ITEM_DEPT_ROOT"));
			}
			uc.getCustomDatas().put("dept", dept);
		}
		
		uc.setSuperAdmin(user.getIsAdmin());
		
		if(accessores != null && accessores.size() > 0){
			for(UserContextAccessor accessor : this.getAccessores()){
				accessor.addCustomDatas(uc);
			}
		}
		
		HttpSession session = request.getSession();
		
		session.setAttribute(SysConstant.USER_CONTEXT_KEY, uc);
		uc.setSessionId(request.getSession().getId());
		
		uc.getCustomDatas().put("menusStr", this.createMenu(request));
		
		ThreadContext.getContext().put(ThreadContext.KEY_USER_CONTEXT, uc);
	}
	
	/**
	 * 更新用户登录信息
	 * @param request
	 * @param user
	 */
	private void updateUserLoginInfo(HttpServletRequest request, User user){
		user.setLoginIp(WebUtil.getIp(request));
		user.setLastLoginTime(new Date());
		this.userService.update(user);
	}
	
	/**
	 * 退出登入
	 * @param request
	 * @param response
	 */
	@RequestMapping("/loginOut")
	public void loginOut(HttpServletRequest request,HttpServletResponse response) {
		try {
			UserContext uc = (UserContext)request.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
			if(uc != null && uc.getUserId() != null){
				request.getSession().invalidate();
				MySessionContext.delSession(request.getSession());
			}
			WebUtil.printSuccess(request, response);
		} catch (RuntimeException e) {
			logger.error("用户退出登入异常: "+e.getMessage(), e);
			WebUtil.printFail(request, response, "用户退出登入出现异常");
		}
	}
	
	/**
	 * 清除验证码
	 * @param request
	 */
	private void clearVerify(HttpServletRequest request){
		request.getSession().removeAttribute(SysConstant.VERITY_CODE_KEY);
	}
	
	/**
	 * 清空用户上下文信息
	 * @param request
	 */
	private void clearUserContext(HttpServletRequest request){
		request.getSession().removeAttribute(SysConstant.USER_CONTEXT_KEY);
		ThreadContext.getContext().remove(ThreadContext.KEY_USER_CONTEXT);
	}
	
	/**
	 * 登录错误次数维护
	 * @param request
	 * @return
	 */
	private String errorCount(HttpServletRequest request){
		Integer errorCount = (Integer)request.getSession().getAttribute("ERROR");
		if(errorCount == null){
			errorCount =1 ;
		}else{
			errorCount++;
		}
		request.getSession().setAttribute("ERROR", errorCount);
		return errorCount >=3 ? "1": "";
	}
	
	//-------------------------------------------------------------------------------------
	
	
	
	
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response) {
		UserContext uc = this.getUserContext(request);
		//获取所有的菜单
		List<Menu> totalMenus = this.menuService.queryAll();
		Map<Long, Menu> idMenus = new HashMap<Long, Menu>(totalMenus.size() * 2);
		for(Menu menu : totalMenus){
			idMenus.put(menu.getMenuId(), menu);
		}
		
		//获取分配给用户的菜单
		List<Menu> userMenus = null;
		if(uc.isSuperAdmin()){
			userMenus = totalMenus;
		}else{
			Set<Long> userMenuIds = userAuthCacheService.getUserAuth(uc.getUserId()).getMenuIds();
			if(userMenuIds != null){
				userMenus = new ArrayList<Menu>(userMenuIds.size());
				for(Long id : userMenuIds){
					userMenus.add(idMenus.get(id));
				}
			}else{
				userMenus = new ArrayList<Menu>(0);
			}
		}
		
		//用户菜单建立  <pid, List<子菜单>>
		Map<Long, List<Menu>> pidMenus = new HashMap<Long, List<Menu>>(userMenus.size());
		List<Menu> menus = null;
		for(Menu menu : userMenus){
			menus = pidMenus.get(menu.getParentId());
			if(menus == null){
				menus = new ArrayList<Menu>();
				pidMenus.put(menu.getParentId(), menus);
			}
			menus.add(menu);
		}
		
		//用户所有tab菜单
		List<Menu> tabMenus = pidMenus.get(0L);
		sortMenu(tabMenus);
		
		StringBuilder strb = new StringBuilder();
		//默认第一个tab菜单打开
		boolean activce = true;
		for(Menu node : tabMenus){
			//递归创建所有菜单的HTML节点
			strb.append(this.createMenuTree(request, node, pidMenus, 1, activce));
			activce=false;
		}
		return new ModelAndView("main")
				.addObject("menus", strb.toString())
				.addObject("idMenus", JSON.toJSONString(idMenus))
				.addObject("uc", uc);
	}
	
	
	private String createMenu(HttpServletRequest request){
		UserContext uc = this.getUserContext(request);
		//获取所有的菜单
		List<Menu> totalMenus = this.menuService.queryAll();
		Map<Long, Menu> idMenus = new HashMap<Long, Menu>(totalMenus.size() * 2);
		for(Menu menu : totalMenus){
			idMenus.put(menu.getMenuId(), menu);
		}
		
		//获取分配给用户的菜单
		List<Menu> userMenus = null;
		if(uc.isSuperAdmin()){
			userMenus = totalMenus;
		}else{
			Set<Long> userMenuIds = userAuthCacheService.getUserAuth(uc.getUserId()).getMenuIds();
			if(userMenuIds != null){
				userMenus = new ArrayList<Menu>(userMenuIds.size());
				for(Long id : userMenuIds){
					userMenus.add(idMenus.get(id));
				}
			}else{
				userMenus = new ArrayList<Menu>(0);
			}
		}
		
		//用户菜单建立  <pid, List<子菜单>>
		Map<Long, List<Menu>> pidMenus = new HashMap<Long, List<Menu>>(userMenus.size());
		List<Menu> menus = null;
		for(Menu menu : userMenus){
			menus = pidMenus.get(menu.getParentId());
			if(menus == null){
				menus = new ArrayList<Menu>();
				pidMenus.put(menu.getParentId(), menus);
			}
			menus.add(menu);
		}
		
		//用户所有tab菜单
		List<Menu> tabMenus = pidMenus.get(0L);
		sortMenu(tabMenus);
		
		StringBuilder strb = new StringBuilder();
		//默认第一个tab菜单打开
		boolean activce = true;
		for(Menu node : tabMenus){
			//递归创建所有菜单的HTML节点
			strb.append(this.createMenuTree(request, node, pidMenus, 1, activce));
			activce=false;
		}
		return strb.toString();
	}

	private String createMenuTree(HttpServletRequest request, Menu node, Map<Long, List<Menu>> pidMenus, int level, boolean activce){
		StringBuilder strb = new StringBuilder();
		
		boolean leaf = node.getMenuType() == 3;
		strb.append("<li").append(activce ? " class='active'" : "").append(">")
			.append(leaf? "<a class='J_menuItem' href=\""+request.getContextPath()+node.getMenuUrl()+"\">"+node.getMenuName()+"</a>" 
						: "<a href='#'><i class='fa fa-th-large'></i><span class='nav-label'>"+node.getMenuName()+"</span><span class='fa arrow'></span></a>");
					
		List<Menu> subMenus = pidMenus.get(node.getMenuId());
		if(subMenus != null && subMenus.size() > 0){
			strb.append("<ul class='"+menuCss.get(level)+(activce ? " collapse in' aria-expanded=true'":" collapse'")+" >");
			sortMenu(subMenus);
			for(Menu menu : subMenus){
				strb.append(this.createMenuTree(request, menu, pidMenus, level+1, false));
			}
			strb.append("</ul>");
		}
		strb.append("</li>");
		return strb.toString();
	}
	
	
	private void sortMenu(List<Menu> menus){
		if(menus == null || menus.size() <2)
			return ;
		Collections.sort(menus, new Comparator<Menu>() {

			@Override
			public int compare(Menu o1, Menu o2) {
				return o1.getOrderNo() - o2.getOrderNo();
			}
		});
	}

	public List<UserContextAccessor> getAccessores() {
		if(accessores == null || accessores.size() < 1)
			return null;
		
		if(!sort.booleanValue()){
			synchronized(sort){
				Collections.sort(accessores, new OrderComparator());
			}
		}
		
		return accessores;
	}

	public void setAccessores(List<UserContextAccessor> accessores) {
		this.accessores = accessores;
	}
}
