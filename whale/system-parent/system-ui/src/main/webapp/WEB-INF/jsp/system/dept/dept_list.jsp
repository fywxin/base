<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <title>机构列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
$(function(){
	grid = $("#gridTable").grid({
	    url: '${ctx}/dept/doList?pid=${pid}',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '30%',
	        align: 'center',
	        formatter: function(value, row, index){
				var strArr = [];
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.id+"')\"><i class='fa fa-pencil'></i> 添加子机构</button>");
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.id+"')\"><i class='fa fa-pencil'></i> 修改</button>");
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.id+"')\"><i class='fa fa-trash-o'></i> 删除</button>");
	        	return strArr.join("");
			}
	    }, {
	        field: 'deptName',
	        width: '20%',
	        title: '机构名称',
	        sortable: true
	    }, {
	        field: 'deptCode',
	        title: '机构编码',
	        width: '10%',
	        sortable: true
	    }, {
	        field: 'deptTel',
	        title: '机构电话',
	        width: '10%',
	        sortable: true
	    }, {
	        field: 'deptAddr',
	        title: '机构地址',
	        sortable: true
	    }, {
	        field: 'remark',
	        width: '15%',
	        title: '备注'
	    }
	   ]
	});
});

function add(){
	
}
</script>
</head>
    
<body style="overflow: hidden;">
	<ul class="nav nav-tabs" id="topTab">
    	<li class="active"><a href="${ctx}/dept/goList">机构列表</a></li>
	</ul>
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
				<tbody>
					<tr>
						<td class="td-label">机构名称</td>
						<td class="td-value"><input type="text" id="deptName" name="deptName"  value="${deptName }" /></td>
						<td class="td-label">机构编码</td>
						<td class="td-value">
							<input type="text" id="deptCode" name="deptCode" value="${deptCode }" />
							<button type="button" class="btn btn-success btn-sm" onclick="search()" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
			
		</form>
		<div id="mytoolbar">
			<button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增 </button>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
		<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>