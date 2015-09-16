<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新增 部门</title>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dept/doSave', onSuccess: function(){
		$.getParent().location.reload();
		$.alert("保存成功");
		$.closeWin();
	}}); 
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
			"orderNo": {
				maxlength: 4
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
						<td class="td-label">父部门</td>
						<td class="td-value">
							<tag:tree nodeName="deptName" nodeId="id" id="pid" nodes="${nodes }" nodePId="pid" value="${pid }"></tag:tree>
						</td>
						<td class="td-label"><span class="required">*</span>部门名称</td>
						<td class="td-value">
							<input type="text" id="deptName" name="deptName" style="width:160px;" value="${item.deptName}" maxlength="64" title="最多64个字"/>
						</td>
						
					</tr>
					<tr>
						<td class="td-label"><span class="required">*</span>部门编码</td>
						<td class="td-value">
							<input type="text" id="deptCode" name="deptCode" style="width:160px;" value="${item.deptCode}" maxlength="32" title="最多32个字"/>
						</td>
						<td class="td-label">联系电话</td>
						<td class="td-value">
							<input type="text" id="deptTel" name="deptTel" style="width:160px;" value="${item.deptTel}" maxlength="32" title="最多32字"  />
						</td>
						
					</tr>
					<tr>
						<td class="td-label">联系地址</td>
						<td class="td-value">
							<input type="text" id="deptAddr" name="deptAddr" style="width:160px;" value="${item.deptAddr}" maxlength="128" title="最多128字"  />
						</td>
						<td class="td-label">排序</td>
						<td class="td-value">
							<input type="text" id="orderNo" name="orderNo" style="width:160px;" value="${nextOrderNo}" maxlength="4" onkeyup="value=value.replace(/[^\d]/g,'')"/>
						</td>
					</tr>
					<tr>
						<td class="td-label">备注</td>
						<td class="td-value" colspan="3">
							<textarea rows="4" cols="99" name="remark" id="remark" title="最多只能输入256个字"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
