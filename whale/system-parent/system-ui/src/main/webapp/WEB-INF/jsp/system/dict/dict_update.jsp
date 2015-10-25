<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dict/doUpdate', onSuccess: function(){
		window.top.layer.msg("修改字典成功！", {time: 2000});
		window.parent.location.reload();
	}}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"dictName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"dictCode": {
				validIllegalChar: true,
				maxlength: 32,
				required: true
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
        	<input type="hidden" id="dictId" name="dictId" value="${item.dictId }"/>
			<input type="hidden" id="status" name="status" value="${item.status }"/>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>字典名称：</label>
                <div class="col-sm-4">
                    <input id="dictName" name="dictName" value="${item.dictName }" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>字典编码：</label>
                <div class="col-sm-4">
                    <input id="dictCode" name="dictCode" value="${item.dictCode }" class="form-control" required="" aria-required="true" readonly="readonly" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">备注：</label>
                <div class="col-sm-4">
                    <textarea id="remark" name="remark" rows="3" cols="58" >${item.remark }</textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12 col-sm-offset-2">
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                </div>
            </div>
		</form>
	</div>
</body>
</html>
