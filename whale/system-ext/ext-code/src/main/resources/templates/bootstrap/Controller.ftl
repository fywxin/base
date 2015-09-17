package ${domain.pkgName!"org.whale.system"}.controller;

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
import org.whale.system.jqgrid.Grid;
<#if domain.treeModel == 3>
import org.whale.system.common.util.TreeUtil;
</#if>

import ${domain.pkgName!"org.whale.system"}.service.${domain.domainName}Service;
import ${domain.pkgName!"org.whale.system"}.domain.${domain.domainName};

/**
 * ${domain.domainCnName}控制器
 *
 * @author ${domain.author}
 * ${.now}
 */
@Controller
@RequestMapping("/${domain.domainName?uncap_first}")
public class ${domain.domainName}Controller extends BaseController {

	@Autowired
	private ${domain.domainName}Service ${domain.domainName?uncap_first}Service;
	<#if domain.treeModel == 2>
	@RequestMapping("/goTree")
	public ModelAndView goTree(HttpServletRequest request, HttpServletResponse response){
	
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_tree");
	}
	</#if>
	
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response<#if domain.treeModel != 3>, Long id</#if>){

		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_list")<#if domain.treeModel != 3>.addObject("id", id)</#if>;
	}
	
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, ${domain.domainName} ${domain.domainName?uncap_first}){
<#if domain.treeModel != 3>
		Page page = Grid.newPage(request);
		page.newCmd(${domain.domainName}.class)<#list domain.attrs as attr><#if attr.inQuery>.like("${attr.sqlName}", ${domain.domainName?uncap_first}.get${attr.name?cap_first}())</#if></#list>;

		this.${domain.domainName?uncap_first}Service.queryPage(page);
</#if>
<#if domain.treeModel == 3>
		WebUtil.print(request, response, TreeUtil.jqGridTree(this.${domain.domainName?uncap_first}Service.queryAll(), 0L));
</#if>
	}
	
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response){
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_save");
	}
	
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, ${domain.domainName} ${domain.domainName?uncap_first}){
	
		this.${domain.domainName?uncap_first}Service.save(${domain.domainName?uncap_first});
		WebUtil.printSuccess(request, response);
	}
	
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, ${domain.idAttr.type} id){
		${domain.domainName} ${domain.domainName?uncap_first} = null;
		if(id == null || (${domain.domainName?uncap_first} = this.${domain.domainName?uncap_first}Service.get(id)) == null ){
			throw new SysException("查找不到 ${domain.domainCnName} id="+id);
		}
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_update").addObject("item", ${domain.domainName?uncap_first});
	}
	
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, ${domain.domainName} ${domain.domainName?uncap_first}){
	
		this.${domain.domainName?uncap_first}Service.update(${domain.domainName?uncap_first});
		WebUtil.printSuccess(request, response);
	}
	
	@RequestMapping("/goView")
	public ModelAndView goView(HttpServletRequest request, HttpServletResponse response, ${domain.idAttr.type} id){
		${domain.domainName} ${domain.domainName?uncap_first} = this.${domain.domainName?uncap_first}Service.get(id);
		if(${domain.domainName?uncap_first} == null){
			throw new SysException("查找不到 ${domain.domainCnName} id="id);
		}
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_view")
				.addObject("item", ${domain.domainName?uncap_first});
	}
	
	@RequestMapping("/doDel")
	public void doDel(HttpServletRequest request, HttpServletResponse response, String ids){
		List<Long> idS = LangUtil.splitIds(ids);
		if(idS == null || idS.size() < 1){
			WebUtil.printFail(request, response, "请选择要删除的记录");
			return ;
		}
		this.${domain.domainName?uncap_first}Service.delete(idS);
		
		WebUtil.printSuccess(request, response);
	}

}