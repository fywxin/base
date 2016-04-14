<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
function goEdit(){
	window.top.goSub("修改字典元素", "${ctx}/dictItem/goUpdate?dictItemId=${item.dictItemId}");
}
</script>

</head>
    
<body>
<div class="row panel-row" id="panelDiv1" >
		<div class="panel panel-default">
			<div class="panel-heading collapsed "  data-toggle="collapse" aria-expanded="false">
				${dictName } - ${item.itemName }</div>
			<div  class="panel-collapse collapse in" aria-expanded="true" >
				<div class="panel-body">
					<div class="table-responsive">
						<table class="panel-body-table table table-bordered " >
							<colgroup>
								<col class="col-xs-1">
								<col class="col-xs-5">
								<col class="col-xs-1">
								<col class="col-xs-5">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row" style="text-align: right;">所属字典</th>
									<td>${dictName }</td>
								</tr>
								<tr>
									<th scope="row" style="text-align: right;">元素名称</th>
									<td>${item.itemName }</td>
								</tr>
								<tr>
									<th scope="row">元素编码</th>
									<td>${item.itemCode }</td>
								</tr>
								<tr>
									<th scope="row">元素值</th>
									<td >${item.itemVal }</td>
								</tr>
								<tr>
									<th scope="row">元素扩展值</th>
									<td >${item.itemValExt }</td>
								</tr>
								<tr>
									<th scope="row">排序</th>
									<td >${item.orderNo }</td>
								</tr>
								<tr>
									<th scope="row">备注</th>
									<td >${item.remark }</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-group" style="border-bottom:0px;margin-left:30px;">
           <button class="btn btn-info" type="button" id="saveBut" onclick="goEdit();"><i class='fa fa-edit'></i> 更新</button>
     </div>
</body>
</html>
