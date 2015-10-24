<%@include file="/jsp/base.jsp" %>
<link href="${html}/plugins/grid/bootstrap-table.min.css" rel="stylesheet">
<script src="${html}/plugins/grid/bootstrap-table.min.js"></script>
<script src="${html}/plugins/grid/bootstrap-table-zh-CN.js"></script>
<script src="${html}/plugins/grid/src/bootstrap-table-resizable.js"></script>
<script src="${html}/plugins/grid/src/colResizable-1.5.source.js"></script>
<script src="${html}/plugins/grid/src/bootstrap-table-editable.js"></script>
<script src="${html}/plugins/grid/src/bootstrap-editable.js"></script>
<style>
.fixed-table-toolbar .bars, .fixed-table-toolbar .columns, .fixed-table-toolbar .search {margin: 5px 0px;}
</style>
<script type="text/javascript">
function search(){
	try{
		var datas = $("#queryForm").serializeArray();
		var param = {};
		for(var i=0; i<datas.length; i++){
			if(datas[i].value != null && $.trim(datas[i].value) != ""){
				param[datas[i].name] = $.trim(datas[i].value);
			}
		}
		$("#gridTable").bootstrapTable('refresh', {query: param});
	}catch(e){}
}
</script>