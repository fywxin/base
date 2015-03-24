<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>${domain.cnName}列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
<#list domain.attrs as attr>
    <#if attr.type == "Date">
    	<#if attr.inQuery>
var time = new Date();
		<#break>
		</#if>
	</#if>
</#list>
$(function(){
	$.grid({
    	url :'${"$"+"{ctx}"}/${domain.name?uncap_first}/doList',
    	uid : "roleId",
    	toolbar: {items: [
<tag:auth authCode="${domain.name?upper_case}_SAVE">
           		{text: '新增${domain.cnName}', icon: 'add', click: function(){
           				$.openWin({url: "'${"$"+"{ctx}"}/${domain.name?uncap_first}/goSave","title":'新增${domain.cnName}'});
           			} 
				}
</tag:auth>
            ]
        },
        columns: [
				{display: '操作', name: 'opt', width: 70, frozen: true, render: function (row){
					var strArr = [];
<tag:auth authCode="${domain.name?upper_case}_UPDATE">
      	        	strArr.push("<a href='#' class='r15' onclick='update(\""+row.${domain.idAttr.name}+"\");'>修改</a>");
</tag:auth>
<tag:auth authCode="${domain.name?upper_case}_DEL">     	        		
      	        	strArr.push("<a href='#' class='r15' onclick='del(\""+row.${domain.idAttr.name}+"\");'>删除</a>");
</tag:auth>
<tag:auth authCode="${domain.name?upper_case}_VIEW">   				
					strArr.push("<a href='#' onclick='view(\""+row.${domain.idAttr.name}+"\");'>查看</a>");
</tag:auth>
					return strArr.join("");
				} },
				<#list domain.attrs as attr>
					<#if attr.inList>
					    <#if attr.type == "Date">
							{display: '${attr.cnName}', name: '${attr.name}', width: 140,
								render: function (row){
									time.setTime(row.${attr.name});
									return time.Format("yyyy-MM-dd hh:mm:ss.S");
								}
							},
						</#if>
						<#if attr.type != "Date">
							{display: '${attr.cnName}', name: '${attr.name}'},
						</#if>
					</#if>
				</#list>
              ]
	});
});

function update(id){
	$.openWin({url: "${"$"+"{ctx}"}/${domain.name?uncap_first}/goUpdate?id="+id, title:"修改${domain.cnName}"});
}

function del(id){
	$.del({url:"${"$"+"{ctx}"}/${domain.name?uncap_first}/doDel", datas:{ids: id}});
}

function view(id){
	$.openWin({url: "${"$"+"{ctx}"}/sms/goView?id="+id, title:"查看${domain.cnName}"});
}

</script>
</head>

<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
			<table >
					<col  width="10%" />
					<col  width="40%"/>
					<col  width="10%"/>
					<col  width="40%"/>
				<tbody>
<#if domain.queryAttrs?? >
	<#list domain.queryAttrs as qAttr>
		<#if qAttr_index%2==0>
						<tr>
		</#if>
							<td class="td-label">${qAttr.cnName}</td>
							<td class="td-value">
								<input type="text" id="${qAttr.name}" name="${qAttr.name}" style="width:160px;" />
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
	</div>
	<div id="grid" style="margin: 0px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>