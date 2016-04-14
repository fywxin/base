<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>更新 ${domain.domainCnName}</title>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url': '${"$"+"{ctx}"}/${domain.domainName?uncap_first}/doUpdate'});
}

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

<body class="my_formBody">
<div class="navbar-fixed-bottom my_toolbar" >
    <button type="button" class="btn btn-primary btn-sm" onclick="save()"><i class="fa fa-hdd-o" ></i> 保存</button>
    <button type="button" class="btn btn-info btn-sm" onclick="$.closeWin();"><i class="fa fa-times" ></i> 关闭</button>
</div>
<div id="formBoxDiv" class="my_formBox" >
    <div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
    <form action="" method="post" id="dataForm">
		<input type="hidden" id="${domain.idAttr.name}" name="${domain.idAttr.name}" value="${r"${item."}${domain.idAttr.name}}" />
        <table class="query">
            <col width="15%"/>
            <col width="35%"/>
            <col width="15%"/>
            <col width="35%"/>
            <tbody>
<#list domain.formAttrs as fAttr>
	<#if fAttr_index%2==0>
					<tr>
	</#if>
						<td class="td-label"><#if !fAttr.isNull><span class="required">*</span></#if>${fAttr.cnName}</td>
						<td class="td-value">
						<#if fAttr.formType == "dict">
							<tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" value="${r"${item."}${fAttr.name}}" headerLabel="-- 请选择 --"></tag:dict>
						<#elseif fAttr.name == "remark" || fAttr.formType == "textarea">
							<textarea id="${fAttr.name}" name="${fAttr.name}" rows="5" title="最多只能输入${fAttr.maxLength}个字"></textarea>
						<#elseif fAttr.formType == "date">
							<input type="text" id="${fAttr.name}" name="${fAttr.name}" style="width:160px;" class="i-date" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" value="${r"${item."}${fAttr.name}}"/>
						<#else>
							<input type="text" id="${fAttr.name}" name="${fAttr.name}" style="width:160px;" value="${r"${item."}${fAttr.name}}" <#if fAttr.type == "Integer" || fAttr.type == "Long">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> />
						</#if>
						</td>
	<#if fAttr_index%2==1>
					</tr>
	</#if>
	<#if fAttr_index%2==0>
		<#if !fAttr_has_next>
					</tr>
		</#if>
	</#if>
</#list>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
