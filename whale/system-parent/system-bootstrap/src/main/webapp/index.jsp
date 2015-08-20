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
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="${html }/favicon.png" type="image/png">

    <title>基础开发框架 - 登录</title>

    <link href="${html }/w3/css/bootstrap.min.css" rel="stylesheet">
    <link href="${html }/w3/css/font-awesome.css" rel="stylesheet">
    <link href="${html }/w3/css/animate.css" rel="stylesheet">
    <link href="${html }/w3/css/style.css" rel="stylesheet">
    <link href="${html }/w3/css/login.css" rel="stylesheet">

</head>

<body class="signin">
    <div class="signinpanel">
        <div class="row">
            <div class="col-sm-7">
                <div class="signin-info">
                    <div class="logopanel m-b">
                        <h1>[ Whale ]</h1>
                    </div>
                    <div class="m-b"></div>
                    <h4>欢迎使用 <strong>Whale 后台基础框架</strong></h4>
                    <ul class="m-b">
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势一</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势二</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势三</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势四</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i> 优势五</li>
                    </ul>
                    <strong>还没有账号？ <a href="#">立即注册&raquo;</a></strong>
                </div>
            </div>
            <div class="col-sm-5" >
            <div id="logindiv">
                <form id="loginForm">
                    <h3 class="no-margins">登录：</h3>
                    <p class="m-t-md">登录到Whale后台基础框架</p>
                    
                    <input type="text" id="userName" name="userName" class="form-control uname" style="color: black;" placeholder="用户名" autocomplete="off" />
                    <input type="password" id="password" name="password" class="form-control pword" style="color: black;" placeholder="密码" autocomplete="off" />
					<div <c:if test="${!verityFlag }">style="display: none;"</c:if> id="verityDiv">
					<input type="text" id="verifycode" name="verifycode" class="form-control code" style="width:160px;float: left;color: black;" placeholder="验证码" maxlength="4" size="4" autocomplete="off" onkeyup="value=value.replace(/[^\d]/g,'')" />
					<img id="secimg" src="${ctx}/code.jpg"  alt="看不清楚，换一张" title="看不清楚，换一张" onclick="javascript:createCode();" style="cursor:pointer;border: 1px solid white;height:32px;width:70px;margin: 15px 0px 15px 5px;">
					</div>
					<div style="clear:both;margin-top: 10px;"></div>
                    <input type="checkbox" id="autoLogin" name="autoLogin" checked="checked" />一个月内自动登录
                </form>
                <button class="btn btn-success btn-block" onclick="return login();">登录</button>
                            <div class="alert alert-danger" style="display: none;margin-bottom: 0px;" id="loginNoteDiv" align="center"></div>
                
                </div>
             </div>
        </div>
        <div class="signup-footer">
            <div class="pull-left">
                &copy; 2015 All Rights Reserved. 
            </div>
        </div>
    </div>
<script src="${html}/w3/js/jquery-2.1.1.min.js"></script>
<script src="${html}/js/cookie.js"></script>
<script type="text/javascript">
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
