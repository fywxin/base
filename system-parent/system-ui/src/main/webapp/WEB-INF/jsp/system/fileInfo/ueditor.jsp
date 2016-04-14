<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>图片上传</title>
	
<%@include file="/jsp/form.jsp" %>
<script src="${resource}/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="${resource}/ueditor/ueditor.all.js" type="text/javascript"></script>
<script src="${resource}/ueditor/lang/zh-cn/zh-cn.js" type="text/javascript"></script>

<script type="" language="Javascript">
$(function(){
	new ToolBar({items:[
		{id:"saveBut", className:"save", func:"save()", text:"保存"},
		{id:"closeBut", className:"close", func:"$.closeWin();return false;", text:"关闭"}
	]});
});

var ue = UE.getEditor('container',{
	initialFrameWidth : parseInt($.clientWidth()) -155 ,
	initialFrameHeight : 350
	});
</script>
</head>

<body style="padding:0;margin:0;">
	<div class="body-box-form" >
	<div class="content-form">
		<div class="panelBar" id="panelBarDiv"></div>
		<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm" style="cursor: pointer;margin-left: ">
				<table cellspacing="0" cellpadding="0" width="100%">
					<col width="10%"/>
					<col width="40%"/>
					<col width="10%"/>
					<col width="40%"/>
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>角色名称</td>
							<td class="td-value"><input type="text" style="width:160px;" id="roleName" name="roleName" maxlength="16" title="最多16个字"/></td>
							<td class="td-label" ><span class="required">*</span>角色编码</td>
							<td class="td-value"><input type="text" style="width:160px;" id="roleCode" name="roleCode" maxlength="64" title="最多64位"/></td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<script id="container" name="content" type="text/plain"></script>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>