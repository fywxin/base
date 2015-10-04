<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>部门 树</title>
<%@include file="/jsp/base.jsp" %>
<%@include file="/jsp/btree.jsp" %>
<script type="text/javascript">
$(window).resize(function(){
	$("#treeDiv, #listFrame").height($.h());
});

$(function () {
	$("#treeDiv, #listFrame").height($.h());
    $('#deptTree').treeview({
        data: ${nodes},
        color: "#428bca",
        showTags: true,
        levels: 3,
        onNodeSelected: function(event, data) {
        	$("#listFrame").attr("src", "${ctx }/user/goList?deptId="+data.id);
        }
    });
});
</script>
</head>
<body class="my_gridBody gray-bg">
	<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2" id="treeDiv" style="padding: 0px;overflow: auto;">
		  		<div id="deptTree"></div>
		  </div>
		  <div class="col-xs-10" id="frameDiv" style="padding: 0px;overflow: hidden;">
		  	<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/user/goList?deptId=${deptId}"></iframe>
		  </div>
		</div>
	</div>
</body>
</html>

