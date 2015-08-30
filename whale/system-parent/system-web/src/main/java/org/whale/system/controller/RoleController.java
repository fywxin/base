package org.whale.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.base.UserContext;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;
import org.whale.system.domain.Role;
import org.whale.system.jqgrid.Grid;
import org.whale.system.service.AuthService;
import org.whale.system.service.MenuService;
import org.whale.system.service.RoleService;

import com.alibaba.fastjson.JSON;

/**
 * 角色管理
 *
 * @author 王金绍
 * 2014年9月6日-下午3:11:14
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private AuthService authService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserAuthCacheService userAuthCacheService;
	@Autowired
	private DictCacheService dictCacheService;
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param roleName
	 * @param roleCode
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="ROLE_LIST",name="查询角色")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){

		return new ModelAndView("system/role/role_list");
	}
	
	
	@org.whale.system.annotation.auth.Auth(code="ROLE_LIST",name="查询角色")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, String roleName, String roleCode){
		Page page = Grid.newPage(request);
		page.put("roleName", roleName);
		page.put("roleCode", roleCode);
		
		this.roleService.queryPage(page);
		
		WebUtil.print(request, response, Grid.grid(page));
	}
	
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="ROLE_SAVE",name="新增角色")
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/role/role_save");
	}
	
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param role
	 */
	@org.whale.system.annotation.auth.Auth(code="ROLE_SAVE",name="新增角色")
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Role role){	
		if(role.getStatus() == null)
			role.setStatus(SysConstant.STATUS_NORMAL);
		this.roleService.save(role);
		WebUtil.printSuccess(request, response);
	}
	
	
	
	/**
	 * 跳转到更新页面
	 * @param request
	 * @param response
	 * @param roleId
	 * @return
	 */
	@org.whale.system.annotation.auth.Auth(code="ROLE_UPDATE",name="修改角色")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long roleId){
		Role role = this.roleService.get(roleId);
		return new ModelAndView("system/role/role_update").addObject("item", role);
	}
	
	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param role
	 */
	@org.whale.system.annotation.auth.Auth(code="ROLE_UPDATE",name="修改角色")
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, Role role){
		Role oldRole = this.roleService.get(role.getRoleId());

		if(!oldRole.getRoleCode().equals(role.getRoleCode().trim())){
			WebUtil.printFail(request, response, "角色编码不能修改");
			return ;
		}
		
		if(role.getStatus() == null)
			role.setStatus(SysConstant.STATUS_NORMAL);
		this.roleService.update(role);
		WebUtil.printSuccess(request, response);
	}

	
	@org.whale.system.annotation.auth.Auth(code="ROLE_AUTH",name="分配权限")
	@RequestMapping("/goSetRoleAuth")
	public ModelAndView goSetRoleAuth(HttpServletRequest request, HttpServletResponse response, Long roleId){
		UserContext uc = this.getUserContext(request);
		List<Auth> totalAuths = null;
		List<Menu> allMenus = null;
		List<Menu> totalMenus = this.menuService.queryAll();
		List<Auth> hasAuths = this.authService.getByRoleId(roleId);
		if(uc.isSuperAdmin()){
			allMenus = totalMenus;
			totalAuths = this.authService.queryAll();
		}else{
			totalAuths = this.authService.getByUserId(uc.getUserId());
			
			List<Auth> temp = new ArrayList<Auth>(totalAuths.size()*2);
			temp.addAll(totalAuths);
			boolean flag = true;
			for(Auth auth : hasAuths){
				flag = true;
				for(Auth auth2 : totalAuths){
					if(auth2.getAuthId() == auth.getAuthId()){
						flag=false;
						break;
					}
				}
				if(flag){
					temp.add(auth);
				}
			}
			
			Map<Long, Menu> totalMenuMap = new HashMap<Long, Menu>();
			for(Menu m : totalMenus){
				totalMenuMap.put(m.getMenuId(), m);
			}
			
			Set<Long> leafIds = new HashSet<Long>();
			for(Auth auth : temp){
				leafIds.add(auth.getMenuId());
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
			
			iterable = dirIds.iterator();
			while(iterable.hasNext()){
				m = totalMenuMap.get(iterable.next());
				allMenus.add(m);
			}
		}
		
		return new ModelAndView("system/role/role_auth_tree")
				.addObject("roleId", roleId)
				.addObject("totalAuths", totalAuths == null ? "[]" : JSON.toJSONString(totalAuths))
				.addObject("hasAuths", hasAuths == null ? "[]" : JSON.toJSONString(hasAuths))
				.addObject("allMenus", allMenus == null ? "[]" : JSON.toJSONString(allMenus));
	}
	
	@org.whale.system.annotation.auth.Auth(code="ROLE_AUTH",name="分配权限")
	@RequestMapping("/doSetRoleAuth")
	public void doSetRoleAuth(HttpServletRequest request, HttpServletResponse response, Long roleId, String authIdS){
		if(roleId == null) {
			WebUtil.printFail(request, response, "数据错误");
			return ;
		}
		
		List<Long> authIds = LangUtil.splitIds(authIdS);
		
		//TODO check out law
		
		this.roleService.saveRoleAuths(roleId, authIds);
		
		if(dictCacheService.isValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_ITEM_FLUSH_AUTH, "auto")){
			this.userAuthCacheService.init(null);
		}
		
		WebUtil.printSuccess(request, response);
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param roleId
	 */
	@org.whale.system.annotation.auth.Auth(code="ROLE_DEL",name="删除角色")
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, String ids){
		List<Long> roleIds = LangUtil.splitIds(ids);
		if(roleIds == null || roleIds.size() < 1){
			WebUtil.printFail(request, response, "请选择待删除记录");
			return ;
		}
		
		this.roleService.delete(roleIds);
		
		if(dictCacheService.isValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_ITEM_FLUSH_AUTH, "auto")){
			this.userAuthCacheService.init(null);
		}
		
		WebUtil.printSuccess(request, response);
	}
	
	@org.whale.system.annotation.auth.Auth(code="ROLE_STATUS",name="启禁角色")
	@RequestMapping("/doChangeState")
	public void doChangeState(HttpServletRequest request, HttpServletResponse response, Long roleId, Integer status){
		if(roleId == null || this.roleService.get(roleId) == null){
			WebUtil.printFail(request, response, "角色不存在");
			return ;
		}
		
		if(status != SysConstant.STATUS_NORMAL && status != SysConstant.STATUS_UNUSE){
			WebUtil.printFail(request, response, "状态数据错误");
			return ;
		}
		
		this.roleService.updateStatus(roleId, status);
		
		if(dictCacheService.isValue(DictConstant.DICT_SYS_CONF, DictConstant.DICT_ITEM_FLUSH_AUTH, "auto")){
			this.userAuthCacheService.init(null);
		}
		
		WebUtil.printSuccess(request, response);
	}
}
