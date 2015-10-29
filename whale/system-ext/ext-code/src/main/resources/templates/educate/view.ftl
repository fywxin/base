<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>查看 ${domain.domainCnName}</title>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">

</script>

</head>

<body style="overflow-x: hidden;">
<div class="row" style="margin:10px 20px;">
    <div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
<#list domain.formAttrs as fAttr>
    <div class="form-group">
        <label class="col-sm-2 control-label">${fAttr.cnName}：</label>
        <div class="col-sm-4">
			<#if fAttr.formType == "dict">
                <tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" readonly="readonly" headerLabel="-- 请选择 --"></tag:dict>
			<#elseif fAttr.name == "remark" || fAttr.formType == "textarea">
                <textarea id="${fAttr.name}" name="${fAttr.name}" rows="5"></textarea>
			<#else>
                <input type="text" id="${fAttr.name}" name="${fAttr.name}" value="${r"${item."}${fAttr.name}}" readonly="readonly" class="form-control" />
			</#if>
        </div>
    </div></#list>
    <div class="form-group">
        <div class="col-sm-12 col-sm-offset-2">
            <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-reply'></i> 返 回</button>
        </div>
    </div>
</div>
</body>
</html>