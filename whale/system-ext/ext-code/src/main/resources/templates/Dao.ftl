package org.whale.${domain.pkgName!"system"}.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.whale.system.base.BaseDao;
import org.whale.${domain.pkgName!"system"}.domain.${domain.name};
<#if domain.pkgName != "system">
import org.whale.system.dao.BaseDao;
</#if>

@Repository
public class ${domain.name}Dao extends BaseDao<${domain.name}, Long> {

<#list domain.attrs as attr>
    <#if !attr.isId && attr.uniqueAble>
    /**
	 * 按 ${attr.cnName} 获取 ${domain.cnName}
	 * @param ${attr.name} ${attr.cnName}
	 * @return
	 */
    public ${domain.name} getBy${attr.name?cap_first }(${attr.type} ${attr.name}) {
    	StringBuilder strb = this.getSqlHead();
    	strb.append("and t.${attr.sqlName}=?");
    	
    	return this.queryForObject(strb.toString(), ${attr.name});
    }
    </#if>
</#list>
	
}