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
		
		openWin: function(options){
			if(!window.winOpener){
				window.winOpener = window.top.winOpener;
			}
			window.top.winOpener = window;
			if(typeof(options.url) != undefined && options.url != null){
				options.content = options.url;
			}
			
			var defaults = {
					type: 2,
				    shadeClose: false,
				    shade: 0.4,
				    area: ['60%', '70%']
			};
			var opts = $.extend(defaults, options);
			window.top.currentOpenId = window.top.layer.open(opts);
			window.openId = window.top.currentOpenId;
		},
		
		closeWin: function(){
			if(window.top.currentOpenId != null){
				window.top.layer.close(window.top.currentOpenId);
				window.top.currentOpenId = window.parent.parent.openId;
				window.parent.openId = null;
			}else{
				window.top.layer.close(window.parent.openId);
				window.top.currentOpenId = window.parent.parent.openId;
				window.parent.openId = null;
			}
		},
		
		//获取父窗口对象
		getParent : function(){
			return window.winOpener || window.top.winOpener;
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
			    		if($("#continueBut").length > 0){
			    			$("#"+formId+" input, #"+formId+" select").attr("readonly", "readonly");
					    	$("#continueBut, #backBut").show();
			    		}
			    		if($("#backBut").length > 0){
			    			$("#backBut").show();
			    		}
			    		if($.isFunction(param.onSuccess)){
			    			param.onSuccess(obj);
			    		}else{
			    			try{getMain().refresh();}catch(e){getMain().location.reload();}
			    			$.msg(obj.msg);
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
				var idArr = $('#gridTable').bootstrapTable('getSelIds');
				
				param.datas = {ids: idArr.join(',')};
			}
			
			param.info = '您确定要删除记录吗？';
			
			$.confirm(param);
		},
		
		confirm: function(param, options){
			var defaults = {
					btn: ['确定','取消'],
					shadeClose: false,
					shade: 0.4
			};
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
					    			$.msg(obj.msg);
									try{
										search();
									}catch(e){}
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
		
		run: function(param){
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
					$.alert('服务请求异常');
					if($.isFunction(param.onError)){
						param.onError();
					}
				},
				success: function(obj){
					if(obj.rs){
						if($.isFunction(param.onSuccess)){
							param.onSuccess(obj);
						}else{
							$.msg(obj.msg);
							try{
								search();
							}catch(e){}
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
		},
		
		alert: function(str){
			window.top.layer.alert(str);
		},
		
		msg: function(str){
			window.top.layer.msg(str, {time: 2000});
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
				showColumns: false,
			    showRefresh: false,
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
			    method: 'POST', //中文乱码解决
			    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			    height: (window.top.mainHeight-$("#queryForm").height()-85),
			    cache: false,
			    onBeforeLoad: function(queryDatas){//加入搜索参数
			    	var datas = $("#queryForm").serializeArray();
					for(var i=0; i<datas.length; i++){
						if(datas[i].value != null && $.trim(datas[i].value) != ""){
							queryDatas[datas[i].name] = $.trim(datas[i].value);
						}
					}
			    }
		};
		var opts = $.extend(defaults, param);
		//关闭选择框提示
		if(opts.columns[0].checkbox){
			opts.columns[0].fix = false;
		}
		opts.columns.splice(0, 0, {field: 'index',width: '30px', align: 'center', fix: false, formatter:function(value, row, index){return index+1;}});
		return $(this).bootstrapTable(opts);
    };
    
})(jQuery);

function resetWidth(){
	$("#gridTable").bootstrapTable('resetWidth', null);
}

function go(name, url){
	window.top.goSub(name, url);
}

function getMain(){
	return window.top.frames["frameMain"];
}

function getSub(){
	return window.top.frames["frameMain"];
}

/**ajax 未登录异常，跳转到登录页面 */
$(document).ajaxError(function(event, request, ajaxOptions, thrownError) {
	if(request.getResponseHeader("login") == "1"){
		window.location.href= window.top.ctx+"/";
	}
});

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



Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}