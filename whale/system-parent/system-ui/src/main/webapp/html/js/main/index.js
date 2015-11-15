var mainHeight = $.h();
function goMain(name, url){
	if($("#liMain a").html() != name){
		$("#liMain a").html(name);
		$("#liSub a").html("");
		$("#liSub").hide(name);
		$("#frameSub").attr("src", "");
	}else{
		$("#frameSub").hide();
	}
	$("#liMain").addClass("active");
	$("#liSub").removeClass("active");
	if(url.indexOf('?') == -1){
		$("#frameMain").show().attr("src", ctx+url+"?"+new Date().getTime());
	}else{
		$("#frameMain").show().attr("src", ctx+url+"&"+new Date().getTime());
	}
	
	return false;
}

function goTab(index){
	if(index == 1){
		$("#liMain").addClass("active");
		$("#liSub").removeClass("active");
		$("#frameSub").hide();
		$("#frameMain").show();
		window.setTimeout("resetListHead()",200);
	}else{
		$("#liMain").removeClass("active");
		$("#liSub").addClass("active");
		$("#frameSub").show();
		$("#frameMain").hide();
	}
}

//子页面列表表头对齐
function resetListHead(){
	try{
		window.frameMain.resetWidth();
	}catch(e){
		try{
			window.frameMain.listFrame.resetWidth();
		}catch(e2){
			
		}
	}
}

function goSub(name, url){
	$("#frameMain").hide();
	$("#liSub, #frameSub").show();
	$("#liMain").removeClass("active");
	$("#liSub").addClass("active");
	$("#liSub a").html(name);
	if(url.indexOf('?') == -1){
		$("#frameSub").attr("src", url+"?"+new Date().getTime());
	}else{
		$("#frameSub").attr("src", url+"&"+new Date().getTime());
	}
}

function accordionClk(t){
	$("ul[tabul='1']").each(function(){
		if($(this).attr("clk") == "1"){
			$(this).collapse('hide').removeAttr("clk");
		}
	});
	$(t).next().children("ul").attr("clk", "1");
}

$(window).resize(function(){
	$("#frameMain, #frameSub, #menuDi").height($.h()-65);
	mainHeight = $.h();
});
$(function() {
	$("#frameMain, #frameSub, #menuDiv").height($.h()-65);
});

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
	goMain("修改密码", "/user/goChangePassword");
}