<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'closeBut', text: '修改', icon:"edit", click: function(){ 
	    	update();
	    	}
	    }
	 ]
	});
});

function update(){
	$.openWin({url: "${ctx}/dictItem/goUpdate?dictItemId=${item.dictItemId}&view=1","title":'编辑字典元素'});
}
</script>

</head>
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<input type="hidden" id="dictId" name="dictId" value="${item.dictId }" />
				<input type="hidden" id="dictItemId" name="dictItemId" value="${item.dictItemId }" />
				<table>
					<col width="10%" />
					<col width="40%" />
					<col width="10%" />
					<col width="40%" />
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>元素名称</td>
							<td class="td-value">${item.itemName }</td>
							<td class="td-label" ><span class="required">*</span>元素编码</td>
							<td class="td-value">${item.itemCode }</td>
						</tr>
						<tr>
							<td class="td-label">元素值</td>
							<td class="td-value">${item.itemVal }</td>
							<td class="td-label" >排列顺序</td>
							<td class="td-value">${item.orderNo }</td>
						</tr>
						<tr>
							<td class="td-label" >元素扩展值</td>
							<td class="td-value" colspan="3">
								<div style="height:30px;" class="textAreaDiv">${item.itemValExt }</div>
							</td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<div style="height:40px;" class="textAreaDiv">${item.remark }</div>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
