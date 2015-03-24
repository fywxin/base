<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
	    	$.closeWin();
	    	return false; 
	    	}
	    }
	 ]
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
							<td class="td-label"><span class="required">*</span>用户名</td>
							<td class="td-value">${item.userName }</td>
							<td class="td-label" ><span class="required">*</span>真实姓名</td>
							<td class="td-value">${item.realName }</td>
						</tr>
						<tr>
							<td class="td-label">联系电话</td>
							<td class="td-value">${item.phone }</td>
							<td class="td-label" >邮箱地址</td>
							<td class="td-value">${item.email }</td>
						</tr>
						<tr>
							<td class="td-label">所属部门</td>
							<td class="td-value">${deptName }</td>
							<td class="td-label">状态</td>
							<td class="td-value">
								<c:if test="${item.status == 2}"><font color="red">禁用 </font></c:if> 
								<c:if test="${item.status == 1}">正常</c:if> 
							</td>
						</tr>
						<tr>
							<td class="td-label">登入时间</td>
							<td class="td-value"><fmt:formatDate value="${item.lastLoginTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="td-label">登入IP</td>
							<td class="td-value">${item.loginIp }</td>
						</tr>
						<tr>
							<td class="td-label">创建时间</td>
							<td class="td-value"><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="td-label">创建人</td>
							<td class="td-value">${creator }</td>
						</tr>
						<tr>
							<td class="td-label" >附加信息</td>
							<td class="td-value" colspan="3">
								<div style="height:40px;" class="textAreaDiv">${item.addOn }</div>
							</td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<div style="height:40px;" class="textAreaDiv">${item.remark }</div>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
