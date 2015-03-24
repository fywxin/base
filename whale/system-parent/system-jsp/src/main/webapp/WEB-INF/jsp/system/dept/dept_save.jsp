<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新增 部门</title>
<%@include file="/html/jsp/common.jsp" %>
<%@include file="/html/jsp/ztree.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/dept/doSave'}); 
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
});

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
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
	<div class="edit-form">
		<form action="" method="post" id="dataForm">
			<table>
				<col width="10%"/>
				<col width="40%"/>
				<col width="10%"/>
				<col width="40%"/>
				<tbody>
					<tr>
						<td class="td-label">父部门</td>
						<td class="td-value">
							${pName }
							<input type="hidden" id="pid" name="pid" style="width:160px;" value="${pid}" />
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
							<textarea rows="4" cols="2" name="remark" id="remark" title="最多只能输入256个字"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
