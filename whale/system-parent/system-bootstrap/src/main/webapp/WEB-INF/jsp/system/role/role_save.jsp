<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">

var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/role/doSave'}); 
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
	<form action="" method="post" id="dataForm">
		<table>
			<col width="10%"/>
			<col width="40%"/>
			<col width="10%"/>
			<col width="40%"/>
			<tbody>
				<tr>
					<td class="td-label"><span class="required">*</span>角色名称</td>
					<td class="td-value"><input type="text" style="width:160px;" id="roleName" name="roleName" maxlength="16" title="最多16个字"/></td>
					<td class="td-label" ><span class="required">*</span>角色编码</td>
					<td class="td-value"><input type="text" style="width:160px;" id="roleCode" name="roleCode" maxlength="64" title="最多64位"/></td>
				</tr>
				<tr>
					<td class="td-label" >备注</td>
					<td class="td-value" colspan="3">
						<textarea id="remark" name="remark" rows="5" title="最多只能输入100个字"></textarea>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>
