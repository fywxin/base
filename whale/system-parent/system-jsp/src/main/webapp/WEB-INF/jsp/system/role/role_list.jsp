<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>角色列表</title>
	
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/role/doList',
    	uid : "roleId",
    	toolbar: {items: [
           		{text: '新增角色', icon: 'add', click: 
           			function(){
           				$.openWin({url: "${ctx}/role/goSave","title":'新增角色'});
           			} 
				}
            ]
        },
        columns: [
      	        {display: '操作', name: 'opt', minWidth: 220,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.roleId+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='del(\""+row.roleId+"\");'>删除</a>");
      	        		
      	        		if(row.status == 2){
      	        			strArr.push("<a href='#' class='r15' onclick='setStatus(\""+row.roleId+"\",\""+row.roleName+"\",1); return false;'>启用</a>");
      	        		}else{
      	        			strArr.push("<a href='#' class='r15' onclick='setStatus(\""+row.roleId+"\",\""+row.roleName+"\",2); return false;'>禁用</a>");
      	        		}
      	        		
      	        		strArr.push("<a href='#' onclick='setAuth(\""+row.roleId+"\"); return false;'>设置权限</a>");
      	        	    return strArr.join("");
  	        	}},
      	        {display: '角色名称', name: 'roleName', minWidth: 140 , isSort: true},
      	        {display: '角色编码', name: 'roleCode' },
      	        {display: '备注', name: 'remark' },
      	        {display: '状态', name: 'status',
      	        	render: function (row){
      	        	    return statusObj[row.status];
      	        	}
      	        }
              ]
	});
});

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
    
<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
			<table >
					<col  width="10%" />
					<col  width="40%"/>
					<col  width="10%"/>
					<col  width="40%"/>
				<tbody>
					<tr>
						<td class="td-label">角色名称</td>
						<td class="td-value"><input type="text" id="roleName" name="roleName" style="width:160px;" value="${roleName }" /></td>
						<td class="td-label">角色编码</td>
						<td class="td-value"><input type="text" id="roleCode" name="roleCode" style="width:160px;"  value="${roleCode }" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<div id="grid" style="margin: 0px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>