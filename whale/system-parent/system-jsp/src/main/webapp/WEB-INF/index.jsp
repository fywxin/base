<%@page import="org.whale.system.base.UserContext"%>
<%@page import="org.whale.system.cache.service.DictCacheService"%>
<%@page import="org.whale.system.common.constant.DictConstant"%>
<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>基础开发平台</title>
<link type="text/css" rel="stylesheet" href="${resource}/css/login.css" />
<script src="${resource}/tool/jquery.js" type="text/javascript"></script>
<script src="${resource}/tool/cookie.js" type="text/javascript"></script>

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

</head>
<script language="javascript">
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
		$("#errorInfoSpan").html("请输入用户名").css("visibility","visible");
		return false;
	}
	if($("#password").val() ==""){
<c:if test="${autoLoginFlag }">
		if($("#encryptedPwd").val() !=""){
			return true;
		}
</c:if>
		$("#errorInfoSpan").html("请输入密码").css("visibility","visible");
		return false;
	}
<c:if test="${verityFlag }">
	if($("#verifycode").val() ==""){
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
		    		if($("#autoLogin").attr('checked') == 'checked'){
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
<body>
<div class="wrap bg-big">
  
  <div class="box-emptylog"></div>
  
  <div class="content wrap-full">
    <div class="box clear">
      <div class="loginbox bg-loginbox fr">
        <div class="lgn-tit" align="center">创智财富业务运营管理平台</div>
        <form class="form-horizontal" id="loginForm">
          <p class="form-ver" id="errorInfoSpan" style="visibility: hidden;"></p>
          <div class="control-group clear">
            <label class="control-label fl" for="inputPassword">用户名</label>
            <div class="controls fr">
              <input id="userName" name="userName" class="ipt ipt-lgn ipt-width195" placeholder="请在此输入您的用户名" type="text" autocomplete="off">
            </div>
          </div>
          <div class="control-group clear">
            <label class="control-label fl" for="inputPassword">密　码</label>
            <div class="controls fr">
              <input id="password" name="password" class="ipt ipt-lgn ipt-width195" placeholder="请在此输入密码"  type="password" autocomplete="off">
            </div>
          </div>
<c:if test="${verityFlag }">
          <div class="control-group clear">
            <label class="control-label fl" for="inputPassword">验证码</label>
            <div class="controls fr">
              <input id="verifycode" name="verifycode" class="ipt ipt-lgn ipt-width116" placeholder="请在此输入验证码"  type="text" maxlength="4" size="4" style="width:60px" onkeyup="value=value.replace(/[^\d]/g,'')">
              <a class="pic-ver radius2px" href="#"><img id="secimg" class="radius2px" src="bg_big.jpg" width="72" height="31" alt="" title="看不清楚，换一张" onclick="javascript:createCode();" style="cursor:pointer;vertical-align: middle;border: 0;"></a>
            </div>
          </div>
</c:if>
<c:if test="${autoLoginFlag }">
          <div class="control-group clear" style="height:16px;">
            <label class="control-label fl" for="inputPassword"></label>
            <div class="controls fr">
              <label><input type="checkbox" id="autoLogin" name="autoLogin" />下次自动登录</label>
            	<input type="hidden" id="encryptedPwd" name="encryptedPwd" />
            </div>
          </div>
</c:if>
          <div class="control-group clear" style="padding-top: 10px;">
            <label class="control-label fl" for="inputPassword"></label>
            <div class="controls fr" style="height:40px;">
              <input class="btn btn-blue" type="button" value="立即登录" onclick="return login();">
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
    
  <div class="footer wrap-full">
    <p>© 版权所有all　rights　reserved　2013-2014　闽ICP备08103886号</p>
  </div>
</div>
</body>
</html>