<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/auth/doSave'}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"authName": {
				validIllegalChar: true,
				maxlength: 16,
				required: true
			},
			"authCode": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"menuId": {
				required: true
			}
		}
	});
});

function fliterMenuType(treeId, treeNode){
	if(treeNode.menuType != 3)
		return false;
	return true;
}

function changeMenuIcon(){
	var nodes = zNodes_menuId;
	for(var i=0; i<nodes.length; i++){
		if(nodes[i].menuType != 3){
			nodes[i].isParent = true;
		}
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
							<td class="td-label"><span class="required">*</span>权限名称</td>
							<td class="td-value"><input type="text" style="width:160px;" id="authName" name="authName" maxlength="16" title="最多16个字"/></td>
							<td class="td-label" ><span class="required">*</span>权限编码</td>
							<td class="td-value">
								<input type="text" style="width:160px;" id="authCode" name="authCode" maxlength="64" title="最多64个字"/>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>所属菜单</td>
							<td class="td-value" colspan="3">
								<tag:tree nodeName="menuName" nodeId="menuId" id="menuId" nodes="${nodes }" nodePId="parentId" value="${defaultId }" canSelectParent="false" beforeClick="fliterMenuType" beforeLoadTree="changeMenuIcon"></tag:tree>
							</td>
							
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
