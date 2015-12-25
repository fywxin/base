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
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>角色名称：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="roleName" name="roleName" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>角色编码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="roleCode" name="roleCode" class="form-control" style="width:250px"  >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">是否可删除：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <select id="canDelFlag" name="canDelFlag" style="width:250px">
                    	<option value="1">是</option>
                    	<option value="0">否</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">备注：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <textarea id="remark" name="remark" rows="3" class="form-control" style="width:250px" ></textarea>
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