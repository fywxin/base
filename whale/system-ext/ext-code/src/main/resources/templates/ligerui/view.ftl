<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>查看 ${domain.domainCnName}</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
	    	$.closeWin();
	    	return false; 
	    	}
	   }]
	});
});
</script>

</head>
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<table cellspacing="0" cellpadding="0" width="100%">
				<col width="10%"/>
				<col width="40%"/>
				<col width="10%"/>
				<col width="40%"/>
				<tbody>
						<#list domain.formAttrs as fAttr>
	<#if fAttr_index%2==0>
					<tr>
	</#if>
						<td class="td-label">${fAttr.cnName}</td>
						<td class="td-value">
						<#if fAttr.formType == "dict">
							<tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" value="${r"${item."}${fAttr.name}}" readonly="true"></tag:dict>
						<#elseif fAttr.name == "remark" || fAttr.formType == "textarea">
							<div style="height:80px;" class="textAreaDiv">${r"${item."}${fAttr.name}}</div>
						<#else>
						${r"${item."}${fAttr.name}}
						</#if>
						</td>
	<#if fAttr_index%2==1>
					</tr>
	</#if>
	<#if fAttr_index%2==0>
		<#if !fAttr_has_next>
					</tr>
		</#if>
	</#if>
</#list>
				</tbody>
		</table>
	</div>
</body>
</html>
