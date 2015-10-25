<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>部门 树</title>
<%@include file="/jsp/btree.jsp" %>
<script type="text/javascript">
$(window).resize(function(){
	$("#treeDiv, #listFrame").height($.h());
});

$(function () {
	$("#treeDiv, #listFrame").height($.h());
	$('#tree').btree({
		nodes: ${nodes},
		idKey: 'id',
		pidKey: 'pid',
		textCol: 'deptName',
		orderCol: 'orderNo',
		onNodeSelected: function(event, data) {
        	$("#listFrame").attr("src", "${ctx }/dept/goList?pid="+data.id);
        }
	});
});
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2" id="treeDiv" style="padding: 0px 5px 0px 0px;overflow: auto;">
		  		<div id="tree"></div>
		  </div>
		  <div class="col-xs-10" id="frameDiv" style="padding: 0px;overflow: hidden;">
		  	<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/dept/goList"></iframe>
		  </div>
		</div>
	</div>
</body>
</html>


