<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>组织列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/dept/doList',
    	uid : "id",
    	pid : "pid",
    	orderCol : "orderNo",
    	checkbox : true,
    	autoCheckChildren : false,
    	orderAsc : true,
    	usePager: false,
    	alternatingRow : false,
    	toolbar: {items: [
           		{text: '新增', icon: 'add', click: 
           			function(){
	           			var idArr = grid.getSelIds();
	           			if(idArr.length != 1){
	           				$.alert('请在列表上选择一个父组织');
	           				return ;
	           			}
	           			
	           			$.openWin({url: "${ctx}/dept/goSave?pid="+idArr[0], "title":'新增组织'});
           			} 
				}
            ]
        },
        columns: [
      	        {display: '操作', name: 'opt', width: 100,frozen: true,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.id+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' onclick='del(\""+row.id+"\")'>删除</a>");
      	        	    return strArr.join("");
  	        	}},
      	        {display: '组织名称', name: 'deptName', id: "deptName", align: 'left', frozen: true,width: 250},
      	        {display: '组织编码', name: 'deptCode' },
      	        {display: '联系电话', name: 'deptTel' },
      	      	{display: '联系地址', name: 'deptAddr' },
      	    	{display: '备注', name: 'remark' }
              ],
         tree: {columnId: 'deptName'}
	});
});

function update(id){
	$.openWin({url: "${ctx}/dept/goUpdate?id="+id,"title":'修改组织'});
}

function del(id){
	$.del({url:"${ctx}/dept/doDelete", datas:{ids:id}});
}

</script>
</head>
    
<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
		</form>
	</div>
	<div id="grid" style="margin: 2px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>