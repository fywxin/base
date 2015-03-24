package org.whale.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.base.BaseDao;
import org.whale.system.base.BaseService;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.AuthDao;
import org.whale.system.dao.MenuDao;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;

@Component
public class MenuService extends BaseService<Menu, Long> {
	
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private AuthDao authDao;
	
	public List<Menu> getDirMenus(){
		return this.menuDao.getDirMenus();
	}
	
	public List<Menu> getByParentId(Long parentId){
		if(parentId == null)
			parentId = 0L;
		return this.menuDao.getByParentId(parentId);
	}
	
	public List<Menu> getMenuByType(Integer menuType){
		if(menuType != 1 || menuType != 2 || menuType != 3){
			throw new SysException("menuType must in (1,2,3)");
		}
		return this.menuDao.getMenuByType(menuType);
	}
	
	public List<Menu> getPublicMenus(){
		return this.menuDao.getPublicMenus();
	}
	
	public Menu getByMenuName(String menuName){
		if(Strings.isBlank(menuName))
			return null;
		return this.menuDao.getByMenuName(menuName.trim());
	}
	
	public Integer getNextOrder(Long parentId){
		if(parentId == null)
			parentId = 0L;
		Integer order = this.menuDao.getCurOrder(parentId);
		if(order == null || order < 1)
			return 1;
		return order+1;
	}
	
	public String checkDelete(List<Long> ids){
		if(ids == null)
			return null;
		List<Menu> child = null;
		List<Auth> auths = null;
		for(Long id : ids){
			if(id == null)
				continue;
			child = this.getByParentId(id);
			if(child != null && child.size() > 0){
				return "存在子菜单，不能删除";
			}
			auths = this.authDao.getByMenuId(id);
			if(auths != null && auths.size() > 0){
				return "存在权限，不能删除";
			}
		}
		return null;
	}

	@Override
	public BaseDao<Menu, Long> getDao() {
		return menuDao;
	}

	public List<Menu> addRootMenu(List<Menu> menus, String rootName){
		if(menus == null)
			menus = new ArrayList<Menu>();
		Menu menu = new Menu();
		menu.setMenuId(0L);
		menu.setMenuName(rootName);
		menu.setMenuType(0);
		menus.add(menu);
		return menus;
	}
}
