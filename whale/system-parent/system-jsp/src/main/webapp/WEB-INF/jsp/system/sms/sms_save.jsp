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
	    	$.save({'url':'${ctx}/sms/doSave'}); 
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
			"content": {
				validIllegalChar: true,
				required: true
			},
			"smsType": {
				required: true
			},
			"toPhones": {
				validIllegalChar: true,
				required: true
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
				<table >
					<col width="10%"/>
					<col width="40%"/>
					<col width="10%"/>
					<col width="40%"/>
					<tbody>
						<tr>
							<td class="td-label">短信内容</td>
							<td class="td-value" colspan="3">
								<input type="text" id="content" name="content" style="width:560px;" value="${item.content}"   />
							</td>
							
						</tr>
						<tr>
							<td class="td-label">短信类型</td>
							<td class="td-value">
								<input type="text" id="smsType" name="smsType" style="width:160px;" value="${item.smsType}"  onkeyup="value=value.replace(/[^\d]/g,'')" />
							</td>
							<td class="td-label">接收号码</td>
							<td class="td-value">
								<input type="text" id="toPhones" name="toPhones" style="width:160px;" value="${item.toPhones}"   />
							</td>
							
						</tr>
						<tr>
							<td class="td-label">定时发送时间</td>
							<td class="td-value">
								<input type="text" id="sendTime" name="sendTime" style="width:160px;" value="${item.sendTime}"   />
							</td>
							<td class="td-label">自定义扩展号</td>
							<td class="td-value">
								<input type="text" id="encode" name="encode" style="width:160px;" value="${item.encode}"   />
							</td>
							
						</tr>
						<tr>
							<td class="td-label">内容超70忽略</td>
							<td class="td-value">
								<select style="width: 160px" id="overLengthIgnore" name="overLengthIgnore" >
									<option value="true">忽略</option>
									<option value="false">分条发送</option>
								</select>
							</td>
							
						</tr>
						
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
