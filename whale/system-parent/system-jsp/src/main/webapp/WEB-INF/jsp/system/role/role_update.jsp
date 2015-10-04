<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>

<script type="text/javascript">

var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: 
	    	function(){
	    		$.save({'url':'${ctx}/role/doUpdate'}); 
	    	}
	    },
	    { line:true },
	    {id: 'closeBut', text: '关闭', icon:"close", click: 
	    	function(){ 
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

<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
	<div class="edit-form">
		<form action="" method="post" id="dataForm">
			<input type="hidden" id="roleId" name="roleId" value="${item.roleId }" />
			<input type="hidden" id="status" name="status" value="${item.status }" />
			<input type="hidden" id="roleCode" name="roleCode" value="${item.roleCode }"/>
			<table>
				<col width="10%"/>
				<col width="40%"/>
				<col width="10%"/>
				<col width="40%"/>
				<tbody>
					<tr>
						<td class="td-label"><span class="required">*</span>角色名称</td>
						<td class="td-value"><input type="text" style="width:160px;" id="roleName" name="roleName" value="${item.roleName }" maxlength="16" title="最多16个字"/></td>
						<td class="td-label" ><span class="required">*</span>角色编码</td>
						<td class="td-value">${item.roleCode }</td>
					</tr>
					<tr>
						<td class="td-label" >备注</td>
						<td class="td-value" colspan="3">
							<textarea id="remark" name="remark" rows="5" title="最多只能输入100个字">
								${item.remark }
							</textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
