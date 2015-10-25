<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/dict/doSave', onSuccess: function(){
		window.top.layer.msg("添加字典成功！", {time: 2000});
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
	<ul class="nav nav-tabs" id="topTab">
    	<li><a href="#" onclick="go('${ctx}/dict/goList')">查询字典</a></li>
    	<li class="active"><a href="#"><i class="fa fa-plus"></i> 新增字典</a></li>
	</ul>
	
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row" style="margin:10px 20px;">
        <form class="form-horizontal m-t" id="dataForm">
            
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>字典名称：</label>
                <div class="col-sm-4">
                    <input id="dictName" name="dictName" class="form-control" required="" aria-required="true" >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="required">*</span>字典编码：</label>
                <div class="col-sm-4">
                    <input id="dictCode" name="dictCode" class="form-control" required="" aria-required="true">
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
                    <button class="btn btn-success" type="button" id="continueBut" onclick="go('${ctx}/dept/goSave?pid=${pid }');" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
                </div>
            </div>
		</form>
	</div>
</body>
</html>
