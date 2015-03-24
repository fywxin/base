package org.whale.${domain.pkgName!"system"}.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.whale.system.base.Page;
import org.whale.system.base.BaseController;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;

import org.whale.${domain.pkgName!"system"}.service.${domain.name}Service;
import org.whale.${domain.pkgName!"system"}.domain.${domain.name};

@Controller
@RequestMapping("/${domain.name?uncap_first}")
public class ${domain.name}Controller extends BaseController {

	@Autowired
	private ${domain.name}Service ${domain.name?uncap_first}Service;
	<#if domain.isTree>
	@RequestMapping("/goTree")
	public ModelAndView goTree(HttpServletRequest request, HttpServletResponse response, Long clkId){
	
		return new ModelAndView("${domain.pkgName!"system"}/${domain.name?uncap_first}/${domain.name?uncap_first}_tree")
				.addObject("clkId", clkId);
	}
	</#if>
	
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){

		return new ModelAndView("${domain.pkgName!"system"}/${domain.name?uncap_first}/${domain.name?uncap_first}_list");
	}
	
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, ${domain.name} ${domain.name?uncap_first}){
		Page page = this.newPage(request);
<#list domain.attrs as attr>
	<#if attr.inQuery>
		page.put("${attr.name}", ${domain.name?uncap_first}.get${attr.name?cap_first}());
	</#if>
</#list>
		
		this.${domain.name?uncap_first}Service.queryPage(page);
		WebUtil.print(request, response, page);
	}
	
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response){
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.name?uncap_first}/${domain.name?uncap_first}_save");
	}
	
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, ${domain.name} ${domain.name?uncap_first}){
	
		this.${domain.name?uncap_first}Service.save(${domain.name?uncap_first});
		WebUtil.printSuccess(request, response);
tpServletResponse response, ${domain.idAttr.type} ${domain.idAttr.name}){
		${domain.name} ${domain.name?uncap_first} = this.${domain.name?uncap_first}Service.get(${domain.idAttr.name});
		if(${domain.name?uncap_first} == null){
			throw new SysException("查找不到 ${domain.cnName} id="+${domain.idAttr.name});
		}
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.name?uncap_first}/${domain.name?uncap_first}_update")
				.addObject("item", ${domain.name?uncap_first});
	}
	
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long id){
		${domain.name} ${domain.name?uncap_first} = null;
		if(id == null || (${domain.name?uncap_first} = this.${domain.name?uncap_first}Service.get(id)) == null ){
			throw new SysException("查找不到 ${domain.cnName} id="+${domain.idAttr.name});
		}
		return new ModelAndView("${domain.pkgName!"system"}/${domain.name?uncap_first}/${domain.name?uncap_first}_update").addObject("item", ${domain.name?uncap_first});
	}
	
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, ${domain.name} ${domain.name?uncap_first}){
	
		this.${domain.name?uncap_first}Service.update(${domain.name?uncap_first});
		WebUtil.printSuccess(request, response);
	}
	
	@RequestMapping("/goView")
	public ModelAndView goView(HttpServletRequest request, HttpServletResponse response, ${domain.idAttr.type} ${domain.idAttr.name}){
		${domain.name} ${domain.name?uncap_first} = this.${domain.name?uncap_first}Service.get(${domain.idAttr.name});
		if(${domain.name?uncap_first} == null){
			throw new SysException("查找不到 ${domain.cnName} id="+${domain.idAttr.name});
		}
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.name?uncap_first}/${domain.name?uncap_first}_view")
				.addObject("item", ${domain.name?uncap_first});
	}
	
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, String ids){
		List<Long> idS = LangUtil.splitIds(ids);
		if(idS == null || idS.size() < 1){
			WebUtil.printFail(request, response, "请选择要删除的记录");
			return ;
		}
		this.${domain.name?uncap_first}Service.delete(idS);
		
		WebUtil.printSuccess(request, response);
	}

}