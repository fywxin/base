$(window).resize(function(){
	var h = getGridDivHeight();
	$("#gridDiv").height(h);
	$("#gridTable").bootstrapTable('resetView', {height: h});
});

$(function(){
	$("#gridDiv").height(getGridDivHeight());
});

function getGridDivHeight(){
	return window.top.mainHeight-$("#queryForm").height()-95;
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