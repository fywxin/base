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
    
<body style="overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>菜单名：</label>
                <div class="col-sm-4">
                    <input id="menuName" name="menuName" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>所属菜单：</label>
                <div class="col-sm-4 input-group">
                    <tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${parentId }"></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>菜单类型：</label>
                <div class="col-sm-4">
                    <select id="menuType" name="menuType" onchange="changeMenuType()" >
						<option value="1">tab菜单</option>
						<option value="2">文件夹菜单</option>
						<option value="3">叶子菜单</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required" id="urlSign" style="visibility:hidden;">*</span>链接地址：</label>
                <div class="col-sm-4">
                    <input id="menuUrl" name="menuUrl" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">打开方式：</label>
                <div class="col-sm-4">
                    <select id="openType" name="openType" style="width:165px">
						<option value="1">窗口内打开</option>
						<option value="2">弹出窗口</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">是否公共：</label>
                <div class="col-sm-4">
                    <select id="isPublic" name="isPublic" style="width:165px">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">图标：</label>
                <div class="col-sm-4">
                    <input id="inco" name="inco" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">排序：</label>
                <div class="col-sm-4">
                    <input id="orderNo" name="orderNo" value="${nextNum }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12 col-sm-offset-2">
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                    <button class="btn btn-success" type="button" id="continueBut" onclick="window.location.reload();" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
                </div>
            </div>
        </form>
     </div>
</body>
</html>