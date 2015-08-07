<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>权限列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/auth/doList?menuId=${menuId}',
    	uid : "authId",
    	toolbar: {items: [
           		{text: '新增权限', icon: 'add', click: 
           			function(){
           				$.openWin({url: "${ctx}/auth/goSave?menuId=${menuId}","title":'新增权限'});
           			} 
				}
			]
        },
        columns: [
      	        {display: '操作', name: 'opt', width: 100,frozen: true,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.authId+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' class='r15' onclick='del(\""+row.authId+"\");'>删除</a>");
      	        		
      	        	    return strArr.join("");
  	        	}},
      	        {display: '权限名称', name: 'authName'},
      	        {display: '权限编码', name: 'authCode'},
      	        {display: '所属菜单', name: 'menuName'}
              ]
	});
});

function update(authId){
	$.openWin({url: "${ctx}/auth/goUpdate?authId="+authId,"title":'编辑权限'});
}

function del(id){
	$.del({url:"${ctx}/auth/doDelete", datas:{ids:id}});
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
							<td class="td-label">权限名称</td>
							<td class="td-value"><input type="text" id="authName" name="authName" style="width:160px;" value="${authName }" /></td>
					
							<td class="td-label">权限编码</td>
							<td class="td-value">
								<input type="text" id="authCode" name="authCode" style="width:160px;" value="${authCode }" />
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