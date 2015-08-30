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
		
		openWin: function(options){
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
		
		confirm: function(str, options, yes, no){
			var defaults = {
					btn: ['确定','取消'],
					shadeClose: false,
					shade: true
			}
			if(typeof(no) == "undefined" || !$.isFunction(no)){
				no = function(id){
					alert("关闭"+id)
				}
			}
			var opts = $.extend(defaults, options);
			
			window.top.layer.confirm(str, opts, yes, no);
		},
		
		alert: function(str){
			
		},
		
		msg: function(str){
			
		},
		
		tip: function(str){
			
		}
	});
	
	$.fn.grid=function(options){
		var defaults = {
				datatype: "json",
				rowNum : 20,
				rowList : [ 10, 20, 30, 50],
				pager : '#gridPager',
				height: $.h()-98-$("#queryForm").height(),
				repeatitems: false,
				altRows: true,
				autowidth: true,
				styleUI: "Bootstrap",
				mtype : "post"
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