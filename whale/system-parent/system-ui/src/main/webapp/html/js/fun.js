(function($){
	$.extend({
		//获取window高度
		h: function(){
			var winHeight;
			if (window.innerHeight){
				winHeight = window.innerHeight; 
			}else if ((document.body) && (document.body.clientHeight)) {
				winHeight = document.body.clientHeight; 
			}
			if (document.documentElement && document.documentElement.clientHeight){
				winHeight = document.documentElement.clientHeight;
			}
			return winHeight;
		},
		
		//获取window宽度
		w: function(){
			var winWidth;
			if (window.innerWidth){
				winWidth = window.innerWidth; 
			} else if ((document.body) && (document.body.clientWidth)) {
				winWidth = document.body.clientWidth; 
			}
			if (document.documentElement && document.documentElement.clientWidth){
				winWidth = document.documentElement.clientWidth;
			}
			return winWidth;
		}
	});
	
	$.fn.btree=function(param){
		var defaults = {
				nodes: [],
				idKey: 'id',
				pidKey: 'pid',
				textCol: 'name',
				orderCol: 'id',
				orderAsc: true
		}
		var opts = $.extend(defaults, param);
		return $(this).treeview({
	        data: list2Tree(opts.nodes, opts.idKey, opts.pidKey, opts.textCol, opts.orderCol, opts.orderAsc),
	        showTags: true,
	        onNodeSelected: function(event, data) {
	        	if($.isFunction(opts.onNodeSelected)){
	        		opts.onNodeSelected(event, data);
	        	}
	        }
	    });
    };
    
    $.fn.grid=function(param){
		var defaults = {
				idField: 'id',
				search: false,
				silentSort: false,
				showColumns: true,
			    showRefresh: true,
			    detailView: false,
			    singleSelect: false,
			    resizable: true,
			    striped: true,
			    pagination: true,
			    sidePagination : 'server',
			    dataField: 'data',
				onlyInfoPagination : true,
			    paginationHAlign:'left',
			    paginationDetailHAlign: 'right',
			    toolbar: "#mytoolbar",
			    paginationFirstText : "首页",
			    paginationPreText: "上一页",
			    paginationNextText: "下一页",
			    paginationLastText: "尾页",
			    height: $.h()-$("#queryForm").height()-45,
			    cache: false
		}
		var opts = $.extend(defaults, param);
		return $(this).bootstrapTable(opts);
    };
    
})(jQuery);

function list2Tree(nodes, idKey, pidKey, textCol, orderCol, orderAsc){
	var i, l;
	var r = [];
	var tmpMap = [];
	for (i=0, l=nodes.length; i<l; i++) {
		tmpMap[nodes[i][idKey]] = nodes[i];
		nodes[i].text = nodes[i][textCol];
	}
	for (i=0, l=nodes.length; i<l; i++) {
		if (tmpMap[nodes[i][pidKey]] && nodes[i][idKey] != nodes[i][pidKey]) {
			if (!tmpMap[nodes[i][pidKey]]["nodes"])
				tmpMap[nodes[i][pidKey]]["nodes"] = [];
			tmpMap[nodes[i][pidKey]]["nodes"].push(nodes[i]);
			//按排序字段排序
			tmpMap[nodes[i][pidKey]]["nodes"].sort(function(o1, o2){
				if(orderAsc){
					return o1[orderCol] - o2[orderCol];
				}else{
					return o2[orderCol] - o1[orderCol];
				}
			});
		} else {
			r.push(nodes[i]);
		}
	}
	r.sort(function(o1, o2){
		if(orderAsc){
			return o1[orderCol] - o2[orderCol];
		}else{
			return o2[orderCol] - o1[orderCol];
		}
	});
	return r;
}