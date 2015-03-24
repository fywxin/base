<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>修改密码</title>
	<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/user/doChangePassword', onSuccess: function(){
	    		$.alert('修改密码成功，请重新登入！');
	    		window.top.location.href="${ctx}/";
				$.closeWin();
	    	}}); 
	    	}
	    },
	    { line:true },
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
	    	$.closeWin();
	    	return false; 
	    	}
	    }
	 ]
	});
	$("#oldPassword")[0].focus();
});

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
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm" enctype="multipart/form-data" >
				<table>
					<col  width="100"/>
					<col  width="350"/>
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