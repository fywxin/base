package org.whale.system.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
			WebUtil.printFail(request, response, "用户名不能为空");
			return ;
		}
		
		if(Strings.isBlank(password) && Strings.isBlank(encryptedPwd)){
			WebUtil.printFail(request, response, "密码不能为空");
			return ;
		}
		
		String msg = null;
		if((Strings.isNotBlank(password) || Strings.isBlank(encryptedPwd)) && SysConstant.LOGIC_TRUE.equals(dictCacheService.getItemValue(DictConstant.DICT_SYS_CONF, "VERITY_CODE_FLAG")) && (msg = this.checkVerify(request)) != null){
			WebUtil.printFail(request, response, msg);
			return ;
		}
		
		User user = this.userService.getByUserName(userName);
		if(user == null || user.getStatus() == SysConstant.STATUS_DEL){
			WebUtil.printFail(request, response, "用户名不存在");
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
				WebUtil.printFail(request, response, "密码不能为空");
				return ;
			}
		}else{
			if(!this.userService.validPasswd(password, user.getPassword())){
				WebUtil.printFail(request, response, "密码错误");
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
		} catch (Exception e) {
			logger.error("绑定用户上下文出现异常", e);
			this.clearUserContext(request);
			WebUtil.printFail(request, response, "绑定用户出现异常，请联系管理员");
			return ;
		}
		if(SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"))){
			WebUtil.printSuccess(request, response, Strings.isBlank(password) ? encryptedPwd : this.userService.getEncryptedPwd(password));
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
		user.setLoginNum(user.getLoginNum() == null ? 1 : user.getLoginNum()+1);
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
	
	//-------------------------------------------------------------------------------------
	
	
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response) {
		UserContext uc = this.getUserContext(request);
		List<Menu> tabMenus = null;
		List<Menu> allMenus = null;
		List<Menu> totalMenus = this.menuService.queryAll();
		
		if(uc.isSuperAdmin()){
			allMenus = totalMenus;
			if(allMenus != null){
				tabMenus = new ArrayList<Menu>();
				for(Menu menu : allMenus){
					if(menu.getMenuType() == 1){
						tabMenus.add(menu);
					}
				}
			}
		}else{
			Map<Long, Menu> totalMenuMap = new HashMap<Long, Menu>();
			for(Menu m : totalMenus){
				totalMenuMap.put(m.getMenuId(), m);
			}
			
			Set<Long> leafIds = userAuthCacheService.getUserAuth(uc.getUserId()).getLeafMenuIds();
			List<Menu> pubMenus = this.menuService.getPublicMenus();
			if(pubMenus != null && pubMenus.size() > 0){
				for(Menu m : pubMenus){
					leafIds.add(m.getMenuId());
				}
			}
			
			Iterator<Long> iterable = leafIds.iterator();
			
			Set<Long> dirIds = new HashSet<Long>();
			allMenus = new ArrayList<Menu>(leafIds.size()*2);
			
			Menu m = null;
			while(iterable.hasNext()){
				m = totalMenuMap.get(iterable.next());
				allMenus.add(m);
				while(m != null && m.getParentId() != 0 && m.getParentId() != null){
					m = totalMenuMap.get(m.getParentId());
					if(m == null)
						break;
					dirIds.add(m.getMenuId());
				}
			}
			
			tabMenus = new ArrayList<Menu>();
			
			iterable = dirIds.iterator();
			while(iterable.hasNext()){
				m = totalMenuMap.get(iterable.next());
				allMenus.add(m);
				if(m.getMenuType() == 1){
					tabMenus.add(m);
				}
			}
		}
		
		Collections.sort(tabMenus, new Comparator<Menu>() {

			@Override
			public int compare(Menu o1, Menu o2) {
				return o1.getOrderNo() - o2.getOrderNo();
			}
		});
		
		String html = "";
		String js = "";
		Long firstTabId = 0L;
		if(tabMenus != null && tabMenus.size() > 0){
			html = this.createHtml(tabMenus);
			js = this.createJs(allMenus, tabMenus);
			firstTabId = tabMenus.get(0).getMenuId();
		}
		return new ModelAndView("main")
			.addObject("html", html)
			.addObject("js", js)
			.addObject("firstTabId", firstTabId)
			.addObject("realName", uc.getRealName());
	}

	
	private String createHtml(List<Menu> tabMenus){
		StringBuilder strb = new StringBuilder();
		for(Menu tabMenu : tabMenus){
			if(tabMenu != null){
				strb.append("<div title='").append(tabMenu.getMenuName()).append("' class='l-scroll' id='div_").append(tabMenu.getMenuId()).append("'>")
					.append("<ul id='tab_").append(tabMenu.getMenuId()).append("' class='ztree' style='overflow:auto;' />")
					.append("</div>\n");
			}
		}
		
		return strb.toString();
	}
	
	/**
	 * 根据tabMenu 分隔出所有tab对应的所有子节点
	 * @param menuList
	 * @param tabMenu
	 * @return
	 */
	private List<Menu> divideByTabMenu(List<Menu> allMenuList, Long parentMenuId){
		List<Menu> list = new ArrayList<Menu>();
		
		for(Menu menu : allMenuList){
			if(menu.getParentId().longValue() == parentMenuId.longValue()){
				list.add(menu);
				//文件夹类型菜单，递归获取其子菜单
				if(menu.getMenuType() == 2){
					list.addAll(this.divideByTabMenu(allMenuList, menu.getMenuId()));
				}
			}
		}
		Collections.sort(list, new Comparator<Menu>() {

			@Override
			public int compare(Menu o1, Menu o2) {
				return o1.getOrderNo() - o2.getOrderNo();
			}
		});
		return list;
	}
	
	
	/**
	 * 根据tab菜单，生成对应数量的ztree js
	 * 关键：每颗tab树菜单下的节点，需由tab向下在 总菜单列表中递归选择出来
	 * @param allMenuList 所有菜单列表
	 * @param tabMenus  tab菜单列表
	 * @return
	 */
	private String createJs(List<Menu> allMenuList, List<Menu> tabMenus){
		StringBuilder strb = new StringBuilder();
		
		List<Menu> menuTree = null;
		for(Menu tabMenu : tabMenus){
			if(tabMenu != null){
				menuTree = this.divideByTabMenu(allMenuList, tabMenu.getMenuId());
				if(menuTree == null)
					continue ;
				strb.append("$.fn.zTree.init($(\"#tab_").append(tabMenu.getMenuId()).append("\"), setting, ").append(JSON.toJSONString(menuTree)).append(");\n\n");
			}
		}
		return strb.toString();
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
