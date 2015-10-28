<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>更新 组织</title>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dept/doUpdate'}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"deptName": {
				validIllegalChar: true,
				required: true,
				maxlength: 64
			},
			"deptCode": {
				validIllegalChar: true,
				maxlength: 32
			},
			"orderNo": {
				maxlength: 4
			},
			"remark": {
				maxlength: 256
			},
			"pid": {
				required: true
			}
		}
	});
});

function filterSelf(){
	var self = zTree_pid.getNodeByParam("id", ${item.id});
	zTree_pid.removeNode(self);
}

</script>

</head>
    
<body style="overflow-x: hidden;"> 
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
        	<input type="hidden" id="id" name="id" value="${item.id}" />
            <div class="form-group">
                <label class="col-sm-2 control-label">父组织：</label>
                <div class="input-group col-sm-4 ">
                    <tag:tree nodeName="deptName" nodeId="id" id="pid" nodes="${nodes }" nodePId="pid" afterLoadTree="filterSelf" value="${item.pid }"></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>组织名称：</label>
                <div class="col-sm-4">
                    <input id="deptName" name="deptName" value="${item.deptName }" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">组织编码：</label>
                <div class="col-sm-4">
                    <input id="deptCode" name="deptCode" value="${item.deptCode }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">联系电话：</label>
                <div class="col-sm-4">
                    <input id="deptTel" name="deptTel" value="${item.deptTel }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">联系地址：</label>
                <div class="col-sm-4">
                    <input id="deptAddr" name="deptAddr" value="${item.deptAddr }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">排序：</label>
                <div class="col-sm-4">
                    <input id="orderNo" name="orderNo" value="${item.orderNo }" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">备注：</label>
                <div class="col-sm-4">
                    <textarea id="remark" name="remark" rows="3" cols="58" >${item.remark }</textarea>
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
