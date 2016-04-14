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
<div class="row panel-row" id="panelDiv1" >
    <div class="panel panel-default">
        <div class="panel-heading collapsed panel-heading-style" href="#panel1" data-toggle="collapse" aria-expanded="false">${domain.domainCnName}详情</div>
        <div id="panel1" class="panel-collapse collapse in" aria-expanded="true" >
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="panel-body-table table table-bordered " >
                        <colgroup>
                            <col class="col-xs-1">
                            <col class="col-xs-5">
                            <col class="col-xs-1">
                            <col class="col-xs-5">
                        </colgroup>
                        <tbody>
<#list domain.formAttrs as fAttr>
                          <tr>
                              <th scope="row">${fAttr.cnName}</th>
                              <td><#if fAttr.formType == "dict"><tag:dict id="${fAttr.name}" dictCode="${fAttr.dictName}" readonly="readonly" ></tag:dict><#else>
                              ${r"${item."}${fAttr.name}}</#if>
                              </td>
                          </tr>
</#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
