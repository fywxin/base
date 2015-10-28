<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
</head>
    
<body style="overflow-x: hidden;"> 
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>用户名：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.userName }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>真实姓名：</label>
                <div class="col-sm-4">
                    <input id="realName" name="realName" value="${item.realName }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-sm-2 control-label">联系电话：</label>
                <div class="col-sm-4">
                    <input id="phone" name="phone"  value="${item.phone }" readonly="readonly" class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">邮箱地址：</label>
                <div class="col-sm-4">
                    <input id="email" name="email"  value="${item.email }" readonly="readonly"  class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">所属部门：</label>
                <div class="col-sm-4">
                   <input id="deptName" name="deptName"  value="${deptName }" readonly="readonly"  class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">状态：</label>
                <div class="col-sm-4">
                    <c:if test="${item.status == 2}"><font color="red">禁用 </font></c:if> 
					<c:if test="${item.status == 1}">正常</c:if> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">登入IP：</label>
                <div class="col-sm-4">
                    <input id="loginIp" name="loginIp"  value="${item.loginIp }" readonly="readonly"  class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">登入时间：</label>
                <div class="col-sm-4">
                    <input id="email" name="email"  value="<fmt:formatDate value="${item.lastLoginTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"  class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">创建人：</label>
                <div class="col-sm-4">
                    <input id="loginIp" name="loginIp"  value="${creator }" readonly="readonly"  class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">创建时间：</label>
                <div class="col-sm-4">
                    <input id="email" name="email"  value="<fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"  class="form-control" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">附加信息：</label>
                <div class="col-sm-4">
                    <textarea id="addOn" name="addOn" rows="2" cols="58" readonly="readonly">${item.addOn }</textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">备注：</label>
                <div class="col-sm-4">
                    <textarea id="remark" name="remark" rows="3" cols="58" readonly="readonly">${item.remark }</textarea>
                </div>
            </div>
        </form>
     </div>
</body>
</html>