<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/menu/doUpdate'}); 
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

</script>

</head>
<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row">
        <form class="form-horizontal m-t" id="dataForm">
        	<input type="hidden" id="menuId" name="menuId" value="${item.menuId }">
        	<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>菜单名：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="menuName" name="menuName" value="${item.menuName }" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>所属菜单：</label>
                <div class="col-sm-4 input-group">
                    <tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${item.parentId }"></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>菜单类型：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input type="hidden" id="menuType" name="menuType" value="${item.menuType}" />
					<c:if test="${item.menuType == 1}"><input class="form-control" style="width:250px" value="tab菜单" readonly="readonly" ></c:if>
					<c:if test="${item.menuType == 2}"><input class="form-control" style="width:250px" value="文件夹菜单" readonly="readonly" ></c:if>
					<c:if test="${item.menuType == 3}"><input class="form-control" style="width:250px" value="叶子菜单" readonly="readonly" ></c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i id="urlSign" style="visibility:hidden;">*</i>链接地址：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <c:if test="${item.menuType == 3}"><input id="menuUrl" name="menuUrl" value="${item.menuUrl }" class="form-control" style="width:250px"  ></c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">打开方式：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <select id="openType" name="openType" style="width:250px">
						<option value="1" <c:if test="${item.openType == 1}">selected="selected"</c:if>>窗口内打开</option>
						<option value="2" <c:if test="${item.openType == 2}">selected="selected"</c:if>>弹出窗口</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">是否公共：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <select id="publicFlag" name="publicFlag" style="width:250px">
						<option value="false" <c:if test="${!item.publicFlag}">selected="selected"</c:if>>否</option>
						<option value="true" <c:if test="${item.publicFlag}">selected="selected"</c:if>>是</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">图标：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="inco" name="inco" value="${item.inco }" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">排序：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="orderNo" name="orderNo" type="number" value="${item.orderNo }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" class="form-control" style="width:250px"  >
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