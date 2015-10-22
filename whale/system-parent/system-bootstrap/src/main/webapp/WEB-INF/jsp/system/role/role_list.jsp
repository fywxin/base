<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>角色列表</title>
<%@include file="/jsp/bgrid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-primary btn-ss'><i class='fa fa-check'></i> 正常</button>",
				2:"<button type='button' class='btn btn-gray btn-ss'><i class='fa fa-lock'></i> 禁用</button>"};
$(function(){
	$("#gridTable").bootstrapTable({
	    url: '${ctx}/role/doList',
	    idField: "roleId",
	    search: true,
	    silentSort: false,
	    pagination: true,
	    showColumns: true,
	    showRefresh: true,
	    detailView: true,
	    singleSelect: false,
	    resizable: true,
	    striped: true,
	    paginationHAlign:'left',
	    paginationDetailHAlign: 'right',
	    toolbar: "#mytoolbar",
	    onlyInfoPagination : false,
	    paginationFirstText : "首页",
	    paginationPreText: "上一页",
	    paginationNextText: "下一页",
	    paginationLastText: "尾页",
	    onEditableSave: function(field, row, oldValue, e){
	    	alert(field);
	    	alert(JSON.stringify(row));
	    	alert(oldValue);
	    	alert(e);
	    },
	    columns: [
		{
		    field: 'roleId',
		    checkbox: true,
		    align: 'center'
		}, {
	        field: 'opt',
	        title: '操作',
	        align: 'center',
	        formatter: function(value, row, index){
				var strArr = [];
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.roleId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
	        		
	        		if(row.status == 2){
	        			strArr.push("<button type='button' class='btn btn-success btn-ss' title='启用' onclick=\"setStatus('"+row.roleId+"','"+row.roleName+"',1); return false;\"><i class='fa fa-unlock'></i> 启用</button>");
	        		}else{
	        			strArr.push("<button type='button' class='btn btn-gray btn-ss' title='禁用' onclick=\"setStatus('"+row.roleId+"','"+row.roleName+"',2); return false;\"><i class='fa fa-lock'></i> 禁用</button>");
	        		}
	        		strArr.push("<button type='button' class='btn btn-default btn-ss' title='设置权限' onclick=\"setAuth('"+row.roleId+"'); return false;\"><i class='fa fa-group'></i> 设置权限</button>");
	        		return strArr.join("");
			}
	    }, {
	        field: 'roleName',
	        title: '角色名称',
	        sortable: true
	    }, {
	        field: 'roleCode',
	        title: '角色编码',
	        sortable: true,
	        editable: true
	    }, {
	        field: 'remark',
	        title: '备注'
	    }, {
	        field: 'status',
	        title: '状态',
	        formatter: function(value, row, index){
				return statusObj[value];
			}
	    }
	   ]
	});
});


function add(){
	$.openWin({content: "${ctx}/role/goSave",title:'新增角色'});
}

function update(roleId){
	$.openWin({content: "${ctx}/role/goUpdate?roleId="+roleId,title:'修改角色'});
}


function del(){
	$.del({url:"${ctx}/role/doDelete"});
}

function setAuth(roleId){
	$.openWin({content:'${ctx}/role/goSetRoleAuth?roleId='+roleId, area: ['400px', '510px'],title: "分配权限"});
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
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
					<col  width="10%" />
					<col  width="40%"/>
					<col  width="10%"/>
					<col  width="40%"/>
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
		<div id="mytoolbar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
				  <button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
			</div>
		<table id="gridTable" ></table>
	</div>
</body>
</html>