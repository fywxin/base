<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/role/doSave'}); 
}
$(function() {
	$("#dataForm").validate({
		rules: {
			"roleName": {
				validIllegalChar: true,
				maxlength: 16,
				required: true
			},
			"roleCode": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"remark":{
				validIllegalChar: true,
				maxlength: 100
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
        	<div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>角色名称：</label>
                <div class="col-sm-4">
                    <input id="roleName" name="roleName" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>角色编码：</label>
                <div class="col-sm-4">
                    <input id="roleCode" name="roleCode" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">备注：</label>
                <div class="col-sm-4">
                    <textarea id="remark" name="remark" rows="3" cols="58" ></textarea>
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