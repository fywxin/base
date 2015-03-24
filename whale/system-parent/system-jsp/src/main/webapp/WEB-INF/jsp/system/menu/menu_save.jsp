<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<%@include file="/html/jsp/ztree.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/menu/doSave'}); 
	    	}
	    },
	    { line:true },
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
	    	$.closeWin();
	    	return false; 
	    	}
	    }
	 ]
	});
});

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

function changeMenuType(){
	var val = parseInt($("#menuType").val());
	if(val == 1){
		$("#urlSign").css("visibility", "hidden");
		$("#menuUrl").css("visibility", "hidden").rules("remove","required");
	}else if(val == 2){
		$("#urlSign").css("visibility", "hidden");
		$("#menuUrl").css("visibility", "hidden").rules("remove","required");
	}else if(val == 3){
		$("#urlSign").css("visibility", "visible");
		$("#menuUrl").css("visibility", "visible").rules("add",{"required":true});
	}
}

</script>

</head>
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<table>
					<col width="10%" />
					<col width="40%" />
					<col width="10%" />
					<col width="40%" />
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>菜单名</td>
							<td class="td-value"><input type="text" style="width:160px;" id="menuName" name="menuName" maxlength="64" title="最多64个字"/></td>
							<td class="td-label" ><span class="required">*</span>所属菜单</td>
							<td class="td-value">
								<tag:tree nodeName="menuName" nodeId="menuId" id="parentId" nodes="${nodes }" nodePId="parentId" value="${parentId }"></tag:tree>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required">*</span>菜单类型</td>
							<td class="td-value">
								<select id="menuType" name="menuType" style="width:165px" onchange="changeMenuType()" >
									<option value="1">tab菜单</option>
									<option value="2">文件夹菜单</option>
									<option value="3">叶子菜单</option>
								</select>
							</td>
							<td class="td-label">图标地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="inco" name="inco" maxlength="128" title="最多128个字"/></td>
							
						</tr>
						<tr>
							<td class="td-label">打开方式</td>
							<td class="td-value">
								<select id="openType" name="openType" style="width:165px">
									<option value="1">窗口内打开</option>
									<option value="2">弹出窗口</option>
								</select>
							</td>
							<td class="td-label">公共菜单</td>
							<td class="td-value" >
								<select id="isPublic" name="isPublic" style="width:165px">
									<option value="0">否</option>
									<option value="1">是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="td-label"><span class="required" id="urlSign" style="visibility:hidden;">*</span>链接地址</td>
							<td class="td-value"><input type="text" style="width:160px;" id="menuUrl" name="menuUrl" maxlength="255" title="最多255个字"/></td>		
							<td class="td-label">排列顺序</td>
							<td class="td-value" ><input type="text" style="width:160px;" id="orderNo" name="orderNo" value="${nextNum }" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="4" title="最多4个字"/></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
