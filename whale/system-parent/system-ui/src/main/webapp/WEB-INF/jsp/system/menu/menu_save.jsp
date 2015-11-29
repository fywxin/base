<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/menu/doSave'}); 
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
			"parentId": {
				required: true
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
    
<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row">
	
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>菜单名：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="menuName" name="menuName" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>所属菜单：</label>
                <div class="col-sm-4 input-group">
                    <tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${parentId }"></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>菜单类型：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <select id="menuType" name="menuType" onchange="changeMenuType()" style="width:250px">
						<option value="1">tab菜单</option>
						<option value="2">文件夹菜单</option>
						<option value="3">叶子菜单</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">是否公共：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <select id="publicFlag" name="publicFlag" style="width:250px;visibility:hidden;">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i id="urlSign" style="visibility:hidden;">*</i>链接地址：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="menuUrl" name="menuUrl" class="form-control" style="width:250px;visibility:hidden;"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">打开方式：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <select id="openType" name="openType" style="width:250px" class="chosen-select" data-placeholder="选择打开方式...">
						<option value="1">窗口内打开</option>
						<option value="2">弹出窗口</option>
					</select>
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">图标：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="inco" name="inco" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">排序：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="orderNo" name="orderNo" type="number" value="${nextNum }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group" style="border-bottom:0px;margin-top:20px;">
                <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                    <button class="btn btn-success" type="button" id="continueBut" onclick="window.location.reload();" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
                    <button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
                </div>
            </div>
            
        </form>
     </div>
</div>
</body>
</html>