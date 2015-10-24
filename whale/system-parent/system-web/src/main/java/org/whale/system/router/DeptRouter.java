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
	
	/**
	 * 构造树结构 lft rgt level
	 * @param rs  返回结果集合
	 * @param node 当前节点
	 * @param pMap 父Id，子列表
	 * @param idMap Id, 节点树对象
	 * @param index 当前index值
	 * @param level 
	 * @return
	 */
	public static Integer loop(List<Map<String, Object>> rs, Map<String, Object> node, Map<Long, List<Map<String, Object>>> pMap, Map<Long, Map<String, Object>> idMap, Integer index, Integer level){
		//进入节点前，先+1，做为上个节点的次节点
		int lft = index+1;
		rs.add(node);
		node.put("level", level);
		node.put("lft", lft);
		
		List<Map<String, Object>> subNodes = pMap.get(node.get("id"));
		if(subNodes != null && subNodes.size() > 0){
			node.put("uiicon", "ui-icon-image");
			node.put("expanded", true);
			level++;
			int num = lft;
			for(Map<String, Object> sub : subNodes){
				num = loop(rs, sub, pMap, idMap, num, level);
			}
			lft = num+1;
		}else{
			node.put("expanded", false);
			node.put("uiicon", "ui-icon-document");
			lft = lft+1;
		}
		node.put("rgt", lft);
		return lft;
	}
	
	@Auth(code="DEPT_SAVE", name="新增部门")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long pid){
		
		return new ModelAndView("system/dept/dept_save")
				.addObject("nodes", JSON.toJSONString(this.deptService.queryAll()))
				.addObject("pid", pid)
				.addObject("nextOrderNo", this.deptService.getNextOrder(pid));
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
		List<Dept> depts = this.deptService.queryAll();
		
		return new ModelAndView("system/dept/dept_update")
				.addObject("item", dept)
				.addObject("depts", JSON.toJSONString(depts));
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
	public Rs doDelete(String ids){
		List<Long> idS = LangUtil.splitIds(ids);
		if(idS == null || idS.size() < 1 || (idS.size() == 1 && idS.get(0) == 0l)){
			return Rs.fail("请选择要删除的记录");
		}
		
		for(Long pid : idS){
			if(this.deptService.count(Cmd.newCmd(Dept.class).eq("pid", pid)) > 0){
				return Rs.fail("部门["+this.deptService.get(pid).getDeptName()+"]下存在子部门，不能删除");
			}
			
			if(this.userService.count(Cmd.newCmd(User.class).eq("deptId", pid)) > 0){
				return Rs.fail("部门["+this.deptService.get(pid).getDeptName()+"]下存在用户，不能删除");
			}
		}
		
		this.deptService.deleteBatch(idS);
		return Rs.success();
	}

}