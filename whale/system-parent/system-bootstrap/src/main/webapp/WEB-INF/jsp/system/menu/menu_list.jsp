<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>菜单列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var menuTypeObj = {1: "tab菜单", 2: "文件夹菜单", 3: "叶子菜单"};
var openTypeObj = {1: "窗口内打开", 2: "弹出窗口"};
var openStateObj = {1: "打开", 2: "合并"};

$(function(){
	$("#gridTable").grid({
		"url":"${ctx}/menu/doList",
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
				"label":"菜单名",
				"width":130
			},{
				"name":"menuUrl",
				"label":"菜单地址",
				"width":230
			},{
				"name":"openType",
				"label":"打开方式",
				"width":90,
				"formatter": function(val, options, rowObject){
					return openTypeObj[val];
				}
			},{
				"name":"openState",
				"label":"打开状态",
				"width":70,
				"formatter": function(val, options, rowObject){
					return openStateObj[val];
				}
			},{
				"name":"isPublic",
				"label":"是否公共",
				"width":50,
				"formatter": function(val, options, rowObject){
					return val == 0 ? "否" : "是";
				}
			},{
				"name":"menuType",
				"label":"菜单类型",
				"width":90,
				"formatter": function(val, options, rowObject){
					return menuTypeObj[val];
				}
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
	$.openWin({url: "${ctx}/menu/goSave","title":'新增菜单'});
}

function update(menuId){
	$.openWin({url: "${ctx}/menu/goUpdate?menuId="+menuId,"title":'编辑菜单'});
}

function del(menuId){
	$.del({url:"${ctx}/menu/doDelete", datas:{menuId: menuId}, onSuccess: function(){
		window.parent.location.reload();
		$.alert("删除菜单成功");
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