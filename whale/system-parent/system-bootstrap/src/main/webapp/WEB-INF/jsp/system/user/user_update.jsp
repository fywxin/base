<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<%@include file="/html/jsp/ztree.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/user/doUpdate'}); 
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
			"userName": {
				validIllegalChar: true,
				maxlength: 16,
				required: true
			},
			"realName":{
				validIllegalChar: true,
				required: true,
				maxlength: 32
			},
			"phone": {
				validIllegalChar: true,
				required: true,
				isMobile: true,
				maxlength: 11
			},
			"email": {
				validIllegalChar: true,
				maxlength: 32,
				email: true
			},
			"addOn":{
				maxlength: 1024
			},
			"deptId":{
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
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<input type="hidden" id="userId" name="userId" value="${item.userId }" />
				<table >
					<col width="10%"/>
					<col width="40%"/>
					<col width="10%"/>
					<col width="40%"/>
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>用户名</td>
							<td class="td-value">${item.userName }</td>
							<td class="td-label" ><span class="required">*</span>真实姓名</td>
							<td class="td-value"><input type="text" style="width:160px;" id="realName" name="realName" value="${item.realName }" maxlength="32" title="最多32位" /></td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>联系电话</td>
							<td class="td-value"><input type="text" style="width:160px;" id="phone" name="phone" value="${item.phone }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" title="最多11个字"/></td>
							<td class="td-label" >邮箱地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="email" name="email" value="${item.email }" maxlength="32" title="最多32位"/></td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>所属部门</td>
							<td class="td-value">
								<tag:tree nodeName="deptName" nodeId="id" id="deptId" nodes="${nodes }" nodePId="pid" mulitSel="false" value="${item.deptId }"></tag:tree>
							</td>
							<td class="td-label" >附加信息</td>
							<td class="td-value">
								<input type="text" style="width:160px;" id="addOn" name="addOn" value="${item.addOn }" title="最多只能输入1024个字" />
							</td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<textarea id="remark" name="remark" rows="5" title="最多只能输入500个字">
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
