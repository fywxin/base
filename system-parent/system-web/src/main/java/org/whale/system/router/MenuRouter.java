package org.whale.system.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Q;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.Menu;
import org.whale.system.service.MenuService;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/menu")
public class MenuRouter extends BaseRouter {
	
	@Autowired
	private MenuService menuService;
	
	@Auth(code="menu:list",name="查询菜单")
	@RequestMapping("/goTree")
	public ModelAndView goTree(Long parentId){
		
		return new ModelAndView("system/menu/menu_tree")
		.addObject("nodes", JSON.toJSONString(this.menuService.queryAll()))
		.addObject("parentId", parentId);
	}
	
	@Auth(code="menu:list",name="查询菜单")
	@RequestMapping("/goList")
	public ModelAndView goList(Long parentId){
		if(parentId == null){
			parentId = 0L;
		}
		return new ModelAndView("system/menu/menu_list").addObject("parentId", parentId);
	}
	
	@Auth(code="menu:list",name="查询菜单")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(Long parentId){
		Page page = this.newPage();
		Q q = page.newQ(Menu.class);
		q.select().selectWrap(",(SELECT p.menuName from sys_menu p WHERE p.menuId = parentId) as parentName ");
		if (parentId != null && parentId != 0l){
			q.eq(Menu.F_parentId, parentId);
		}

		this.menuService.queryPage(page);
		
		return page;
	}
	
	@Auth(code="menu:save",name="新增菜单")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long parentId){
		if(parentId == null)
			parentId = 0L;
		
		return new ModelAndView("system/menu/menu_save")
				.addObject("parentId", parentId)
				.addObject("nodes", toMenuJson(this.menuService.addRootMenu(this.menuService.getDirMenus(), "菜单管理")))
				.addObject("nextNum", this.menuService.getNextOrder(parentId));
	}
	
	/**
	 * 保存操作
	 * @param menu
	 */
	@Auth(code="menu:save",name="新增菜单")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(Menu menu){
		if(menu.getParentId() == null)
			menu.setParentId(0L);
		
		if(menu.getMenuType() == 3 && Strings.isBlank(menu.getMenuUrl())){
			return Rs.fail("链接菜单 URL 不能为空");
		}
		
		if(menu.getMenuType() == 1 && menu.getParentId() != 0 ){
			return Rs.fail("类型错误");
		}
		
		if(menu.getMenuType() != 1 && menu.getParentId() == 0){
			return Rs.fail("请选择父菜单");
		}
		if(menu.getPublicFlag() && menu.getMenuType() != 3){
			return Rs.fail("非链接菜单不能是公共菜单");
		}
		
		if(menu.getOrderNo() == null){
			menu.setOrderNo(1);
		}
		
		this.menuService.save(menu);
		return Rs.success(menu.getMenuId());
	}
	
	@Auth(code="menu:update",name="修改菜单")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long menuId, Integer view){
		Menu menu = this.menuService.get(menuId);
		return new ModelAndView("system/menu/menu_update")
				.addObject("item", menu)
				.addObject("nodes", toMenuJson(this.menuService.addRootMenu(this.menuService.getDirMenus(), "菜单管理")))
				.addObject("view", view);
	}
	
	/**
	 * 更新操作
	 * @param menu
	 */
	@Auth(code="menu:update",name="修改菜单")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Menu menu, String oldMenuName){
		if(menu.getParentId() == null)
			menu.setParentId(0L);
		
		if(menu.getMenuType() == 3 && Strings.isBlank(menu.getMenuUrl())){
			return Rs.fail("链接菜单 URL 不能为空");
		}
		
		if(menu.getMenuType() == 1 && menu.getParentId() != 0 ){
			return Rs.fail("类型错误");
		}
		
		if(menu.getMenuType() != 1 && menu.getParentId() == 0){
			return Rs.fail("请选择父菜单");
		}
		
		if(menu.getOrderNo() == null){
			menu.setOrderNo(1);
		}
		
		this.menuService.update(menu);
		
		return Rs.success();
	}
	
	
	/**
	 * 删除操作
	 * @param menuId
	 */
	@Auth(code="menu:del",name="删除菜单")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(Long menuId){
		if(menuId == null){
			return Rs.fail("请选择待删除菜单");
		}
		String info = this.menuService.checkDelete(menuId);
		if(info != null){
			return Rs.fail(info);
		}
		this.menuService.delete(menuId);
		return Rs.success();
	}
	
	public static String toMenuJson(List<Menu> menus){
		if(menus == null || menus.size() < 1)
			return "[]";
		return JSON.toJSONString(menus);
	}
	
} 
