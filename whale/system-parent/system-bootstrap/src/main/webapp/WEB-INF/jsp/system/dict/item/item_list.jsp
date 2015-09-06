<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典元素列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-primary btn-ss'><i class='fa fa-check'></i> 正常</button>",
		2:"<button type='button' class='btn btn-gray btn-ss'><i class='fa fa-lock'></i> 禁用</button>"};
$(function(){
	$("#gridTable").grid({
			url :'${ctx}/dictItem2/doList?dictId=${dictId}',
			colNames: ['操作', '元素名称', '元素编码','元素值','备注', '状态'],
			colModel: [
	            {name:'opt',index:'opt', width:250, fixed:true, sortable:false, resize:false, align: "center",
				formatter: function(cellvalue, options, row){
					var strArr = [];
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='修改' onclick=\"update('"+row.dictItemId+"')\"><i class='fa fa-pencil'></i> 修改</button>");
					strArr.push("<button type='button' class='btn btn-default btn-ss' title='删除' onclick=\"del('"+row.dictItemId+"')\"><i class='fa fa-trash-o'></i> 删除</button>");

		        		if(row.status == 2){
		        			strArr.push("<button type='button' class='btn btn-success btn-ss' title='启用' onclick=\"setStatus('"+row.dictItemId+"','"+row.itemName+"',1); return false;\"><i class='fa fa-unlock'></i> 启用</button>");
		        		}else{
		        			strArr.push("<button type='button' class='btn btn-gray btn-ss' title='禁用' onclick=\"setStatus('"+row.dictItemId+"','"+row.itemName+"',2); return false;\"><i class='fa fa-lock'></i> 禁用</button>");
		        		}
		        		return strArr.join("");
				
				}},
				{name:'itemName',width:160,formatter: function(cellvalue, options, row){
					return "<a href='#' onclick='view("+row.dictItemId+")'>"+row.itemName+"</a>";
				}},
				{name:'itemCode', width:160},
				{name:'itemVal', width:160},
				{name:'remark', width:160},
				{name:'status',index:'status', fixed:true, width:80,
					formatter: function(cellvalue, options, rowObject){
						return statusObj[cellvalue];
					}	
				}
		]
	});
});


function add(){
	$.openWin({url: "${ctx}/dictItem2/goSave?dictId=${dictId}","title":'新增元素'});
}

function update(dictItemId){
	$.openWin({url: "${ctx}/dictItem2/goUpdate?view=0&dictItemId="+dictItemId,"title":'编辑元素'});
}

function del(id){
	$.del({url:"${ctx}/dictItem2/doDelete", datas:{ids: id}, onSuccess: function(){
		$.alert("删除字典元素成功");
		window.parent.location.href="${ctx}/dict2/goTree?clkId=${dictId}";
	}});
}

function setStatus(id,name,type){
	var url = "${ctx}/dictItem2/doChangeState?status=1&dictItemId="+id;
	var opt = "启用";
	if(type == 2){
		url = "${ctx}/dictItem2/doChangeState?status=2&dictItemId="+id;
		opt = "禁用";
	}
	
	$.confirm({info:'您确定要[ '+opt+' ]字典元素[ '+name+' ]吗？', url : url});
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
							<td class="td-label">元素名称</td>
							<td class="td-value"><input type="text" id="itemName" name="itemName" style="width:160px;" value="${itemName }" /></td>
							<td class="td-label">元素编码</td>
							<td class="td-value">
								<input type="text" id="itemCode" name="itemCode" style="width:160px;" value="${itemCode }" />
								<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>			
							</td>
						</tr>
					</tbody>
				</table>
			<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm" onclick="add()"><i class="fa fa-plus"></i> 新  增</button>
			</div>
		</form>
		<table id="gridTable" ></table>
		<div id="gridPager"></div>
	</div>
</body>
</html>