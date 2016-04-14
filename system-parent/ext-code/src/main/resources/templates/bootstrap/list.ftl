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
        colModel: [
<#if domain.treeModel != 3>
			{name:'${domain.idAttr.name}',index:'${domain.idAttr.name}', width:30,fixed:true, resize:false, formatter:
                function(val, options, row){
                    return '<input type="checkbox" value="'+row.${domain.idAttr.name}+'" name="chk_col">';
                }
        	},
</#if>
            {name:'opt',index:'opt', width:100, fixed:true, sortable:false, resize:false, align: "center",label:'操作',
                formatter: function(val, options, row){
                    var strArr = [];
          	<tag:auth authCode="${domain.domainName?upper_case}_UPDATE">
                    strArr.push("<button type='button' class='btn btn-default btn-ss' title='编辑' onclick=\"update('"+row.${domain.idAttr.name}+"')\"><i class='fa fa-pencil'></i> 编辑</button>");
            </tag:auth>
            <tag:auth authCode="${domain.domainName?upper_case}_DEL">
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.${domain.idAttr.name}+"')\"><i class='fa fa-trash-o'></i> 删除</button>");
            </tag:auth>
					return strArr.join("");
                }},
		<#if domain.treeModel == 3>
            {name:'name', width:180, label:'树字段'},
		</#if>
<#list domain.listAttrs as attr>
	<#if attr.type == "Date">
            {name:'${attr.sqlName}',width:120, label:'${attr.cnName}',
                formatter: function(val, options, row){
                    time.setTime(val);
                    return time.Format("yyyy-MM-dd hh:mm:ss.S");
				}
            }<#if (attr_has_next)>,</#if><#elseif attr.formType == "dict">
            {name:'${attr.sqlName}', width:120, label:'${attr.cnName}',
                formatter: function(val, options, row){
                    return dict_${attr.name}[val];
                }
            }<#if (attr_has_next)>,</#if><#else>
            {name:'${attr.sqlName}', width:160, label:'${attr.cnName}'}<#if (attr_has_next)>,</#if>
	</#if>
</#list>
		<#if domain.treeModel == 3>
            ,{name:'lft', hidden:true},
            {name:'rgt', hidden:true},
            {name:'level', hidden:true},
            {name:'uiicon', hidden:true}
		</#if>
        ]<#if domain.treeModel == 3>,</#if>
<#if domain.treeModel == 3>
		"loadonce":true,
		"rowNum":500,
		"scrollrows":true,
		"treeGrid":true,
		"ExpandColumn":"name",
		"treedatatype":"json",
		"treeGridModel":"nested",
		"treeReader":{
			"left_field":"lft",
			"right_field":"rgt",
			"level_field":"level",
			"leaf_field":"isLeaf",
			"expanded_field":"expanded",
			"loaded":"loaded",
			"icon_field":"icon"
    	}
</#if>
    });
});

function add(){
    $.openWin({url: "${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goSave","title":'新增${domain.domainCnName}'});
}

function update(id){
    $.openWin({url: "${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goUpdate?id="+id,"title":'编辑${domain.domainCnName}'});
}

function del(id){
    $.del({url:"${"$"+"{ctx}"}/${domain.domainName?uncap_first}/doDelete", datas:{ids:id}});
}
function view(id){
    $.openWin({url: "${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goView?id="+id, title:"查看${domain.domainCnName}"});
}
</script>
</head>

<body class="my_gridBody gray-bg">
<div class="my_gridBox">
    <form id="queryForm" >
        <table class="query">
            <col  width="15%" />
            <col  width="35%"/>
            <col  width="15%" />
            <col  width="35%"/>
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
        <div class="my_gridToolBar">
            <button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>
            <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
            <button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
        </div>
    </form>
    <table id="gridTable" ></table>
    <div id="gridPager"></div>
</div>
</body>
</html>