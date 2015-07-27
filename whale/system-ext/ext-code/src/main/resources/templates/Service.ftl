package ${domain.pkgName!"org.whale.system"}.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;

import org.whale.system.base.BaseDao;
import org.whale.system.service.BaseService;
import ${domain.pkgName!"org.whale.system"}.dao.${domain.domainName}Dao;
import ${domain.pkgName!"org.whale.system"}.domain.${domain.domainName};

/**
 * ${domain.domainCnName}管理
 *
 * @author 王金绍
 * ${.now}
 */
@Service
public class ${domain.domainName}Service extends BaseService<${domain.domainName}, Long> {

	@Autowired
	private ${domain.domainName}Dao ${domain.domainName?uncap_first}Dao;
	
<#list domain.attrs as attr>
    <#if !attr.isId && attr.isUnique>
    /**
	 * 按 ${attr.cnName} 获取 ${domain.domainCnName}
	 * @param ${attr.name} ${attr.cnName}
	 * @return
	 */
    public ${domain.domainName} getBy${attr.name?cap_first }(${attr.type} ${attr.name}) {
    	<#if attr.type == "String">
    	if(Strings.isBlank(${attr.name})){
    		return null;
    	}
    	<#else>
    	if(${attr.name} == null){
    		return null;
    	}
    	</#if>
    	
    	return this.${domain.domainName?uncap_first}Dao.getBy${attr.name?cap_first }(${attr.name});
    }
    </#if>
</#list>
	
	@Override
	public BaseDao<${domain.domainName}, Long> getDao() {
		return ${domain.domainName?uncap_first}Dao;
	}

}