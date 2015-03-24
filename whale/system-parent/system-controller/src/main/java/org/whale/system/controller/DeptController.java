package org.whale.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.auth.annotation.Auth;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.WebUtil;
import org.whale.system.service.DeptService;
import org.whale.system.service.UserService;
import org.whale.system.domain.Dept;
import org.whale.system.domain.User;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController {

	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	
	
	@Auth(code="DEPT_LIST", name="查询部门")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/dept/dept_list");
	}
	
	@Auth(code="DEPT_LIST", name="查询部门")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, Dept dept){
		Page page = this.newPage(request);
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
		this.deptService.queryPage(page);
		
		WebUtil.print(request, response, page);
	}
	
	@Auth(code="DEPT_SAVE", name="新增部门")
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response, Long pid){
		String pName = "";
		if(pid != null){
			Dept pDept = this.deptService.get(pid);
			if(pDept == null){
				throw new SysException("查找不到 父部门pid="+pid);
			}
			pName = pDept.getDeptName();
		}
		
		return new ModelAndView("system/dept/dept_save")
				.addObject("pid", pid)
				.addObject("pName", pName)
				.addObject("nextOrderNo", this.deptService.getNextOrder(pid));
	}
	
	@Auth(code="DEPT_SAVE", name="新增部门")
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Dept dept){

		this.deptService.save(dept);
		WebUtil.printSuccess(request, response);
	}
	
	@Auth(code="DEPT_UPDATE", name="修改部门")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long id){
		Dept dept = null;
		if(id == null || (dept = this.deptService.get(id)) == null){
			throw new SysException("查找不到 部门 id="+id);
		}
		List<Dept> depts = this.deptService.queryAll();
		
		return new ModelAndView("system/dept/dept_update")
				.addObject("item", dept)
				.addObject("depts", JSON.toJSONString(depts));
	}
	
	@Auth(code="DEPT_UPDATE", name="修改部门")
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, Dept dept){
		
		this.deptService.update(dept);
		WebUtil.printSuccess(request, response);
	}
	
	@Auth(code="DEPT_DEL", name="删除部门")
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, String ids){
		List<Long> idS = LangUtil.splitIds(ids);
		if(idS == null || idS.size() < 1 || (idS.size() == 1 && idS.get(0) == 0l)){
			WebUtil.printFail(request, response, "请选择要删除的记录");
			return ;
		}
		
		for(Long pid : idS){
			List<Dept> depts = this.deptService.getByPid(pid);
			if(depts != null && depts.size() > 0){
				WebUtil.printFail(request, response, "部门["+this.deptService.get(pid).getDeptName()+"]下存在子部门，不能删除");
				return ;
			}
			
			List<User> users = this.userService.getByDeptId(pid);
			if(users != null && users.size() > 0){
				WebUtil.printFail(request, response, "部门["+this.deptService.get(pid).getDeptName()+"]下存在用户，不能删除");
				return ;
			}
		}
		
		this.deptService.delete(idS);
		WebUtil.printSuccess(request, response);
	}

}