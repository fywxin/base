<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>查看 ${domain.domainCnName}</title>
    <%@include file="/jsp/form.jsp" %>
    <script type="text/javascript">
    </script>
</head>

<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
    <div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
    <div class="row">
        <form class="form-horizontal m-t" id="dataForm">
            <input type="hidden" id="${domain.idAttr.name}" name="${domain.idAttr.name}" value="${r"${item."}${domain.idAttr.name}}" />
        <#list domain.formAttrs as fAttr>
            <div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><#if !fAttr.isNull><span class="required">*</span></#if>${fAttr.cnName}：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                    <#if fAttr.formType == "dict">
                        <tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" headerLabel="-- 请选择 --" readonly="readonly" ></tag:dict>
                    <#elseif fAttr.name == "remark" || fAttr.formType == "textarea">
                        <textarea id="${fAttr.name}" name="${fAttr.name}" rows="3" class="form-control" style="width:250px" readonly="readonly">${r"${item."}${fAttr.name}}</textarea>
                    <#else>
                        <input type="text" id="${fAttr.name}" name="${fAttr.name}" value="${r"${item."}${fAttr.name}}" <#if fAttr.type == "Integer" || fAttr.type == "Long">onkeyup="value=value.replace(/[^\d]/g,'')"</#if> class="form-control" style="width:250px" readonly="readonly" />
                    </#if>
                </div>
            </div></#list>
            <div class="form-group" style="border-bottom:0px;margin-top:20px;">
                <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                    <button class="btn btn-default" type="button" id="goBackBut" onclick="window.top.goTab(1);"><i class='fa fa-mail-reply'></i> 返 回</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>