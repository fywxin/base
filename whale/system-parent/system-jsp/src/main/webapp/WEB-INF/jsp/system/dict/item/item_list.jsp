<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典元素列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/dictItem/doList?dictId=${dictId}',
    	uid : "roleId",
    	toolbar: {items: [
           		{text: '新增元素', icon: 'add', click: 
           			function(){
           				$.openWin({url: "${ctx}/dictItem/goSave?dictId=${dictId}","title":'新增元素'});
           			} 
				}
			]
        },
        columns: [
      	        {display: '操作', name: 'opt', width: 140,frozen: true,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.dictItemId+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='del(\""+row.dictItemId+"\");'>删除</a>");
      	        		
      	        		if(row.status == 2){
      	        			strArr.push("<a href='#' class='r15' onclick='setStatus(\""+row.dictItemId+"\",\""+row.itemName+"\",1); return false;'>启用</a>");
      	        		}else{
      	        			strArr.push("<a href='#' class='r15' onclick='setStatus(\""+row.dictItemId+"\",\""+row.itemName+"\",2); return false;'>禁用</a>");
      	        		}
      	        		
      	        	    return strArr.join("");
  	        	}},
      	        {display: '元素名称', name: 'itemName', frozen: true, width: 250},
      	        {display: '元素编码', name: 'itemCode', frozen: true, width: 250},
      	      	{display: '元素值', name: 'itemVal', width: 250},
      	        {display: '备注', name: 'remark'},
      	      	{display: '状态', name: 'status', width:80,
      	        	render: function (row){
      	        	    return statusObj[row.status];
      	        	}
      	        }
              ]
	});
});

function update(dictItemId){
	$.openWin({url: "${ctx}/dictItem/goUpdate?view=0&dictItemId="+dictItemId,"title":'编辑元素'});
}

function del(id){
	$.del({url:"${ctx}/dictItem/doDelete", datas:{ids: id}, onSuccess: function(){
		$.alert("删除字典元素成功");
		window.parent.location.href="${ctx}/dict/goTree?clkId=${dictId}";
	}});
}

function setStatus(id,name,type){
	var url = "${ctx}/dictItem/doChangeState?status=1&dictItemId="+id;
	var opt = "启用";
	if(type == 2){
		url = "${ctx}/dictItem/doChangeState?status=2&dictItemId="+id;
		opt = "禁用";
	}
	
	$.sure({info:'您确定要[ '+opt+' ]字典元素[ '+name+' ]吗？', url : url});
}
</script>
</head>
    
    
<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
				<table>
						<col  width="10%" />
						<col  width="40%"/>
						<col  width="10%"/>
						<col  width="40%"/>
					<tbody>
						<tr>
							<td class="td-label">元素名称</td>
							<td class="td-value"><input type="text" id="itemName" name="itemName" style="width:160px;" value="${itemName }" /></td>
							<td class="td-label">元素编码</td>
							<td class="td-value"><input type="text" id="itemCode" name="itemCode" style="width:160px;" value="${itemCode }" /></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		<div id="grid" style="margin: 0px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>