<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <c:if test="${uc.isMock}">
                        <i class="fa fa-exclamation-triangle" style="position:relative;top: 3px;color: red;font-size: larger;" title="模拟用户">${uc.realName } [模拟]</i>
                    </c:if>
                    <c:if test="${!uc.isMock}">
                        <span class="glyphicon glyphicon-user padding-right-small" style="position:relative;top: 3px;">${uc.realName }</span>
                    </c:if>
                    <i class="fa fa-caret-down"></i>
                </a>
              <ul class="dropdown-menu">
                  <c:if test="${uc.isMock}">
                      <li><a href="javascript:;" onclick="admin()">返回管理员</a></li>
                      <li class="divider"></li>
                  </c:if>

                <li><a href="javascript:;">个人资料</a></li>
                <li class="divider"></li>
                <li><a href="javascript:;" onclick="changePassword()">修改密码</a></li>
                <li class="divider"></li>
                <li><a href="javascript:;" onclick="loginOut()">安全退出</a></li>
              </ul>
            </li>
          </ul>

        </div>
      </div>
    <div class="sidebar-nav" id="menuDiv" style="width: 200px;overflow: auto;">
    	<ul>${uc.ext.menuStr }</ul>
    </div>
    <style type="text/css">
        .content-tabs {
            position: relative;
            height: 42px;
            background: #fafafa;
            line-height: 40px;
            border-bottom: solid 2px #2f4050;
        }
        .content-tabs .roll-left {
            margin-left: 10px;
            left: 0;
            border-right: solid 1px #eee;
        }
        .content-tabs .roll-nav, .page-tabs-list {
            position: absolute;
            width: 40px;
            height: 40px;
            text-align: center;
            color: #999;
            z-index: 2;
            top: 0;
        }
        nav.page-tabs .page-tabs-content {
            float: left;
        }
        .content-tabs button {
            background: #fff;
            border: 0;
            height: 40px;
            width: 40px;
            outline: 0;
        }
        nav.page-tabs {
            margin-left: 40px;
            width: 100000px;
            height: 40px;
            overflow: hidden;
        }
        article, aside, details, figcaption, figure, footer, header, hgroup, main, menu, nav, section, summary {
            display: block;
        }

        .roll-right.J_tabRight {
            right: 140px;
        }
        .content-tabs .roll-right {
            right: 0;
            border-left: solid 1px #eee;
        }
        .content-tabs .roll-nav, .page-tabs-list {
            position: absolute;
            width: 40px;
            height: 40px;
            text-align: center;
            color: #999;
            z-index: 2;
            top: 0;
        }
        .content-tabs button {
            background: #fff;
            border: 0;
            height: 40px;
            width: 40px;
            outline: 0;
        }

        .page-tabs a {
            color: #999;
            display: block;
            float: left;
            border-right: solid 1px #eee;
            padding: 0 15px;
        }
        .J_menuTab {
            -webkit-transition: all .3s ease-out 0s;
            transition: all .3s ease-out 0s;
        }
        a {
            cursor: pointer;
        }
        a {
            color: #337ab7;
            text-decoration: none;
        }
        a {
            background-color: transparent;
        }
        .roll-right.btn-group {
            right: 60px;
            width: 80px;
            padding: 0;
        }
        .content-tabs .roll-right {
            right: 0;
            border-left: solid 1px #eee;
        }
        .content-tabs .roll-nav, .page-tabs-list {
            position: absolute;
            width: 40px;
            height: 40px;
            text-align: center;
            color: #999;
            z-index: 2;
            top: 0;
        }
        .btn-group, .btn-group-vertical {
            position: relative;
            display: inline-block;
            vertical-align: middle;
        }

        #content-main {
            height: calc(100% - 140px);
            overflow: hidden;
        }

        .page-tabs a.active {
            background: #2f4050;
            color: #a7b1c2;
        }
    </style>

    <div class="content" style="padding: 0px 5px 0px 5px;overflow: hidden;margin-left: 200px;">
        <div class="row content-tabs">
            <button class="roll-nav roll-left"><i class="fa fa-backward"></i>
            </button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content" style="margin-left: 10px;" id="pageTabs">
                    <a href="javascript:;" class="J_menuTab" data-id="index_v1.html">首页</a>
                    <a href="javascript:;" class="J_menuTab" data-id="layouts.html">布局
                        <i class="fa fa-times-circle"></i></a>
                    <a href="javascript:;" class="J_menuTab" data-id="graph_flot.html">
                        Flot <i class="fa fa-times-circle"></i>
                    </a>
                    <a href="javascript:;" class="J_menuTab active" data-id="mail_compose.html">
                        写信 <i class="fa fa-times-circle"></i>
                    </a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
            </button>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe class="J_iframe" id="f0" width="100%" height="100%" frameborder="0" src="${ctx}/user/goTree"></iframe>
        </div>
    </div>
    
<script src="${html }/js/jquery-1.11.1.min.js"></script>
<script src="${html }/js/cookie.js"></script>
<script src="${html }/ui/bootstrap/js/bootstrap.js"></script>
<script src="${html }/plugins/layer/layer.min.js"></script>
<script src="${html }/js/fun.js"></script>
<script src="${html }/js/main/index.js"></script>
<script type="text/javascript">
var ctx="${ctx}";
<c:if test="${uc.isMock}">
    function admin(){
        $.get("${ctx}/adminLogin", function (obj) {
            if(obj.rs){
                window.location.reload();
            }else{
                $.alert(obj.msg);
            }
        })
    }
</c:if>

    function openTab(url, name){
        $("#pageTabs").append("<")
    }
</script>
</body>
</html>