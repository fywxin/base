<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>角色列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-success btn-ss'><i class='fa fa-check'></i> 正常</button>",
				2:"<button type='button' class='btn btn-default btn-ss'><i class='fa fa-lock'></i> 禁用</button>"};

$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/role/doList',
	    idField: 'roleId',
	    columns: [
		  {
	        field: 'opt',
	        title: '操作',
	        width: '18%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	var strArr = [];
	            strArr.push('<a href="javascript:;" class="link" onclick=go("修改角色","${ctx}/role/goUpdate?roleId='+row.roleId+'") >修改</a>');
	            strArr.push('<span class="link-sep">|</span>');
	            if(row.status == 2){
	              strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.roleId+',"'+row.roleName+'",1) >启用</a>');
	            }else{
	              strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.roleId+',"'+row.roleName+'",2) >禁用</a>');
	            }
	            strArr.push('<span class="link-sep">|</span>');
	            strArr.push('<a href="javascript:;" class="link" onclick=go("设置权限","${ctx}/role/goSetRoleAuth?roleId='+row.roleId+'") >设置权限</a>');
        		return strArr.join("");
			    }
	    }, {
	        field: 'roleName',
	        width: '20%',
	        title: '角色名称',
	        sortable: true
	    }, {
	        field: 'roleCode',
	        title: '角色编码',
	        width: '20%',
	        sortable: true
	    }, {
	        field: 'remark',
	        title: '备注'
	    }, {
	        field: 'status',
	        title: '状态',
	        width: '5%',
	        formatter: function(value, row, index){
				    return statusObj[value];
			    }
	    }
	   ]
	});
});

function del(){
	$.del({url:"${ctx}/role/doDelete"});
}

function setStatus(id,name,type){
	var url = "${ctx}/role/doChangeState?status=1&roleId="+id;
	var opt = "启用";
	if(type == 2){
		url = "${ctx}/role/doChangeState?status=2&roleId="+id;
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
						<td class="td-label">角色名称</td>
						<td class="td-value"><input type="text" id="roleName" name="roleName" style="width:160px;" value="${roleName }" /></td>
						<td class="td-label">角色编码</td>
						<td class="td-value">
							<input type="text" id="roleCode" name="roleCode" style="width:160px;"  value="${roleCode }" />
							<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增角色', '${ctx}/role/goSave')"><i class="fa fa-plus"></i> 新  增</button>
			<button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>
