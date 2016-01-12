package ${domain.pkgName!"com.cyou.fz.trade"}.${domain.domainName?uncap_first}.bean;

<#list domain.attrs as attr>
    <#if attr.type == "Date">
import java.util.Date;
		<#break>
	</#if>
</#list>
import com.cyou.fz.commons.mybatis.selecterplus.mybatis.annotation.Column;
import com.cyou.fz.commons.mybatis.selecterplus.mybatis.annotation.Id;
import com.cyou.fz.commons.mybatis.selecterplus.mybatis.annotation.Table;

/**
* Created by wjs on ${.now?date}.
*/
@Table("${domain.domainSqlName}")
public class ${domain.domainName} {
	
	@Id
	@Column("${domain.idAttr.sqlName}")
	private ${domain.idAttr.type} ${domain.idAttr.name};
	
<#list domain.attrs as attr >
	<#if !attr.isId>
  	@Column("${attr.sqlName}")
	private ${attr.type} ${attr.name};

	</#if>
</#list>
	
	/**${domain.idAttr.cnName} */
	public ${domain.idAttr.type} get${domain.idAttr.name?cap_first}(){ return ${domain.idAttr.name}; }
	
	/**${domain.idAttr.cnName} */
	public void set${domain.idAttr.name?cap_first}(${domain.idAttr.type} ${domain.idAttr.name}){ this.${domain.idAttr.name} = ${domain.idAttr.name}; }
<#list domain.attrs as attr>
	<#if !attr.isId>
	
	/**${attr.cnName} */
	public ${attr.type} get${attr.name?cap_first}(){ return ${attr.name}; }
	
	/**${attr.cnName} */
	public void set${attr.name?cap_first}(${attr.type} ${attr.name}){ this.${attr.name} = ${attr.name}; }
	</#if>
</#list>

}