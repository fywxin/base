package ${domain.pkgName!"org.whale.system"}.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.whale.system.base.Cmd;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.base.BaseRouter;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;
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
public class ${domain.domainName}Router extends BaseRouter {

	@Autowired
	private ${domain.domainName}Service ${domain.domainName?uncap_first}Service;
	<#if domain.treeModel == 2>
    @Auth(code="${domain.domainName?uncap_first}:list", name="查询${domain.domainCnName}")
	@RequestMapping("/goTree")
	public ModelAndView goTree(){
	
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_tree");
	}
	</#if>

	@Auth(code="${domain.domainName?uncap_first}:list", name="查询${domain.domainCnName}")
	@RequestMapping("/goList")
	public ModelAndView goList(<#if domain.treeModel != 3>, ${domain.idAttr.type} id</#if>){

		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_list")<#if domain.treeModel != 3>.addObject("id", id)</#if>;
	}

	@Auth(code="${domain.domainName?uncap_first}:list", name="查询${domain.domainCnName}")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(${domain.domainName} ${domain.domainName?uncap_first}){
		Page page = this.newPage();
		page.newCmd(${domain.domainName}.class)<#list domain.attrs as attr><#if attr.inQuery>.like("${attr.sqlName}", ${domain.domainName?uncap_first}.get${attr.name?cap_first}())</#if></#list>;

		this.${domain.domainName?uncap_first}Service.queryPage(page);
		return page;
	}

	@Auth(code="${domain.domainName?uncap_first}:save", name="新增${domain.domainCnName}")
	@RequestMapping("/goSave")
	public ModelAndView goSave(){
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_save");
	}

	@Auth(code="${domain.domainName?uncap_first}:save", name="新增${domain.domainCnName}")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(${domain.domainName} ${domain.domainName?uncap_first}){
	
		this.${domain.domainName?uncap_first}Service.save(${domain.domainName?uncap_first});
		return Rs.success();
	}

	@Auth(code="${domain.domainName?uncap_first}:update", name="更新${domain.domainCnName}")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(${domain.idAttr.type} id){
		${domain.domainName} ${domain.domainName?uncap_first} = null;
		if(id == null || (${domain.domainName?uncap_first} = this.${domain.domainName?uncap_first}Service.get(id)) == null ){
			throw new SysException("查找不到 ${domain.domainCnName} id="+id);
		}
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_update").addObject("item", ${domain.domainName?uncap_first});
	}

	@Auth(code="${domain.domainName?uncap_first}:update", name="更新${domain.domainCnName}")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(${domain.domainName} ${domain.domainName?uncap_first}){
	
		this.${domain.domainName?uncap_first}Service.update(${domain.domainName?uncap_first});
		return Rs.success();
	}

	@Auth(code="${domain.domainName?uncap_first}:view", name="查看${domain.domainCnName}")
	@RequestMapping("/goView")
	public ModelAndView goView(${domain.idAttr.type} id){
		${domain.domainName} ${domain.domainName?uncap_first} = this.${domain.domainName?uncap_first}Service.get(id);
		if(${domain.domainName?uncap_first} == null){
			throw new SysException("查找不到 ${domain.domainCnName} id="id);
		}
		
		return new ModelAndView("${domain.pkgName!"system"}/${domain.domainName?uncap_first}/${domain.domainName?uncap_first}_view")
				.addObject("item", ${domain.domainName?uncap_first});
	}

	@Auth(code="${domain.domainName?uncap_first}:del", name="删除${domain.domainCnName}")
	@ResponseBody
	@RequestMapping("/doDel")
	public Rs doDel(${domain.idAttr.type} id){
		if(id == null){
			return Rs.fail(request, response, "请选择要删除的记录");
		}
		this.${domain.domainName?uncap_first}Service.delete(id);

		return Rs.success();
	}

}