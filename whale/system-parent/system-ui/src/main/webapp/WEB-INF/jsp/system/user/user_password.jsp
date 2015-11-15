<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>修改密码</title>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/user/doChangePassword', onSuccess: function(){
		$.alert('修改密码成功，请重新登入！');
		window.top.location.href="${ctx}/";
	}}); 
}
//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"oldPassword": {
				validIllegalChar: true,
				maxlength: 12,
				minlength: 6,
				required: true
			},
			"newPassword1": {
				validIllegalChar: true,
				maxlength: 12,
				minlength: 6,
				required: true
			},
			"newPassword2": {
				validIllegalChar: true,
				maxlength: 12,
				minlength: 6,
				required: true,
				equalTo: "#newPassword1"
			}
		},
		messages:{
			"newPassword2": {
				equalTo: "密码不一致"
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
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>旧密码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input type="password" id="oldPassword" name="oldPassword" maxlength="10" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>新密码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input type="password" id="newPassword1" name="newPassword1" maxlength="10" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>确认新密码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input type="password" id="newPassword3" name="newPassword3" maxlength="10" class="form-control" style="width:250px" >
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