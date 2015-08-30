<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>角色列表</title>
	<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-primary btn-ss'><i class='fa fa-check'></i> 正常</button>",
				2:"<button type='button' class='btn btn-gray btn-ss'><i class='fa fa-lock'></i> 禁用</button>"};
$(function(){
	$("#gridTable").grid({
    	url :'${ctx}/role/doList',
    	colNames: ['操作', '角色名称', '角色编码', '备注', '状态'],
       	colModel: [{name:'id',index:'', width:280, fixed:true, sortable:false, resize:false, align: "center",
					formatter: function(cellvalue, options, row){
						var strArr = [];
						strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.roleId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
      	        		
      	        		if(row.status == 2){
      	        			strArr.push("<button type='button' class='btn btn-success btn-ss' title='启用' onclick=\"setStatus(\""+row.roleId+"\",\""+row.roleName+"\",1); return false;\"><i class='fa fa-unlock'></i> 启用</button>");
      	        		}else{
      	        			strArr.push("<button type='button' class='btn btn-gray btn-ss' title='禁用' onclick=\"setStatus(\""+row.roleId+"\",\""+row.roleName+"\",2); return false;\"><i class='fa fa-lock'></i> 禁用</button>");
      	        		}
      	        		strArr.push("<button type='button' class='btn btn-default btn-ss' title='设置权限' onclick=\"setAuth(\""+row.roleId+"\"); return false;\"><i class='fa fa-group'></i> 设置权限</button>");
      	        		strArr.push("<button type='button' class='btn btn-danger btn-ss' title='删除' onclick=\"del('"+row.roleId+"')\"><i class='fa fa-trash-o'></i> 删除</button>");    
      	        		return strArr.join("");
					
					}},
					{name:'roleName',index:'roleName', width:160, sortable:false},
					{name:'roleCode',index:'roleCode', width:160, sortable:false},
					{name:'remark',index:'remark', width:260, sortable:false},
					{name:'status',index:'status', width:60,
						formatter: function(cellvalue, options, rowObject){
							return statusObj[cellvalue];
						}	
					}
			]
	});
});


function add(){
	$.openWin({content: "${ctx}/role/goSave",title:'新增角色', area:['40%', '50%']});
}

function update(roleId){
	$.openWin({url: "${ctx}/role/goUpdate?roleId="+roleId,"title":'修改角色'});
}


function del(roleId){
	$.del({url:"${ctx}/role/doDelete", datas:{ids: roleId}});
}

function setAuth(roleId){
	$.openWin({url:'${ctx}/role/goSetRoleAuth?roleId='+roleId, width:450, height:520,title: "分配权限"});
}

function setStatus(id,name,type){
	var url = "${ctx}/role/doChangeState?status=1&roleId="+id;
	var opt = "启用";
	if(type == 2){
		url = "${ctx}/role/doChangeState?status=2&roleId="+id;
		opt = "禁用";
	}
	$.sure({info:'您确定要[ '+opt+' ]用户[ '+name+' ]吗？', url : url});
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
			<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
			</div>
		</form>
		<table id="gridTable" ></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>