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
	        width: '10%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	var strArr = [];
	            strArr.push('<a href="javascript:;" class="link" onclick=go("修改菜单","${ctx}/menu/goUpdate?menuId='+row.menuId+'")>修改</a>');
	            strArr.push('<span class="link-sep">|</span>');
	            strArr.push('<a href="javascript:;" class="link" onclick=del('+row.menuId+') >删除</a>');
	        	return strArr.join("");
			}
	    }, {
	        field: 'menuName',
	        width: '15%',
	        title: '菜单名'
	    }, {
	        field: 'menuUrl',
	        title: '菜单地址'
	    }, {
	        field: 'menuInco',
	        title: '图标',
	        width: '8%'
	    }, {
	        field: 'openType',
	        title: '打开方式',
	        width: '10%',
	        formatter: function(value, row, index){
				return openTypeObj[value];
			}
	    }, {
	        field: 'isPublic',
	        title: '是否公共',
	        width: '9%',
	        formatter: function(value, row, index){
	        	return value == 0 ? "否" : "是";
			}
	    }, {
	        field: 'menuType',
	        title: '菜单类型',
	        width: '10%',
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
