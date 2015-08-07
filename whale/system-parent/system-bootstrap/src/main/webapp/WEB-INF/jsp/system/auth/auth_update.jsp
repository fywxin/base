<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<%@include file="/html/jsp/ztree.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/auth/doUpdate'}); 
	    	}
	    },
	    { line:true },
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){
	    	$.getWinOpener().grid.loadData();
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
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<input type="hidden" name="authId" value="${item.authId }" />
				<input type="hidden" name="oldAuthCode" value="${item.authCode }" />
				<table>
					<col width="10%" />
					<col width="40%" />
					<col width="10%" />
					<col width="40%" />
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>权限名称</td>
							<td class="td-value"><input type="text" style="width:160px;" id="authName" name="authName" value="${item.authName }" maxlength="16" title="最多16个字"/></td>
							<td class="td-label" ><span class="required">*</span>权限编码</td>
							<td class="td-value">
								<input type="text" style="width:160px;" id="authCode" name="authCode" value="${item.authCode }" maxlength="64" title="最多64个字"/>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>所属菜单</td>
							<td class="td-value" colspan="3">
								<tag:tree nodeName="menuName" nodeId="menuId" id="menuId" nodes="${nodes }" nodePId="parentId" value="${item.menuId }" canSelectParent="false" beforeClick="fliterMenuType" beforeLoadTree="changeMenuIcon"></tag:tree>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
