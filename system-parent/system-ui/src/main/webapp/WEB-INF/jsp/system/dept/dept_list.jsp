<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <title>组织列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/dept/doList?pid=${pid}',
	    idField: 'id',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '18%',
	        align: 'center',
	        formatter: function(value, row, index){
				var strArr = [];
				<tag:auth authCode="dept:save">
				strArr.push("<a href='javascript:;' class='link' onclick=\"go('添加子组织', '${ctx}/dept/goSave?pid="+row.id+"')\">添加子组织</a>");
				</tag:auth>
				<tag:auth authCode="dept:update">
				strArr.push('<span class="link-sep">|</span>');
				strArr.push("<a href='javascript:;' class='link' onclick=\"go('修改组织', '${ctx}/dept/goUpdate?id="+row.id+"')\">修改</a>");
				
				</tag:auth>
				<tag:auth authCode="dept:del">
				strArr.push('<span class="link-sep">|</span>');
				strArr.push("<a href='javascript:;' class='link' onclick=\"del('"+row.id+"')\">删除</a>");
				</tag:auth>
				return strArr.join("");
			}
	    }, {
	        field: 'deptName',
	        width: '15%',
	        title: '组织名称',
	        sortable: true
	    }, {

	        field: 'deptCode',
	        title: '组织编码',
	        width: '15%',
	        sortable: true
	    }, {
			field: 'parentName',
			width: '15%',
			title: '父组织',
			sortable: true
		}, {
	        field: 'deptTel',
	        title: '组织电话',
	        width: '10%'
	    }, {
	        field: 'deptAddr',
	        title: '组织地址',
	        width: '15%'
	    }, {
	        field: 'remark',
	        title: '备注'
	    }
	   ]
	});
});

function del(id){
	$.del({url:"${ctx}/dept/doDelete", datas:{id: id}, onSuccess: function(){
		window.parent.location.reload();
		$.msg("删除部门成功");
	}});
}
</script>
</head>
    
<body style="overflow: hidden;">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
				<tbody>
					<tr>
						<td class="td-label">组织名称</td>
						<td class="td-value"><input type="text" id="deptName" name="deptName"  value="${deptName }" /></td>
						<td class="td-label">组织编码</td>
						<td class="td-value">
							<input type="text" id="deptCode" name="deptCode" value="${deptCode }" />
							<button type="button" class="btn btn-success btn-xs" onclick="search(1)" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
		<tag:auth authCode="dept:save">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增组织','${ctx}/dept/goSave')"><i class="fa fa-plus"></i> 新  增 </button>
		</tag:auth>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>