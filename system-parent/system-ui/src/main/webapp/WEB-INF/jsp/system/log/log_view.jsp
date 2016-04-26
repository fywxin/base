<%@page import="org.whale.system.common.util.TimeUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>查看 日志</title>
<%@include file="/jsp/form.jsp" %>
</head>
    
<body>
<div class="row panel-row" id="panelDiv1" >
    <div class="panel panel-default">
        <div class="panel-heading collapsed panel-heading-style" href="#panel1" data-toggle="collapse" aria-expanded="false">
            日志详情<span class="caret pull-right toggle-caret" ></span></div>
        <div id="panel1" class="panel-collapse collapse in" aria-expanded="true" >
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="panel-body-table table table-bordered " >
                        <colgroup>
                            <col class="col-xs-1">
                            <col class="col-xs-3">
                            <col class="col-xs-1">
                            <col class="col-xs-3">
                            <col class="col-xs-1">
                            <col class="col-xs-3">
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row" style="text-align: right;">对象</th>
                            <td>${item.clazz }#${item.method}</td>
                            <th scope="row" style="text-align: right;">模块</th>
                            <td>${item.module }</td>

                            <th scope="row" style="text-align: right;">处理结果</th>
                            <td>
                                <c:if test="${item.rs == 0}"><button type='button' class='btn btn-success btn-ss' style='cursor:default'><i class='fa fa-check'></i> 成功</button></c:if>
                                <c:if test="${item.rs == 1}"><button type='button' class='btn btn-warning btn-ss' style='cursor:default'><i class='fa fa-exclamation'></i> 系统异常</button></c:if>
                                <c:if test="${item.rs == 2}"><button type='button' class='btn btn-warning btn-ss' style='cursor:default'><i class='fa fa-check'></i> OrmException</button></c:if>
                                <c:if test="${item.rs == 3}"><button type='button' class='btn btn-info btn-ss' style='cursor:default'><i class='fa fa-minus'></i> 运行时异常</button></c:if>
                                <c:if test="${item.rs == 4}"><button type='button' class='btn btn-danger btn-ss' style='cursor:default'><i class='fa fa-times'></i> 业务异常</button></c:if>
                                <c:if test="${item.rs == 10}"><button type='button' class='btn btn-info btn-ss' style='cursor:default'><i class='fa fa-info'></i> 未知异常</button></c:if>
                                耗时 ${item.costTime } (ms)</td>
                        </tr>
                        <tr>
                            <th scope="row">操作人</th>
                            <td>${item.userName }</td>
                            <th scope="row">IP</th>
                            <td>${item.ip }</td>
                            <th scope="row">操作时间</th>
                            <td>
                                ${createTime }
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">日志描述</th>
                            <td colspan="5">${item.info }</td>
                        </tr>
                        <tr>
                            <th scope="row">请求参数</th>
                            <td colspan="5">
                                ${item.params }
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>