package org.whale.system.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Cmd;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.TreeUtil;
import org.whale.system.service.DeptService;
import org.whale.system.service.UserService;
import org.whale.system.domain.Dept;
import org.whale.system.domain.User;
import org.whale.system.jqgrid.Grid;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dept")
public class DeptRouter extends BaseRouter {

	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	
	@Auth(code="DEPT_LIST", name="查询部门")
	@RequestMapping("/goTree")
	public ModelAndView goTree(){
		String nodes = "[]";
		List<Dept> depts = this.deptService.queryAll();
		if(depts != null){
			nodes = JSON.toJSONString(depts);
		}
		return new ModelAndView("system/dept/dept_tree").addObject("nodes", nodes);
	}
	
	@Auth(code="DEPT_LIST", name="查询部门")
	@RequestMapping("/goList")
	public ModelAndView goList(Long pid){
		return new ModelAndView("system/dept/dept_list").addObject("pid", pid);
	}
	
	@Auth(code="DEPT_LIST", name="查询部门")
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
		
	@Auth(code="DEPT_SAVE", name="新增部门")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long pid){
		return new ModelAndView("system/dept/dept_save")
				.addObject("nodes", JSON.toJSONString(this.deptService.queryAll()))
				.addObject("pid", pid)
				.addObject("orderNo", this.deptService.getNextOrder(pid));
	}
	
	@Auth(code="DEPT_SAVE", name="新增部门")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(Dept dept){
		this.deptService.save(dept);
		return Rs.success();
	}
	
	@Auth(code="DEPT_UPDATE", name="修改部门")
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
	
	@Auth(code="DEPT_UPDATE", name="修改部门")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Dept dept){
		this.deptService.update(dept);
		return Rs.success();
	}
	
	@Auth(code="DEPT_DEL", name="删除部门")
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