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
                <#if !fAttr.isNull >required: true,</#if>
				<#if fAttr.type == "String">validIllegalChar: true</#if>
			}<#if fAttr_has_next>,</#if>
</#list>
		}
	});
});
</script>

</head>

<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
    <div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
    <div class="row">
        <form class="form-horizontal m-t" id="dataForm">
<#list domain.formAttrs as fAttr>
        <div class="form-group">
            <label class="col-sm-3 col-md-2 col-lg-1 control-label"><#if !fAttr.isNull><i>*</i></#if>${fAttr.cnName}：</label>
            <div class="col-sm-9 col-md-10 col-lg-11 form-inline"  style="padding-left:0px;">
                <#if fAttr.formType == "dict">
                    <tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" headerLabel="-- 请选择 --"></tag:dict>
                <#elseif fAttr.name == "remark" || fAttr.formType == "textarea">
                    <textarea id="${fAttr.name}" name="${fAttr.name}" rows="3" class="form-control" style="width:250px"></textarea>
                <#else>
                    <input type="text" id="${fAttr.name}" name="${fAttr.name}" value="${r"${item."}${fAttr.name}}" <#if fAttr.type == "Integer" || fAttr.type == "Long">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> class="form-control" style="width:250px" />
                </#if>
            </div>
        </div>
</#list>
        <div class="form-group" style="border-bottom:0px;margin-top:20px;">
            <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                <button class="btn btn-success" type="button" id="continueBut" onclick="window.location.reload();" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
                <button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
            </div>
        </div>
        </form>
    </div>
</div>
</body>
</html>