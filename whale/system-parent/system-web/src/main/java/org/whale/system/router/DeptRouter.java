package org.whale.system.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Cmd;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.common.exception.SysException;
import org.whale.system.service.DeptService;
import org.whale.system.service.UserService;
import org.whale.system.domain.Dept;
import org.whale.system.domain.User;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dept")
public class DeptRouter extends BaseRouter {

	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	
	@Auth(code="dept:list", name="查询部门")
	@RequestMapping("/goTree")
	public ModelAndView goTree(){
		String nodes = "[]";
		List<Dept> depts = this.deptService.queryAll();
		if(depts != null){
			nodes = JSON.toJSONString(depts);
		}
		return new ModelAndView("system/dept/dept_tree").addObject("nodes", nodes);
	}
	
	@Auth(code="dept:list", name="查询部门")
	@RequestMapping("/goList")
	public ModelAndView goList(Long pid){
		if(pid == null){
			pid = 0L;
		}
		return new ModelAndView("system/dept/dept_list").addObject("pid", pid);
	}
	
	@Auth(code="dept:list", name="查询部门")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(Long pid, String deptName, String deptCode, Integer limit){
		Page page = this.newPage();
		Cmd cmd = page.newCmd(Dept.class);
		if(pid != null){
			cmd.eq("pid", pid);
		}
		cmd.like("deptName", deptName);
		cmd.like("deptCode", deptCode);
		this.deptService.queryPage(page);
		
		return page;
	}
		
	@Auth(code="dept:save", name="新增部门")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long pid){
		return new ModelAndView("system/dept/dept_save")
				.addObject("nodes", JSON.toJSONString(this.deptService.queryAll()))
				.addObject("pid", pid)
				.addObject("orderNo", this.deptService.getNextOrder(pid));
	}
	
	@Auth(code="dept:save", name="新增部门")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(@Validate Dept dept){
		if(dept.getPid() == null){
			dept.setPid(0L);
		}
		if(dept.getDeptType() == null){
			dept.setDeptType("1");
		}
		this.deptService.save(dept);
		return Rs.success();
	}
	
	@Auth(code="dept:update", name="修改部门")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long id){
		Dept dept = null;
		if(id == null || (dept = this.deptService.get(id)) == null){
			throw new SysException("查找不到 部门 id="+id);
		}
		
		return new ModelAndView("system/dept/dept_update")
				.addObject("item", dept)
				.addObject("nodes", JSON.toJSONString(this.deptService.queryAll()));
	}
	
	@Auth(code="dept:update", name="修改部门")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Dept dept){
		this.deptService.update(dept);
		return Rs.success();
	}
	
	@Auth(code="dept:del", name="删除部门")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(Long id){
		if(id == null){
			return Rs.fail("请选择要删除的记录");
		}
		
		if(this.deptService.count(Cmd.newCmd(Dept.class).eq("pid", id)) > 0){
			return Rs.fail("部门["+this.deptService.get(id).getDeptName()+"]下存在子部门，不能删除");
		}
		
		if(this.userService.count(Cmd.newCmd(User.class).eq("deptId", id)) > 0){
			return Rs.fail("部门["+this.deptService.get(id).getDeptName()+"]下存在用户，不能删除");
		}
		
		this.deptService.delete(id);
		return Rs.success();
	}

}