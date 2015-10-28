<%@page import="org.whale.system.common.util.TimeUtil"%>
<%@page import="org.whale.system.domain.Log"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>查看 日志</title>
<%@include file="/jsp/form.jsp" %>
</head>
    
<body style="overflow-x: hidden;"> 
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-2 control-label">应用：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.appId }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">处理结果：</label>
                <div class="col-sm-4">
                    <c:if test="${item.rsType == 1 }"><span class='sgreen'>正常</span></c:if>
					<c:if test="${item.rsType == 2 }"><span class='sred'>系统异常</span></c:if>
					<c:if test="${item.rsType == 3 }"><span class='sorange'>OrmException</span></c:if>
					<c:if test="${item.rsType == 4 }"><span class='sorange'>运行时异常</span></c:if>
					<c:if test="${item.rsType == 5 }"><span class='sred'>业务异常</span></c:if>
					<c:if test="${item.rsType == 0 }"><span class='sgray'>未知异常</span></c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">操作类型：</label>
                <div class="col-sm-4">
                    <c:if test="${item.opt == 'save' }">新增</c:if>
					<c:if test="${item.opt == 'update' }">修改</c:if>
					<c:if test="${item.opt == 'delete' }">删除</c:if>
					<c:if test="${item.opt == 'saveBatch' }">批量新增</c:if>
					<c:if test="${item.opt == 'updateBatch' }">批量修改</c:if>
					<c:if test="${item.opt == 'deleteBatch' }">批量删除</c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">对象名称：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.cnName }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">表名称：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.tableName }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">ip地址：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.ip }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">操作人：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${userName }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">创建时间：</label>
                <div class="col-sm-4">
                   
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">uri：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.uri }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">调用链顺序：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.callOrder }" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">方法耗时：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.methodCostTime}(ms)" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">总耗时：</label>
                <div class="col-sm-4">
                    <input id="userName" name="userName" value="${item.costTime}(ms)" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">sql：</label>
                <div class="col-sm-4">
                    <div style="height:90px;" class="my_textAreaDiv">${item.sqlStr}</div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">请求参数：</label>
                <div class="col-sm-4">
                    <div style="height:40px;" class="my_textAreaDiv">${item.argStr}</div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">处理结果：</label>
                <div class="col-sm-4">
                   <input id="userName" name="userName" value="${item.rsType}" readonly="readonly" class="form-control" required="" aria-required="true" >
                </div>
            </div>
			</form>
     </div>
</body>
</html>