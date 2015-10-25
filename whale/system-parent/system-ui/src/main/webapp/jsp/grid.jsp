<%@include file="/jsp/base.jsp" %>
<link href="${html}/plugins/grid/bootstrap-table.min.css" rel="stylesheet">
<script src="${html}/plugins/grid/bootstrap-table.min.js"></script>
<script src="${html}/plugins/grid/bootstrap-table-zh-CN.js"></script>
<script src="${html}/plugins/grid/src/bootstrap-table-resizable.js"></script>
<script src="${html}/plugins/grid/src/colResizable-1.5.source.js"></script>
<script src="${html}/plugins/grid/src/bootstrap-table-editable.js"></script>
<script src="${html}/plugins/grid/src/bootstrap-editable.js"></script>
<link href="${html}/css/over-write.css" rel="stylesheet">
<script type="text/javascript">
$(window).resize(function(){
	setGridDivHeight();
});

$(function(){
	setGridDivHeight();
});

function setGridDivHeight(){
	$("#gridDiv").height(window.top.mainHeight-$("#queryForm").height()-85);
}

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