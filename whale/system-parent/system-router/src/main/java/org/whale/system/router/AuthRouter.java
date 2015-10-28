package org.whale.system.router;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TreeUtil;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;
import org.whale.system.service.AuthService;
import org.whale.system.service.MenuService;

@Controller
@RequestMapping("/auth")
public class AuthRouter extends BaseRouter {

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
	public ModelAndView goTree(Long clkId){
		
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
			.addObject("nodes", MenuRouter.toMenuJson(menus))
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
	public ModelAndView goList(Long menuId){
		
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
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(String authName, String authCode, Long menuId){
		Page page = this.newPage();
		StringBuilder strb = new StringBuilder();
		strb.append(" FROM sys_auth t WHERE 1=1 ");
		
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
			strb.append(" AND t.menuId in(").append(LangUtil.joinIds(menuIds)).append(")");
		}
		
		if(Strings.isNotBlank(authName)){
			strb.append(" AND t.authName like ?");
			page.addArg("%"+authName.trim()+"%");
		}
		if(Strings.isNotBlank(authCode)){
			strb.append(" AND t.authCode like ?");
			page.addArg("%"+authCode.trim()+"%");
		}
		
		page.setCountSql("SELECT count(1) "+strb.toString());
		page.setSql("SELECT t.*,(select m.menuName from sys_menu m where m.menuId = t.menuId) as menuName "+strb.toString()+" ORDER BY t.authCode");
		
		this.authService.queryPage(page);
		return page;
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_SAVE",name="新增权限")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long menuId){
		Menu menu = this.menuService.get(menuId);
		Long defaultId = null;
		if(menu.getMenuType() == 3){
			defaultId = menuId;
		}
		
		return new ModelAndView("system/auth/auth_save")
			.addObject("nodes", MenuRouter.toMenuJson(this.menuService.queryAll()))
			.addObject("defaultId", defaultId);
	}
	
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param auth
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_SAVE",name="新增权限")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(Auth auth){		
		Menu menu = this.menuService.get(auth.getMenuId());
		if(menu == null){
			return Rs.fail("找不到对应的菜单");
		}
		if(menu.getMenuType() != 3){
			return Rs.fail("只能挂在叶子菜单之下");
		}
		
		this.authService.save(auth);
		
		return Rs.success();
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
	public ModelAndView goUpdate(String authCode){
		Auth auth = this.authService.get(authCode);
		return new ModelAndView("system/auth/auth_update")
			.addObject("nodes", MenuRouter.toMenuJson(this.menuService.queryAll()))
			.addObject("item", auth);
	}
	
	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param auth
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_UPDATE",name="修改权限")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Auth auth){
		Menu menu = this.menuService.get(auth.getMenuId());
		if(menu == null){
			return Rs.fail("找不到对应的菜单");
		}
		if(menu.getMenuType() != 3){
			return Rs.fail("只能挂在叶子菜单之下");
		}
		
		this.authService.update(auth);
		return Rs.success();
	}
	
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param authId
	 */
	@org.whale.system.annotation.auth.Auth(code="AUTH_DEL",name="删除权限")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(String authCodes){
		if(Strings.isBlank(authCodes)){
			return Rs.fail("请选择记录");
		}
		String[] authCodeS = authCodes.split(",");
		if(authCodeS == null || authCodeS.length < 1){
			return Rs.fail("请选择记录");
		}
		this.authService.transDelete(authCodeS);
		
		if(dictCacheService.isValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_ITEM_FLUSH_AUTH, "auto")){
			this.userAuthCacheService.init(null);
		}
		
		return Rs.success();
	}

}
