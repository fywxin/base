<%@page import="org.whale.system.base.UserContext"%>
<%@page import="org.whale.system.cache.service.DictCacheService"%>
<%@page import="org.whale.system.common.constant.DictConstant"%>
<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="zh-CN" style="overflow: hidden;">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${resource}/widget/styles/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${resource}/widget/jqwidgets/styles/jqx.base.css" type="text/css" /> 
	<link href="${resource}/widget/jqwidgets/styles/jqx.bootstrap.css" rel="stylesheet"> 
	<link href="${resource}/widget/jqwidgets/styles/jqx.energyblue.css" rel="stylesheet"> 
    
	<style type="text/css">
	.sidebar {
width: 190px;
float: left;
position: relative;
border: 1px solid #ccc;
border-width: 0 1px 0 0;
background-color: #f2f2f2;
}
.main-content {
margin-left: 190px;
margin-right: 0;
margin-top: 0;
min-height: 100%;
padding: 0;
}
.breadcrumbs {
position: relative;
border-bottom: 1px solid #e5e5e5;
background-color: #f5f5f5;
min-height: 41px;
line-height: 40px;
padding: 0 12px 0 0;
display: block;
}
.page-content {
background: #fff;
margin: 0;
padding: 8px 20px 24px;
}
.navbar {
margin-left: 0;
margin-right: 0;
border: 0;
-webkit-box-shadow: none;
box-shadow: none;
border-radius: 0;
margin: 0;
padding-left: 0;
padding-right: 0;
min-height: 45px;
position: relative;
background: #438eb9;
}
	</style>
</head>
<body style="overflow: hidden;">
      <div class="navbar" >
        
    </div>
    
	<div style="position: relative;">
		<div class="sidebar" style="height: 800px;">
			<div id="jqxNavigationBar" >
                    <div>
                        Fantasy</div>
                    <div>
                        <ul>
                            <li><a onclick="addTab()">J. R. R. Tolkien</a></li>
                            <li>Terry Brooks</li>
                            <li>J. K. Rowling</li>
                        </ul>
                    </div>
                    <div>
                        Science Fiction</div>
                    <div>
                        <ul>
                            <li>H. G. Wells</li>
                            <li>Simon R. Green</li>
                        </ul>
                    </div>
                    <div>
                        Horror</div>
                    <div>
                        <ul>
                            <li>H. P. Lovecraft</li>
                            <li>Stephen King</li>
                        </ul>
                    </div>
                </div>
			
		</div>
		<div class="main-content" style="">
			<div id='jqxTabs' style="float: left;overflow:hidden; ">
            <ul style="margin-left:10px;" id="unorderedList">
                <li>Node.js</li>
                <li>Active Server Pages</li>
                <li canselect='false' hasclosebutton='false'>Add New Tab</li>
            </ul>
            <div>
                Node.js is an event-driven I/O server-side JavaScript environment based on V8. It
                is intended for writing scalable network programs such as web servers. It was created
                by Ryan Dahl in 2009, and its growth is sponsored by Joyent, which employs Dahl.
                Similar environments written in other programming languages include Twisted for
                Python, Perl Object Environment for Perl, libevent for C and EventMachine for Ruby.
                Unlike most JavaScript, it is not executed in a web browser, but is instead a form
                of server-side JavaScript. Node.js implements some CommonJS specifications. Node.js
                includes a REPL environment for interactive testing.
            </div>
            <div style="width: 100%;height: 100%;">
               <iframe frameborder="0" src="http://www.tao123.com/" scrolling="auto" width="100%" height="850px"></iframe>
            </div>
            <div>
            </div>
        </div>

		</div>
		
	</div>

<script type="text/javascript" src="${resource}/widget/scripts/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${resource}/widget/scripts/bootstrap.min.js"></script>
<script type="text/javascript" src="${resource}/widget/jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="${resource}/widget/jqwidgets/jqxnavigationbar.js"></script>
<script type="text/javascript" src="${resource}/widget/jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="${resource}/widget/jqwidgets/jqxsplitter.js"></script>
<script type="text/javascript" src="${resource}/widget/jqwidgets/jqxtabs.js"></script>


<script type="text/javascript">
var tabs = null;
$(document).ready(function(){
	 $("#jqxNavigationBar").jqxNavigationBar({  height: "100%", width: "100%", expandMode: "singleFitHeight", theme: 'energyblue' });
	 tabs = $('#jqxTabs').jqxTabs({ height: '100%', width: '99%',  showCloseButtons: true, theme: 'energyblue'});
	 
	 $('#jqxTabs').on('selected', function (event) {
         displayEvent(event);
     });
});

function addTab(){
	tabs.jqxTabs('addLast', 'tabTitle', '<iframe frameborder="0" src="http://www.tao123.com/" scrolling="auto" width="100%" height="850px"></iframe>');
}
</script>
</body>
</html>
