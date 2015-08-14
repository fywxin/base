<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>组织列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$(window).resize(function(){
		$("#grid-table").jqGrid('setGridWidth', $("#breadcrumbs", parent.document).width()+20).jqGrid('setGridHeight', $.h());
	});
	
	$("#grid-table").jqGrid({
    	url :'${ctx}/dept/doList',
    	datatype: "json",
    	colNames: ['组织名称','组织编码','联系电话','联系地址','备注'],
    	colModel:[
					{name:'deptName',index:'deptName', width:160},
					{name:'deptCode',index:'deptCode',width:90},
					{name:'deptTel',index:'deptTel', width:90},
					{name:'deptAddr',index:'deptAddr', width:120} ,
					{name:'remark',index:'remark', width:170} 
				],
		altRows: true,
		treeGrid: true
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
    
<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
		</form>
	</div>
	<table id="grid-table"></table>
</body>
</html>