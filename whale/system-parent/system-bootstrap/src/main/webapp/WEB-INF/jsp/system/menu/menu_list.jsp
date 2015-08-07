<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>菜单列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var menuTypeObj = {1: "tab菜单", 2: "文件夹菜单", 3: "叶子菜单"};
var openTypeObj = {1: "窗口内打开", 2: "弹出窗口"};
var openStateObj = {1: "打开", 2: "合并"};
$(function(){
	$.grid({
    	url :'${ctx}/menu/doList',
    	uid : "menuId",
    	pid : "parentId",
    	orderCol : "orderNo",
    	orderAsc : true,
    	usePager: false,
    	alternatingRow : false,
    	toolbar: {items: [
           		{text: '新增菜单', icon: 'add', click: 
           			function(){
	           			var idArr = grid.getSelIds();
	           			if(idArr.length > 1){
	           				$.alert('请选择一个父菜单');
	           				return ;
	           			}
	           			if(idArr != null && idArr.length == 0){
	           				$.openWin({url: "${ctx}/menu/goSave", "title":'新增菜单'});
	           			}else{
	           				$.openWin({url: "${ctx}/menu/goSave?pid="+idArr[0], "title":'新增菜单'});
	           			}
           			} 
				}
            ]
        },
        columns: [
      	        {display: '操作', name: 'opt', width: 100,frozen: true,
      	        	render: function (row){
      	        		var strArr = [];
      	        		strArr.push("<a href='#' class='r15' onclick='update(\""+row.menuId+"\");'>修改</a>");
      	        		
      	        		strArr.push("<a href='#' onclick='del(\""+row.menuId+"\")'>删除</a>");
      	        	    return strArr.join("");
  	        	}},
      	        {display: '菜单名称', name: 'menuName', id: "menuName", align: 'left', frozen: true,width: 200},
      	        {display: '链接地址', name: 'menuUrl', width:300, align: 'left', frozen: true},
      	      	{display: '菜单类型', name: 'menuType',width: 130,
      	        	render: function (row){
          	        	    return menuTypeObj[row.menuType];
          	        	} },
      	    	{display: '打开方式', name: 'openType',width: 150,
      	        	render: function (row){
          	        	    return openTypeObj[row.openType];
          	        	} },
      	    	{display: '打开状态', name: 'openState',width: 120,
      	        	render: function (row){
          	        	    return openStateObj[row.openState];
          	        	} },
      	      	{display: '是否公共', name: 'isPublic', width: 80,
      	        	render: function (row){
          	        	    return yesObj[row.isPublic];
          	        	} },
          	    {display: '图标', name: 'inco' }
              ],
         tree: {columnId: 'menuName'}
	});
});

function update(menuId){
	$.openWin({url: "${ctx}/menu/goUpdate?view=0&menuId="+menuId,"title":'编辑菜单'});
}

function del(id){
	$.del({url:"${ctx}/menu/doDelete", datas:{ids:id}});
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