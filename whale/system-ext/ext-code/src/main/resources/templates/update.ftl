<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>更新 ${domain.cnName}</title>
<%@include file="/html/jsp/common.jsp" %>
<script language="javascript">
$(function(){
	new ToolBar({items:[
		{id:"saveBut", className:"save", func:"save()", text:"保存"},
		{id:"closeBut", className:"close", func:"$.closeWin();return false;", text:"关闭"}
	]});
});

function save(){
	if(!$("#dataForm").valid()) {return false;}
	toolBar.disableBut("saveBut");
	showAjaxHtml({"wait": true});
	
	var datas = $("#dataForm").serialize();
	$.ajax({
		url:'${"$"+"{ctx}"}/${domain.name?uncap_first}/doUpdate',
		type: 'post',
		data: datas,
		dataType: 'json',
		cache: false,
		error: function(obj){
			showAjaxHtml({"wait": false, "msg": '保存数据出错~'});
			toolBar.enableBut("saveBut");
	    },
	    success: function(obj){
	    	showAjaxHtml({"wait": false, "msg": obj.msg, "rs": obj.rs});
	    	if(obj.rs){
				$.info(obj.msg, function(){
					$.getWinOpener().grid.reload();
					$.getWindow().close();
				});
	    	}else{
	    		toolBar.enableBut("saveBut");
	    	}
	    }
	 });
}

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
								<input type="text" id="${fAttr.name}" name="${fAttr.name}" style="width:160px;" value="${r"${item."}${fAttr.name}}" <#if fAttr.width gt 0>maxlength="${fAttr.width?c}" title="最多${fAttr.width?c}字"</#if> <#if fAttr.type == "Integer">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> />
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
