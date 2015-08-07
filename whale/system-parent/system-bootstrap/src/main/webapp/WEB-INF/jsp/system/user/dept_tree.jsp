<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>部门 树</title>
<%@include file="/html/jsp/parent.jsp" %>
<%@include file="/html/jsp/ztree.jsp" %>
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
				name: "deptName"
			},
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "pid",
				rootPId: null
			}
		},

		callback: {
			beforeClick: function(treeId, treeNode) {
				if (treeNode.isParent) {
					zTree.expandNode(treeNode, true);
				}
				$("#listFrame").attr("src", "${ctx}/user/goList?deptId="+treeNode.id);
				return true;
			}
		}
	};
	
	var zNodes =${nodes};
	
	$(document).ready(function(){
		zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
		var root = zTree.getNodesByFilter(function (node) { return node.level == 0 }, true);
		zTree.expandNode(root, true);
		$("#listFrame").attr("src", "${ctx}/user/goList?deptId="+root.id);
		setSpace();
	});
	
	function setSpace() {
		var width = parseInt(document.documentElement.scrollWidth);
		var height=$.h()-8;
		$("#mainTable").css("width",width+"px");
		$("#tree, #mainTable, #listFrame").css("height",height+"px");
	}
	
	function getNode(id){
		return zTree.getNodeByParam("id", id);
	}
    </script>
</head>
<body style="width:100%;height:100%;border:0;overflow:hidden;">
<table id="mainTable" style="position: absolute;left: 0px;top: 0px;">
	<tr>
		<td width=200px align=left valign=top style="border: solid 1px #CCC;">
			<ul id="tree" class="ztree" style="width:200px; overflow:auto;"></ul>
		</td>
		<td align=left valign=top>
			<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100%></iframe>
		</td>
	</tr>
</table>
</body>
</html>


