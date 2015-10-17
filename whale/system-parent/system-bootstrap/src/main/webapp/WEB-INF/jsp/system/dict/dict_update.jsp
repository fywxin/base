<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dict/doUpdate', onSuccess: function(){
		$.getParent().parent.location.reload();
		$.alert("保存成功");
		$.closeWin();
	}}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"dictName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"dictCode": {
				validIllegalChar: true,
				maxlength: 32,
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
    
<body class="my_formBody"> 
	<div class="navbar-fixed-bottom my_toolbar" >
		<button type="button" class="btn btn-primary btn-sm" onclick="save()"><i class="fa fa-hdd-o" ></i> 保存</button>
		<button type="button" class="btn btn-info btn-sm" onclick="$.closeWin();"><i class="fa fa-times" ></i> 关闭</button>
	</div>
	<div id="formBoxDiv" class="my_formBox" >
		<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
		<form action="" method="post" id="dataForm">
				<input type="hidden" id="dictId" name="dictId" value="${item.dictId }"/>
				<input type="hidden" id="status" name="status" value="${item.status }"/>
				<table class="query">
					<col width="10%" />
					<col width="40%" />
					<col width="10%" />
					<col width="40%" />
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>字典名</td>
							<td class="td-value"><input type="text" style="width:160px;" id="dictName" name="dictName" value="${item.dictName }" maxlength="64"/></td>
							<td class="td-label" ><span class="required">*</span>字典编码</td>
							<td class="td-value">${item.dictCode  }
								<input type="hidden" style="width:160px;" id="dictCode" name="dictCode" value="${item.dictCode }" maxlength="32" />
							</td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<textarea id="remark" name="remark" rows="4" cols="99" title="最多只能输入500个字">${item.remark }</textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
