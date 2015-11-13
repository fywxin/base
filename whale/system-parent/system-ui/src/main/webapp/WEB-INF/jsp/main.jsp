<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">

    <title>基础开发框架 - 主页</title>

    <link href="${html }/ui/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${html }/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="${html }/ui/stylesheets/theme.css" rel="stylesheet">
    <link href="${html }/ui/stylesheets/premium.css" rel="stylesheet">
    
	<style type="text/css">
	body {font-family: Helvetica, Georgia, Arial, sans-serif, 宋体;font-size: 13px;_font-size: 12px}
	.navbar-default .navbar-brand, .navbar-default .navbar-brand:hover {color: #fff;}
    </style>
    <!--[if lt IE 9]>
      <script src="${html }/js/html5shiv.min.js"></script>
    <![endif]-->
</head>

<body class=" theme-blue" style="overflow: hidden;">
	<!--[if lt IE 7 ]> <body class="ie ie6" style="overflow: hidden;"> <![endif]-->
  	<!--[if IE 7 ]> <body class="ie ie7 " style="overflow: hidden;"> <![endif]-->
  	<!--[if IE 8 ]> <body class="ie ie8 " style="overflow: hidden;"> <![endif]-->
  	<!--[if IE 9 ]> <body class="ie ie9 " style="overflow: hidden;"> <![endif]-->
  	<!--[if (gt IE 9)|!(IE)]><!--> 
  	<!--<![endif]-->

    <div class="navbar navbar-default" role="navigation" >
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="" href="index.html"><span class="navbar-brand"><span class="fa fa-paper-plane"></span> Whale</span></a></div>
        <div class="navbar-collapse collapse" style="height: 1px;">
          <ul id="main-menu" class="nav navbar-nav navbar-right">
            <li class="dropdown hidden-xs">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <span class="glyphicon glyphicon-user padding-right-small" style="position:relative;top: 3px;"></span> ${uc.realName }
                    <i class="fa fa-caret-down"></i>
                </a>
              <ul class="dropdown-menu">
                <li><a href="./">个人资料</a></li>
                <li class="divider"></li>
                <li><a href="./">修改密码</a></li>
                <li class="divider"></li>
                <li><a href="#" onclick="loginOut()">安全退出</a></li>
              </ul>
            </li>
          </ul>

        </div>
      </div>
    <div class="sidebar-nav" id="menuDiv" style="width: 200px;overflow: auto;">
    	<ul>${uc.customDatas.menuStr }</ul>
    </div>
    <div class="content" style="padding: 3px 5px 0px 3px;overflow: hidden;margin-left: 200px;">
    	<ul class="nav nav-tabs" id="topTab">
    		<li id="liMain" onclick="goTab(1)" class="active"><a href="#"></a></li>
    		<li id="liSub" onclick="goTab(2)" style="display:none;"><a href="#"></a></li>
		</ul>
        <iframe id="frameMain" name="frameMain" src="" style="overflow:hidden;" scrolling="no" frameborder="no" width="100%" ></iframe>
        <iframe id="frameSub" name="frameSub" src="" style="overflow:hidden;display: none;" scrolling="no" frameborder="no" width="100%" ></iframe>
    </div>
    
<script src="${html }/js/jquery-1.11.1.min.js"></script>
<script src="${html }/js/cookie.js"></script>
<script src="${html }/ui/bootstrap/js/bootstrap.js"></script>
<script src="${html }/plugins/layer/layer.min.js"></script>
<script src="${html }/js/fun.js"></script>
<script src="${html }/js/main/index.js"></script>
<script type="text/javascript">
var ctx="${ctx}";
</script>
</body>
</html>