var winOpener = null;
var winDialog = null;
var waitObj = null;
(function($){
	$.extend({
		grid : function(param, id){
			id = id || "grid";
			param.formId = param.formId || "queryForm";
			param.rownumbers = param.rownumbers || true;
			param.height = param.height || ($.h()-$("#"+param.formId).height()-5);
			param.pageSize = param.pageSize || 20;
			
			//统一将行记录转为数结构记录
			if(param.tree){
				param.onSuccess = function(data, grid){
					var i,l,
					key = grid.options.uid,
					parentKey = grid.options.pid,
					childKey = "children",
					orderCol = grid.options.orderCol,
					orderAsc = grid.options.orderAsc,
					sNodes = data.datas;

					var r = [];
					var tmpMap = [];
					for (i=0, l=sNodes.length; i<l; i++) {
						tmpMap[sNodes[i][key]] = sNodes[i];
					}
					for (i=0, l=sNodes.length; i<l; i++) {
						if (tmpMap[sNodes[i][parentKey]] && sNodes[i][key] != sNodes[i][parentKey]) {
							if (!tmpMap[sNodes[i][parentKey]][childKey])
								tmpMap[sNodes[i][parentKey]][childKey] = [];
							tmpMap[sNodes[i][parentKey]][childKey].push(sNodes[i]);
							//按排序字段排序
							tmpMap[sNodes[i][parentKey]][childKey].sort(function(o1, o2){
								if(orderAsc){
									return o1[orderCol] - o2[orderCol];
								}else{
									return o2[orderCol] - o1[orderCol];
								}
							});
						} else {
							r.push(sNodes[i]);
						}
					}
					r.sort(function(o1, o2){
						if(orderAsc){
							return o1[orderCol] - o2[orderCol];
						}else{
							return o2[orderCol] - o1[orderCol];
						}
					});
					data.datas=r;
					//alert(JSON.stringify(data)); 参考ztree  transformTozTreeFormat  将线性列表转树形列表
				}
			}
			
			window[id] = $("#"+id).ligerGrid(param);
			
			$("#"+param.formId+" input[type='text'], #"+param.formId+" select").keydown(function(a){
						if(a.keyCode==13){
							window[id].loadData();
						}
			});
			
			$("#queryBut").click(function(a){
					window[id].loadData();
			});
			
		    $("#pageloading").hide();
		},
		
		//弹出窗口对象
		openWin : function(param){
			
			if(!window.winOpener){
				window.winOpener = window.top.winOpener;
			}
			window.top.winOpener = window;
			
			
			param.height = param.height || screen.availHeight * 0.65;
			param.width = param.width || screen.availWidth * 0.75;
			try{
				winDialog = window.top.$.ligerDialog.open(param);
			}catch(e){
				winDialog = $.ligerDialog.open(param);
			}
			window.top.winDialog = winDialog;
			return winDialog;
		},
		
		//获取父窗口对象
		getWinOpener : function(){
			return window.winOpener || window.top.winOpener;
		},
		
		//获取当前弹出窗口对象
		getWin : function(){
			if($.getWinOpener())
				return $.getWinOpener().winDialog || window.top.winDialog;
			else
				return window.top.winDialog;
		},
		
		//关闭窗口
		closeWin : function(){
			$.getWin().hide();
		},
		
		//确认对话框
		confirm : function(param){
			return window.top.$.ligerDialog.confirm(param.info, function (yes) {
				if(yes){
					if($.isFunction(param.ok)){
						param.ok();
					}
				}else{
					if($.isFunction(param.cancel)){
						param.cancel();
					}
				}
			});
		},
		
		//提示信息
		alert : function(info, callback, Title){
			var title = Title || '';
			return window.top.$.ligerDialog.alert(info, title, 'warn', callback);
		},
		
		//正在保存
		wait : function(info){
			var msg = info || '正在保存中,请稍候...';
			return window.top.$.ligerDialog.waitting(msg);
		},
		
		//判断是否是非法字符
		resolvChar: function(str){
			var htmlReg = /<.*?>/g;
			if(htmlReg.test(str)){ return true; }
			return false;
		},
		
		save : function(param){
			var formId = param.formId || "dataForm";
			if($.isFunction(param.beforeSave)){
    			param.beforeSave();
    		}
			
			try{
				if(!$("#"+formId).valid()) {return false;}
			}catch(e){}
			
			param.datas = param.datas || $("#"+formId).serialize();
			toolBar.setDisabled("saveBut");
			
			$("#infoBoxDiv").html("").css("display","none");
			waitObj = $.wait();
			
			$.ajax({
				url: param.url,
				type: 'post',
				data: param.datas,
				dataType: 'json',
				cache: false,
				error: function(obj){
					waitObj.close();
					$("#infoBoxDiv").html('保存数据出错~').css("display","block");
					toolBar.setEnabled("saveBut");
					if($.isFunction(param.onError)){
						param.onError();
					}
			    },
			    success: function(obj){
			    	waitObj.close();
			    	if(obj.rs){
			    		if($.isFunction(param.onSucess)){
			    			param.onSucess(obj);
			    		}else{
			    			$.alert(obj.msg);
			    			try{
		    					$.getWinOpener().grid.loadData();
		    				}catch(e){}
		    				$.closeWin();
			    		}
			    	}else{
			    		$("#infoBoxDiv").html(obj.msg).css("display","block");
			    		toolBar.setEnabled("saveBut");
			    		if($.isFunction(param.onFail)){
			    			param.onFail(obj);
			    		}
			    	}
			    }
			 });
		},
		
		sure : function(param){
			$.confirm({
				 info: param.info,
				 ok: function () {
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
					    			grid.reload();
									$.alert(obj.msg);
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
				}
			});
		},
		
		del : function(param){
			if(param.datas == null){
				var idArr = grid.getSelIds();
				if(idArr.length < 1){
					$.alert('请选择需要删除的记录');
					return ;
				}
				param.datas = {ids: idArr.join(',')};
			}
			
			param.info = '您确定要删除记录吗？';
			
			$.sure(param);
		}
	});
})(jQuery);

$(document).ajaxError(function(event, request, ajaxOptions) {
	if(request.status==401){
		for(var i in request){
			alert(i+" | "+request[i])
		}
	}
});

var statusObj = {1:"正常", 2: "<span class='sred'>禁用</span>"};
var yesObj = {1:"是", 0: "否"};