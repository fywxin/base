package org.whale.system.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Cmd;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
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
import org.whale.system.service.DeptService;
import org.whale.system.service.RoleService;
import org.whale.system.service.UserService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/user")
public class UserRouter extends BaseRouter {
	
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
	public ModelAndView goTree(){
		String nodes = "[]";
		List<Dept> list = this.deptService.queryAll();
		if(list != null && list.size() > 0){
			nodes = JSON.toJSONString(list);
		}
		return new ModelAndView("system/user/dept_tree")
				.addObject("nodes", nodes)
				.addObject("rootName", DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "ITEM_DEPT_ROOT"));
	}
	
	@Auth(code="USER_LIST",name="查询用户")
	@RequestMapping("/goList")
	public ModelAndView goList(Long deptId){
		
		return new ModelAndView("system/user/user_list")
			.addObject("deptId", deptId);
	}
	
	@Auth(code="USER_LIST",name="查询用户")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(String userName, String realName, Long deptId){
		Page page = this.newPage();
		Cmd cmd = page.newCmd(User.class)
				.select("userId","userName","realName","deptId","email","phone")
				.selectWrap(",(select d.deptName from sys_dept d where d.id = t.deptId) as deptName")
				.like("userName", userName)
				.like("realName", realName);
		if(deptId != null && !deptId.equals(0L)){
			cmd.eq("deptId", deptId);
		}
		
		this.userService.queryPage(page);
		
		return page;
	}
	
	@Auth(code="USER_SAVE",name="新增用户")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long deptId){
		List<Dept> depts = this.deptService.queryAll();
		
		return new ModelAndView("system/user/user_save")
				.addObject("nodes", JSON.toJSONString(depts))
				.addObject("deptId", deptId);
	}
	
	@Auth(code="USER_SAVE",name="新增用户")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(User user, String repassword){
		if(user.getPassword() == null || !user.getPassword().equals(repassword)){
			return Rs.fail("密码不一致");
		}
		
		if(user.getDeptId() == null || this.deptService.get(user.getDeptId()) == null){
			return Rs.fail("请选择用户所属部门");
		}

		this.userService.save(user);
		return Rs.success();
	}
	
	@Auth(code="USER_UPDATE",name="修改用户")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long userId){
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
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(User user){
		if(user.getDeptId() == null || this.deptService.get(user.getDeptId()) == null){
			return Rs.fail("请选择用户所属部门");
		}
		
		UserContext uc = this.getUserContext();
		if(!uc.isSuperAdmin() && user.getAdminFlag()){
			return Rs.fail("你无权修改该用户信息");
		}
		
		this.userService.update(user);
		return Rs.success();
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
	public ModelAndView goSetUserRole(Long userId){
		User user = this.userService.get(userId);
		if(user.getAdminFlag()){
			throw new SysException("你无权操作对该用户分配角色");
		}
		
		UserContext uc = this.getUserContext();
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
	@ResponseBody
	@RequestMapping("/doSetUserRole")
	public Rs doSetUserRole(Long userId, String roleIdS){
		if(userId == null){
			return Rs.fail("用户不能为空");
		}
		List<Long> roleIds = LangUtil.splitIds(roleIdS);
		
		//TODO check out law
		
		this.userService.transSaveUserRole(userId, roleIds);
		
		//更新当前用户权限数据
		this.userAuthCacheService.putUserAuth(userId);
		
		return Rs.success();
	}
	
	/**
	 * 跳转到修改密码页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goChangePassword")
	public ModelAndView goChangePassword(){
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
	@ResponseBody
	public Rs doChangePassword(String oldPassword, String newPassword1, String newPassword2) {
		UserContext uc = this.getUserContext();
		
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
			return Rs.fail(strb.toString());
		}
		
		this.userService.updatePassword(uc.getUserId(),newPassword1.trim());
		
		WebUtil.getSession().removeAttribute(SysConstant.USER_CONTEXT_KEY);
		return Rs.success("修改密码成功");
	}
	
	@Auth(code="USER_VIEW",name="查看用户")
	@RequestMapping("/goView")
	public ModelAndView goView(Long userId){
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
	public ModelAndView goUserMainPage(){
		return new ModelAndView("system/user/user_main_page");
	}
	
	
	@Auth(code="USER_DEL",name="删除用户")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(Long userId){
		User user = null;
		if(userId == null || (user = this.userService.get(userId)) == null){
			return Rs.fail("请选择删除记录");
		}
		
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if(uc == null){
			return Rs.fail("你未登录，请重新登录");
		}
		if(user.getAdminFlag()){
			return Rs.fail("超级管理员不能被删除");
		}
		if(userId == uc.getUserId()){
			return Rs.fail("亲，你在自杀吗？");
		}
		this.userService.delete(userId);
		
		this.userAuthCacheService.delUserAuth(userId);
		
		return Rs.success();
	}
	
	@Auth(code="USER_PASS_REST",name="重置密码")
	@ResponseBody
	@RequestMapping("/doRestPassword")
	public Rs doRestPassword(Long userId){
		if(userId != null){
			this.userService.updatePassword(userId, SysConstant.USER_DEFAULT_PASSWORD);
			return Rs.success();
		}else{
			return Rs.fail("请选择要重置密码的用户");
		}
	}
	
	@Auth(code="USER_STATUS",name="启禁用户")
	@ResponseBody
	@RequestMapping("/doChangeState")
	public Rs doChangeState(Long userId, Integer status){
		if(userId == null || this.userService.get(userId) == null){
			return Rs.fail("用户不存在");
		}
		if(status != SysConstant.STATUS_NORMAL && status != SysConstant.STATUS_UNUSE){
			return Rs.fail("用户状态码错误");
		}
		
		this.userService.updateState(userId, status);
		return Rs.success("设置用户状态成功！");
	}
	
}
