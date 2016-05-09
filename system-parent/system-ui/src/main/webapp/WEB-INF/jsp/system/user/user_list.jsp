<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-success btn-ss' style='cursor:default'><i class='fa fa-check'></i> 正常</button>",
		2:"<button type='button' class='btn btn-default btn-ss' style='cursor:default'><i class='fa fa-lock'></i> 禁用</button>"};

$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/user/doList?deptId=${deptId}',
	    idField: 'userId',
	    columns: [
          {
           	field: 'chk',
           	checkbox: true,
           	width: '4%',
    	    align: 'center'
          },
		  {
	        field: 'opt',
	        title: '操作',
	        width: '28%',
	        align: 'center',
	        formatter: function(value, row, index){
	            var strArr = [];
	        	<tag:auth authCode="user:update">
	            strArr.push('<a href="javascript:;" class="link" onclick=go("修改用户","${ctx}/user/goUpdate?userId='+row.userId+'") >修改</a>');
	            </tag:auth>
	            if(!row.adminFlag){
	            	<tag:auth authCode="user:status">
	            	strArr.push('<span class="link-sep">|</span>');
	                if(row.status == 2){
	                  strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.userId+',"'+row.userName+'",1) >启用</a>');
	                }else{
	                  strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.userId+',"'+row.userName+'",2) >禁用</a>');
	                }
	                </tag:auth>
	                <tag:auth authCode="user:restPassword">
	                strArr.push('<span class="link-sep">|</span>');
	                strArr.push('<a href="javascript:;" class="link" onclick=resetPassword('+row.userId+') >重置密码</a>');
	                </tag:auth>
	          		<tag:auth authCode="user:role">
	                strArr.push('<span class="link-sep">|</span>');
	                strArr.push('<a href="javascript:;" class="link" onclick=go("分配角色","${ctx}/user/goSetUserRole?userId='+row.userId+'") >分配角色</a>');
	                </tag:auth>
					<tag:auth authCode="" authAdmin="true">
					strArr.push('<span class="link-sep">|</span>');
					strArr.push('<a href="javascript:;" class="link" onclick=mock('+row.userId+') > 模拟</a>');
					</tag:auth>
	            }

        		return strArr.join("");
			}
	    }, {
	        field: 'userName',
	        title: '用户名',
	        sortable: true,
	        formatter: function(value, row, index){
				    return "<a href='javascript:;' onclick=\"go('用户查看','${ctx}/user/goView?userId="+row.userId+"')\">"+row.userName+"</a>";
			}
	    }, {
	        field: 'realName',
	        title: '真实姓名',
	        width: '14%',
	        sortable: true
	    }, {
	        field: 'phone',
	        title: '联系电话',
	        width: '15%'
	    }, {
	        field: 'deptName',
	        title: '所属组织',
	        width: '16%',
	        sortable: true
	    }, {
	        field: 'status',
	        title: '状态',
	        width: '7%',
	        align: 'center',
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
	function mock(userId){
	$.get('${ctx}/mockLogin?userId='+userId, function(obj){
		if(obj.rs){
			window.top.location.reload();
		}else{
			$.alert(obj.msg);
		}
	});
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
							<button type="button" class="btn btn-success btn-xs" onclick="search(1)" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
		<tag:auth authCode="user:save">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增用户','${ctx}/user/goSave?deptId=${deptId}')"><i class="fa fa-plus"></i> 新  增 </button>
		</tag:auth>
		<tag:auth authCode="user:del">
			<button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
		</tag:auth>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>
