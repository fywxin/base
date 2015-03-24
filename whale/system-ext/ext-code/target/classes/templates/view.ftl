<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>查看 ${domain.cnName}</title>
<%@include file="/html/jsp/common.jsp" %>
<script language="javascript">
$(function(){
	new ToolBar({items:[
		{id:"closeBut", className:"close", func:"$.closeWin();return false;", text:"关闭"}
	]});
});
</script>

</head>
    
<body>
<div class="body-box-form" >
	<div class="content-form">
		<div class="panelBar" id="panelBarDiv"></div>
		<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<input type="hidden" id="${domain.idAttr.name}" name="${domain.idAttr.name}" value="${r"${item."}${domain.idAttr.name}}" />
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
								${r"${item."}${fAttr.name}}
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
			</form>
		</div>
	</div>
</div>
</body>
</html>
