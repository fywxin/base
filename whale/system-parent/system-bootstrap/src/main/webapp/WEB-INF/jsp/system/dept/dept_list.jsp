<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>组织列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
$(function(){
	$(window).resize(function(){
		$("#grid-table").jqGrid('setGridWidth', $("#breadcrumbs", parent.document).width()+20).jqGrid('setGridHeight', $.h());
	});
	
	$("#grid-table").grid({
		treeGrid: true,
		ExpandColumn : 'name',
    	url :'${ctx}/dept/doList',
    	colNames: ['组织名称','组织编码','联系电话','联系地址','备注'],
    	colModel:[
					{name:'deptName',index:'deptName', width:160},
					{name:'deptCode',index:'deptCode',width:90},
					{name:'deptTel',index:'deptTel', width:90},
					{name:'deptAddr',index:'deptAddr', width:120} ,
					{name:'remark',index:'remark', width:170} 
				]
		
	});
});

function update(id){
	$.openWin({url: "${ctx}/dept/goUpdate?id="+id,"title":'修改组织'});
}

function del(id){
	$.del({url:"${ctx}/dept/doDelete", datas:{ids:id}});
}

</script>
</head>
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
				  <button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
			</div>
		</form>
		<table id="gridTable" ></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>