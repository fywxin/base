<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>部门 树</title>
<%@include file="/jsp/base.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
$(window).resize(function(){
	$("#treeDiv, #listFrame").height($.h());
});

var zTree;

var setting = {
	view: {
		dblClickExpand: false,
		showLine: true,
		selectedMulti: false
	},
	
	data: {
		key: {
			name: "name"
		},
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "pid",
			rootPId: -1
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
	$("#treeDiv, #listFrame").height($.h());
	zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
});


function clickTree(treeNode){
	if(treeNode.id == 0){
		$("#listFrame").attr("src", "${ctx}/dict/goList");
	}else if(treeNode.pid == 0){
		$("#listFrame").attr("src", "${ctx}/dictItem/goList?dictId="+treeNode.id);
	}else{
		var id = treeNode.id;
		id=id.substring(2,id.length);
		$("#listFrame").attr("src", "${ctx}/dictItem/goView?dictItemId="+id);
	}
}
</script>
</head>
<body class="my_gridBody gray-bg">
	<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2" id="treeDiv" style="padding: 0px;overflow: auto;">
		  		<div id="tree" class="ztree"></div>
		  </div>
		  <div class="col-xs-10" id="frameDiv" style="padding: 0px;overflow: hidden;">
		  	<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/dict/goList"></iframe>
		  </div>
		</div>
	</div>
</body>
</html>


