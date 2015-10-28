<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>部门 树</title>
<%@include file="/jsp/base.jsp" %>
<script type="text/javascript">
$(window).resize(function(){
	$("#treeFrame, #listFrame").height($.h());
});
$(function(){
	$("#treeFrame, #listFrame").height($.h());
})

function goAuth(menuId){
	$("#listFrame").attr("src", "${ctx }/auth/goList?menuId="+menuId);
}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2" id="treeDiv" style="padding: 0px;overflow: auto;">
		  	<iframe id="treeFrame" name="treeFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/auth/goMenuTree?clkId=${clkId}"></iframe>
		  </div>
		  <div class="col-xs-10" id="frameDiv" style="padding: 0px;overflow: hidden;">
		  	<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/auth/goList?clkId=${clkId}"></iframe>
		  </div>
		</div>
	</div>
</body>
</html>