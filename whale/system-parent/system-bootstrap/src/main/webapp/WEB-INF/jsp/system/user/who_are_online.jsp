<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var allDatas = null;
$(function (){
	$.ajax({
		url : "${ctx}/doWhoAreOnline",
		data : null,
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		dataType: 'json',
		type: 'post',
		cache: false,
		error: function(e){
	        $.alert('出现异常'+e);
	    },
	    success: function(obj){
	    	window.allDatas=eval(obj);
	    	f_showCustomers(); 
		}
	});
});

//显示顾客
function f_showCustomers(){
    g = $("#maingrid").ligerGrid({
        columns: [
		{ display: '用户Id', name: 'userId' },
        { display: '用户名', name: 'userName' },
        { display: '真实姓名', name: 'realName' }
        ], 
		frozen:false,
		data: allDatas.user,
        showTitle: false,
        height: $.h()-4,
        rownumbers: true,
        detail: { onShowDetail: f_showOrder }
    });
}
function f_getOrdersData(userName)
{
    var data = { datas: [] };
    data.datas = allDatas.login[userName];
    data.total = allDatas.login[userName].length;
    return data;
}
//显示顾客订单
function f_showOrder(row, detailPanel,callback)
{
    var grid = document.createElement('div'); 
    $(detailPanel).append(grid);
    $(grid).css('margin',8).ligerGrid({
        columns:
                    [
                    { display: '登录Ip', name: 'ip', minWidth: 200 },
                    { display: '登录时间', name: 'loginTime', minWidth: 200},
                    { display: 'sessionId', name: 'sessionId', minWidth: 300 },
                    { display: '退出', name: 'opt', minWidth: 200,render: function(row){
                    	return "<a href='#' onclick='loginOut(\""+row.sessionId+"\");'>退出</a>";
                    } }], 
                    showToggleColBtn: false,
        			data: f_getOrdersData(row.userName) , 
        			showTitle: false, 
        			onAfterShowData: callback,
        			frozen:false,
        			rownumbers: true,
        			height: 240
    });  
}



function loginOut(sessionId){
	$.confirm({
		 info:'您确定要将该用户强制退出吗？',
		 ok: function () {
	        $.ajax({
				url : "${ctx}/doForceLoginOut?sessionId="+sessionId,
				data : null,
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				dataType: 'json',
				type: 'post',
				cache: false,
				error: function(){
			        $.alert('用户强制退出出现异常');
			    },
			    success: function(obj){
			    	$.alert(obj.msg);
			    	if(obj.rs){
			    		grid.reload();
			    	}
				}
			});
		}
	});
}

</script>
</head>
    
<body style="padding:1px;overflow: hidden;">
    <div id="maingrid"></div>
	<br/> 
 	<div style="display:none;"></div>
</body>
</html>
