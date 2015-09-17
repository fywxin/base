<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>权限列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
$(function(){
	$("#gridTable").grid({
			url :'${ctx}/auth/doList?menuId=${menuId}',
			colModel: [{name:'id',index:'id', width:30,fixed:true, resize:false, formatter: 
		       		function(cellvalue, options, row){
							return '<input type="checkbox" value="'+row.authId+'" name="chk_col">';
		       		}
		       	},
	        	{name:'opt',index:'opt', width:100, fixed:true, sortable:false, resize:false, align: "center",label:'操作',
				formatter: function(cellvalue, options, row){
					var strArr = [];
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.authId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
		        	return strArr.join("");
				}},
				{name:'authName',width:160, label:'权限名称'},
				{name:'authCode', width:160, label:'权限编码'},
				{name:'menuName', width:160, label:'所属菜单'}
		]
	});
});

function add(){
	$.openWin({url: "${ctx}/auth/goSave?menuId=${menuId}","title":'新增权限'});
}

function update(authId){
	$.openWin({url: "${ctx}/auth/goUpdate?authId="+authId,"title":'编辑权限'});
}

function del(id){
	$.del({url:"${ctx}/auth/doDelete", datas:{ids:id}});
}

</script>
</head>
    
<body class="my_gridBody gray-bg">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
					<col  width="15%" />
					<col  width="35%"/>
					<col  width="15%" />
					<col  width="35%"/>
				<tbody>
						<tr>
							<td class="td-label">权限名称</td>
							<td class="td-value"><input type="text" id="authName" name="authName" style="width:160px;" value="${authName }" /></td>
					
							<td class="td-label">权限编码</td>
							<td class="td-value">
								<input type="text" id="authCode" name="authCode" style="width:160px;" value="${authCode }" />
								<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>	
							</td>
						</tr>
					</tbody>
				</table>
			<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
				  <button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
			</div>
		</form>
		<table id="gridTable" ></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>