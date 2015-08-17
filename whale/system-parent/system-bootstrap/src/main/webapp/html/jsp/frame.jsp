<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">

    <title>基础开发框架</title>

    <link href="${html}/www/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="${html}/www/font-awesome/css/font-awesome.css?v=4.3.0" rel="stylesheet">
    <link href="${html}/www/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
    <link href="${html}/www/css/plugins/jqgrid/ui.jqgrid.css" rel="stylesheet">
    <link href="${html}/www/css/animate.css" rel="stylesheet">
    <link href="${html}/www/css/style.css?v=2.2.0" rel="stylesheet">
    <link href="${html}/css/my.css" rel="stylesheet">
    
    <script src="${html}/www/js/jquery-2.1.1.min.js"></script>
	<script src="${html}/www/js/bootstrap.min.js?v=3.4.0"></script>
	<script src="${html}/www/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="${html}/www/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="${html}/www/js/plugins/jqgrid/i18n/grid.locale-cn.js"></script>
	<script src="${html}/www/js/plugins/jqgrid/jquery.jqGrid.min.js"></script>
	<script src="${html}/www/js/hplus.js?v=2.2.0"></script>
	<script src="${html}/www/js/plugins/pace/pace.min.js"></script>
	<script src="${html}/www/js/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script src="${html}/js/cookie.js"></script>	
	<script src="${html}/js/fun.js"></script>
</head>
<body style="overflow: hidden;">
    <div id="wrapper">
        <%@include file="/html/jsp/nav-sidebar.jsp" %>
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom" id="navbarDiv">
                <%@include file="/html/jsp/nav-topbar.jsp" %>
            </div>