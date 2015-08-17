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
	Integer errorCount = (Integer)request.getSession().getAttribute("ERROR");
	
	boolean verityFlag = SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "VERITY_CODE_FLAG")) || (errorCount != null && errorCount >= 3);
	pageContext.setAttribute("verityFlag", verityFlag);
	
	boolean autoLoginFlag = SysConstant.LOGIC_TRUE.equals(DictCacheService.getThis().getItemValue(DictConstant.DICT_SYS_CONF, "AUTO_LOGIN_FLAG"));
	pageContext.setAttribute("autoLoginFlag", autoLoginFlag);
%>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<meta name="renderer" content="webkit">
		
		<title>基础开发平台</title>

		<link href="${html}/www/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
	    <link href="${html}/www/font-awesome/css/font-awesome.css?v=4.3.0" rel="stylesheet">
	
	    <link href="${html}/www/css/animate.css" rel="stylesheet">
	    <link href="${html}/www/css/style.css?v=2.2.0" rel="stylesheet">

    	<script src="${html}/www/js/jquery-2.1.1.min.js"></script>
    	<script src="${html}/www/js/bootstrap.min.js?v=3.4.0"></script>
    	<script src="${html}/js/cookie.js"></script>	     
	</head>

<body class="gray-bg">
    <div class="middle-box text-center loginscreen  animated fadeInDown">
        <div>
            <h2>基础开发框架</h2>
            
            <form class="m-t" role="form" id="loginForm">
            <input type="hidden" id="encryptedPwd" name="encryptedPwd">
                <div class="form-group">
                    <input type="text" id="userName" name="userName" class="form-control" placeholder="用户名" autocomplete="off">
                </div>
                <div class="form-group">
                    <input type="password" id="password" name="password" class="form-control" placeholder="密码" autocomplete="off">
                </div>
				<div class="form-group" <c:if test="${!verityFlag }">style="display: none;"</c:if> id="verityDiv">
					<input type="text" id="verifycode" name="verifycode" class="form-control" style="width:190px;float: left;" placeholder="验证码" maxlength="4" size="4" autocomplete="off" onkeyup="value=value.replace(/[^\d]/g,'')" />
					<img id="secimg" src="${ctx}/code.jpg" alt="看不清楚，换一张" title="看不清楚，换一张" onclick="javascript:createCode();" style="cursor:pointer;border: 0;padding-left: 20px;height:33px;width:100px">
				</div>
				<c:if test="${autoLoginFlag }">
				<div class="form-group" align="left">
					<input type="checkbox" id="autoLogin" name="autoLogin" checked="checked" /> 一个月内自动登录
				</div>
				</c:if>
				
                <button type="button" onclick="return login();" class="btn btn-primary block full-width m-b">登 录</button>
                <p class="text-muted text-center"> <a href="login.html#"><small>忘记密码了？</small></a> | <a href="register.html">注册一个新账号</a>
                </p>
				<div class="alert alert-danger" style="display: none;" id="loginNoteDiv" align="center"></div>
            </form>
        </div>
    </div>
<script type="text/javascript">
$(function($) {
	 <c:if test="${autoLoginFlag }">
		if($.cookie("userName") != null && $.cookie("userName") != ""){
			$("#userName").val($.cookie("userName"));
			$("#encryptedPwd").val($.cookie("encryptedPwd"));
			$("#autoLogin").attr('checked', 'checked');
			login();
			return ;
		}
	</c:if>
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
		$("#verifycode").keydown(function(event){
			if(event.keyCode == 13){
				login();
			}
		})
		$("#userName")[0].focus();
});
function error(msg, id){
	$("#loginNoteDiv").html(msg).show();
	if(id != null){
		document.getElementById(id).focus();
		document.getElementById(id).select();
	}
	return false;
}

function sendForm(){
	if($.trim($("#userName").val()) == ""){
		return error("请输入用户名", "userName");
	}
	if($.trim($("#password").val()) == ""){
<c:if test="${autoLoginFlag }">
		if($("#encryptedPwd").val() !=""){
			return true;
		}
</c:if>
		return error("请输入登录密码", "password");
	}
	
	if($("#verityDiv").css("display") != "none" && $.trim($("#verifycode").val()) ==""){
		return error("请输入验证码", "verifycode");
	}
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
		    		if(obj.code == "1"){
		    			$("#verifycode").val("");
		    			$("#verityDiv").show();
		    			createCode();
		    		}
		    		
		    		error(obj.msg, null);
		    	}
			}
		});
}

function createCode(){
	$("#secimg").attr("src","${ctx}/code.jpg?"+new Date().getTime());
}
</script>
</body>
</html>
