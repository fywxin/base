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
	    url: '${ctx}/menu/doList?parentId=${parentId}',
	    idField: 'menuId',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '18%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	var strArr = [];
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"go('修改菜单','${ctx}/menu/goUpdate?menuId="+row.menuId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
				strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.menuId+"')\"><i class='fa fa-trash-o'></i> 删除</button>");
	        	return strArr.join("");
			}
	    }, {
	        field: 'menuName',
	        width: '10%',
	        title: '菜单名',
	        sortable: true
	    }, {
	        field: 'menuUrl',
	        title: '菜单地址',
	        width: '25%'
	    }, {
	        field: 'menuInco',
	        title: '菜单图标',
	        width: '5%'
	    }, {
	        field: 'openType',
	        title: '打开方式',
	        width: '5%',
	        formatter: function(value, row, index){
				return openTypeObj[value];
			}
	    }, {
	        field: 'isPublic"',
	        title: '是否公共',
	        width: '5%',
	        formatter: function(value, row, index){
	        	return value == 0 ? "否" : "是";
			}
	    }, {
	        field: 'openState',
	        title: '打开状态',
	        width: '5%',
	        formatter: function(value, row, index){
				return openStateObj[value];
			}
	    }, {
	        field: 'menuType',
	        title: '菜单类型',
	        width: '5%',
	        formatter: function(value, row, index){
				return menuTypeObj[value];
			}
	    }
	   ]
	});
});


function del(menuId){
	$.del({url:"${ctx}/menu/doDelete", datas:{menuId: menuId}});
}
</script>
</head>
    
<body style="overflow: hidden;">
	<div class="my_gridBox">
		<form id="queryForm" >
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增菜单','${ctx}/menu/goSave')"><i class="fa fa-plus"></i> 新  增 </button>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>