package org.whale.system.router;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.log.Log;
import org.whale.system.annotation.log.LogHelper;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.*;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.*;
import org.whale.system.domain.Dept;
import org.whale.system.domain.Role;
import org.whale.system.domain.User;
import org.whale.system.service.DeptService;
import org.whale.system.service.RoleService;
import org.whale.system.service.UserService;

import java.util.List;

@Log(module = "用户", value = "")
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
	
	@Auth(code="user:list",name="查询用户")
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
	
	@Auth(code="user:list",name="查询用户")
	@RequestMapping("/goList")
	public ModelAndView goList(Long deptId){
		
		return new ModelAndView("system/user/user_list")
			.addObject("deptId", deptId);
	}
	
	@Auth(code="user:list",name="查询用户")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(String userName, String realName, Long deptId){
		Page page = this.newPage();
		Q q = page.newQ(User.class)
					.select("userId","userName","realName","deptId","email","phone","status", "adminFlag" )
					.selectWrap(",(select d.deptName from sys_dept d where d.id = deptId) as deptName")
					.like(User.F_userName, userName)
					.like(User.F_realName, realName);
		if(deptId != null && !deptId.equals(0L)){
			q.eq(User.F_deptId, deptId);
		}
		if(!UserContext.get().isSuperAdmin()){
			q.eq(User.F_adminFlag, false);
		}
		
		this.userService.queryPage(page);
		
		return page;
	}
	
	@Auth(code="user:save",name="新增用户")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long deptId){
		List<Dept> depts = this.deptService.queryAll();
		
		return new ModelAndView("system/user/user_save")
				.addObject("nodes", JSON.toJSONString(depts))
				.addObject("deptId", deptId);
	}

	@Log("新增用户 {}")
	@Auth(code="user:save",name="新增用户")
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

		LogHelper.addPlaceHolder(user);
		return Rs.success();
	}

	@Auth(code="user:update",name="修改用户")
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

	@Log("修改用户 {}")
	@Auth(code="user:update",name="修改用户")
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

		LogHelper.addPlaceHolder(user);
		return Rs.success();
	}

	/**
	 * 分配角色
	 * @param userId
	 * @return
	 */
	@Auth(code="user:role",name="分配角色")
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
	 * @param userId
	 * @param roleIdS
	 * @return
	 */
	@Log("用户分配角色 用户:{} 分配角色：{}")
	@Auth(code="user:role",name="分配角色")
	@ResponseBody
	@RequestMapping("/doSetUserRole")
	public Rs doSetUserRole(Long userId, String roleIdS){
		if(userId == null){
			return Rs.fail("用户不能为空");
		}

		List<Long> roleIds = ListUtil.longList(roleIdS);

		//TODO check out law
		
		this.userService.transSaveUserRole(userId, roleIds);
		
		//更新当前用户权限数据
		this.userAuthCacheService.putUserAuth(userId);
		
		return Rs.success();
	}

	/**
	 * 跳转到修改密码页面
	 * @return
	 */
	@RequestMapping("/goChangePassword")
	public ModelAndView goChangePassword(){
		return new ModelAndView("system/user/user_password");
	}

	/**
	 * 修改密码
	 * @param oldPassword
	 * @param newPassword1
	 * @param newPassword2
	 * @return
	 */
	@Log("修改密码 用户:{} ,旧密码：{}, 新密码：{}")
	@ResponseBody
	@RequestMapping("/doChangePassword")
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
		
		this.userService.updatePassword(uc.getUserId(), newPassword1.trim());
		
		WebUtil.getSession().removeAttribute(SysConstant.USER_CONTEXT_KEY);

		LogHelper.addPlaceHolder(UserContext.userName(), oldPassword, newPassword1);
		return Rs.success("修改密码成功");
	}
	
	@Auth(code="user:list",name="查询用户")
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
				.addObject("deptName", deptName)
				.addObject("lastLoginTime", TimeUtil.formatTime(user.getLastLoginTime(), TimeUtil.COMMON_FORMAT))
				.addObject("createTime", TimeUtil.formatTime(user.getCreateTime(), TimeUtil.COMMON_FORMAT));
	}

	/**
	 * 跳转用户主页
	 * @return
	 */
	@RequestMapping("/goUserMainPage")
	public ModelAndView goUserMainPage(){
		return new ModelAndView("system/user/user_main_page");
	}


	@Log("删除用户 用户:{}")
	@Auth(code="user:del",name="删除用户")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(String ids){
		User user = null;
		if(Strings.isBlank(ids)){
			return Rs.fail("请选择删除记录");
		}
		
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if(uc == null){
			return Rs.fail("你未登录，请重新登录");
		}
		
		List<Long> idList = ListUtil.longList(ids);
		if(idList.contains(uc.getUserId())){
			return Rs.fail("亲，你在自杀吗？");
		}
		for(int i = idList.size()-1; i>=0; i--){
			user = this.userService.get(idList.get(i));
			if(user == null){
				idList.remove(i);
				continue;
			}
			if(user.getAdminFlag()){
				return Rs.fail("超级管理员不能被删除");
			}
		}
		
		for(Long userId : idList){
			this.userService.delete(userId);
			this.userAuthCacheService.delUserAuth(userId);
		}

		LogHelper.addPlaceHolder(user.getUserName());
		return Rs.success();
	}

	@Log("重置密码 用户:{}")
	@Auth(code="user:restPassword",name="重置密码")
	@ResponseBody
	@RequestMapping("/doRestPassword")
	public Rs doRestPassword(Long userId){
		User user = this.userService.get(userId);
		if(user != null){

			this.userService.updatePassword(userId, SysConstant.USER_DEFAULT_PASSWORD);

			LogHelper.addPlaceHolder(user.getUserName());
			return Rs.success();
		}else{
			return Rs.fail("请选择要重置密码的用户");
		}
	}

	@Log("启禁用户 用户:{}, 状态")
	@Auth(code="user:status",name="启禁用户")
	@ResponseBody
	@RequestMapping("/doChangeState")
	public Rs doChangeState(Long userId, Integer status){
		User user = this.userService.get(userId);
		if(user == null){
			return Rs.fail("用户不存在");
		}
		if(status != SysConstant.STATUS_NORMAL && status != SysConstant.STATUS_UNUSE){
			return Rs.fail("用户状态码错误");
		}
		this.userService.updateState(user, status);

		LogHelper.addPlaceHolder(user.getUserName(), status == SysConstant.STATUS_NORMAL ? "启用" : "禁用");
		return Rs.success("设置用户状态成功！");
	}
	
}
