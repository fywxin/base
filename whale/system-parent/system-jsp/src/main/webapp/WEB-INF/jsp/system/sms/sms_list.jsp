<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>短信列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var smsStatusObj= {1:"待发送", 2:"发送成功",3:"<span class='sred'>发送失败</span>",4:"忽略"};
var time = new Date();
$(function(){
	$.grid({
    	url :'${ctx}/sms/doList',
    	uid : "roleId",
    	toolbar: {items: [
           		{text: '新增短信', icon: 'add', click: 
           			function(){
           			$.openWin({url: "${ctx}/sms/goSave","title":'新增短信'});
           			} 
				}
            ]
        },
        columns: [
				{display: '操作', name: 'opt', width: 70, frozen: true,render: function (row){
					return "<a href='#' onclick='view(\""+row.id+"\");'>查看</a>"
				} },
      	        {display: '短信内容', name: 'content', minWidth: 440, align: 'left'},
      	        {display: '短信类型', name: 'smsType' },
      	        {display: '接收号码', name: 'toPhones', width: 120},
      	      	{display: '返回信息', name: 'resMsg' },
      	    	{display: '返回时间', name: 'recTime',width: 140,
      	        	render: function (row){
          	        	   time.setTime(row.recTime);
          				return time.Format("yyyy-MM-dd hh:mm:ss.S");
          	        	} },
      	  		{display: '重发次数', name: 'retryTime'},
      	  		{display: '返回状态', name: 'resStatus'},
      	        
      	      	{display: '创建时间', name: 'createTime',width: 140,
    	        	render: function (row){
        	        	   time.setTime(row.createTime);
        				return time.Format("yyyy-MM-dd hh:mm:ss.S");
        	        	} },
     	       	{display: '状态', name: 'status',width: 80,
           	        	render: function (row){
           	        	    return smsStatusObj[row.status];
           	        	}
           	        }
              ]
	});
});

function view(id){
	$.openWin({url: "${ctx}/sms/goView?id="+id,"title":'查看短信'});
}

</script>
</head>
    
<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
				<table>
						<col  width="10%" />
						<col  width="40%"/>
						<col  width="10%"/>
						<col  width="40%"/>
					<tbody>
						<tr>
							<td class="td-label">短信内容</td>
							<td class="td-value">
								<input type="text" id="content" name="content" style="width:160px;" value="${item.content}" />
							</td>
							<td class="td-label">短信类型</td>
							<td class="td-value">
								<input type="text" id="smsType" name="smsType" style="width:160px;" value="${item.smsType}" />
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	<div id="grid" style="margin: 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>