<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>实体对象列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
$(function(){
	$.grid({
    	url :'${ctx}/domain/doList',
    	uid : "roleId",
    	toolbar: {items: [
<tag:auth authCode="DOMAIN_SAVE">
           		{text: '新增实体对象', icon: 'add', click: function(){
           				$.openWin({url: "'${ctx}/domain/goSave","title":'新增实体对象'});
           			} 
				}
</tag:auth>
            ]
        },
        columns: [
				{display: '操作', name: 'opt', width: 70, frozen: true, render: function (row){
					var strArr = [];
<tag:auth authCode="DOMAIN_UPDATE">
      	        	strArr.push("<a href='#' class='r15' onclick='update(\""+row.id+"\");'>修改</a>");
</tag:auth>
<tag:auth authCode="DOMAIN_DEL">     	        		
      	        	strArr.push("<a href='#' class='r15' onclick='del(\""+row.id+"\");'>删除</a>");
</tag:auth>
<tag:auth authCode="DOMAIN_VIEW">   				
					strArr.push("<a href='#' onclick='view(\""+row.id+"\");'>查看</a>");
</tag:auth>
					return strArr.join("");
				} },
							{display: '实体名', name: 'name'},
							{display: '中文名', name: 'cnName'},
							{display: '数据库', name: 'dbName'},
							{display: '类名', name: 'clazzName'},
							{display: '是否树', name: 'isTree'},
							{display: '包名称', name: 'pkgName'},
							{display: '表单列数', name: 'formColNum'},
              ]
	});
});

function update(id){
	$.openWin({url: "'${ctx}/domain/goUpdate?id="+id,"title":'修改实体对象'});
}

function del(id){
	$.del({url:"'${ctx}/domain/doDel", datas:{ids: id}});
}

function view(id){
	$.openWin({url: "'${ctx}/sms/goView?id="+id,"title":'查看实体对象'});
}

</script>
</head>

<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
			<table >
					<col  width="10%" />
					<col  width="40%"/>
					<col  width="10%"/>
					<col  width="40%"/>
				<tbody>
				</tbody>
			</table>
		</form>
	</div>
	<div id="grid" style="margin: 0px 2px 1px 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>