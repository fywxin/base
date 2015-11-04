<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-success btn-ss'><i class='fa fa-check'></i> 正常</button>",
		2:"<button type='button' class='btn btn-default btn-ss'><i class='fa fa-lock'></i> 禁用</button>"};

$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/user/doList?deptId=${deptId}',
	    idField: 'userId',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '45%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	var strArr = [];
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"go('修改用户','${ctx}/user/goUpdate?userId="+row.userId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
        		
        		if(row.status == 2){
        			strArr.push("<button type='button' class='btn btn-success btn-ss' title='启用' onclick=\"setStatus('"+row.userId+"','"+row.userName+"',1); return false;\"><i class='fa fa-unlock'></i> 启用</button>");
        		}else{
        			strArr.push("<button type='button' class='btn btn-default btn-ss' title='禁用' onclick=\"setStatus('"+row.userId+"','"+row.userName+"',2); return false;\"><i class='fa fa-lock'></i> 禁用</button>");
        		}
        		strArr.push("<button type='button' class='btn btn-default btn-ss' title='重置密码' onclick=\"resetPassword('"+row.userId+"'); return false;\"><i class='fa fa-undo'></i> 重置密码</button>");
        		strArr.push("<button type='button' class='btn btn-default btn-ss' title='分配角色' onclick=\"go('分配角色','${ctx}/user/goSetUserRole?userId="+row.userId+"'); return false;\"><i class='fa fa-child'></i> 分配角色</button>");
        		return strArr.join("");
			}
	    }, {
	        field: 'userName',
	        width: '15%',
	        title: '用户名',
	        formatter: function(value, row, index){
				return "<a href='javascript:;' onclick=\"go('查看用户名','${ctx}/user/goView?userId="+row.userId+"')\">"+row.userName+"</a>";
			}
	    }, {
	        field: 'realName',
	        title: '真实姓名',
	        width: '15%',
	        sortable: true
	    }, {
	        field: 'phone',
	        title: '联系电话',
	        width: '10%',
	        sortable: true
	    }, {
	        field: 'deptName',
	        title: '所属组织',
	        width: '10%',
	        sortable: true
	    }, {
	        field: 'status',
	        title: '状态',
	        width: '5%',
	        formatter: function(value, row, index){
				return statusObj[row.status];
			}
	    }
	   ]
	});
});

function setRole(userId){
	$.openWin({url:'${ctx}/user/goSetUserRole?userId='+userId, area: ['400px', '505px'], title: "分配角色"});
}

function del(){
	$.del({url:"${ctx}/user/doDelete"});
}

function resetPassword(userId){
	$.confirm({info:'您确定要重置密码吗？', url : "${ctx}/user/doRestPassword?userId="+userId, onSuccess: function(){
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
	$.confirm({info:'您确定要[ '+opt+' ]用户[ '+name+' ]吗？', url : url});
}
</script>
</head>

<body style="overflow: hidden;">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
				<tbody>
					<tr>
							<td class="td-label">用户名</td>
							<td class="td-value"><input type="text" id="userName" name="userName" style="width:160px;" value="${userName }" /></td>
							<td class="td-label">姓名</td>
							<td class="td-value">
								<input type="text" id="realName" name="realName" style="width:160px;"  value="${realName }" />	
							<button type="button" class="btn btn-info btn-sm" onclick="search(1)" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增用户','${ctx}/user/goSave?deptId=${deptId}')"><i class="fa fa-plus"></i> 新  增 </button>
			<button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>