<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/grid.jsp" %>
<title>日志</title>
<script type="text/javascript">
var rsStatus = {1:"<button type='button' class='btn btn-success btn-ss'><i class='fa fa-check'></i> 成功</button>",
				2:"<button type='button' class='btn btn-warning btn-ss'><i class='fa fa-exclamation'></i> 系统异常</button>",
				3:"<button type='button' class='btn btn-warning btn-ss'><i class='fa fa-check'></i> OrmException</button>",
				4:"<button type='button' class='btn btn-info btn-ss'<i class='fa fa-minus'></i>> 运行时异常</button>",
				5:"<button type='button' class='btn btn-danger btn-ss'><i class='fa fa-times'></i> 业务异常</button>",
				6:"<button type='button' class='btn btn-info btn-ss'><i class='fa fa-info'></i> 未知异常</button>"};
var time = new Date();
$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/log/doList',
	    idField: 'id',
	    columns: [
		{
	        field: 'opt',
	        title: '操作',
	        width: '7%',
	        align: 'center',
	        formatter: function(value, row, index){
	        	return '<a href="javascript:;" class="link" onclick=go("查看日志","${ctx}/log/goView?id='+row.id+'") >查看</a>';
			}
	    }, {
	        field: 'cnName',
	        width: '10%',
	        title: '对象名称'
	    }, {
	        field: 'tableName',
	        title: '表名称',
	        width: '10%'
	    }, {
	        field: 'uri',
	        title: 'uri'
	    }, {
	        field: 'opt',
	        title: '操作类型',
	        width: '5%'
	    }, {
	        field: 'time',
	        title: '耗时',
	        width: '8%',
	        formatter: function(value, row, index){
	        	return row.methodCostTime+'<span class="link-sep">|</span>'+row.costTime;
			}
	    }, {
	        field: 'ip',
	        title: 'ip地址',
	        width: '9%'
	    }, {
	        field: 'createTime',
	        title: '创建时间',
	        width: '9%'
	    }, {
	        field: 'userName',
	        title: '操作人',
	        width: '7%'
	    }, {
	        field: 'rsType',
	        title: '结果',
	        width: '7%',
	        formatter: function(value, row, index){
				return rsStatus[value];
			}
	    }
	   ]
	});
});
</script>
</head>
<body style="overflow: hidden;">
	<div class="my_gridBox">
		<form id="queryForm" >
			<table class="query">
				<col width="8%" />
				<col  width="25%"/>
				<col  width="8%" />
				<col  width="25%"/>
				<col  width="8%" />
				<col  width="25%"/>
				<tbody>
					<tr>
						<td class="td-label">处理结果</td>
						<td>
							<select id="rsType" name="rsType" data-placeholder="选择省份..." class="chosen-select" style="width: 165px;">
								<option value="">--请选择--</option>
								<option value="1">处理成功</option>
								<option value="11">返回异常</option>
								<optgroup td-label="异常">
									<option value="2">系统异常</option>
									<option value="3">ORM异常</option>
									<option value="4">运行时异常</option>
									<option value="5">业务异常</option>
									<option value="0">其他异常</option>
								</optgroup>
							</select>
						</td>
						<td class="td-label">操作类型</td>
						<td>
							<select id="opt" name="opt" style="width: 165px;">
								<option value="">--请选择--</option>
								<option value="dll">变更操作</option>
								<optgroup td-label="详细变更操作" >
									<option value="save" >新增</option>
									<option value="saves" >循环新增</option>
									<option value="saveBatch" >批量新增</option>
									<option value="update" >修改</option>
									<option value="saves" >循环修改</option>
									<option value="updateBatch" >批量修改</option>
									<option value="delete" >删除</option>
									<option value="deleteBy" >按条件删除</option>
									<option value="deleteBatch" >批量删除</option>
								</optgroup>
								<option value="find">查询操作</option>
								<optgroup td-label="详细查询操作" >
									<option value="get" >根据ID获取对象</option>
									<option value="getObject" >按条件获取对象</option>
									<option value="query" >对象列表查询</option>
									<option value="queryPage" >分页查询</option>
									<option value="queryAll">全量查询</option>
									<option value="queryForNumber" >数字查询</option>
									<option value="queryForList">queryForList</option>
									<option value="queryForMap">queryForMap</option>
									<option value="queryOther">queryOther</option>
								</optgroup>
							</select>
						</td>
						<td class="td-label">所属应用</td>
						<td>
							<input type="text" id="appId" name="appId" style="width:160px;" value="${item.appId}" />
						</td>
					</tr>
					<tr>
						<td class="td-label">方法耗时</td>
						<td>
							>&nbsp;<input type="text" id="methodCostTime" name="methodCostTime" onkeyup="value=value.replace(/[^\d]/g,'')" style="width:145px;padding:1px;" />
						</td>
					
						<td class="td-label">调用耗时</td>
						<td>
							>&nbsp;<input type="text" id="costTime" name="costTime"  onkeyup="value=value.replace(/[^\d]/g,'')" style="width:145px;padding:1px;" />
						</td>
					
						<td class="td-label">操作时间</td>
						<td>
							<input type="text" style="width:130px;" id="startTime" name="startTime" />
							至
							<input type="text" style="width:130px;" id="endTime" name="endTime" />
						</td>
					</tr>
					<tr>
						<td class="td-label">表名称</td>
						<td>
							<input type="text" id="tableName" name="tableName" style="width:160px;" value="${item.tableName}" />
						</td>
						<td class="td-label">uri</td>
						<td>
							<input type="text" id="uri" name="uri" style="width:160px;" value="${item.uri}" />
						</td>
						<td class="td-label">操作人</td>
						<td>
							<input type="text" id="userName" name="userName" style="width:160px;" value="${item.userName}" />&nbsp;&nbsp;
       						<button type="button" class="btn btn-success btn-xs" onclick="search()"><i class="fa fa-search" ></i> 查  询</button>
						</td>
					</tr>
				</tbody>
			</table>
				
			</form>
				<div id="gridDiv" style="overflow-y: auto;overflow-x: hidden;">
			<table id="gridTable" ></table>
		</div>
	</div>
</body>
<script src="${html }/plugins/layer/laydate/laydate.js"></script>
<script>
    //日期范围限制
    var start = {
        elem: '#startTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        min: '1990-01-01 23:59:59', //最大日期
        max: laydate.now(),
        istime: true,
        istoday: false,
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#endTime',
        format: 'YYYY-MM-DD hh:mm:ss',
        max: laydate.now(),
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate.skin("huanglv");
    laydate(end);
</script>
</html>