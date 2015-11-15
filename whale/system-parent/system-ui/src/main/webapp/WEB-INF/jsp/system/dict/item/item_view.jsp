<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
$(function() {
	$("input, textarea").attr("readonly", "readonly");
});
</script>

</head>
    
<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row">
        <form class="form-horizontal m-t" id="dataForm">
			<input type="hidden" id="dictId" name="dictId" value="${item.dictId }" />
			<input type="hidden" id="dictItemId" name="dictItemId" value="${item.dictItemId }" />
			<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">所属字典：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="dictName" name="dictName" class="form-control" style="width:250px" value="${dictName }" readonly="readonly" >
                </div>
            </div>
			<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>元素名称：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="itemName" name="itemName" value="${item.itemName }" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">元素编码：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="itemCode" name="itemCode" value="${item.itemCode }" class="form-control" style="width:250px" readonly="readonly" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">元素值：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="itemVal" name="itemVal" value="${item.itemVal }" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">元素扩展值：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="itemValExt" name="itemValExt" value="${item.itemValExt }" class="form-control" style="width:250px" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">排列顺序：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <input id="orderNo" name="orderNo" value="${item.orderNo }" class="form-control" style="width:250px"  value="${nextNum }" maxlength="2" onkeyup="value=value.replace(/[^\d]/g,'')">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label">备注：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <textarea id="remark" name="remark" rows="3"  class="form-control" style="width:250px">${item.remark }</textarea>
                </div>
            </div>
		</form>
	</div>
</div>
</body>
</html>
