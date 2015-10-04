package org.whale.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseController;
import org.whale.system.base.Cmd;
import org.whale.system.base.Page;
import org.whale.system.base.UserContext;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Dept;
import org.whale.system.domain.Role;
import org.whale.system.domain.User;
import org.whale.system.jqgrid.Grid;
import org.whale.system.service.DeptService;
import org.whale.system.service.RoleService;
import org.whale.system.service.UserService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private UserAuthCacheService userAuthCacheService;
	
	@Auth(code="USER_LIST",name="查询用户")
	@RequestMapping("/goTree")
	public ModelAndView goTree(HttpServletRequest request, HttpServletResponse response, Long pid){
		if(pid == null){
			pid = 0L;
		}
		List<Map<String, Object>> depts = this.userService.queryDeptTree();
		String nodes = "[]";
		if(depts != null){
			//pid, children
			Map<Long, List<Map<String, Object>>> pMap = new HashMap<Long, List<Map<String,Object>>>();
			//id, node
			Map<Long, Map<String, Object>> idMap = new HashMap<Long, Map<String,Object>>();
			
			List<Map<String, Object>> tmp = null;
			
			for(Map<String, Object> map : depts){
				idMap.put((Long)map.get("id"), map);
				tmp = pMap.get((Long)map.get("pid"));
				if(tmp == null){
					tmp = new ArrayList<Map<String,Object>>();
					pMap.put((Long)map.get("pid"), tmp);
				}
				tmp.add(map);
			}
			List<Map<String, Object>> rootDepts = pMap.get(pid);
			
			List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
			if(rootDepts != null){
				for(Map<String, Object> root : rootDepts){
					rs.add(this.createDeptTree(root, pMap, idMap));
				}
				
//				Map<String, Object> state = new HashMap<String, Object>();
//				state.put("selected", true);
//				rs.get(0).put("state", state);
				nodes = JSON.toJSONString(rs);
			}
		}
		
		return new ModelAndView("system/user/dept_tree")
				.addObject("nodes", nodes);
	}
	
	/**
	 * 构建部门树
	 * @param map
	 * @param pMap
	 * @param idMap
	 * @return
	 */
	@SuppressWarnings("all")
	private Map<String, Object> createDeptTree(Map<String, Object> map, Map<Long, List<Map<String, Object>>> pMap, Map<Long, Map<String, Object>> idMap){
		Map<String, Object> node = new HashMap<String, Object>();
		Long userNum = (Long)map.get("userNum");
		node.put("id", map.get("id"));
		node.put("text", map.get("deptName"));
		node.put("tags", new Long[]{userNum});
		map.put("node", node); //与转换后的节点简历关联关系，方便更新userNum值
		
		//向上递归至根节点，上级节点userNum值加上本节点值, 
		Map<String, Object> parentNode = null;//父节点
		Map<String, Object> tmpNode = null;//父节点对应转换后的树节点
		Long pid = (Long)map.get("pid");
		while(pid != null && pid != -1L && (parentNode = idMap.get(pid)) != null){
			tmpNode = (Map<String, Object>)parentNode.get("node");
			Long[] tags = (Long[])tmpNode.get("tags");
			tmpNode.put("tags", new Long[]{(tags[0]+userNum)});
			pid = (Long)parentNode.get("pid");
		}
		
		//递归建立nodes
		List<Map<String, Object>> subNodes = pMap.get(map.get("id"));
		if(subNodes != null && subNodes.size() > 0){
			List<Map<String, Object>> subs = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> sub : subNodes){
				subs.add(this.createDeptTree(sub, pMap, idMap));
			}
			node.put("nodes", subs);
		}
		return node;
	}
	
	
	@Auth(code="USER_LIST",name="查询用户")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response, Long deptId){
		
		return new ModelAndView("system/user/user_list")
			.addObject("deptId", deptId);
	}
	
	@Auth(code="USER_LIST",name="查询用户")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, String userName, String realName, Long deptId){
		Page page = this.newPage(request);
		Cmd cmd = page.newCmd(User.class)
					.select("userId","userName","realName","deptId","email","phone" )
					.selectWrap(",(select d.deptName from sys_dept d where d.id = t.deptId) as deptName")
					.like("userName", userName)
					.like("realName", realName);
		if(deptId != null && !deptId.equals(0L)){
			cmd.eq("deptId", deptId);
		}
		
		this.userService.queryPage(page);
		
		WebUtil.print(request, response, Grid.grid(page));
	}
	
	@Auth(code="USER_SAVE",name="新增用户")
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response, Long deptId){
		List<Dept> depts = this.deptService.queryAll();
		
		return new ModelAndView("system/user/user_save")
				.addObject("nodes", JSON.toJSONString(depts))
				.addObject("deptId", deptId);
	}
	
	@Auth(code="USER_SAVE",name="新增用户")
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, User user, String repassword){
		if(user.getPassword() == null || !user.getPassword().equals(repassword)){
			WebUtil.printFail(request, response, "密码不一致");
			return ;
		}
		
		if(user.getDeptId() == null || this.deptService.get(user.getDeptId()) == null){
			WebUtil.printFail(request, response, "请选择用户所属部门");
			return ;
		}

		this.userService.save(user);
		WebUtil.printSuccess(request, response);
	}
	
	@Auth(code="USER_UPDATE",name="修改用户")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long userId){
		User user = this.userService.get(userId);
		
		String nodes = "[]";
		List<Dept> depts = this.deptService.queryAll();
		if(depts != null && depts.size() > 0){
			nodes = JSON.toJSONString(depts);
		}
		return new ModelAndView("system/user/user_update")
				.addObject("item", user)
				.addObject("nodes", nodes)
				.addObject("rootName", DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "ITEM_DEPT_ROOT"));
	}
	
	@Auth(code="USER_UPDATE",name="修改用户")
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, User user){
		if(user.getDeptId() == null || this.deptService.get(user.getDeptId()) == null){
			WebUtil.printFail(request, response, "请选择用户所属部门");
			return ;
		}
		
		UserContext uc = this.getUserContext(request);
		if(!uc.isSuperAdmin() && user.getIsAdmin()){
			WebUtil.printFail(request, response, "你无权修改该用户信息");
			return ;
		}
		
		this.userService.update(user);
		WebUtil.printSuccess(request, response);
	}

	/**
	 * 分配角色
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@Auth(code="USER_ROLE",name="分配角色")
	@RequestMapping("/goSetUserRole")
	public ModelAndView goSetUserRole(HttpServletRequest request, HttpServletResponse response, Long userId){
		User user = this.userService.get(userId);
		if(user.getIsAdmin()){
			throw new SysException("你无权操作对该用户分配角色");
		}
		
		UserContext uc = this.getUserContext(request);
		List<Role> totalRoles = null;
		if(uc.isSuperAdmin()){
			totalRoles = this.roleService.queryAll();
		}else{
			totalRoles = this.roleService.getByUserId(uc.getUserId());
		}
		
		List<Role> hasRoles = this.roleService.getByUserId(userId);
		
		return new ModelAndView("system/user/user_role_tree")
				.addObject("userId", userId)
				.addObject("totalRoles", totalRoles == null ? "[]" : JSON.toJSONString(totalRoles))
				.addObject("hasRoles", hasRoles == null ? "[]" : JSON.toJSONString(hasRoles));
	}
	
	/**
	 * 分配角色
	 * @param request
	 * @param response
	 * @param userId
	 * @param roleIdS
	 */
	@Auth(code="USER_ROLE",name="分配角色")
	@RequestMapping("/doSetUserRole")
	public void doSetUserRole(HttpServletRequest request, HttpServletResponse response, Long userId, String roleIdS){
		if(userId == null){
			WebUtil.printFail(request, response, "用户不能为空");
			return ;
		}
		List<Long> roleIds = LangUtil.splitIds(roleIdS);
		
		//TODO check out law
		
		this.userService.saveUserRole(userId, roleIds);
		
		//更新当前用户权限数据
		this.userAuthCacheService.putUserAuth(userId);
		
		WebUtil.printSuccess(request, response);
	}
	
	/**
	 * 跳转到修改密码页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goChangePassword")
	public ModelAndView goChangePassword(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/user/user_password");
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	@RequestMapping("/doChangePassword")
	public void doChangePassword(HttpServletRequest request, HttpServletResponse response, String oldPassword, String newPassword1, String newPassword2) {
		UserContext uc = this.getUserContext(request);
		
		boolean flag = false;
		StringBuilder strb = new StringBuilder();
		
		User user = this.userService.get(uc.getUserId());
		if(Strings.isBlank(oldPassword) || !this.userService.validPasswd(oldPassword, user.getPassword())){
			strb.append("原密码错误，请重新输入<br/>");
			flag = true;
		}
		
		if(Strings.isBlank(newPassword1)){
			strb.append("新密码不能为空<br/>");
			flag = true;
		}else{
			if(!newPassword1.equals(newPassword2) || newPassword1.trim().length()<6){
				strb.append("新密码输入错误<br/>");
				flag = true;
			}
			if(Strings.hasChinese(newPassword1+newPassword2)) {
				strb.append("密码不能包含汉字<br/>");
				flag = true;
			}
		}
		if(flag){
			WebUtil.printFail(request, response, strb.toString());
			return ;
		}
		
		this.userService.updatePassword(uc.getUserId(),newPassword1.trim());
		
		request.getSession().removeAttribute(SysConstant.USER_CONTEXT_KEY);
		WebUtil.printSuccess(request, response, "修改密码成功");
	}
	
	@Auth(code="USER_VIEW",name="查看用户")
	@RequestMapping("/goView")
	public ModelAndView goView(HttpServletRequest request, HttpServletResponse response, Long userId){
		User user = this.userService.get(userId);
		
		String creator="";
		User create = null;
		if(user.getCreateUserId() != null &&(create = this.userService.get(user.getCreateUserId()))!=null)
			creator = create.getRealName();
		
		String deptName = "";
		Dept dept = null;
		if((dept = this.deptService.get(user.getDeptId())) != null){
			deptName = dept.getDeptName();
		}
		
		return new ModelAndView("system/user/user_view")
				.addObject("item", user)
				.addObject("creator", creator)
				.addObject("deptName", deptName);
	}

	/**
	 * 跳转用户主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goUserMainPage")
	public ModelAndView goUserMainPage(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/user/user_main_page");
	}
	
	
	@Auth(code="USER_DEL",name="删除用户")
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, Long userId){
		User user = null;
		if(userId == null || (user = this.userService.get(userId)) == null){
			WebUtil.printFail(request, response, "请选择删除记录");
			return ;
		}
		
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if(uc == null){
			WebUtil.printFail(request, response, "你未登录，请重新登录");
			return ;
		}
		if(user.getIsAdmin()){
			WebUtil.printFail(request, response, "超级管理员不能被删除");
			return ;
		}
		if(userId == uc.getUserId()){
			WebUtil.printFail(request, response, "亲，你在自杀吗？");
			return ;
		}
		this.userService.delete(userId);
		
		this.userAuthCacheService.delUserAuth(userId);
		
		WebUtil.printSuccess(request, response);
	}
	
	@Auth(code="USER_PASS_REST",name="重置密码")
	@RequestMapping("/doRestPassword")
	public void doRestPassword(HttpServletRequest request, HttpServletResponse response, Long userId){
		if(userId != null){
			this.userService.updatePassword(userId, SysConstant.USER_DEFAULT_PASSWORD);
			WebUtil.printSuccess(request, response);
		}else{
			WebUtil.printFail(request, response, "请选择要重置密码的用户");
		}
	}
	
	@Auth(code="USER_STATUS",name="启禁用户")
	@RequestMapping("/doChangeState")
	public void doChangeState(HttpServletRequest request, HttpServletResponse response, Long userId, Integer status){
		if(userId == null || this.userService.get(userId) == null){
			WebUtil.printFail(request, response, "用户不存在");
			return ;
		}
		if(status != SysConstant.STATUS_NORMAL && status != SysConstant.STATUS_UNUSE){
			WebUtil.printFail(request, response, "用户状态码错误");
			return ;
		}
		
		this.userService.updateState(userId, status);
		WebUtil.printSuccess(request, response, "设置用户状态成功！");
	}
	
}
