<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户列表</title>
	
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/user/doList?deptId=${deptId}',
    	uid : "userId",
    	toolbar: {items: [
           		{text: '新增用户', icon: 'add', click: 
           			function(){
           				$.openWin({url: "${ctx}/user/goSave?deptId=${deptId}","title":'新增用户'});
           			} 
				}
			]
        },
        columns: [
      	        {display: '操作', name: 'opt', width: 290,frozen: true,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.userId+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='del(\""+row.userId+"\");'>删除</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='view(\""+row.userId+"\");'>查看</a>");
      	        		
      	        		if(row.status == 2){
      	        			strArr.push("<a href='#' class='r15' onclick='setStatus(\""+row.userId+"\",\""+row.userName+"\",1); return false;'>启用</a>");
      	        		}else{
      	        			strArr.push("<a href='#' class='r15' onclick='setStatus(\""+row.userId+"\",\""+row.userName+"\",2); return false;'>禁用</a>");
      	        		}
      	        		
						strArr.push("<a href='#' class='r15' onclick='resetPassword(\""+row.userId+"\");'>重置密码</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='setRole(\""+row.userId+"\");'>分配角色</a>");
      	        		
      	        	    return strArr.join("");
  	        	}},
      	        {display: '用户名', name: 'userName', minWidth: 140,frozen: true },
      	        {display: '真实姓名', name: 'realName', width:140 },
      	        {display: '联系电话', name: 'phone', width:130 },
	      	    {display: '邮箱', name: 'email', width:130 },
	      	    {display: '所属部门', name: 'deptName', width:220 },
      	        {display: '状态', name: 'status', width:80,
      	        	render: function (row){
      	        	    return statusObj[row.status];
      	        	}
      	        }
              ]
	});
});

function update(userId){
	$.openWin({url: "${ctx}/user/goUpdate?userId="+userId,"title":'编辑用户'});
}

function view(userId){
	$.openWin({url: "${ctx}/user/goView?userId="+userId,"title":'查看用户'});
}

function setRole(userId){
	$.openWin({url:'${ctx}/user/goSetUserRole?userId='+userId, width:450, height:520,title: "分配角色"});
}

function del(userId){
	$.del({url:"${ctx}/user/doDelete", datas:{userId: userId}});
}

function resetPassword(userId){
	$.sure({info:'您确定要重置密码吗？', url : "${ctx}/user/doRestPassword?userId="+userId, onSuccess: function(){
		$.alert("重置密码 [<font color=red>111111</font>] 成功");
	}});
}

function setStatus(id,name,type){
	var url = "${ctx}/user/doChangeState?status=1&userId="+id;
	var opt = "启用";
	if(type == 2){
		url = "${ctx}/user/doChangeState?status=2&userId="+id;
		opt = "禁用";
	}
	$.sure({info:'您确定要[ '+opt+' ]用户[ '+name+' ]吗？', url : url});
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
							<td class="td-label">用户名</td>
							<td class="td-value"><input type="text" id="userName" name="userName" style="width:160px;" value="${userName }" /></td>
							<td class="td-label">姓名</td>
							<td class="td-value">
								<input type="text" id="realName" name="realName" style="width:160px;"  value="${realName }" />	
								<button id="queryBut" type="button" class="btn-query">查询</button>				
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>

	<div id="grid" style="margin: 0px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>

