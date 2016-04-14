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
	    url: '${ctx}/auth/doList?userId=${item.userId}',
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
									<th scope="row" style="text-align: right;">用户名</th>
									<td>${item.userName }</td>
									<th scope="row" style="text-align: right;">真实姓名</th>
									<td>${item.realName }</td>
								</tr>
								<tr>
									<th scope="row" style="text-align: right;">联系电话</th>
									<td>${item.phone }</td>
									<th scope="row" style="text-align: right;">邮箱地址</th>
									<td>${item.email }</td>
								</tr>
								<tr>
									<th scope="row">所属部门</th>
									<td>${deptName }</td>
									<th scope="row">状态</th>
									<td>
										<c:if test="${item.status == 2}"><span class="bg-danger">禁用 </span></c:if> 
										<c:if test="${item.status == 1}">正常</c:if>
									</td>
								</tr>
								<tr>
									<th scope="row">登入IP</th>
									<td>${item.loginIp }</td>
									<th scope="row">登入时间</th>
									<td>${lastLoginTime }</td>
								</tr>
								<tr>
									<th scope="row">创建人</th>
									<td>${creator }</td>
									<th scope="row">创建时间</th>
									<td>${createTime }</td>
								</tr>
								<tr>
									<th scope="row">附加信息</th>
									<td colspan="3">${item.remark }</td>

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
				分配用户 [${item.userName }] 的权限<span class="caret pull-right toggle-caret" ></span></div>
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