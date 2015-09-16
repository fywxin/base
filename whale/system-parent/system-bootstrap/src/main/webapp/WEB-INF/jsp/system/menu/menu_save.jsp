<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/menu/doSave', onSuccess: function(){
		$.getParent().location.reload();
		$.alert("保存成功");
		$.closeWin();
	}}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"menuName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"menuUrl": {
				maxlength: 255
			},
			"inco": {
				maxlength: 128
			},
			"orderNo": {
				maxlength: 4
			}
		}
	});
});

function changeMenuType(){
	var val = parseInt($("#menuType").val());
	if(val == 1){
		$("#urlSign").css("visibility", "hidden");
		$("#menuUrl").css("visibility", "hidden").rules("remove","required");
	}else if(val == 2){
		$("#urlSign").css("visibility", "hidden");
		$("#menuUrl").css("visibility", "hidden").rules("remove","required");
	}else if(val == 3){
		$("#urlSign").css("visibility", "visible");
		$("#menuUrl").css("visibility", "visible").rules("add",{"required":true});
	}
}

</script>

</head>
    
<body class="my_formBody"> 
	<div class="navbar-fixed-bottom my_toolbar" >
		<button type="button" class="btn btn-primary btn-sm" onclick="save()"><i class="fa fa-hdd-o" ></i> 保存</button>
		<button type="button" class="btn btn-info btn-sm" onclick="$.closeWin();"><i class="fa fa-times" ></i> 关闭</button>
	</div>
	<div id="formBoxDiv" class="my_formBox" >
		<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
		<form action="" method="post" id="dataForm">
			<table class="query">
				<col width="15%"/>
				<col width="35%"/>
				<col width="15%"/>
				<col width="35%"/>
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>菜单名</td>
							<td class="td-value"><input type="text" style="width:160px;" id="menuName" name="menuName" maxlength="64" title="最多64个字"/></td>
							<td class="td-label" ><span class="required">*</span>所属菜单</td>
							<td class="td-value">
								<tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${parentId }"></tag:tree>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>菜单类型</td>
							<td class="td-value">
								<select id="menuType" name="menuType" style="width:165px" onchange="changeMenuType()" >
									<option value="1">tab菜单</option>
									<option value="2">文件夹菜单</option>
									<option value="3">叶子菜单</option>
								</select>
							</td>
							<td class="td-label">图标地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="inco" name="inco" maxlength="128" title="最多128个字"/></td>
							
						</tr>
						<tr>
							<td class="td-label">打开方式</td>
							<td class="td-value">
								<select id="openType" name="openType" style="width:165px">
									<option value="1">窗口内打开</option>
									<option value="2">弹出窗口</option>
								</select>
							</td>
							<td class="td-label">公共菜单</td>
							<td class="td-value" >
								<select id="isPublic" name="isPublic" style="width:165px">
									<option value="0">否</option>
									<option value="1">是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required" id="urlSign" style="visibility:hidden;">*</span>链接地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="menuUrl" name="menuUrl" maxlength="255" title="最多255个字"/></td>		
							<td class="td-label">排列顺序</td>
							<td class="td-value" ><input type="text" style="width:160px;" id="orderNo" name="orderNo" value="${nextNum }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" title="最多4个字"/></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
