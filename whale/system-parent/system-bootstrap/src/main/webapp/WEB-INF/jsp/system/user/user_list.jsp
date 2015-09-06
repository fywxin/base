<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-primary btn-ss'><i class='fa fa-check'></i> 正常</button>",
		2:"<button type='button' class='btn btn-gray btn-ss'><i class='fa fa-lock'></i> 禁用</button>"};
$(function(){
	$("#gridTable").grid({
			url :'${ctx}/user/doList?deptId=${deptId}',
			colNames: ['','操作', '用户名', '真实姓名','联系电话', '所属部门', '状态'],
			colModel: [{name:'userId',index:'userId', width:30,fixed:true, resize:false, formatter: 
		       		function(cellvalue, options, row){
							return '<input type="checkbox" value="'+row.userId+'" name="chk_col">';
		       		}
		       	},
	           {name:'opt',index:'opt', width:300, fixed:true, sortable:false, resize:false, align: "center",
			formatter: function(cellvalue, options, row){
				var strArr = [];
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.userId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
	        		
	        		if(row.status == 2){
	        			strArr.push("<button type='button' class='btn btn-success btn-ss' title='启用' onclick=\"setStatus('"+row.userId+"','"+row.userName+"',1); return false;\"><i class='fa fa-unlock'></i> 启用</button>");
	        		}else{
	        			strArr.push("<button type='button' class='btn btn-gray btn-ss' title='禁用' onclick=\"setStatus('"+row.userId+"','"+row.userName+"',2); return false;\"><i class='fa fa-lock'></i> 禁用</button>");
	        		}
	        		strArr.push("<button type='button' class='btn btn-default btn-ss' title='重置密码' onclick=\"resetPassword('"+row.userId+"'); return false;\"><i class='fa fa-undo'></i> 重置密码</button>");
	        		strArr.push("<button type='button' class='btn btn-default btn-ss' title='分配角色' onclick=\"setRole('"+row.userId+"'); return false;\"><i class='fa fa-child'></i> 分配角色</button>");
	        		return strArr.join("");
			
			}},
			{name:'userName',width:160,formatter: function(cellvalue, options, row){
				return "<a href='#' onclick='view("+row.userId+")'>"+row.userName+"</a>";
			}},
			{name:'realName', width:160},
			{name:'phone', width:160},
			{name:'deptName', width:260},
			{name:'status',index:'status', fixed:true, width:80,
				formatter: function(cellvalue, options, rowObject){
					return statusObj[cellvalue];
				}	
			}
		]
	});
});

function add(){
	$.openWin({url: "${ctx}/user/goSave?deptId=${deptId}","title":'新增用户'});
}

function update(userId){
	$.openWin({url: "${ctx}/user/goUpdate?userId="+userId,"title":'编辑用户'});
}

function view(userId){
	$.openWin({url: "${ctx}/user/goView?userId="+userId,"title":'查看用户'});
}

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
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
					<col  width="15%" />
					<col  width="35%"/>
					<col  width="15%" />
					<col  width="35%"/>
				<tbody>
						<tr>
							<td class="td-label">用户名</td>
							<td class="td-value"><input type="text" id="userName" name="userName" style="width:160px;" value="${userName }" /></td>
							<td class="td-label">姓名</td>
							<td class="td-value">
								<input type="text" id="realName" name="realName" style="width:160px;"  value="${realName }" />	
								<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>			
							</td>
						</tr>
					</tbody>
				</table>
			<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
				  <button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
			</div>
		</form>
		<table id="gridTable" ></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>