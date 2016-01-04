<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
function getGridDivHeight(){
	return $.h()-$("#panelDiv1").height()-100;
}
$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/auth/doList?roleId=${item.roleId}',
	    idField: 'authCode',
	    pageSize: 20,
	    height: $.h()-$("#panelDiv1").height()-120,
	    columns: [
	              {
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

<body>
	<div class="row panel-row" id="panelDiv1" >
		<div class="panel panel-default">
			<div class="panel-heading collapsed panel-heading-style" href="#panel1" data-toggle="collapse" aria-expanded="false">
				用户详情<span class="caret pull-right toggle-caret" ></span></div>
			<div id="panel1" class="panel-collapse collapse in" aria-expanded="true" >
				<div class="panel-body">
					<div class="table-responsive">
						<table class="panel-body-table table table-bordered " >
							<colgroup>
								<col class="col-xs-1">
								<col class="col-xs-5">
								<col class="col-xs-1">
								<col class="col-xs-5">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row" style="text-align: right;">角色名称</th>
									<td>${item.roleName }</td>
									<th scope="row" style="text-align: right;">角色编码</th>
									<td>${item.roleCode }</td>
								</tr>
								<tr>
									<th scope="row" style="text-align: right;">可删除</th>
									<td><c:if test="${!item.canDelFlag}">否</c:if> 
										<c:if test="${item.canDelFlag}"><span class="bg-success">是</span></c:if></td>
									<th scope="row" style="text-align: right;">状态</th>
									<td><c:if test="${item.status == 2}"><span class="bg-danger">禁用 </span></c:if> 
										<c:if test="${item.status == 1}"><span class="bg-success">正常</span></c:if></td>
								</tr>
								<tr>
									<th scope="row">备注</th>
									<td colspan="3">
										${item.remark }</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row panel-row">
		<div class="panel panel-default">
			<div class="panel-heading collapsed panel-heading-style" href="#panel2" data-toggle="collapse" aria-expanded="false">
				角色 [${item.roleName }] 的权限<span class="caret pull-right toggle-caret" ></span></div>
			<div id="panel2" class="panel-collapse collapse in" aria-expanded="false">
				<div class="panel-body">
					<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
						<table id="gridTable" ></table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>