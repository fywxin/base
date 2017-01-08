package ${domain.pkgName!"org.whale.system"}.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.whale.system.base.Page;
import org.whale.system.base.BaseController;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.util.ListUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;
<#if domain.treeModel == 3>
import org.whale.system.common.util.TreeUtil;
</#if>

import ${domain.pkgName!"org.whale.system"}.service.${domain.domainName}Service;
import ${domain.pkgName!"org.whale.system"}.dao.${domain.domainName}Dao;
import ${domain.pkgName!"org.whale.system"}.bean.${domain.domainName};

import javax.servlet.http.HttpServletResponse;

/**
 * ${domain.domainCnName}控制器
 *
 * @author ${domain.author}
 * ${.now}
 */
@Controller
@RequestMapping("/service/${domain.domainName?uncap_first}")
public class ${domain.domainName}Controller extends BaseController {

	@Autowired
	private ${domain.domainName}Dao ${domain.domainName?uncap_first}Dao;
	@Autowired
	private ${domain.domainName}Service ${domain.domainName?uncap_first}Service;


	@RequestMapping("/query${domain.domainName}.do")
	public void doQuery(HttpServletResponse response, ${domain.domainName} ${domain.domainName?uncap_first}){
		Page page = this.newPage();
		this.${domain.domainName?uncap_first}Service.queryPage(page, ${domain.domainName?uncap_first});
		WebUtil.success(response, page);
	}

	@RequestMapping("/add${domain.domainName}.do")
	public void doAdd(HttpServletResponse response, @Validate ${domain.domainName} ${domain.domainName?uncap_first}){

		this.${domain.domainName?uncap_first}Service.save(${domain.domainName?uncap_first});
		WebUtil.success(response);
	}

	@ResponseBody
	@RequestMapping("/update${domain.domainName}.do")
	public void doUpdate(HttpServletResponse response, @Validate ${domain.domainName} ${domain.domainName?uncap_first}){

		this.${domain.domainName?uncap_first}Service.update(${domain.domainName?uncap_first});
		WebUtil.success(response);
	}

	@RequestMapping("/delete${domain.domainName}.do")
	public void doDelete(HttpServletResponse response, ${domain.idAttr.type} ${domain.idAttr.name}){
		if(Strings.isBlank(${domain.idAttr.name})){
			WebUtil.fail(response, "请选择要删除的记录");
			return ;
		}
		this.${domain.domainName?uncap_first}Service.delete(${domain.idAttr.name});
		WebUtil.success(response);
	}

	@ResponseBody
	@RequestMapping("/get${domain.domainName}.do")
	public void doGet(HttpServletResponse response, ${domain.idAttr.type} ${domain.idAttr.name}){
		${domain.domainName} ${domain.domainName?uncap_first} = null;
		if(${domain.idAttr.name} == null || (${domain.domainName?uncap_first} = this.${domain.domainName?uncap_first}Service.get(${domain.idAttr.name})) == null ){
			WebUtil.fail(response, "查找不到 ${domain.domainCnName}");
			return ;
		}
		WebUtil.success(response, ${domain.domainName?uncap_first});
	}
}