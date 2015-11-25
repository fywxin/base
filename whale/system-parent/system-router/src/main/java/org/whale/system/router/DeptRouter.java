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
import org.whale.system.common.util.LangUtil;
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
	
	
	@Auth(code="DEPT_LIST", name="查询组织")
	@RequestMapping("/goList")
	public ModelAndView goList(){
		return new ModelAndView("system/dept/dept_list");
	}
	
	@Auth(code="DEPT_LIST", name="查询组织")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(Dept dept){
		Page page = this.newPage();
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
		page.newCmd(Dept.class);
		this.deptService.queryPage(page);
		
		return page;
	}
	
	@Auth(code="DEPT_SAVE", name="新增组织")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long pid){
		String pName = "";
		if(pid != null){
			Dept pDept = this.deptService.get(pid);
			if(pDept == null){
				throw new SysException("查找不到 父组织pid="+pid);
			}
			pName = pDept.getDeptName();
		}
		
		return new ModelAndView("system/dept/dept_save")
				.addObject("pid", pid)
				.addObject("pName", pName)
				.addObject("nextOrderNo", this.deptService.getNextOrder(pid));
	}
	
	@Auth(code="DEPT_SAVE", name="新增组织")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(Dept dept){

		this.deptService.save(dept);
		return Rs.success();
	}
	
	@Auth(code="DEPT_UPDATE", name="修改组织")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long id){
		Dept dept = null;
		if(id == null || (dept = this.deptService.get(id)) == null){
			throw new SysException("查找不到 组织 id="+id);
		}
		List<Dept> depts = this.deptService.queryAll();
		
		return new ModelAndView("system/dept/dept_update")
				.addObject("item", dept)
				.addObject("depts", JSON.toJSONString(depts));
	}
	
	@Auth(code="DEPT_UPDATE", name="修改组织")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Dept dept){
		
		this.deptService.update(dept);
		return Rs.success();
	}
	
	@Auth(code="DEPT_DEL", name="删除组织")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(String ids){
		List<Long> idS = LangUtil.splitIds(ids);
		if(idS == null || idS.size() < 1 || (idS.size() == 1 && idS.get(0) == 0l)){
			return Rs.fail("请选择要删除的记录");
		}
		
		for(Long pid : idS){
			if(this.deptService.count(Cmd.newCmd(Dept.class).eq("pid", pid)) > 0){
				return Rs.fail("组织["+this.deptService.get(pid).getDeptName()+"]下存在子组织，不能删除");
			}
			
			if(this.userService.count(Cmd.newCmd(User.class).eq("deptId", pid)) > 0){
				return Rs.fail("组织["+this.deptService.get(pid).getDeptName()+"]下存在用户，不能删除");
			}
		}
		
		this.deptService.deleteBatch(idS);
		return Rs.success();
	}

}