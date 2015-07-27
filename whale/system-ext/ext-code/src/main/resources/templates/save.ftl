<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新增 ${domain.domainCnName}</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${"$"+"{ctx}"}/${domain.domainName?uncap_first}/doSave'}); 
	    	}
	    },
	    { line:true },
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
	    	$.closeWin();
	    	return false; 
	    	}
	    }
	 ]
	});
});

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
<#list domain.formAttrs as fAttr>
			"${fAttr.name}": {
				<#if fAttr.type == "String">validIllegalChar: true,</#if>
				<#if !fAttr.nullAble >required: true,</#if>
				<#if fAttr.width gt 0>maxlength: ${fAttr.width?c}</#if>
			}<#if fAttr_has_next>,</#if>
</#list>
		}
	});
});
</script>

</head>
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
	<div class="edit-form">
		<form action="" method="post" id="dataForm">
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
							<input type="text" id="${fAttr.name}" name="${fAttr.name}" style="width:160px;" value="${r"${item."}${fAttr.name}}" <#if fAttr.width gt 0>maxlength="${fAttr.width}" title="最多${fAttr.width}字"</#if> <#if fAttr.type == "Integer">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> />
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
</body>
</html>
