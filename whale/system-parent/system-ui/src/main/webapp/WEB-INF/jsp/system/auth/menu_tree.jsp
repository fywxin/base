<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>菜单树</title>
    
<script src="${html }/js/jquery-1.11.1.min.js"></script>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
var zTree;

var setting = {
	view: {
		dblClickExpand: false,
		showLine: true,
		selectedMulti: false
	},
	
	data: {
		key: {
			name: "menuName"
		},
		simpleData: {
			enable:true,
			idKey: "menuId",
			pIdKey: "parentId",
			rootPId: null
		}
	},

	callback: {
		beforeClick: function(treeId, treeNode) {
			if (treeNode.isParent) {
				zTree.expandNode(treeNode, true);
			}
			clickTree(treeNode);
			return true;
		}
	}
};

var zNodes =${nodes};

$(document).ready(function(){
	for(var i=0; i<zNodes.length; i++){
		zNodes[i].isParent = (zNodes[i].menuType != 3);
		zNodes[i].open = (zNodes[i].menuType != 3);
	}
	zNodes.push({"menuId": "-99", "parentId": 0, "isParent": false, "menuType": 4, menuName: "未分配菜单权限"})
	zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
});


function clickTree(treeNode){
	window.parent.goAuth(treeNode.menuId)
}
</script>
</head>
<body>
	<div id="tree" class="ztree" ></div>
</body>
</html>