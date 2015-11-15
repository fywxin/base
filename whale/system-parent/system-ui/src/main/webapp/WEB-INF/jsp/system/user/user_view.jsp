<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
</head>
    
<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><span class="required">*</span>用户名：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="userName" name="userName" value="${item.userName }" readonly="readonly"class="form-control" style="width:250px" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><span class="required">*</span>真实姓名：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="realName" name="realName" value="${item.realName }" readonly="readonly"class="form-control" style="width:250px" required="" aria-required="true" >
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">联系电话：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="phone" name="phone"  value="${item.phone }" readonly="readonly"class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">邮箱地址：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="email" name="email"  value="${item.email }" readonly="readonly" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">所属部门：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                   <input id="deptName" name="deptName"  value="${deptName }" readonly="readonly" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">状态：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <c:if test="${item.status == 2}"><font color="red">禁用 </font></c:if> 
					<c:if test="${item.status == 1}">正常</c:if> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">登入IP：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="loginIp" name="loginIp"  value="${item.loginIp }" readonly="readonly" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">登入时间：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="email" name="email"  value="<fmt:formatDate value="${item.lastLoginTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">创建人：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="loginIp" name="loginIp"  value="${creator }" readonly="readonly" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">创建时间：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="email" name="email"  value="<fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">附加信息：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <textarea id="addOn" name="addOn" rows="2" class="form-control" style="width:250px" readonly="readonly">${item.addOn }</textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">备注：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <textarea id="remark" name="remark" rows="3" class="form-control" style="width:250px" readonly="readonly">${item.remark }</textarea>
                </div>
            </div>
             <div class="form-group" style="border-bottom:0px;margin-top:20px;">
                <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                    <button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" ><i class='fa fa-mail-reply'></i> 返回</button>
                </div>
            </div>
        </form>
     </div>
    </div>
</body>
</html>