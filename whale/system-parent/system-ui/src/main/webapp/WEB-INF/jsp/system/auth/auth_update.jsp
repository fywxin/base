<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/auth/doUpdate'}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"authName": {
				validIllegalChar: true,
				maxlength: 32,
				required: true
			},
			"authCode": {
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
    
<body style="overflow-x: hidden;"> 
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-2 control-label">权限名称：</label>
                <div class="col-sm-4 ">
                    <input id="authName" name="authName" value="${item.authName }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">权限编码：</label>
                <div class="col-sm-4 ">
                    <input id="authCode" name="authCode" value="${item.authCode }" class="form-control" readonly="readonly" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">所属菜单：</label>
                <div class="input-group col-sm-4 ">
                    <tag:tree nodeName="menuName" nodeId="menuId" id="menuId" nodes="${nodes }" nodePId="parentId" value="${item.menuId }" canSelectParent="false" beforeClick="fliterMenuType" beforeLoadTree="changeMenuIcon"></tag:tree>
                </div>
            </div>
			<div class="form-group">
                <div class="col-sm-12 col-sm-offset-2">
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                </div>
            </div>
        </form>
     </div>
</body>
</html>