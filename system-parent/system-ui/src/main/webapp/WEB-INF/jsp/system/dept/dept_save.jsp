<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新增 组织</title>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dept/doSave'}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"deptName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"deptCode": {
				validIllegalChar: true,
				maxlength: 32,
                required: true
			},
			"deptTel": {
				validIllegalChar: true,
				maxlength: 32
			},
			"deptAddr": {
				validIllegalChar: true,
				maxlength: 128
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
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">父组织：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <tag:tree nodeName="deptName" nodeId="id" id="pid" nodes="${nodes }" nodePId="pid" afterLoadTree="filterSelf" value="${pid }"></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>组织名称：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline" style="padding-left:0px;">
                    <input id="deptName" name="deptName" class="form-control" style="width:250px" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>组织编码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline" style="padding-left:0px;">
                    <input id="deptCode" name="deptCode" class="form-control" style="width:250px" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">组织类型：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline" style="padding-left:0px;">
                    <input id="deptType" name="deptType" class="form-control" style="width:250px">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">联系电话：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline" style="padding-left:0px;">
                    <input id="deptTel" name="deptTel" class="form-control" style="width:250px">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">联系地址：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline"  style="padding-left:0px;">
                    <input id="deptAddr" name="deptAddr" class="form-control" style="width:250px">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">排序：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline"  style="padding-left:0px;">
                    <input id="orderNo" name="orderNo" value="${orderNo }" type="number" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" class="form-control" style="width:250px">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">备注：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline"  style="padding-left:0px;">
                    <textarea id="remark" name="remark" rows="3" class="form-control" style="width:250px"></textarea>
                </div>
            </div>
            <div class="form-group" style="border-bottom:0px;margin-top:20px;">
                <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保存</button>
                    <button class="btn btn-success" type="button" id="continueBut" onclick="window.location.reload();" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
					<button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
                </div>
            </div>
        </form>
     </div>
</div>
</body>
</html>
