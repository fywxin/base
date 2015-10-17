<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
$(function(){
	$("#gridTable").grid({
			url :'${ctx}/dict/doList',
			colNames: ['操作', '字典名称', '字典编码','备注'],
			colModel: [
	           {name:'opt',index:'opt', width:200, fixed:true, sortable:false, resize:false, align: "center",
				formatter: function(cellvalue, options, row){
					var strArr = [];
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.dictId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.dictId+"')\"><i class='fa fa-trash-o'></i> 删除</button>");
		        	return strArr.join("");
				
				}},
			{name:'dictName',width:160},
			{name:'dictCode', width:160},
			{name:'remark', width:160}
		]
	});
});


function add(){
	$.openWin({url: "${ctx}/dict/goSave","title":'新增字典'});
}

function update(dictId){
	$.openWin({url: "${ctx}/dict/goUpdate?dictId="+dictId,"title":'编辑字典'});
}

function del(dictId){
	$.del({url:"${ctx}/dict/doDelete", datas:{dictId: dictId}, onSuccess: function(){
		window.parent.location.reload();
		$.alert("删除字典成功");
	}});
}
</script>
</head>
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
					<col  width="15%" />
					<col  width="35%"/>
					<col  width="15%" />
					<col  width="35%"/>
				<tbody>
						<tr>
							<td class="td-label">字典名称</td>
							<td class="td-value"><input type="text" id="dictName" name="dictName" style="width:160px;" value="${dictName }" /></td>
							<td class="td-label">字典编码</td>
							<td class="td-value">
							<input type="text" id="dictCode" name="dictCode" style="width:160px;" value="${dictCode }" />
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