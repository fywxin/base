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
		$.closeWin();
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
    
<body class="my_formBody"> 
	<div class="navbar-fixed-bottom my_toolbar" >
		<button type="button" class="btn btn-primary btn-sm" onclick="save()"><i class="fa fa-hdd-o" ></i> 保存</button>
		<button type="button" class="btn btn-info btn-sm" onclick="$.closeWin();"><i class="fa fa-times" ></i> 关闭</button>
	</div>
	<div id="formBoxDiv" class="my_formBox" >
		<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
		<form action="" method="post" id="dataForm">
			<table class="query">
					<col  width="100"/>
					<col />
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>旧密码</td>
							<td class="td-value"><input type="password" id="oldPassword" name="oldPassword" maxlength="10" style="width:160px;"  title="请输入密码,6~10位."/></td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>新密码</td>
							<td class="td-value"><input type="password" style="width:160px;" id="newPassword1" maxlength="10" name="newPassword1" title="请输入密码,6~10位." /></td>
							
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>确认新密码</td>
							<td class="td-value"><input type="password" style="width:160px;" id="newPassword2" maxlength="10" name="newPassword2" title="请输入密码,6~10位." /></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>