<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
</head>
    
<body>
<div class="row" style="padding: 10px 30px;">
    <div class="panel panel-default">
        <div class="panel-heading collapsed" href="#infoDiv"  data-toggle="collapse" aria-expanded="false" style="cursor: pointer;margin-top: 0;font-size: 16px;padding: 7px 15px;">
            	用户详情
        </div>
        <div id="infoDiv" class="panel-collapse collapse in" aria-expanded="true" style="">
            <div class="panel-body">
            	<div class="table-responsive">
    <table class="table table-bordered">
        <colgroup>
            <col class="col-xs-1">
            <col class="col-xs-5">
            <col class="col-xs-1">
            <col class="col-xs-5">
        </colgroup>
        <tbody>
        <tr>
            <th scope="row">
                订单编号
            </th>
            <td>201512251134131279</td>
            <th scope="row">
                商品名称
            </th>
            <td>6094.00金 ＝ 590.00元 【绿色商品，安全迅速】</td>
        </tr>
        <tr>
            <th scope="row">
                商户订单编号
            </th>
            <td>JS201512254170366040</td>
            <th scope="row">
                订单状态
            </th>
            <td>已支付</td>
        </tr>
        <tr>
            <th scope="row">
                商品编号
            </th>
            <td>132000015343</td>
            <th scope="row">
                商品数量
            </th>
            <td>1</td>
        </tr>
        <tr>
            <th scope="row">
                商品类型
            </th>
            <td>游戏币</td>
            <th scope="row">
                商品单价
            </th>
            <td>0.01</td>
        </tr>
        <tr>
            <th scope="row">
                交易类型
            </th>
            <td>寄售交易</td>
            <th scope="row">
                交易方式
            </th>
            <td>0</td>
        </tr>
        <tr>
            <th scope="row">
                	下单时间
            </th>
            <td>2015-12-25 11:34:12</td>
            <th scope="row">
                	商品来源
            </th>
            <td>5173</td>
        </tr>
        <tr>
            <th scope="row">
                	订单失效时间
            </th>
            <td></td>
            <th scope="row">
                	发货客服
            </th>
            <td>331605173</td>
        </tr>
        </tbody>
    </table>
</div>
            </div>
        </div>
    </div>
</div>
                            
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