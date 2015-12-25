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
	    idField: 'dictId',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '12%',
	        align: 'center',
	        formatter: function(value, row, index){
				var strArr = [];
				<tag:auth authCode="dict:update">
				strArr.push('<a href="javascript:;" class="link" onclick=go("修改字典","${ctx}/dict/goUpdate?dictId='+row.dictId+'") >修改</a>');
				</tag:auth>
				<tag:auth authCode="dict:del">
				strArr.push('<span class="link-sep">|</span>');
	            strArr.push('<a href="javascript:;" class="link" onclick=del('+row.dictId+') >删除</a>');
	            </tag:auth>
	            return strArr.join("");
			}
	    }, {
	        field: 'dictName',
	        width: '30%',
	        title: '字典名称'
	    }, {
	        field: 'dictCode',
	        title: '字典编码',
	        width: '30%',
	        sortable: true
	    }, {
	        field: 'remark',
	        title: '备注',
	        fix: true
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
							<button type="button" class="btn btn-success btn-xs" onclick="search(1)" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
		<tag:auth authCode="dict:save">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增字典','${ctx}/dict/goSave')"><i class="fa fa-plus"></i> 新  增 </button>
		</tag:auth>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>