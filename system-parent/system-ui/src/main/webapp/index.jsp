<%@page import="org.whale.system.base.UserContext"%>
<%@page import="org.whale.system.cache.service.DictCacheService"%>
<%@page import="org.whale.system.common.constant.DictConstant"%>
<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<%
	UserContext uc = (UserContext)request.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
	if(uc != null && uc.getUserId() != null){
		response.setHeader("Location", request.getContextPath()+"/main");
		response.sendRedirect(request.getContextPath()+"/main");
	}
	
	boolean verityFlag = SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "VERITY_CODE_FLAG"));
	pageContext.setAttribute("verityFlag", verityFlag);
	
	boolean autoLoginFlag = SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"));
	pageContext.setAttribute("autoLoginFlag", autoLoginFlag);
%>
    <meta charset="utf-8">
    <title>Whale管理平台</title>
    <%@include file="/jsp/base.jsp" %>
    <script src="${html }/js/cookie.js"></script>
    <link rel="stylesheet" type="text/css" href="${html }/ui/stylesheets/theme.css">
    <link rel="stylesheet" type="text/css" href="${html }/ui/stylesheets/premium.css">
	<style type="text/css">
	.navbar-default .navbar-brand, .navbar-default .navbar-brand:hover {color: #fff;}
	body {font-family: 'Microsoft Yahei';}
    </style>
    <!--[if lt IE 9]>
      <script src="${html }/js/html5shiv.min.js"></script>
    <![endif]-->
</head>
<body class=" theme-blue">
  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
  <!--[if (gt IE 9)|!(IE)]><!--> 
   
  <!--<![endif]-->
	<DIV>
    <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <a class="" href="index.html"><span class="navbar-brand"><span class="fa fa-paper-plane"></span>Whale平台</span></a></div>

        <div class="navbar-collapse collapse" style="height: 1px;">

        </div>
      </div>
    </div>
    


        <div class="dialog">
        <p class="bg-danger" style="padding: 10px 25px;visibility: hidden;border: 1px solid #F54343;border-radius: 3px;" id="errorInfoSpan" ></p>
    <div class="panel panel-default">
        <p class="panel-heading no-collapse">幼宝管理系统</p>
        <div class="panel-body">
            <form id="loginForm">
        	<input type="hidden" id="encryptedPwd" name="encryptedPwd" >
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" id="userName" name="userName" placeholder="用户名" class="form-control span12">
                </div>
                <div class="form-group">
                <label>密码</label>
                    <input type="password" id="password" name="password" placeholder="密码"  class="form-control span12 form-control">
                </div>
                
<c:if test="${verityFlag }">
          <div class="form-group" id="verifyCodeDiv" style="display:none;" show="0">
            <label >验证码</label>
            <div class="form-inline">
            <input id="verifycode" name="verifycode" class="form-control span6" placeholder="验证码"  type="text" maxlength="4" size="4" style="width:120px" onkeyup="value=value.replace(/[^\d]/g,'')">
              <a class="pic-ver radius2px" href="#"><img id="secimg" src="" width="72" height="31" alt="" title="看不清楚，换一张" onclick="javascript:createCode();" style="cursor:pointer;vertical-align: middle;border: 0;margin:5px;"></a>
            </div>
              
          </div>
</c:if>
                
                <a href="#" onclick="return login();" class="btn btn-primary pull-right"> 登录</a>
                <label class="remember-me"><input type="checkbox" id="autoLogin" name="autoLogin" > 记住我</label>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
    <p class="pull-right" style=""><a href="#" target="blank" style="font-size: .75em; margin-top: .25em;">Whale所有</a></p>
</div>
</head>
<script type="text/javascript">
String.prototype.trim = function() {
    return this.replace(/(^\s+)|(\s+$)/g, "");
}

function clientHeight(){
	var b;
	if(window.innerHeight){
		b=window.innerHeight;
	}else{
		if((document.body)&&(document.body.clientHeight)){
			b=document.body.clientHeight;
		}
	}
	if(document.documentElement&&document.documentElement.clientHeight){
		b=document.documentElement.clientHeight;
	}
	return b;
}
function sendForm(){
	if($("#userName").val() ==""){
		$("#userName").val("").focus();
		$("#errorInfoSpan").html("请输入用户名").css("visibility","visible");
		return false;
	}
	if($("#password").val() ==""){
<c:if test="${autoLoginFlag }">
		if($("#encryptedPwd").val() !=""){
			return true;
		}
</c:if>
		$("#password").val("").focus();
		$("#errorInfoSpan").html("请输入密码").css("visibility","visible");
		return false;
	}
<c:if test="${verityFlag }">
	if($("#verifycode").val() =="" && $("#verifyCodeDiv").attr("show") == "1"){
		$("#verifycode").val("").focus();
		$("#errorInfoSpan").html("请输入验证码").css("visibility","visible");
		return false;
	}
</c:if>
	return true;
}

function login(){
	if(!sendForm())
		return false;
	var dates=$("#loginForm").serialize();
	$.ajax({
		    url: "${ctx}/login",
		    type: 'post',
		    data: dates,
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		   	timeout: 30000,
		   	dataType: 'json',
		   	cache: false,
		    error: function(){
		        alert('用户登入网络连接出错~');
		        window.location.reload();
		    },
		    success: function(obj){
		    	//重启服务器，令牌失效，重新获取令牌
		    	if(typeof(obj) == 'undefined' || obj == null){
		    		window.location.reload();
		    	}
		    	if(obj.rs){
<c:if test="${autoLoginFlag }">
		    		if($("#autoLogin")[0].checked){
		    			if($("#password").val() != null){
		    				$.cookie("userName", $("#userName").val(), { expires: 30 });
			    			$.cookie("encryptedPwd", obj.msg, { expires: 30 });
		    			}
		    		}else{
		    			$.cookie("userName", null);
			    		$.cookie("encryptedPwd", null); 
		    		}
</c:if>
		    		window.location.href="${ctx}/main";
		    	}else{
		    		createCode();
		    		$("#errorInfoSpan").html(obj.msg).css("visibility","visible");
		    		var code = parseInt(obj.code);
		    		if(code > 10){
		    			$("#verifyCodeDiv").show().attr("show", "1");
		    		}else{
		    			code = code * 10 + 1;
		    		}
		    		$("#verifycode").val("");
		    		if(code > 30){
		    			$("#verifycode").focus();
		    		}else if(code > 20){
		    			$("#password").val("").focus();
		    		}else{
		    			$("#userName").val("").focus();
		    		}
		    	}
			}
		});
}

function createCode(){
<c:if test="${verityFlag }">
	$("#secimg").attr("src","${ctx}/code.jpg?"+new Date().getTime());
</c:if>
}


$(document).ready(function(){
<c:if test="${autoLoginFlag }">
	if($.cookie("userName") != null && $.cookie("userName") != ""){
		$("#userName").val($.cookie("userName"));
		$("#encryptedPwd").val($.cookie("encryptedPwd"));
		$("#autoLogin").attr('checked', 'checked');
		login();
		return ;
	}
</c:if>
	var h = clientHeight();
	
	//防止session过期时，index页面在子页面中打开
	if(self != top){
		window.top.location="${ctx}/";
	}
	createCode();
	$("#userName").keydown(function(event){
		if(event.keyCode == 13){
			$("#password")[0].focus();
		}
	})
	
	$("#password").keydown(function(event){
		if(event.keyCode == 13){
<c:if test="${verityFlag }">
		$("#verifycode")[0].focus();
</c:if>
<c:if test="${!verityFlag }">
			login();
</c:if>
		}
	})
<c:if test="${verityFlag }">
	$("#verifycode").keydown(function(event){
		if(event.keyCode == 13){
			login();
		}
	})
</c:if>
	$("#userName")[0].focus();
});

</script>
  
</body></html>

