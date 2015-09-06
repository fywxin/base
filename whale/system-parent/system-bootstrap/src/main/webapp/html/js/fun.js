(function($){
	$.extend({
		//获取window高度
		h: function(){
			var winHeight;
			if (window.innerHeight) winHeight = window.innerHeight; 
			else if ((document.body) && (document.body.clientHeight)) 
				winHeight = document.body.clientHeight; 
			if (document.documentElement 
				&& document.documentElement.clientHeight){winHeight = document.documentElement.clientHeight;}

			return winHeight;
		},
		
		//获取window宽度
		w: function(){
			var winWidth;
			if (window.innerWidth) winWidth = window.innerWidth; 
			else if ((document.body) && (document.body.clientWidth)) 
				winWidth = document.body.clientWidth; 
			if (document.documentElement 
				&& document.documentElement.clientWidth){winWidth = document.documentElement.clientWidth;}

			return winWidth;
		},
		
		save : function(param){
			if(window.saving){
				return ;
			}
			window.saving = true;
			var formId = param.formId || "dataForm";
			if($.isFunction(param.beforeSave)){
    			param.beforeSave();
    		}
			
			try{
				if(!$("#"+formId).valid()) {return false;}
			}catch(e){}
			
			param.datas = param.datas || $("#"+formId).serialize();
			$("#saveBut").hide();
			$("#infoBoxDiv").html("").hide();
			
			var loadId = $.wait();
			
			$.ajax({
				url: param.url,
				type: 'post',
				data: param.datas,
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
			    		if($.isFunction(param.onSuccess)){
			    			param.onSuccess(obj);
			    		}else{
			    			try{
		    					$.getParent().reGrid();
		    				}catch(e){}
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
		},
		
		parseTree : function(datas, id, pid, name, order, asc){
			key = id || "id",
			parentKey = pid || "pid",
			nameKey = name || "name",
			childKey = "nodes",
			orderCol = order || key,
			orderAsc = asc || true,
			sNodes = datas;

			var r = [];
			var tmpMap = [];
			for (i=0, l=sNodes.length; i<l; i++) {
				tmpMap[sNodes[i][key]] = sNodes[i];
				sNodes[i].text = sNodes[i][nameKey];
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
			return r;
		}
		
		
	});
	
	$.fn.grid=function(options){
		var defaults = {
				datatype: "json",
				rowNum : 20,
				rowList : [ 10, 20, 30, 50],
				pager : '#gridPager',
				repeatitems: false,
				altRows: true,
				height: $.h()-98-$("#queryForm").height(),
				width: $.w()-20,
				styleUI: "Bootstrap",
				mtype : "post",
				viewrecords: true,
				prmNames: {page:"page",rows:"rows", npage:null}
		};
		var opts = $.extend(defaults, options);
        return $(this).jqGrid(opts);
    };
    
    $.fn.serializeJson=function(){
        var serializeObj={};
        var array=this.serializeArray();
        var str=this.serialize();
        $(array).each(function(){
            if(serializeObj[this.name]){
                if($.isArray(serializeObj[this.name])){
                    serializeObj[this.name].push(this.value);
                }else{
                    serializeObj[this.name]=[serializeObj[this.name],this.value];
                }
            }else{
                serializeObj[this.name]=this.value; 
            }
        });
        return serializeObj;
    };
})(jQuery);

Date.prototype.Format = function (fmt) { //author: meizz 
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