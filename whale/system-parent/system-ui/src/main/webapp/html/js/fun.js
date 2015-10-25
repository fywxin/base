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
		},
		
		save : function(param){
			if(window.saving){
				return ;
			}
			var formId = param.formId || "dataForm";
			if($.isFunction(param.beforeSave)){
    			param.beforeSave();
    		}
			try{
				if(!$("#"+formId).valid()) {return false;}
			}catch(e){}
			
			$("#saveBut").hide();
			$("#infoBoxDiv").html("").hide();
			var loadId = $.wait();
			window.saving = true;
			$.ajax({
				url: param.url,
				type: 'post',
				data: param.datas || $("#"+formId).serialize(),
				dataType: 'json',
				cache: false,
				error: function(obj){
					window.saving = false;
					window.top.layer.close(loadId);
					$("#infoBoxDiv").html('保存数据出错~').show();
					$("#saveBut").show();
					if($.isFunction(param.onError)){
						param.onError();
					}
			    },
			    success: function(obj){
			    	window.saving = false;
			    	window.top.layer.close(loadId);
			    	if(obj.rs){
			    		$("#"+formId+" input, #"+formId+" select").attr("readonly", "readonly");
			    		$("#"+formId+" textarea").attr("disabled", "disabled");
				    	$("#continueBut").show();
			    		if($.isFunction(param.onSuccess)){
			    			param.onSuccess(obj);
			    		}else{
			    			window.top.layer.msg(obj.msg, {time: 2000});
			    		}
			    	}else{
			    		$("#infoBoxDiv").html(obj.msg).show();
			    		$("#saveBut").show();
			    		if($.isFunction(param.onFail)){
			    			param.onFail(obj);
			    		}
			    	}
			    }
			 });
		},
		
		del : function(param){
			if(param.datas == null){
				var chks = $("#gridTable input:checkbox:checked[name='chk_col']");
				if(chks.length < 1){
					$.alert('请选择需要删除的记录');
					return ;
				}
				var idArr = [];
				chks.each(function(){
					idArr.push($(this).val());
				});
				
				param.datas = {ids: idArr.join(',')};
			}
			
			param.info = '您确定要删除记录吗？';
			
			$.confirm(param);
		},
		
		confirm: function(param, options){
			var defaults = {
					btn: ['确定','取消'],
					shadeClose: false,
					shade: 0.4,
			}
			var opts = $.extend(defaults, options);
			
			var confirmId= window.top.layer.confirm(param.info, opts, function () {
				 if($.isFunction(param.beforeAjax)){
						param.beforeAjax();
					 }
			        $.ajax({
						url : param.url,
						data : param.datas,
						contentType: "application/x-www-form-urlencoded; charset=UTF-8",
						dataType: 'json',
						type: 'post',
						cache: false,
						error: function(){
							window.top.layer.close(confirmId);
					        $.alert('服务请求异常');
					        if($.isFunction(param.onError)){
								param.onError();
							}
					    },
					    success: function(obj){
					    	window.top.layer.close(confirmId);
					    	if(obj.rs){
					    		if($.isFunction(param.onSuccess)){
					    			param.onSuccess(obj);
					    		}else{
									$.alert(obj.msg);
									reGrid();
					    		}
					    	}else{
					    		if($.isFunction(param.onFail)){
					    			param.onFail(obj);
					    		}else{
					    			$.alert(obj.msg);
					    		}
					    	}
						}
					});
				});
			
		},
		
		alert: function(str){
			window.top.layer.alert(str);
		},
		
		msg: function(str){
			
		},
		
		tip: function(str){
			
		},
		
		wait: function(str){
			return window.top.layer.load();
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
			    pageList: [10, 20, 50, 100],
			    height: (window.top.winHeight-$("#queryForm").height()-45)+"px",
			    cache: false
		}
		var opts = $.extend(defaults, param);
		return $(this).bootstrapTable(opts);
    };
    
})(jQuery);

function go(url){
	window.location.href=url;
}

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