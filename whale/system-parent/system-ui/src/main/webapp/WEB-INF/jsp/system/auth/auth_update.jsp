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
    
<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">权限名称：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="authName" name="authName" value="${item.authName }" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">权限编码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="authCode" name="authCode" value="${item.authCode }" class="form-control" style="width:250px"  readonly="readonly" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">所属菜单：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <tag:tree nodeName="menuName" nodeId="menuId" id="menuId" nodes="${nodes }" nodePId="parentId" value="${item.menuId }" canSelectParent="false" beforeClick="fliterMenuType" beforeLoadTree="changeMenuIcon"></tag:tree>
                </div>
            </div>
			<div class="form-group" style="border-bottom:0px;margin-top:20px;">
                <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                    <button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
                </div>
            </div>
        </form>
     </div>
  </div>
</body>
</html>