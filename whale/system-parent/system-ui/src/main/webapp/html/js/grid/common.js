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
	$("#queryForm select").chosen({
		no_results_text: "未找到", 
		search_contains: true,
		disable_search_threshold: 10
	});
});

function getGridDivHeight(){
	return window.top.mainHeight-$("#queryForm").height()-85;
}

function search(pageNo){
	try{
		var param = {};
		if(pageNo != null && typeof(pageNo) != "undefined" && pageNo > 0){
			param.offset = (pageNo-1) * 10;
		}
		$("#gridTable").bootstrapTable('getOptions').pageNumber = pageNo;
		$("#gridTable").bootstrapTable('refresh', {query: param});
	}catch(e){}
}

function refresh(){
	try{
		$("#gridTable").bootstrapTable('refresh', {});
	}catch(e){}
}