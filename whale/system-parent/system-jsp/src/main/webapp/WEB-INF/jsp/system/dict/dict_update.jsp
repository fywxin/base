<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
	    	$.save({'url':'${ctx}/dict/doUpdate', onSuccess: function(){
	    		$.alert("保存成功");
	    		if($.trim($("#dictName").val()) != "${item.dictName}"){
					$.getWinOpener().parent.location.reload();
	    		}
	    		$.getWinOpener().grid.reload();
	    		$.closeWin();
	    	}}); 
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
			"dictName": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"dictCode": {
				validIllegalChar: true,
				maxlength: 32,
				required: true
			},
			"remark":{
				validIllegalChar: true,
				maxlength: 500
			}
		}
	});
});

</script>

</head>
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<input type="hidden" id="dictId" name="dictId" value="${item.dictId }"/>
				<input type="hidden" id="status" name="status" value="${item.status }"/>
				<table>
					<col width="10%" />
					<col width="40%" />
					<col width="10%" />
					<col width="40%" />
					<tbody>
						<tr>
							<td class="td-label"><span class="required">*</span>字典名</td>
							<td class="td-value"><input type="text" style="width:160px;" id="dictName" name="dictName" value="${item.dictName }" maxlength="64" title="最多64个字"/></td>
							<td class="td-label" ><span class="required">*</span>字典编码</td>
							<td class="td-value">${item.dictCode  }
								<input type="hidden" style="width:160px;" id="dictCode" name="dictCode" value="${item.dictCode }" maxlength="32" title="最多32个字"/>
							</td>
						</tr>
						<tr>
							<td class="td-label" >备注</td>
							<td class="td-value" colspan="3">
								<textarea id="remark" name="remark" rows="5" title="最多只能输入500个字">${item.remark }</textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
