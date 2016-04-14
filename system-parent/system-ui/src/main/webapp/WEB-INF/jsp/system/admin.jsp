<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'ForbidVisitException.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">

div.but {
	background: #d5e3f7;
	padding: 5px;
	color: #3372bc;
	width: 158px;
	margin:5px 50px 0px 20px;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
	float:left;
}
</style>

<script language="javascript" src="${resource}/tool/jquery.js"></script>
<script type="text/javascript">
    	
    	function goTop(){
    		window.top.location.href ="${ctx}/";
    	}
    </script>
</head>

<body>
	<div align="center">
	<div class="but">
		<a href="${ctx }/doFlushAuthUris">刷新权限URL信息</a>
	</div>
	<div class="but">
		<a href="${ctx }/doFlushDictCache">刷新字典缓存</a>
	</div>
	</div>
</body>
</html>
