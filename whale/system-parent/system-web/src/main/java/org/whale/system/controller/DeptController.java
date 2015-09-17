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
import org.whale.system.base.BaseController;
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
	public void doList(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		//获取所有部门数据
		List<Dept> depts = this.deptService.queryAll();
		//部门转为树节点集合
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>(depts.size());
		if(depts != null && depts.size() > 0){
			//父Id，子列表
			Map<Long, List<Map<String, Object>>> pMap = new HashMap<Long, List<Map<String,Object>>>();
			//Id, 节点树对象
			Map<Long, Map<String, Object>> idMap = new HashMap<Long, Map<String, Object>>();
			
			//转换Temp
			List<Map<String, Object>> tmpList = null;
			Map<String, Object> tmp = null;
			for(Dept dept : depts){//pMap、idMap构造
				tmp = new HashMap<String, Object>();
				tmp.put("name", dept.getDeptName());
				tmp.put("id", dept.getId());
				tmp.put("pid", dept.getPid());
				tmp.put("deptAddr", dept.getDeptAddr());
				tmp.put("deptTel", dept.getDeptTel());
				tmp.put("remark", dept.getRemark());
				tmp.put("deptCode", dept.getDeptCode());
				idMap.put(dept.getId(), tmp);
				
				tmpList = pMap.get(dept.getPid());
				if(tmpList == null){
					tmpList = new ArrayList<Map<String,Object>>();
					pMap.put(dept.getPid(), tmpList);
				}
				tmpList.add(tmp);
			}
			
			//获取根节点集合
			List<Map<String, Object>> rootList = pMap.get(0L);
			int num=0;
			for(Map<String, Object> root : rootList){//从根节点循环获取树结构对象类别
				num = loop(rs, root, pMap, idMap, num, 0);
			}
			
		}
		map.put("rows", rs);
		WebUtil.print(request, response, map);
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
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response, Long pid){
		
		return new ModelAndView("system/dept/dept_save")
				.addObject("nodes", JSON.toJSONString(this.deptService.queryAll()))
				.addObject("pid", pid)
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