package org.whale.system.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.AuthAdmin;
import org.whale.system.annotation.auth.AuthLogin;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Rs;
import org.whale.system.base.UserContext;
import org.whale.system.base.UserContextAccessor;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.*;
import org.whale.system.domain.Dept;
import org.whale.system.domain.Menu;
import org.whale.system.domain.Role;
import org.whale.system.domain.User;
import org.whale.system.service.DeptService;
import org.whale.system.service.MenuService;
import org.whale.system.service.RoleService;
import org.whale.system.service.UserService;
import org.whale.system.servlet.MySessionContext;

@Controller
public class MainRouter extends BaseRouter {
	
	private static final Logger logger = LoggerFactory.getLogger(MainRouter.class);
	
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
	@Autowired
	private RoleService roleService;
	
	private Boolean sort = Boolean.FALSE;

	/**
	 * 返回管理员
	 * @return
	 */
	@AuthLogin
	@ResponseBody
	@RequestMapping("/adminLogin")
	public Rs adminLogin(){
		if (!UserContext.get().getIsMock()){
			return Rs.fail("用户不存在");
		}
		this.bindUserContext(this.userService.getByUserName("admin"));
		return Rs.success();
	}

	/**
	 * 模拟登录
	 * @param userId
	 * @return
	 */
	@AuthAdmin
	@ResponseBody
	@RequestMapping("/mockLogin")
	public Rs mockLogin(Long userId){
		User user = this.userService.get(userId);
		if (user == null){
			return Rs.fail("用户["+userId+"]不存在");
		}
		this.bindUserContext(user);
		UserContext uc = UserContext.get();
		ReflectionUtil.setFieldValue(uc, "isMock", true);
		return Rs.success();
	}

	
	/**
	 * 用户登录
	 * @param userName
	 * @param password
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Rs doLogin(String userName, String password, String encryptedPwd){
		if(logger.isInfoEnabled()){
			logger.info("用户登录[{}/{}] ：ip[{}]",userName, password, WebUtil.getIp());
		}
		
		if(Strings.isBlank(userName)){
			return Rs.fail("用户名不能为空", "1"+errorCount());
		}
		
		if(Strings.isBlank(password) && Strings.isBlank(encryptedPwd)){
			return Rs.fail("密码不能为空", "2"+errorCount());
		}
		
		Integer errorCount = (Integer)WebUtil.getSession().getAttribute("ERROR");
		String msg = null;
		if((Strings.isNotBlank(password) || Strings.isBlank(encryptedPwd)) 
				&& SysConstant.LOGIC_TRUE.equals(dictCacheService.getItemValue(DictConstant.DICT_SYS_CONF, "VERITY_CODE_FLAG")) 
				&& (errorCount != null && errorCount >= 3 ) 
				&& (msg = this.checkVerify()) != null){
			return Rs.fail(msg, "3"+errorCount());
		}
		
		User user = this.userService.getByUserName(userName);
		if(user == null || user.getStatus() == SysConstant.STATUS_DEL){
			return Rs.fail("用户名不存在", "1"+errorCount());
		}
		if(user.getStatus() != SysConstant.STATUS_NORMAL){
			return Rs.fail("用户已禁止登录，请联系管理员", "1"+errorCount());
		}
		if(Strings.isBlank(password)){
			if(SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"))){
				if(!encryptedPwd.equals(user.getPassword())){
					return Rs.fail("请登录", "1"+errorCount());
				}
			}else{
				return Rs.fail("密码不能为空", "2"+errorCount());
			}
		}else{
			if(!this.userService.validPasswd(password, user.getPassword())){
				return Rs.fail("密码错误", "2"+errorCount());
			}
		}
		try {
			if(SysConstant.LOGIC_TRUE.equals(dictCacheService.getItemValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_TIEM_ONLY_SINGLE_LOGIN))){
				List<HttpSession> sessions = null;
				UserContext uc = null;
				if((sessions = MySessionContext.getSessionByUserId(user.getUserId())) != null && sessions.size() > 0) {
					uc = (UserContext) sessions.get(0).getAttribute(SysConstant.USER_CONTEXT_KEY);
					if(uc != null && !uc.getIp().equals(WebUtil.getIp())){
						return Rs.fail("该帐号已于["+TimeUtil.formatTime(uc.getLoginTime(), TimeUtil.COMMON_FORMAT)+"] 在IP: ["+uc.getIp()+"] 登录，不允许重复登录!", "1"+errorCount());
						
					}else{
						for(HttpSession session : sessions){
							if(!session.getId().equals(WebUtil.getSession().getId())){
								session.invalidate();
								MySessionContext.delSession(session);
							}
						}
					}
				}
			}
			this.bind(user);
			WebUtil.getSession().removeAttribute("ERROR");
		} catch (Exception e) {
			logger.error("绑定用户上下文出现异常", e);
			this.clearUserContext();
			return Rs.fail("绑定用户出现异常，请联系管理员", "1"+errorCount());
		}
		if(SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"))){
			return Rs.success(Strings.isBlank(password) ? encryptedPwd : user.getPassword());
		}else{
			return Rs.success();
		}
	}
	
	/**
	 * 验证验证码
	 * @return 返回null表示成功
	 */
	private String checkVerify(){
		String verifycode = WebUtil.getRequest().getParameter("verifycode");
		if(Strings.isBlank(verifycode)){
			return "请输入验证码";
		}
		Object code = WebUtil.getSession().getAttribute(SysConstant.VERITY_CODE_KEY);
		if(code == null){
			return "登入超时，请重新登入";
		}
		if(!verifycode.equalsIgnoreCase(code.toString())){
			return "验证码错误";
		}
		this.clearVerify();
		return null;
	}
	
	/**
	 * 绑定用户数据
	 * @param user
	 */
	private void bind(User user){
		this.bindUserContext(user);
		this.updateUserLoginInfo(user);
	}
	
	/**
	 * 绑定用户上下文
	 * @param user
	 */
	public void bindUserContext(User user){
		UserContext uc = new UserContext();
		uc.setIp(WebUtil.getIp());
		uc.setLoginTime(new Date());
		uc.setRealName(user.getRealName());
		uc.setUserId(user.getUserId());
		uc.setUserName(user.getUserName());
		uc.setUserType(user.getUserType());
		if(!user.getAdminFlag()){
			Dept dept = this.deptService.get(user.getDeptId());
			if(dept == null){
				dept = new Dept();
				dept.setId(0L);
				dept.setDeptCode("ROOT");
				dept.setDeptName(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "ITEM_DEPT_ROOT"));
			}
			uc.getExt().put("dept", dept);
		}

		ReflectionUtil.setFieldValue(uc, "isSuperAdmin", user.getAdminFlag());
		if(!uc.isSuperAdmin()){
			List<Role> roles = this.roleService.getByUserId(uc.getUserId());
			uc.getExt().put("roles", roles);
		}
		
		HttpSession session = WebUtil.getSession();
		
		session.setAttribute(SysConstant.USER_CONTEXT_KEY, uc);
		uc.setSessionId(WebUtil.getSession().getId());
		//绑定菜单
		this.attachMenu();
		UserContext.set(uc);

		if(accessores != null && accessores.size() > 0){
			for(UserContextAccessor accessor : this.getAccessores()){
				accessor.onLogin(uc);
			}
		}
	}
	
	/**
	 * 更新用户登录信息
	 * @param user
	 */
	private void updateUserLoginInfo(User user){
		user.setLoginIp(WebUtil.getIp());
		user.setLastLoginTime(System.currentTimeMillis());
		this.userService.update(user);
	}
	
	/**
	 * 退出登入
	 */
	@ResponseBody
	@RequestMapping("/loginOut")
	public Rs loginOut() {
		try {
			UserContext uc = (UserContext)WebUtil.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
			if(uc != null && uc.getUserId() != null){
				WebUtil.getSession().invalidate();
				MySessionContext.delSession(WebUtil.getSession());
			}
			return Rs.success();
		} catch (RuntimeException e) {
			logger.error("用户退出登入异常: "+e.getMessage(), e);
			return Rs.fail("用户退出登入出现异常");
		}
	}
	
	/**
	 * 清除验证码
	 */
	private void clearVerify(){
		WebUtil.getSession().removeAttribute(SysConstant.VERITY_CODE_KEY);
	}
	
	/**
	 * 清空用户上下文信息
	 */
	private void clearUserContext(){
		WebUtil.getSession().removeAttribute(SysConstant.USER_CONTEXT_KEY);
		ThreadContext.getContext().remove(ThreadContext.KEY_USER_CONTEXT);
	}
	
	/**
	 * 登录错误次数维护
	 * @return
	 */
	private String errorCount(){
		Integer errorCount = (Integer)WebUtil.getSession().getAttribute("ERROR");
		if(errorCount == null){
			errorCount =1 ;
		}else{
			errorCount++;
		}
		WebUtil.getSession().setAttribute("ERROR", errorCount);
		return errorCount >=3 ? "1": "";
	}
	
	//-------------------------------------------------------------------------------------
	
	
	
	
	@RequestMapping("/main")
	public ModelAndView main() {
		UserContext uc = this.getUserContext();
		if(uc.getExt().get("menuStr") == null){
			this.attachMenu();
		}
		
		return new ModelAndView("main")
				.addObject("uc", uc);
	}
	
	
	private void attachMenu(){
		UserContext uc = this.getUserContext();
		//获取所有的菜单
		//获取所有的菜单
		List<Menu> totalMenus = this.menuService.queryAll();
		//公共菜单
		List<Menu> publicMenus = new ArrayList<Menu>();
		//TODO 未配置权限的菜单
		
		Map<Long, Menu> idMenus = new HashMap<Long, Menu>(totalMenus.size() * 2);
		for(Menu menu : totalMenus){
			idMenus.put(menu.getMenuId(), menu);
			if(menu.getPublicFlag() && menu.getMenuType() == 3){
				publicMenus.add(menu);
			}
		}
		
		//获取分配给用户的菜单
		Set<Menu> userMenus = null;
		if(uc.isSuperAdmin()){
			userMenus = new HashSet<Menu>(totalMenus);
		}else{
			userMenus = new HashSet<Menu>();
			if(publicMenus.size() > 0){
				for(Menu pubMenu : publicMenus){
					userMenus.add(pubMenu);
					pubMenu = idMenus.get(pubMenu.getParentId());
					while(pubMenu != null){//递归加入公共菜单的父菜单
						userMenus.add(pubMenu);
						pubMenu = idMenus.get(pubMenu.getParentId());
					}
				}
			}
			
			Set<Long> userMenuIds = userAuthCacheService.getUserAuth(uc.getUserId()).getMenuIds();
			if(userMenuIds != null){
				for(Long id : userMenuIds){
					userMenus.add(idMenus.get(id));
				}
			}
		}
		
		StringBuilder strb = new StringBuilder();
		if(userMenus.size() > 0){
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
			//默认第一个tab菜单打开
			boolean activce = true;
			for(Menu node : tabMenus){
				//递归创建所有菜单的HTML节点
				strb.append(this.createMenuTree(node, pidMenus, 1, activce));
				activce=false;
			}
		}
		uc.getExt().put("menuStr", strb.toString());
	}

	private String createMenuTree(Menu node, Map<Long, List<Menu>> pidMenus, int level, boolean activce){
		StringBuilder strb = new StringBuilder();
		
		if(node.getMenuType() != 3){//tab | folder
			if(node.getMenuType() == 1){//tab
				strb.append("<li onclick='accordionClk(this)'>")
					.append("	<a href='javascript:;' data-target='.menu-").append(node.getMenuId()).append("' class='nav-header' data-toggle='collapse' ").append(activce? "" : "class='collapsed'").append(" >")
					.append("		<i class='fa fa-fw ").append(Strings.isBlank(node.getInco()) ? "fa-dashboard" : node.getInco()).append("'></i> "+node.getMenuName()+"<i class='fa fa-collapse'></i>")
					.append("	</a>")
					.append("</li><li>")
					.append("	<ul class='menu-"+node.getMenuId()+" nav nav-list collapse ").append(activce? "in' clk='1'" : "'").append(" tabul='1'>");
			}else{//folder
				strb.append("<li>")
					.append("	<a href='javascript:;' data-target='.menu-").append(node.getMenuId()).append("' data-toggle='collapse' class='collapsed'>")
					.append("		<span class='fa fa-folder' style='padding-left:").append(level>2 ? 15 : 0).append("px'></span> "+node.getMenuName()).append("<i class='fa fa-collapse'></i>")
					.append("	</a>")
					.append("	<ul class='menu-"+node.getMenuId()+" nav nav-list collapse' style='border:0px;padding-left:").append(level>2 ? 15 : 0).append("px'>");
			}
			
				
			List<Menu> subMenus = pidMenus.get(node.getMenuId());
			if(subMenus != null && subMenus.size() > 0){
				sortMenu(subMenus);
				for(Menu menu : subMenus){
					strb.append(this.createMenuTree(menu, pidMenus, level+1, false));
				}
				
			}
			strb.append("</ul></li>");
		}else{//leaf
			//strb.append("<li ><a href='javascript:;' onclick=\"tab.addTab({'id':'"+node.getMenuId()+"', 'name':'"+node.getMenuName()+"','url':'"+node.getMenuUrl()+"','active':true})\"><span class='fa ").append(Strings.isBlank(node.getInco()) ? "fa-caret-right" : node.getInco()).append(" ' style='padding-left:").append(level>2 ? 15 : 0).append("px'></span> "+node.getMenuName()+"</a></li>");
			strb.append(new StringBuilder().append("<li ><a href='javascript:;' onclick=\"goMain('").append(node.getMenuName()).append("','").append(node.getMenuUrl()).append("')\"><span class='fa ").toString()).append((Strings.isBlank(node.getInco())) ? "fa-caret-right" : node.getInco()).append(" ' style='padding-left:").append((level > 2) ? 15 : 0).append(new StringBuilder().append("px'></span> ").append(node.getMenuName()).append("</a></li>").toString());

		}
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
