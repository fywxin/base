<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="org.whale.system.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	out.write( new ActionEnter(request).exec() );
	
%>