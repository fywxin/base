<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
<%@include file="/jsp/base.jsp" %>
<%@include file="/jsp/btree.jsp" %>
<script type="text/javascript">
$(window).resize(function(){
	$("#treeDiv, #listFrame").height($.h());
});

var nodes = $.parseTree(${nodes});

$(function () {
	$("#treeDiv, #listFrame").height($.h());
    $('#tree').treeview({
        data: nodes,
        color: "#428bca",
        showTags: true,
        levels: 2,
        onNodeSelected: function(event, data) {
        	if(data.id == 0){
    			$("#listFrame").attr("src", "${ctx}/dict/goList");
    		}else if(data.pid == 0){
    			$("#listFrame").attr("src", "${ctx}/dictItem/goList?dictId="+data.id);
    		}else{
    			var id = data.id;
    			id=id.substring(2,id.length);
    			$("#listFrame").attr("src", "${ctx}/dictItem/goView?dictItemId="+id);
    		}
        }
    });
});

</script>
</head>
<body class="my_gridBody gray-bg">
	<div class="container-fluid">
		<div class="row">
		  <div class="col-xs-2" id="treeDiv" style="padding: 0px;overflow: auto;">
		  		<div id="tree"></div>
		  </div>
		  <div class="col-xs-10" id="frameDiv" style="padding: 0px;overflow: hidden;">
		  		<iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${ctx }/dict/goList"></iframe>
		  </div>
		</div>
	</div>
</body>
</html>


