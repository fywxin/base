<%@page import="org.whale.system.controller.ueditor.ActionEnter"%>
<%@page import="org.springframework.web.multipart.MultipartHttpServletRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "multipart/form-data");
	
	MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	
	String rs = new ActionEnter(request).exec();
	out.write(rs);
%>