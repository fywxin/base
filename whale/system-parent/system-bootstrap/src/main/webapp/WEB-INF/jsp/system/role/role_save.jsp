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
    
<body class="my_formBody"> 
	<div class="navbar-fixed-bottom my_toolbar" >
		<button type="button" class="btn btn-primary btn-sm" onclick="save()"><i class="fa fa-hdd-o" ></i> 保存</button>
		<button type="button" class="btn btn-info btn-sm" onclick="$.closeWin();"><i class="fa fa-times" ></i> 关闭</button>
	</div>
	<div id="formBoxDiv" class="my_formBox" >
		<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
		<form action="" method="post" id="dataForm">
			<table class="query">
				<col width="15%"/>
				<col width="35%"/>
				<col width="15%"/>
				<col width="35%"/>
				<tbody>
					<tr>
						<td class="td-label"><span class="required">*</span>角色名称</td>
						<td class="td-value"><input type="text" style="width:160px;" id="roleName" name="roleName" value="${item.roleName }" maxlength="16" /></td>
						<td class="td-label" ><span class="required">*</span>角色编码</td>
						<td class="td-value">
						<input type="text" style="width:160px;" id="roleCode" name="roleCode" value="${item.roleCode }" maxlength="16" /></td>
					</tr>
					<tr>
						<td class="td-label" >备注</td>
						<td class="td-value" colspan="3">
							<textarea id="remark" name="remark" rows="5" cols="99" title="最多只能输入100个字">
								${remark }
							</textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
