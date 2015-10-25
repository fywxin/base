<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/grid.jsp" %>

<title>日志</title>
<script type="text/javascript">
var rsStatus = {1:"<button type='button' class='btn btn-primary btn-ss'><i class='fa fa-check'></i> 成功</button>",
				2:"<button type='button' class='btn btn-warning btn-ss'><i class='fa fa-exclamation'></i> 系统异常</button>",
				3:"<button type='button' class='btn btn-warning btn-ss'><i class='fa fa-check'></i> OrmException</button>",
				4:"<button type='button' class='btn btn-info btn-ss'<i class='fa fa-minus'></i>> 运行时异常</button>",
				5:"<button type='button' class='btn btn-danger btn-ss'><i class='fa fa-times'></i> 业务异常</button>",
				6:"<button type='button' class='btn btn-info btn-ss'><i class='fa fa-info'></i> 未知异常</button>"};
var time = new Date();
$(function (){
	$("#gridTable").grid({
		url: "${ctx}/log/doList",
		colNames: ['操作', '对象名称', '表名称', 'uri', '操作类型', '方法耗时(ms)','调用耗时(ms)', 'ip地址', '创建时间', '操作人', '结果'],
		colModel: [{name:'id',index:'', width:80, fixed:true, sortable:false, resize:false, align: "center",
					formatter: function(cellvalue, options, rowObject){
						return "<button type='button' class='btn btn-default btn-ss' title='查看' onclick=\"view('"+cellvalue+"')\"><i class='fa fa-info'></i> 查看</button>";
					
					}},
					{name:'cnName',index:'cnName', width:60},
					{name:'tableName',index:'tableName', width:60},
					{name:'uri',index:'uri', width:60},
					{name:'opt',index:'opt', width:60},
					{name:'methodCostTime',index:'methodCostTime', width:60},
					{name:'costTime',index:'costTime', width:60},
					{name:'ip',index:'ip', width:60},
					{name:'createTime',index:'createTime', width:60},
					{name:'userName',index:'userName', width:60},
					{name:'rsType',index:'rsType', width:40,
						formatter: function(cellvalue, options, rowObject){
							return rsStatus[cellvalue];
						}	
					}
			]
	});
	
});

function view(id){
	$.openWin({title: '查看日志',content: '${ctx}/log/goView?id='+id});
}
</script>
</head>
<body class="my_gridBody gray-bg" >
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
				<div class="my_gridToolBar">
				  <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-plus"></i> 新  增</button>
				  <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-pencil"></i> 修  改</button>
				  <button type="button" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删  除</button>
				</div>
			</form>
				<table id="gridTable" ></table>
				<div id="gridPager"></div>
			</div>
</body>
<script src="${html }/w3/js/plugins/layer/laydate/laydate.js"></script>
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