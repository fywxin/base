<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>${domain.domainCnName}树</title>
<%@include file="/jsp/base.jsp" %>
<%@include file="/jsp/btree.jsp" %>
<script type="text/javascript">
    $(window).resize(function(){
        $("#treeDiv, #listFrame").height($.h());
    });

    var nodes = $.parseTree(${"$"+"{nodes}"},"id","pid","name","orderNo");

    $(function () {
        $("#treeDiv, #listFrame").height($.h());
        $('#tree').treeview({
            data: nodes,
            color: "#428bca",
            showTags: true,
            levels: 3,
            onNodeSelected: function(event, data) {
                $("#listFrame").attr("src", "${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goList?id="+data.id);
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
            <iframe id="listFrame" name="listFrame" frameborder=0 scrolling=auto width=100% src="${"$"+"{ctx}"}/${domain.domainName?uncap_first}/goList"></iframe>
        </div>
    </div>
</div>
</body>
</html>


