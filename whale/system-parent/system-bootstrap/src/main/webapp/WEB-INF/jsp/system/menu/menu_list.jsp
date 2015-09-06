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
    	url :'${ctx}/menu/doList',
    	gridGrid:true,
    	treeGridModel: 'adjacency',
    	ExpandColumn:"name",
    	pager: false,
    	treeReader : {
    	      level_field: "level",
    	      parent_id_field: "pid",
    	      leaf_field: "isleaf",
    	      expanded_field: "expanded"
    	    },
    	colNames: ['菜单名称', '链接地址', '菜单类型', '是否公共'],
       	colModel: [
       	           
					{name:'name',index:'name', width:260},
					{name:'menuUrl',index:'menuUrl', width:160},
					{name:'menuType',index:'menuType', width:260},
					{name:'isPublic',index:'isPublic', fixed:true, width:80,
						formatter: function(cellvalue, options, rowObject){
							return openStateObj[cellvalue];
						}	
					}
			]
	});
});
</script>
</head>
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
					<col  width="10%" />
					<col  width="40%"/>
					<col  width="10%"/>
					<col  width="40%"/>
				<tbody>
					<tr>
						<td class="td-label">角色名称</td>
						<td class="td-value"><input type="text" id="roleName" name="roleName" style="width:160px;" value="${roleName }" /></td>
						<td class="td-label">角色编码</td>
						<td class="td-value">
							<input type="text" id="roleCode" name="roleCode" style="width:160px;"  value="${roleCode }" />
							<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
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