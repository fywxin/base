<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>部门列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">

$(function(){
	$("#gridTable").grid({
		"url":"${ctx}/dept/doList",
		"colModel":[
			{name:'id', width:130, fixed:true, sortable:false, resize:false, align: "center",label:"操作",
				formatter: function(val, options, row){
					var strArr = [];
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.id+"')\"><i class='fa fa-pencil'></i> 修改</button>");
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.id+"')\"><i class='fa fa-trash-o'></i> 删除</button>");
		        	return strArr.join("");
				
				}
			},{
				"name":"name",
				"label":"部门名",
				"width":180
			},{
				"name":"deptAddr",
				"label":"部门地址",
				"width":130
			},{
				"name":"deptTel",
				"label":"部门电话",
				"width":90
			},{
				"name":"deptCode",
				"label":"部门编码",
				"width":120
			},{
				"name":"remark",
				"label":"备注",
				"width":120
			},{
				"name":"lft",
				"hidden":true
			},{
				"name":"rgt",
				"hidden":true
			},{
				"name":"level",
				"hidden":true
			},{
				"name":"uiicon",
				"hidden":true
			}
		],
		"loadonce":true,
		"rowNum":500,
		"scrollrows":true,
		"treeGrid":true,
		"ExpandColumn":"name",
		"treedatatype":"json",
		"treeGridModel":"nested",
		"treeReader":{
			"left_field":"lft",
			"right_field":"rgt",
			"level_field":"level",
			"leaf_field":"isLeaf",
			"expanded_field":"expanded",
			"loaded":"loaded",
			"icon_field":"icon"
		}
	});
});

function add(){
	var id = $("#gridTable").jqGrid("getGridParam","selrow");
	$.openWin({url: "${ctx}/dept/goSave?pid="+id,"title":'新增部门'});
}

function update(id){
	$.openWin({url: "${ctx}/dept/goUpdate?id="+id,"title":'编辑部门'});
}

function del(id){
	$.del({url:"${ctx}/dept/doDelete", datas:{id: id}, onSuccess: function(){
		window.parent.location.reload();
		$.alert("删除部门成功");
	}});
}
</script>
</head>
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
			</div>
			</form>
		<table id="gridTable" ></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>