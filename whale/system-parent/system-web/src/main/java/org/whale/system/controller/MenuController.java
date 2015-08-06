package org.whale.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Menu;
import org.whale.system.service.MenuService;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {
	
	@Autowired
	private MenuService menuService;
	
	@Auth(code="MENU_LIST",name="查询菜单")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){
		
		return new ModelAndView("system/menu/menu_list");
	}
	
	@Auth(code="MENU_LIST",name="查询菜单")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, Menu menu){
		Page page = this.newPage(request);
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
		page.put("menuName", menu.getMenuName());
		page.put("parentId", menu.getParentId());
		
		this.menuService.queryPage(page);
		
		WebUtil.print(request, response, page);
	}
	
	@Auth(code="MENU_SAVE",name="新增菜单")
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response, Long parentId){
		if(parentId == null)
			parentId = 0L;
		
		return new ModelAndView("system/menu/menu_save")
				.addObject("parentId", parentId)
				.addObject("nodes", toMenuJson(this.menuService.addRootMenu(this.menuService.getDirMenus(), "菜单管理")))
				.addObject("nextNum", this.menuService.getNextOrder(parentId));
	}
	
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param menu
	 */
	@Auth(code="MENU_SAVE",name="新增菜单")
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Menu menu){
		if(menu.getParentId() == null)
			menu.setParentId(0L);
		
		if(menu.getMenuType() == 3 && Strings.isBlank(menu.getMenuUrl())){
			WebUtil.printFail(request, response, "链接菜单 URL 不能为空");
			return ;
		}
		
		if(menu.getMenuType() == 1 && menu.getParentId() != 0 ){
			WebUtil.printFail(request, response, "类型错误");
			return ;
		}
		
		if(menu.getMenuType() != 1 && menu.getParentId() == 0){
			WebUtil.printFail(request, response, "请选择父菜单");
			return ;
		}
		
		if(menu.getOrderNo() == null){
			menu.setOrderNo(1);
		}
		if(menu.getIsPublic() != 1){
			menu.setIsPublic(0);
		}
		
		this.menuService.save(menu);
		WebUtil.printSuccess(request, response, menu.getMenuId());
	}
	
	@Auth(code="MENU_UPDATE",name="修改菜单")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long menuId, Integer view){
		Menu menu = this.menuService.get(menuId);
		return new ModelAndView("system/menu/menu_update")
				.addObject("item", menu)
				.addObject("nodes", toMenuJson(this.menuService.addRootMenu(this.menuService.getDirMenus(), "菜单管理")))
				.addObject("view", view);
	}
	
	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param menu
	 */
	@Auth(code="MENU_UPDATE",name="修改菜单")
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, Menu menu, String oldMenuName){
		if(menu.getParentId() == null)
			menu.setParentId(0L);
		
		if(menu.getMenuType() == 3 && Strings.isBlank(menu.getMenuUrl())){
			WebUtil.printFail(request, response, "链接菜单 URL 不能为空");
			return ;
		}
		
		if(menu.getMenuType() == 1 && menu.getParentId() != 0 ){
			WebUtil.printFail(request, response, "类型错误");
			return ;
		}
		
		if(menu.getMenuType() != 1 && menu.getParentId() == 0){
			WebUtil.printFail(request, response, "请选择父菜单");
			return ;
		}
		
		if(menu.getOrderNo() == null){
			menu.setOrderNo(1);
		}
		
		if(menu.getIsPublic() != 1){
			menu.setIsPublic(0);
		}
		
		this.menuService.update(menu);
		
		WebUtil.printSuccess(request, response);
	}
	
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param menuId
	 */
	@Auth(code="MENU_DEL",name="删除菜单")
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, String ids){
		List<Long> menuIds = LangUtil.splitIds(ids);
		if(menuIds == null || menuIds.size() < 1){
			WebUtil.printFail(request, response, "请选择待删除菜单");
			return ;
		}
		String info = this.menuService.checkDelete(menuIds);
		if(info != null){
			WebUtil.printFail(request, response, info);
			return ;
		}
		this.menuService.delete(menuIds);
		WebUtil.printSuccess(request, response);
	}
	
	public static String toMenuJson(List<Menu> menus){
		if(menus == null || menus.size() < 1)
			return "[]";
	//		PropertyFilter filter = new PropertyFilter() {
	//		public boolean apply(Object source, String name, Object value) {
	//			return !("url".equals(name));
	//		}
	//	};
	//	SerializeWriter out = new SerializeWriter();
	//	JSONSerializer serializer = new JSONSerializer(out);
	//	serializer.getPropertyFilters().add(filter);
	//	serializer.write(menus);
		return JSON.toJSONString(menus);
	}
	
} 
