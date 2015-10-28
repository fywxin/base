<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/user/doUpdate'}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"userName": {
				validIllegalChar: true,
				maxlength: 16,
				required: true
			},
			"realName":{
				validIllegalChar: true,
				required: true,
				maxlength: 32
			},
			"phone": {
				validIllegalChar: true,
				required: true,
				isMobile: true,
				maxlength: 11
			},
			"email": {
				validIllegalChar: true,
				maxlength: 32,
				email: true
			},
			"addOn":{
				maxlength: 1024
			},
			"deptId":{
				required: true
			},
			"remark":{
				validIllegalChar: true,
				maxlength: 500
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
			<input type="hidden" id="userId" name="userId" value="${item.userId }" />
			<div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>用户名：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" class="form-control" value="${item.userName }" readonly="readonly" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>真实姓名：</label>
                <div class="col-sm-4">
                    <input id="realName" name="realName" class="form-control" value="${item.realName }" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">联系电话：</label>
                <div class="col-sm-4">
                    <input id="phone" name="phone" value="${item.phone }" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">邮箱地址：</label>
                <div class="col-sm-4">
                    <input id="email" name="email" value="${item.email }" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">所属组织：</label>
                <div class="col-sm-4 input-group">
                   <tag:tree nodeName="deptName" nodeId="id" id="deptId" value="${item.deptId }" nodes="${nodes }" nodePId="pid" mulitSel="false" ></tag:tree>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">附加信息：</label>
                <div class="col-sm-4">
                    <textarea id="addOn" name="addOn" rows="2" cols="58" >${item.addOn }</textarea>
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