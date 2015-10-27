<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dictItem/doSave', onSuccess: function(){
		window.top.layer.msg("添加字典成功！", {time: 2000});
		window.parent.location.reload();
	}});  
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"itemName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"itemCode": {
				validIllegalChar: true,
				maxlength: 32,
				required: true
			},
			"itemVal": {
				maxlength: 1024
			},
			"itemValExt": {
				maxlength: 1024
			},
			"orderNo": {
				maxlength: 5
			},
			"remark":{
				validIllegalChar: true,
				maxlength: 500
			}
		}
	});
});

</script>

</head>
    

<body style="overflow-x: hidden;"> 
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
			<input type="hidden" id="dictId" name="dictId" value="${dictId }" />
			<div class="form-group">
                <label class="col-sm-2 control-label">所属字典：</label>
                <div class="col-sm-4">
                    <input id="dictName" name="dictName" class="form-control" value="${dictName }" readonly="readonly" >
                </div>
            </div>
			<div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>元素名称：</label>
                <div class="col-sm-4">
                    <input id="itemName" name="itemName" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>元素编码：</label>
                <div class="col-sm-4">
                    <input id="itemCode" name="itemCode" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">元素值：</label>
                <div class="col-sm-4">
                    <input id="itemVal" name="itemVal" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">元素扩展值：</label>
                <div class="col-sm-4">
                    <input id="itemValExt" name="itemValExt" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">排列顺序：</label>
                <div class="col-sm-4">
                    <input id="orderNo" name="orderNo" class="form-control" value="${nextNum }" maxlength="5" onkeyup="value=value.replace(/[^\d]/g,'')">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">备注：</label>
                <div class="col-sm-4">
                    <textarea id="remark" name="remark" rows="3" cols="58" ></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12 col-sm-offset-2">
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                    <button class="btn btn-success" type="button" id="continueBut" onclick="window.location.reload();" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
                </div>
            </div>
		</form>
	</div>
</body>
</html>
