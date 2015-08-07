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
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>基础开发平台</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${html}/ace/css/bootstrap.min.css" />
		<link rel="stylesheet" href="//cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${html}/ace/css/ace.min.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${html}/ace/css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="${html}/ace/css/ace-rtl.min.css" />

		<!--[if lte IE 9]>
		<link rel="stylesheet" href="${html}/ace/css/ace-ie.min.css" />
		<script src="${html}/ace/js/html5shiv.min.js"></script>
		<script src="${html}/ace/js/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="login-layout light-login">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class="ace-icon fa fa-leaf green"></i>
									<span class="red">Whale</span>
									<span class="white" id="id-text2">管理平台</span>
								</h1>
								<h4 class="blue" id="id-company-text">&copy; 大盗公司</h4>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="ace-icon fa fa-coffee green"></i>
												请输入登录信息
											</h4>

											<div class="space-6"></div>

											<form id="loginForm">
												<input type="hidden" id="encryptedPwd" name="encryptedPwd">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="userName" name="userName" class="form-control" placeholder="用户名" autocomplete="off" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="password" name="password" class="form-control" placeholder="登录密码" autocomplete="off" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>
													<div <c:if test="${!verityFlag }">style="display: none;"</c:if> id="verityDiv">
													<label  class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="verifycode" name="verifycode" class="col-xs-7" placeholder="验证码" maxlength="4" size="4" autocomplete="off" onkeyup="value=value.replace(/[^\d]/g,'')" />
														</span>
														<img id="secimg" src="${ctx}/code.jpg" width="120" height="32" alt="看不清楚，换一张" title="看不清楚，换一张" onclick="javascript:createCode();" style="cursor:pointer;border: 0;padding-left: 20px;">
													</label>
												</div>
												<c:if test="${autoLoginFlag }">
													<div class="clearfix">
														<input type="checkbox" id="autoLogin" name="autoLogin" checked="checked" /> 一个月内自动登录
													</div>
												</c:if>
													<div class="space"></div>
													<div class="clearfix">
														<button type="button" class="width-100  btn btn-sm btn-success" onclick="return login();">
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">立即登录</span>
														</button>
													</div>
												
													<div class="clearfix" id="loginNoteDiv" align="center">
														<h5 class="red lighter bigger" >
															<i class="ace-icon fa fa-exclamation-triangle hide"></i>
															&nbsp;<span style="display: none"></span>
														</h5>
													</div>
												</fieldset>
											</form>
										</div>

										<div class="toolbar clearfix">
											<div>
												<a href="#" data-target="#forgot-box" class="forgot-password-link">
													<i class="ace-icon fa fa-arrow-left"></i>
													忘记密码
												</a>
											</div>

											<div>
												<a href="#" data-target="#signup-box" class="user-signup-link">
													用户注册
													<i class="ace-icon fa fa-arrow-right"></i>
												</a>
											</div>
										</div>
									</div>
								</div>

								<div id="forgot-box" class="forgot-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header red lighter bigger">
												<i class="ace-icon fa fa-key"></i>
												找回密码
											</h4>

											<div class="space-6"></div>
											<p>
												请输入你的邮箱地址获取密码
											</p>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control" placeholder="邮箱" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>

													<div class="clearfix">
														<button type="button" class="width-35 pull-right btn btn-sm btn-danger">
															<i class="ace-icon fa fa-lightbulb-o"></i>
															<span class="bigger-110">发送!</span>
														</button>
													</div>
												</fieldset>
											</form>
										</div><!-- /.widget-main -->

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												返回登录页面
												<i class="ace-icon fa fa-arrow-right"></i>
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.forgot-box -->

								<div id="signup-box" class="signup-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header green lighter bigger">
												<i class="ace-icon fa fa-users blue"></i>
												新用户注册
											</h4>

											<div class="space-6"></div>
											<p> 请输入你的详细信息: </p>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control" placeholder="邮箱" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="用户名" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="确认密码" />
															<i class="ace-icon fa fa-retweet"></i>
														</span>
													</label>

													<label class="block">
														<input type="checkbox" class="ace" />
														<span class="lbl">
															点击注册按钮表示您已同意并接受
															<a >《大盗网用户使用协议》</a>
														</span>
													</label>

													<div class="space-24"></div>

													<div class="clearfix">
														<button type="reset" class="width-30 pull-left btn btn-sm">
															<i class="ace-icon fa fa-refresh"></i>
															<span class="bigger-110">重置</span>
														</button>

														<button type="button" class="width-65 pull-right btn btn-sm btn-success">
															<span class="bigger-110">注册</span>

															<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
														</button>
													</div>
												</fieldset>
											</form>
										</div>

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												<i class="ace-icon fa fa-arrow-left"></i>
												返回登录页面
											</a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!--[if !IE]> -->
		<script src="${html}/js/jquery.211.js"></script>
		<!-- <![endif]-->

		<!--[if IE]>
<script src="${html}/js/jquery.1101.js"></script>
<![endif]-->
		<script src="${html}/js/cookie.js"></script>

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${html}/ace/js/jquery.min.js'>"+"<"+"/script>");
		</script>
		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${html}/ace/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${html}/ace/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
<script type="text/javascript">
jQuery(function($) {
	 $(document).on('click', '.toolbar a[data-target]', function(e) {
		e.preventDefault();
		var target = $(this).data('target');
		$('.widget-box.visible').removeClass('visible');
		$(target).addClass('visible');
	 });
	 
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
	$("#loginNoteDiv span").html(msg).show();
	$("#loginNoteDiv i").removeClass("hide");
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
