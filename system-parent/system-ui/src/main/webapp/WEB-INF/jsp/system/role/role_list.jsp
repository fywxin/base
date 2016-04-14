<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>角色列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-success btn-ss' style='cursor:default'><i class='fa fa-check'></i> 正常</button>",
				2:"<button type='button' class='btn btn-default btn-ss' style='cursor:default'><i class='fa fa-lock'></i> 禁用</button>"};

$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/role/doList',
	    idField: 'roleId',
	    columns: [
		  {
	        field: 'opt',
	        title: '操作',
	        width: '25%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	var strArr = [];
	        	<tag:auth authCode="role:update">
	            strArr.push('<a href="javascript:;" class="link" onclick=go("修改角色","${ctx}/role/goUpdate?roleId='+row.roleId+'") >修改</a>');
	            </tag:auth>
	            <tag:auth authCode="role:status">
	            strArr.push('<span class="link-sep">|</span>');
	            if(row.status == 2){
	              strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.roleId+',"'+row.roleName+'",1) >启用</a>');
	            }else{
	              strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.roleId+',"'+row.roleName+'",2) >禁用</a>');
	            }
	            </tag:auth>
	            <tag:auth authCode="role:auth">
	            strArr.push('<span class="link-sep">|</span>');
	            strArr.push('<a href="javascript:;" class="link" onclick=go("设置权限","${ctx}/role/goSetRoleAuth?roleId='+row.roleId+'") >设置权限</a>');
	            </tag:auth>
	            <tag:auth authCode="role:del">
	            if(row.canDelFlag){
	            	strArr.push('<span class="link-sep">|</span>')
					strArr.push("<a href='javascript:;' class='link' onclick=\"del('"+row.roleId+"')\">删除</a>");
	            }
	            </tag:auth>
	            return strArr.join("");
			    }
	    }, {
	        field: 'roleName',
	        width: '20%',
	        title: '角色名称',
	        sortable: true,
	        formatter: function(value, row, index){
			    return "<a href='javascript:;' onclick=\"go('查看角色','${ctx}/role/goView?roleId="+row.roleId+"')\">"+row.roleName+"</a>";
		}
	    }, {
	        field: 'roleCode',
	        title: '角色编码',
	        width: '20%',
	        sortable: true
	    },{
	        field: 'remark',
	        title: '备注',
	        fix: true
	    }, {
	        field: 'canDelFlag',
	        title: '可删除',
	        width: '80px',
	        align: 'center',
	        formatter: function(value, row, index){
	        	return value ? '<span class="bg-success">是</span>' : "否";
	        }
	    }, {
	        field: 'status',
	        title: '状态',
	        width: '5%',
	        align: 'center',
	        formatter: function(value, row, index){
				    return statusObj[value];
			    }
	    }
	   ]
	});
	
});

function del(id){
	$.del({url:"${ctx}/role/doDelete?id="+id});
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

function sw(w){
	alert($(w).attr("checked"));
	alert($(w).val());
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
		<tag:auth authCode="role:save">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增角色', '${ctx}/role/goSave')"><i class="fa fa-plus"></i> 新  增</button>
		</tag:auth>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>
