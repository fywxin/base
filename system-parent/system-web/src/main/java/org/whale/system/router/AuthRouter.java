package org.whale.system.router;

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
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.util.ListUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TreeUtil;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;
import org.whale.system.domain.User;
import org.whale.system.service.AuthService;
import org.whale.system.service.MenuService;
import org.whale.system.service.UserService;

import java.util.ArrayList;
import java.util.List;

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
	@Autowired
	private UserService userService;
	
	@org.whale.system.annotation.auth.Auth(code="auth:list",name="查询权限")
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
		return new ModelAndView("system/auth/auth_tree")
		.addObject("nodes", MenuRouter.toMenuJson(menus))
		.addObject("clkId", clkId);
	}
	
	
	/**
	 * 跳转到列表页面
	 * @param menuId
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="auth:list",name="查询权限")
	@RequestMapping("/goList")
	public ModelAndView goList(Long menuId){
		
		return new ModelAndView("system/auth/auth_list")
			.addObject("menuId", menuId)
			.addObject("usable", SysConstant.STATUS_NORMAL);
	}
	
	/**
	 * 跳转到列表页面
	 * @param authName
	 * @param authCode
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="auth:list",name="查询权限")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(String authName, String authCode, Long menuId, Long userId, Long roleId){
		Page page = this.newPage();
		StringBuilder strb = new StringBuilder();
		strb.append(" FROM sys_auth t WHERE 1=1 ");
		
		if(userId == null && roleId == null){
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
						menuIds = ListUtil.listCol(leafMenus, "menuId");
					}
					if(menuIds == null){
						menuIds = new ArrayList<Long>(1);
						menuIds.add(-1L);
					}
				}
				strb.append(" AND t.menuId in(").append(ListUtil.join(menuIds)).append(")");
			}else{
				if(menuId != null && menuId.equals(-99L)){
					strb.append(" AND (t.menuId IS NULL OR t.menuId = 0) ");
				}
			}
			
			if(Strings.isNotBlank(authName)){
				strb.append(" AND t.authName like ?");
				page.addArg("%"+authName.trim()+"%");
			}
			if(Strings.isNotBlank(authCode)){
				strb.append(" AND t.authCode like ?");
				page.addArg("%"+authCode.trim()+"%");
			}
		}else{
			if(userId != null){
				User user = this.userService.get(userId);
				if(user == null){
					throw new BusinessException("用户["+userId+"]不存在");
				}
				if(!user.getAdminFlag()){
					strb.append(" AND t.authCode in(select DISTINCT(ra.authCode) from sys_user_role ur, sys_role_auth ra WHERE ur.roleId = ra.roleId and ur.userId = ?)");
					page.addArg(userId);
				}
			}else{
				strb.append(" AND t.authCode in(select DISTINCT(ra.authCode) from sys_role_auth ra WHERE ra.roleId = ?)");
				page.addArg(roleId);
			}
		}
		
		page.setCountSql("SELECT count(1) "+strb.toString());
		page.setSql("SELECT t.*,(select m.menuName from sys_menu m where m.menuId = t.menuId) as menuName "+strb.toString()+" ORDER BY t.authCode");
		
		this.authService.queryPage(page);
		return page;
	}
	
	
	/**
	 * 跳转到更新页面
	 * @param authCode
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="auth:update",name="修改权限")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(String authCode){
		Auth auth = this.authService.get(authCode);
		return new ModelAndView("system/auth/auth_update")
			.addObject("nodes", MenuRouter.toMenuJson(this.menuService.queryAll()))
			.addObject("item", auth);
	}
	
	/**
	 * 更新操作
	 * @param auth
	 */
	@org.whale.system.annotation.auth.Auth(code="auth:update",name="修改权限")
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

}
