<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/dict/doList',
    	uid : "roleId",
    	toolbar: {items: [
           		{text: '新增字典', icon: 'add', click: 
           			function(){
           				$.openWin({url: "${ctx}/dict/goSave","title":'新增字典'});
           			} 
				}
			]
        },
        columns: [
      	        {display: '操作', name: 'opt', width: 100,frozen: true,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.dictId+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='del(\""+row.dictId+"\");'>删除</a>");
      	        		
      	        	    return strArr.join("");
  	        	}},
      	        {display: '字典名称', name: 'dictName', width: 250},
      	        {display: '字典编码', name: 'dictCode', width: 250},
      	        {display: '备注', name: 'remark'}
              ]
	});
});

function update(dictId){
	$.openWin({url: "${ctx}/dict/goUpdate?dictId="+dictId,"title":'编辑字典'});
}

function del(dictId){
	$.del({url:"${ctx}/dict/doDelete", datas:{dictId: dictId}, onSuccess: function(){
		window.parent.location.reload();
		$.alert("删除字典成功");
	}});
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
							<td class="td-label">字典名称</td>
							<td class="td-value"><input type="text" id="dictName" name="dictName" style="width:160px;" value="${dictName }" /></td>
							<td class="td-label">字典编码</td>
							<td class="td-value">
							<input type="text" id="dictCode" name="dictCode" style="width:160px;" value="${dictCode }" />
							<button id="queryBut" type="button" class="btn-query">查询</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		<div id="grid" style="margin: 0px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>