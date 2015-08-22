package ${domain.pkgName!"org.whale.system"}.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.whale.system.base.BaseDao;
import ${domain.pkgName!"org.whale.system"}.domain.${domain.domainName};

/**
 * ${domain.domainCnName}Dao 
 *
 * @author 王金绍
 * ${.now}
 */
@Repository
public class ${domain.domainName}Dao extends BaseDao<${domain.domainName}, Long> {

<#list domain.attrs as attr>
    <#if !attr.isId && attr.isUnique>
    /**
	 * 按 ${attr.cnName} 获取 ${domain.domainCnName}
	 * @param ${attr.name} ${attr.cnName}
	 * @return
	 */
    public ${domain.domainName} getBy${attr.name?cap_first }(${attr.type} ${attr.name}) {
    	StringBuilder strb = this.getSqlHead();
    	strb.append("and t.${attr.sqlName}=?");
    	
    	return this.queryForObject(strb.toString(), ${attr.name});
    }
    </#if>
</#list>
	
}