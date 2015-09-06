package org.whale.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.TreeUtil;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;
import org.whale.system.jqgrid.Grid;
import org.whale.system.service.AuthService;
import org.whale.system.service.MenuService;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {

	@Autowired
	private AuthService authService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserAuthCacheService userAuthCacheService;
	@Autowired
	private DictCacheService dictCacheService;
	
	@org.whale.system.annotation.auth.Auth(code="AUTH_LIST",name="查询权限")
	@RequestMapping("/goTree")
	public ModelAndView goTree(HttpServletRequest request, HttpServletResponse response, Long clkId){
		
		List<Menu> menus = this.menuService.queryAll();
		if(menus == null)
			menus = new ArrayList<Menu>(1);
		if(clkId == null){
			if(menus.size() > 0)
				clkId = menus.get(0).getMenuId();
			else
				clkId = 0L;
		}
		
		return new ModelAndView("system/auth/menu_tree")
			.addObject("nodes", MenuController.toMenuJson(menus))
			.addObject("clkId", clkId);
	}
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param authName
	 * @param authCode
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_LIST",name="查询权限")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response, Long menuId){
		
		return new ModelAndView("system/auth/auth_list")
			.addObject("menuId", menuId)
			.addObject("usable", SysConstant.STATUS_NORMAL);
	}
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param authName
	 * @param authCode
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_LIST",name="查询权限")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, String authName, String authCode, Long menuId){
		Page page = Grid.newPage(request);
		page.put("authName", authName);
		page.put("authCode", authCode);
		Menu menu = this.menuService.get(menuId);
		if(menu != null){
			List<Long> menuIds = null;
			if(menu.getMenuType() == 3){
				menuIds = new ArrayList<Long>(1);
				menuIds.add(menuId);
			}else{
				List<Menu> menus = this.menuService.queryAll();
				List<Menu> leafMenus = TreeUtil.getAllSubLeaf(menus, menuId);
				if(leafMenus != null){
					menuIds = LangUtil.getIdList(leafMenus,"menuId");
				}
				if(menuIds == null){
					menuIds = new ArrayList<Long>(1);
					menuIds.add(-1L);
				}
			}
			page.put("menuIds", LangUtil.joinIds(menuIds));
		}
		
		this.authService.queryPage(page);
		WebUtil.print(request, response, Grid.grid(page));
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_SAVE",name="新增权限")
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response, Long menuId){
		Menu menu = this.menuService.get(menuId);
		Long defaultId = null;
		if(menu.getMenuType() == 3){
			defaultId = menuId;
		}
		
		return new ModelAndView("system/auth/auth_save")
			.addObject("nodes", MenuController.toMenuJson(this.menuService.queryAll()))
			.addObject("defaultId", defaultId);
	}
	
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param auth
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_SAVE",name="新增权限")
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Auth auth){		
		Menu menu = this.menuService.get(auth.getMenuId());
		if(menu == null){
			WebUtil.printFail(request, response, "找不到对应的菜单");
			return ;
		}
		if(menu.getMenuType() != 3){
			WebUtil.printFail(request, response, "只能挂在叶子菜单之下");
			return ;
		}
		
		this.authService.save(auth);
		
		WebUtil.printSuccess(request, response);
	}
	
	/**
	 * 跳转到更新页面
	 * @param request
	 * @param response
	 * @param authId
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_UPDATE",name="修改权限")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long authId){
		Auth auth = this.authService.get(authId);
		return new ModelAndView("system/auth/auth_update")
			.addObject("nodes", MenuController.toMenuJson(this.menuService.queryAll()))
			.addObject("item", auth);
	}
	
	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param auth
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_UPDATE",name="修改权限")
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, Auth auth){
		Menu menu = this.menuService.get(auth.getMenuId());
		if(menu == null){
			WebUtil.printFail(request, response, "找不到对应的菜单");
			return ;
		}
		if(menu.getMenuType() != 3){
			WebUtil.printFail(request, response, "只能挂在叶子菜单之下");
			return ;
		}
		
		this.authService.update(auth);
		WebUtil.printSuccess(request, response);
	}
	
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param authId
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_DEL",name="删除权限")
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, String ids){
		List<Long> authIds = LangUtil.splitIds(ids);
		if(authIds == null || authIds.size() < 1){
			WebUtil.printFail(request, response, "请选择记录");
			return ;
		}
		this.authService.delete(authIds);
		
		if(dictCacheService.isValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_ITEM_FLUSH_AUTH, "auto")){
			this.userAuthCacheService.init(null);
		}
		
		WebUtil.printSuccess(request, response);
	}

}
