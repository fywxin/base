<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">

    <title>基础开发框架</title>

    <link href="${html}/www/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="${html}/www/font-awesome/css/font-awesome.css?v=4.3.0" rel="stylesheet">
    
    <link href="${html}/www/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
    <link href="${html}/www/css/plugins/jqgrid/ui.jqgrid.css" rel="stylesheet">
    
    <link href="${html}/www/css/animate.css" rel="stylesheet">
    <link href="${html}/www/css/style.css?v=2.2.0" rel="stylesheet">
    <link href="${html}/css/my.css" rel="stylesheet">
    <!--[if !IE]> -->
    <script src="${html}/www/js/jquery-2.1.1.min.js"></script>
    <!-- <![endif]-->
    <!--[if IE]>
	<script src="${html}/www/js/jquery.1101.js"></script>
	<![endif]-->
	<script src="${html}/www/js/bootstrap.min.js?v=3.4.0"></script>
	<script type="text/javascript">
$(function (){
	//页面跳转时，保持菜单栏状态
	if($.cookie("mini-navbar") == "true"){
		$("body").addClass("mini-navbar");
	}else{
		$("body").removeClass("mini-navbar");
	}
	$(window).resize(function(){
		try{
			$("#gridTable").jqGrid('setGridWidth', $("#navbarDiv").width()-20).jqGrid('setGridHeight', $.h()-142-$("#queryForm").height());
		}catch(e){}
	});
});
function afterSmoothlyMenu(){
	try{
		setTimeout(function() {$("#gridTable").jqGrid('setGridWidth', $("#navbarDiv").width()-20);}, 600);
	}catch(e){}
	$.cookie("mini-navbar", $("body").hasClass("mini-navbar"));
}
</script>
</head>
<body style="overflow: hidden;">
    <div id="wrapper">
        <%@include file="/html/jsp/nav-sidebar.jsp" %>
        <%@include file="/html/jsp/nav-topbar.jsp" %>
       