$(window).resize(function(){
	var h = getGridDivHeight();
	$("#gridDiv").height(h);
	$("#gridTable").bootstrapTable('resetView', {height: h});
});

$(function(){
	$("#gridDiv").height(getGridDivHeight());
	$("#queryForm input, #queryForm select").keydown(function(event){
		if(event.keyCode==13){
			search(1);
		}
	});
});

function getGridDivHeight(){
	return window.top.mainHeight-$("#queryForm").height()-95;
}

function search(pageNo){
	try{
		var param = {};
		if(pageNo != null && typeof(pageNo) != "undefined" && pageNo > 0){
			param.offset = (pageNo-1) * 10;
		}
		$("#gridTable").bootstrapTable('refresh', {query: param});
	}catch(e){}
}

function refresh(){
	try{
		$("#gridTable").bootstrapTable('refresh', {});
	}catch(e){}
}