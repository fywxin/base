<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/menu/doUpdate', onSuccess: function(){
		$.msg("保存成功");
		window.parent.location.reload();
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

</script>

</head>
    
<body style="overflow-x: hidden;"> 
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
        	<input type="hidden" id="menuId" name="menuId" value="${item.menuId }">
        	<div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>菜单名：</label>
                <div class="col-sm-4">
                    <input id="menuName" name="menuName" value="${item.menuName }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>所属菜单：</label>
                <div class="col-sm-4 input-group">
                    <tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${item.parentId }"></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>菜单类型：</label>
                <div class="col-sm-4">
                    <input type="hidden" id="menuType" name="menuType" value="${item.menuType}" />
					<c:if test="${item.menuType == 1}">tab菜单</c:if>
					<c:if test="${item.menuType == 2}">文件夹菜单</c:if>
					<c:if test="${item.menuType == 3}">叶子菜单</c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required" id="urlSign" style="visibility:hidden;">*</span>链接地址：</label>
                <div class="col-sm-4">
                    <c:if test="${item.menuType == 3}"><input id="menuUrl" name="menuUrl" value="${item.menuUrl }" class="form-control" ></c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">打开方式：</label>
                <div class="col-sm-4">
                    <select id="openType" name="openType" style="width:165px">
						<option value="1" <c:if test="${item.openType == 1}">selected="selected"</c:if>>窗口内打开</option>
									<option value="2" <c:if test="${item.openType == 2}">selected="selected"</c:if>>弹出窗口</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">是否公共：</label>
                <div class="col-sm-4">
                    <select id="isPublic" name="isPublic" style="width:165px">
						<option value="0" <c:if test="${item.isPublic != 1}">selected="selected"</c:if>>否</option>
									<option value="1" <c:if test="${item.isPublic == 1}">selected="selected"</c:if>>是</option>
					</select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">图标：</label>
                <div class="col-sm-4">
                    <input id="inco" name="inco" value="${item.inco }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">排序：</label>
                <div class="col-sm-4">
                    <input id="orderNo" name="orderNo" value="${item.orderNo }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" class="form-control" >
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