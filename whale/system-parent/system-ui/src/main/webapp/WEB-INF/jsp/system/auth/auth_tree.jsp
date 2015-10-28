<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>部门 树</title>
<%@include file="/jsp/base.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
$(window).resize(function(){
	$("#treeFrame, #listFrame").height($.h());
});
$(function(){
	$("#treeFrame, #listFrame").height($.h());
})

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
	$("#listFrame").attr("src", "${ctx }/auth/goList?menuId="+treeNode.menuId);
}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2" id="treeDiv" style="padding: 0px;overflow: auto;">
		  	<div id="tree" class="ztree" ></div>
		  </div>
		  <div class="col-xs-10" id="frameDiv" style="padding: 0px;overflow: hidden;">
		  	<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/auth/goList?clkId=${clkId}"></iframe>
		  </div>
		</div>
	</div>
</body>
</html>