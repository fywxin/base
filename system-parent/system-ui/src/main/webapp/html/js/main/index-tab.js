var mainHeight = $.h();

$(window).resize(function(){
	$("#menuDiv, .J_iframe").height($.h()-65);
	mainHeight = $.h();
});
$(function() {
	$("#menuDiv").height($.h()-65);
});

function accordionClk(t){
	$("ul[tabul='1']").each(function(){
		if($(this).attr("clk") == "1"){
			$(this).collapse('hide').removeAttr("clk");
		}
	});
	$(t).next().children("ul").attr("clk", "1");
}

function loginOut(){
	$.ajax({
		    url: ctx+"/loginOut",
		    type: 'post',
		    error: function(){
		        window.location.href=ctx;
		    },
		    success: function(obj){
		    	$.cookie("userName", null);
	    		$.cookie("encryptedPwd", null); 
		    	window.location.href=ctx;
			}
		});
}

function changePassword(){
	tab.addTab({id:"pwd",name:"修改密码", url:"/user/goChangePassword"});
}