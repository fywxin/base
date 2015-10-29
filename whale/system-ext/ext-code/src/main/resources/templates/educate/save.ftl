<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>新增 ${domain.domainCnName}</title>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
    $.save({'url':'${"$"+"{ctx}"}/${domain.domainName?uncap_first}/doSave'});
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
<#list domain.formAttrs as fAttr>
			"${fAttr.name}": {
				<#if fAttr.type == "String">validIllegalChar: true,</#if>
				<#if !fAttr.isNull >required: true</#if>
			}<#if fAttr_has_next>,</#if>
</#list>
		}
	});
});
</script>

</head>

<body style="overflow-x: hidden;">
<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
<div class="row" style="margin:10px 20px;">
    <form class="form-horizontal m-t" id="dataForm">
<#list domain.formAttrs as fAttr>
    <div class="form-group">
		<label class="col-sm-2 control-label"><#if !fAttr.isNull><span class="required">*</span></#if>${fAttr.cnName}：</label>
        <div class="col-sm-4">
			<#if fAttr.formType == "dict">
                <tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" headerLabel="-- 请选择 --"></tag:dict>
			<#elseif fAttr.name == "remark" || fAttr.formType == "textarea">
                <textarea id="${fAttr.name}" name="${fAttr.name}" rows="5"></textarea>
			<#else>
                <input type="text" id="${fAttr.name}" name="${fAttr.name}" value="${r"${item."}${fAttr.name}}" <#if fAttr.type == "Integer" || fAttr.type == "Long">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> class="form-control" />
			</#if>
        </div>
    </div></#list>
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