package ${domain.pkgName!"org.whale.system"}.domain;

<#list domain.attrs as attr>
    <#if attr.type == "Date">
import java.util.Date;
		<#break>
	</#if>
</#list>

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;
<#if domain.treeModel == 3>
import java.util.HashMap;
import java.util.Map;
import org.whale.system.common.util.TreeNode;
</#if>

/**
 * ${domain.domainCnName}
 *
 * @author wjs
 * @Date ${.now?date}
 */
@Table(value="${domain.domainSqlName}", cnName="${domain.domainCnName}")
public class ${domain.domainName} extends BaseEntry<#if domain.treeModel == 3> implements TreeNode</#if> {
	private static final long serialVersionUID = -${.now?long?c}l;
	
	@Id
	@Column(name="${domain.idAttr.sqlName}", cnName="${domain.idAttr.cnName}")
	private ${domain.idAttr.type} ${domain.idAttr.name};
	
<#list domain.attrs as attr >
	<#if !attr.isId>
	<#if !attr.isNull>@Validate(required=true)
  	@Column(cnName="${attr.cnName}", name="${attr.sqlName}"<#if attr.isUnique>, unique=${attr.isUnique?c}</#if><#if !attr.isEdit>, updateable=${attr.isEdit?c}</#if>)
	private ${attr.type} ${attr.name};
	
	</#if>
<#if attr.isNull>
@Column(cnName="${attr.cnName}", name="${attr.sqlName}"<#if attr.isUnique>, unique=${attr.isUnique?c}</#if><#if !attr.isEdit>, updateable=${attr.isEdit?c}</#if>)
	private ${attr.type} ${attr.name};
	
	</#if>
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

<#if domain.treeModel == 3>
	@Override
	public Long id() {
		return ${domain.treeId};
	}

	@Override
	public Long pid() {
		return ${domain.treePid};
	}

	@Override
	public String name() {
		return ${domain.treeName};
	}

	@Override
	public Map<String, Object> asMap() {
		Map<String, Object> tmp = new HashMap<String, Object>();
	<#list domain.attrs as attr>
		<#if !attr.isId>
        	tmp.put("${attr.name}", ${attr.name});
		</#if>
	</#list>
		return tmp;
	}
</#if>

}