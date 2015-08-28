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
		}
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