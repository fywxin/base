<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>字典元素列表</title>
<%@include file="/jsp/grid.jsp" %>
<script type="text/javascript">
var statusObj = {1:"<button type='button' class='btn btn-success btn-ss' style='cursor:default'><i class='fa fa-check'></i> 正常</button>",
		2:"<button type='button' class='btn btn-default btn-ss' style='cursor:default'><i class='fa fa-lock'></i> 禁用</button>"};
		
$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/dictItem/doList?dictId=${dictId}',
	    idField: 'dictItemId',
	    columns: [
		{
		    field: 'chk',
		    checkbox: true,
		    width: '35px',
		    align: 'center'
		}, {
	        field: 'opt',
	        title: '操作',
	        width: '100px',
	        align: 'center',
	        fix: true,
	        formatter: function(value, row, index){
        		var strArr = [];
        		<tag:auth authCode="dictItem:update">
	            strArr.push('<a href="javascript:;" class="link" onclick=go("修改字典元素","${ctx}/dictItem/goUpdate?dictItemId='+row.dictItemId+'") >修改</a>');
	            </tag:auth>
	            <tag:auth authCode="dictItem:status">
	            strArr.push('<span class="link-sep">|</span>');
	            if(row.status == 2){
	              strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.dictItemId+',"'+row.itemName+'",1) >启用</a>');
	            }else{
	              strArr.push('<a href="javascript:;" class="link" onclick=setStatus('+row.dictItemId+',"'+row.itemName+'",2) >禁用</a>');
	            }
	            </tag:auth>
	            return strArr.join("");
			}
	    }, {
	        field: 'itemName',
	        width: '140px',
	        title: '元素名称',
	        sortable: true
	    }, {
	        field: 'itemCode',
	        title: '元素编码',
	        width: '160px',
	        sortable: true
	    }, {
	        field: 'itemVal',
	        title: '元素值',
	        width: '140px',
	        fix: true
	    }, {
	        field: 'remark',
	        title: '备注',
	        fix: true
	    }, {
	        field: 'status',
	        title: '状态',
	        width: '50px',
	        align: 'center',
	        formatter: function(cellvalue, options, rowObject){
				return statusObj[cellvalue];
			}
	    }
	   ]
	});
});

function del(id){
	$.del({url:"${ctx}/dictItem/doDelete", onSuccess: function(){
		$.msg("删除字典元素成功");
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
	
	$.confirm({info:'您确定要[ '+opt+' ]字典元素[ '+name+' ]吗？', url : url});
}
</script>
</head>

<body style="overflow: hidden;">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
				<tbody>
					<tr>
						<td class="td-label">元素名称</td>
						<td class="td-value"><input type="text" id="itemName" name="itemName" style="width:160px;" value="${itemName }" /></td>
						<td class="td-label">元素编码</td>
						<td class="td-value">
							<input type="text" id="itemCode" name="itemCode" style="width:160px;" value="${itemCode }" />
							<button type="button" class="btn btn-success btn-xs" onclick="search(1)" style="margin-left:15px;"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div id="mytoolbar" style="margin-left:5px;">
			<tag:auth authCode="dictItem:save">
			<button type="button" class="btn btn-primary btn-sm" onclick="go('新增字典元素','${ctx}/dictItem/goSave?dictId=${dictId}')"><i class="fa fa-plus"></i> 新  增 </button>
			</tag:auth>
			<tag:auth authCode="dictItem:del">
			<button type="button" class="btn btn-danger btn-sm" onclick="del()"><i class="fa fa-trash-o"></i> 删  除</button>
			</tag:auth>
		</div>
		<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
</html>