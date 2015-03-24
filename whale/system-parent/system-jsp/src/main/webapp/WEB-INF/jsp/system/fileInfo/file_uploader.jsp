<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/html/jsp/common.jsp" %>
		<title>图片上传</title>
	</head>
<script type="text/javascript">
</script>

<body style="padding:0;margin:0;">
	<div class="body-box-form" >
	<div class="content-form">
		<div class="panelBar" id="panelBarDiv"></div>
		<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm" style="cursor: pointer;margin-left: ">
				<table>
					<col width="10%"/>
					<col width="40%"/>
					<col width="10%"/>
					<col width="40%"/>
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>角色名称</td>
							<td class="td-value"><input type="text" style="width:160px;" id="roleName" name="roleName" maxlength="16" title="最多16个字"/></td>
							<td class="td-label" ><span class="required">*</span>角色编码</td>
							<td class="td-value"><input type="text" style="width:160px;" id="roleCode" name="roleCode" maxlength="64" title="最多64位"/></td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<tag:file id="inf" maxNum="2" ></tag:file>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>