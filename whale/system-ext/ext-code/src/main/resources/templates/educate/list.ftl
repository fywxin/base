<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<#list domain.listAttrs as attr>
	<#if attr.formType == "dict">
<%@page import="org.whale.system.cache.service.DictCacheService"%>
		<#break>
	</#if>
</#list>
<!DOCTYPE html>
<html>
<head>
	<title>${domain.domainCnName}列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
<#list domain.listAttrs as attr>
	<#if attr.formType == "dict">
var dict_${attr.name} = <%=DictCacheService.getThis().getMapJson("${attr.dictName}") %>;
	</#if>
</#list>

<#list domain.attrs as attr>
    <#if attr.type == "Date">
    	<#if attr.inQuery>
var time = new Date();
		<#break>
		</#if>
	</#if>
</#list>

$(function(){
    $("#gridTable").grid({
        url :'${"$"+"{ctx}"}/${domain.domainName?uncap_first}/doList?id=${"$"+"{id}"}',
        idField: '${domain.idAttr.sqlName}',
        columns: [
            {
                field: 'chk',
                checkbox: true,
                width: '3%',
                align: 'center'
            },
			{
                field: 'opt',
                title: '操作',
                width: '20%',
                align: 'center',
                formatter: function(value, row, index){
                    var strArr = [];
				<tag:auth authCode="${domain.domainName?uncap_first}:update">
                    strArr.push("<a href='javascript:;' onclick=\"go('修改${domain.domainCnName}','${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goUpdate?id="+row.${domain.idAttr.sqlName}+"')\">修改</a>");
				</tag:auth>
					return strArr.join("");
                }
            },
<#list domain.listAttrs as attr>
			{
				field: '${attr.sqlName}',
                title: '${attr.cnName}',<#if attr.type == "dict">
				formatter: function(value, row, index){
                    return dict_${attr.name}[value];
                },</#if>
                width: '10%'
			}<#if (attr_has_next)>,</#if>
</#list>
		]
    });
});

function del(id){
    $.del({url:"${"$"+"{ctx}"}/${domain.domainName?uncap_first}/doDel"});
}
function view(id){
    $.openWin({url: "${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goView?id="+id, title:"查看${domain.domainCnName}"});
}
</script>
</head>

<body style="overflow: hidden;">
<div class="my_gridBox">
    <form id="queryForm" >
        <table class="query">
            <tbody>
<#if domain.queryAttrs?? >
	<#list domain.queryAttrs as qAttr>
		<#if qAttr_index%2==0>
						<tr>
		</#if>
							<td class="td-label">${qAttr.cnName}</td>
							<td class="td-value">
							<#if qAttr.formType == "dict">
								<tag:dict id="${qAttr.name}" dictCode="${qAttr.dictName}" headerLabel="-- 请选择 --"></tag:dict>
							<#else>
								<input type="text" id="${qAttr.name}" name="${qAttr.name}" style="width:160px;" value="${r"${item."}${qAttr.name}}" <#if qAttr.type == "Integer" || qAttr.type == "Long">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> />
							</#if>
							</td>
		<#if qAttr_index%2==1>
						</tr>
		</#if>
		<#if qAttr_index%2==0>
			<#if !qAttr_has_next>
						</tr>
			</#if>
		</#if>
	</#list>
</#if>
				</tbody>
			</table>
    </form>
    <div id="mytoolbar" style="margin-left:5px;">
        <tag:auth authCode="${domain.domainName?uncap_first}:save">
        <button type="button" class="btn btn-primary btn-sm" onclick="go('新增${domain.domainCnName}','${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goSave')"><i class="fa fa-plus"></i> 新 增 </button>
        </tag:auth>
        <tag:auth authCode="${domain.domainName?uncap_first}:del">
		<button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
        </tag:auth>
    </div>
    <div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
        <table id="gridTable" ></table>
    </div>
</div>
</body>
</html>