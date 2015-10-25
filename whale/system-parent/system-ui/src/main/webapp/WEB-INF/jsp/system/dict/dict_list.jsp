<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/dict/doList',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '30%',
	        align: 'center',
	        formatter: function(value, row, index){
				var strArr = [];
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"go('修改字典', '${ctx}/dict/goUpdate?dictId="+row.dictId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.dictId+"')\"><i class='fa fa-trash-o'></i> 删除</button>");
	        	return strArr.join("");
			}
	    }, {
	        field: 'dictName',
	        width: '20%',
	        title: '字典名称',
	        sortable: true
	    }, {
	        field: 'dictCode',
	        title: '字典编码',
	        width: '20%',
	        sortable: true
	    }, {
	        field: 'remark',
	        title: '备注'
	    }
	   ]
	});
});

function del(dictId){
	$.del({url:"${ctx}/dict/doDelete", datas:{dictId: dictId}, onSuccess: function(){
		window.parent.location.reload();
		$.alert("删除字典成功");
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
						<td class="td-label">字典名称</td>
						<td class="td-value"><input type="text" id="dictName" name="dictName" style="width:160px;" value="${dictName }" /></td>
						<td class="td-label">字典编码</td>
						<td class="td-value">
							<input type="text" id="dictCode" name="dictCode" style="width:160px;" value="${dictCode }" />
							<button type="button" class="btn btn-info btn-sm" onclick="search()" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增字典','${ctx}/dict/goSave')"><i class="fa fa-plus"></i> 新  增 </button>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>