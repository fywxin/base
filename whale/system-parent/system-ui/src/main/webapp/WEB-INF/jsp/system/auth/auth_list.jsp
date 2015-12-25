<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>权限列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">

$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/auth/doList?menuId=${menuId}',
	    idField: 'authCode',
	    pageSize: 20,
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '8%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	var strArr = [];
	        	<tag:auth authCode="auth:update">
	        	strArr.push('<a href="javascript:;" class="link" onclick=go("修改权限","${ctx}/auth/goUpdate?authCode='+row.authCode+'") >修改</a>');
	        	</tag:auth> 
	        	return strArr.join("");
			}
	    }, {
	        field: 'authName',
	        width: '30%',
	        title: '权限名称',
	        sortable: true
	    }, {
	        field: 'authCode',
	        title: '权限编码',
	        sortable: true
	    }, {
	        field: 'menuName',
	        title: '所属菜单',
	        width: '30%'
	    }
	   ]
	});
});
</script>
</head>
   
<body style="overflow: hidden;">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
				<tbody>
					<tr>
						<td class="td-label">权限名称</td>
						<td class="td-value"><input type="text" id="authName" name="authName" style="width:160px;" value="${authName }" /></td>
				
						<td class="td-label">权限编码</td>
						<td class="td-value">
							<input type="text" id="authCode" name="authCode" style="width:160px;" value="${authCode }" />
							<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;margin-top: 10px;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>