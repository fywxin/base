package org.whale.${domain.pkgName!"system"}.domain;

<#list domain.attrs as attr>
    <#if attr.type == "Date">
import java.util.Date;
		<#break>
	</#if>
</#list>

import org.whale.system.base.BaseEntry;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Table;
<#list domain.attrs as attr >
	<#if !attr.isId>
	<#if attr.orderNum gt 0>
import org.whale.system.jdbc.annotation.Order;
		<#break>
	</#if>
	</#if>
</#list>

<#if domain.pkgName != "system">
import org.whale.system.domain.BaseEntry;
</#if>

/**
 * ${domain.cnName}
 *
 * @author 王金绍
 * @Date ${.now?date}
 */
@Table(value="${domain.dbName}", cnName="${domain.cnName}")
public class ${domain.name} extends BaseEntry {

	private static final long serialVersionUID = -${.now?long?c}l;
	
	@Id
	@Column(name="${domain.idAttr.sqlName}", cnName="${domain.idAttr.cnName}")
	private ${domain.idAttr.type} ${domain.idAttr.name};
	
<#list domain.attrs as attr >
	<#if !attr.isId>
	<#if attr.orderNum gt 0>
	@Order(index=${attr.orderNum})
	</#if>
  	@Column(cnName="${attr.cnName}", name="${attr.sqlName}"<#if !attr.nullAble>, nullable=${attr.nullAble?c}</#if><#if attr.uniqueAble>, unique=${attr.uniqueAble?c}</#if><#if !attr.updateAble>, updateable=${attr.updateAble?c}</#if>)
	private ${attr.type} ${attr.name};
	
	</#if>
</#list>
	
	/**${domain.idAttr.cnName} */
	public ${domain.idAttr.type} get${domain.idAttr.name?cap_first}(){
		return ${domain.idAttr.name};
	}
	
	/**${domain.idAttr.cnName} */
	public void set${domain.idAttr.name?cap_first}(${domain.idAttr.type} ${domain.idAttr.name}){
		this.${domain.idAttr.name} = ${domain.idAttr.name};
	}
<#list domain.attrs as attr>
	<#if !attr.isId>
	
	/**${attr.cnName} */
	public ${attr.type} get${attr.name?cap_first}(){
		return ${attr.name};
	}
	
	/**${attr.cnName} */
	public void set${attr.name?cap_first}(${attr.type} ${attr.name}){
		this.${attr.name} = ${attr.name};
	}
	</#if>
</#list>

}