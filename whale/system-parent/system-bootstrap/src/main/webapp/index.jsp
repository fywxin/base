<%@page import="org.whale.system.base.UserContext"%>
<%@page import="org.whale.system.cache.service.DictCacheService"%>
<%@page import="org.whale.system.common.constant.DictConstant"%>
<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- http://runjs.cn/code/cy7frwm4 -->
<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="ie ie6" lang="zh-CN"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="zh-CN"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="zh-CN"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	
	<title>基础开发平台</title>
<%-- 	<link type="text/css" rel="stylesheet" href="${resource}/ui/css/bootstrap-theme.min.css" /> --%>
<%-- 	<link type="text/css" rel="stylesheet" href="${resource}/ui/css/bootstrap.min.css" /> --%>
	<link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
	<link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap-theme.min.css" rel="stylesheet">
	
	<script src="${resource}/com/jquery.js" type="text/javascript"></script>
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
      <script src="${resource}/com/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
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

<style type="text/css">
body{background: #444;font-family:"宋体";}
.loginBox{width:420px;height:300px;padding:0 20px;border:1px solid #fff; color:#000; margin-top:40px; border-radius:8px;background: white;box-shadow:0 0 15px #222; background: -moz-linear-gradient(top, #fff, #efefef 8%);background: -webkit-gradient(linear, 0 0, 0 100%, from(#f6f6f6), to(#f4f4f4));font:11px/1.5em 'Microsoft YaHei' ;position: absolute;left:50%;top:50%;margin-left:-210px;margin-top:-165px;}
.loginBox h2{height:45px;font-size:20px;font-weight:normal;}
.loginBox .left{border-right:1px solid #ccc;height:100%;padding-right: 20px; }
.regBtn{margin-top:21px;}
.select{opacity: 1.0 }
.unselect{opacity: 0.2}

</style>

<script type="text/javascript">
$(document).ready(function(){
    $(".btn-group").on("click", function(){
        $(this).find(".btn").toggleClass("select").toggleClass("unselect");  
    });

});
</script>
</head>
<div class="container">
  <div class="loginBox row">
  		<h2 class="text-center">后台登录</h2>
		<form id="wyccn" name="wyccn" action="http://jx.5idf.com" method="post" class="form-horizontal">
		  <div class="form-group has-success">
		    <label for="nick_name" class="col-sm-2 col-md-2 control-label">用户名</label>
		    <div class="col-sm-10 col-md-10">
		      <input type="text" class="form-control" id="userName" name="userName" placeholder="用户名" value="" autocomplete="off">
		    </div>
		  </div>
		  <div class="form-group has-success">
		    <label for="user_password" class="col-sm-2 col-md-2 control-label">密码</label>
		    <div class="col-sm-10 col-md-10">
		      <input type="password" class="form-control" id="password" name="password" placeholder="密码" autocomplete="off">
		    </div>
		  </div>
		  <div class="form-group has-success">
		  	<label for="user_password" class="col-sm-2 col-md-2 control-label">验证码<button class="btn btn-default" ><span class="glyphicon glyphicon-refresh" aria-hidden="true" ></span></button></label>
	  	  	<div class="col-sm-4 col-md-4">
		      <input type="password" class="form-control" id="password" name="password" placeholder="验证码" autocomplete="off" >
		    </div>
		    
		  	<label for="user_password" class="col-sm-2 col-md-2 control-label" style="padding-left:0px;">自动登录</label>
	  	  	<div class="btn-group col-sm-4 col-md-4" data-toggle="buttons-radio">
	  	  		<button type="button" class="btn btn-success select">On</button>
			    <button type="button" class="btn btn-danger unselect">Off</button>
			</div>
		  </div>
	  	  <div class="form-group">
	  	  	<div class="col-sm-offset-4 col-sm-10" style="color: #990033;"></div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-4 col-sm-4 col-md-4">
		      	<button class="btn btn-primary" data-loading-text="正在登录..."  type="submit"> 立即登录<span class="glyphicon glyphicon-log-in" aria-hidden="true" style="margin-left:10px;"></span> </button>
		    </div>
		  </div>
  		</form>
	</div>
</div>
<!--.content-->
	<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
</body>
</html>