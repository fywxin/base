package org.whale.system.router;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.annotation.log.Log;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Q;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.common.exception.SysException;
import org.whale.system.domain.Dept;
import org.whale.system.domain.User;
import org.whale.system.annotation.log.LogHelper;
import org.whale.system.service.DeptService;
import org.whale.system.service.UserService;

import java.util.List;

@Log(module = "部门", value = "")
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
		Q q = page.newQ(Dept.class);
		q.select("*, (SELECT p.deptName FROM sys_dept p WHERE p.id = pid) parentName ");
		if(pid != null && pid != 0L){
			q.eq(Dept.F_pid, pid);
		}
		q.like(Dept.F_deptName, deptName).like(Dept.F_deptCode, deptCode).asc(Dept.F_pid).desc(Dept.F_orderNo);
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

	@Log("新增部门 名称: {},编码:{}")
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
		LogHelper.addPlaceHolder(dept.getDeptName(), dept.getDeptCode());
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

	@Log("修改部门 id: {}, 名称:{}")
	@Auth(code="dept:update", name="修改部门")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Dept dept){

		this.deptService.update(dept);
		LogHelper.addPlaceHolder(dept.getId(), dept.getDeptName());
		return Rs.success();
	}

	@Log("删除部门 {}")
	@Auth(code="dept:del", name="删除部门")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(Long id){
		if(id == null){
			return Rs.fail("请选择要删除的记录");
		}
		
		if(this.deptService.count(Q.newQ(Dept.class).eq("pid", id)) > 0){
			return Rs.fail("部门["+this.deptService.get(id).getDeptName()+"]下存在子部门，不能删除");
		}
		
		if(this.userService.count(Q.newQ(User.class).eq("deptId", id)) > 0){
			return Rs.fail("部门["+this.deptService.get(id).getDeptName()+"]下存在用户，不能删除");
		}
		this.deptService.delete(id);

		LogHelper.addPlaceHolder(id);
		return Rs.success();
	}

}