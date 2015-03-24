<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
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
			zNodes[i].isParent = (zNodes[i].menuType != 3)
		}
		
		zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
		setSpace();
		var node = zTree.getNodeByParam("menuId", "${clkId}");
		if(node != null){
			zTree.expandNode(node);
			clickTree(node);
		}
	});
	
	function setSpace() {
		var width = parseInt(document.documentElement.scrollWidth);
		var height=$.h()-20;
		$("#mainTable").css("width",width+"px").css("height",height+"px");
		$("#tree").css("height",height+"px");
		$("#testIframe").css("height",$("#mainTable").css("height"));
	}
	
	function clickTree(treeNode){
		$("#testIframe").attr("src", "${ctx}/auth/goList?menuId="+treeNode.menuId);
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
			<iframe id="testIframe" name="testIframe" frameborder=0 scrolling=auto width=100% ></iframe>
		</td>
	</tr>
</table>
</body>
</html>


