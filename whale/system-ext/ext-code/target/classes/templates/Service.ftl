package org.whale.${domain.pkgName!"system"}.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;

import org.whale.system.base.BaseDao;
import org.whale.system.base.BaseService;
import org.whale.${domain.pkgName!"system"}.dao.${domain.name}Dao;
import org.whale.${domain.pkgName!"system"}.domain.${domain.name};
<#if domain.pkgName != "system">
import org.whale.system.service.BaseService;
</#if>

/**
 * ${domain.cnName} 管理
 *
 * @author 王金绍
 * ${.now}
 */
@Service
public class ${domain.name}Service extends BaseService<${domain.name}, Long> {

	@Autowired
	private ${domain.name}Dao ${domain.name?uncap_first}Dao;
	
<#list domain.attrs as attr>
    <#if !attr.isId && attr.uniqueAble>
    /**
	 * 按 ${attr.cnName} 获取 ${domain.cnName}
	 * @param ${attr.name} ${attr.cnName}
	 * @return
	 */
    public ${domain.name} getBy${attr.name?cap_first }(${attr.type} ${attr.name}) {
    	<#if attr.type == "String">
    	if(Strings.isBlank(${attr.name})){
    		return null;
    	}
    	<#else>
    	if(${attr.name} == null){
    		return null;
    	}
    	</#if>
    	
    	return this.${domain.name?uncap_first}Dao.getBy${attr.name?cap_first }(${attr.name});
    }
    </#if>
</#list>
	
	@Override
	public BaseDao<${domain.name}, Long> getDao() {
		return ${domain.name?uncap_first}Dao;
	}

}