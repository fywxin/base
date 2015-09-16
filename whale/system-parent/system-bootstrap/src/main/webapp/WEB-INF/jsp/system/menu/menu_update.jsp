<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
function save(){
	$.save({'url':'${ctx}/menu/doUpdate', onSuccess: function(){
		$.getParent().location.reload();
		$.alert("保存成功");
		$.closeWin();
	}}); 
}

//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"menuName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"menuUrl": {
				maxlength: 255
			},
			"inco": {
				maxlength: 128
			},
			"orderNo": {
				maxlength: 4
			}
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
				<input type="hidden" name="menuId" value="${item.menuId }" />
				<table class="query">
					<col width="15%"/>
					<col width="35%"/>
					<col width="15%"/>
					<col width="35%"/>
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>菜单名</td>
							<td class="td-value"><input type="text" style="width:160px;" id="menuName" name="menuName" value="${item.menuName }" maxlength="64" title="最多64个字"/></td>
							<td class="td-label" ><span class="required">*</span>所属菜单</td>
							<td class="td-value">
								<tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${item.parentId }"></tag:tree>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>菜单类型</td>
							<td class="td-value">
								<input type="hidden" id="menuType" name="menuType" value="${item.menuType}" />
								<c:if test="${item.menuType == 1}">tab菜单</c:if>
								<c:if test="${item.menuType == 2}">文件夹菜单</c:if>
								<c:if test="${item.menuType == 3}">叶子菜单</c:if>
							</td>
							<td class="td-label">图标地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="inco" name="inco" value="${item.inco }" maxlength="128" title="最多128个字"/></td>
							
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>打开方式</td>
							<td class="td-value">
								<select id="openType" name="openType" style="width:165px">
									<option value="1" <c:if test="${item.openType == 1}">selected="selected"</c:if>>窗口内打开</option>
									<option value="2" <c:if test="${item.openType == 2}">selected="selected"</c:if>>弹出窗口</option>
								</select>
							</td>
							
							<td class="td-label">公共菜单</td>
							<td class="td-value">
								<select id="isPublic" name="isPublic" style="width:165px">
									<option value="0" <c:if test="${item.isPublic != 1}">selected="selected"</c:if>>否</option>
									<option value="1" <c:if test="${item.isPublic == 1}">selected="selected"</c:if>>是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="td-label">链接地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="menuUrl" name="menuUrl" value="${item.menuUrl }" maxlength="255" title="最多255个字"/></td>
							<td class="td-label">排列顺序</td>
							<td class="td-value"><input type="text" style="width:160px;" id="orderNo" name="orderNo"  value="${item.orderNo }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" title="最多4个字"/></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
