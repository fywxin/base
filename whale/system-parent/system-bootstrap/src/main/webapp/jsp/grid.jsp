<%@include file="/jsp/base.jsp" %>
<link href="${html}/w3/css/plugins/jqgrid/ui.jqgrid.css" rel="stylesheet">
<script src="${html}/w3/js/plugins/jqgrid/i18n/grid.locale-cn.js"></script>
<script src="${html}/w3/js/plugins/jqgrid/jquery.jqGrid.min.js"></script>
<style type="text/css">
::-webkit-scrollbar{width: 16px;background-color: #F5F5F5;}
.table>tbody>tr>td, .table>tbody>tr>th {padding: 7px;}
</style>
<script type="text/javascript">
$(window).resize(function(){
	$("#gridTable").jqGrid('setGridWidth', $.w()-20).jqGrid('setGridHeight', $.h()-98-$("#queryForm").height());
});

function search(){
	 $("#gridTable").jqGrid('setGridParam',{
	    postData: $("#queryForm").serializeJson(),
	    page:1
	   }).trigger("reloadGrid");
}
</script>
