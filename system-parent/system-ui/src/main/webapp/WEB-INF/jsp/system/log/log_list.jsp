<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/grid.jsp" %>
<title>日志</title>
<script type="text/javascript">
var rsStatus = {0:"<button type='button' class='btn btn-success btn-ss' style='cursor:default'><i class='fa fa-check'></i> 成功</button>",
				1:"<button type='button' class='btn btn-warning btn-ss' style='cursor:default'><i class='fa fa-exclamation'></i> 系统异常</button>",
				2:"<button type='button' class='btn btn-warning btn-ss' style='cursor:default'><i class='fa fa-check'></i> OrmException</button>",
				3:"<button type='button' class='btn btn-info btn-ss' style='cursor:default'><i class='fa fa-minus'></i> 运行时异常</button>",
				4:"<button type='button' class='btn btn-danger btn-ss' style='cursor:default'><i class='fa fa-times'></i> 业务异常</button>",
				10:"<button type='button' class='btn btn-info btn-ss' style='cursor:default'><i class='fa fa-info'></i> 未知异常</button>"};
var time = new Date();
$(function(){
	$("#gridTable").grid({
	    url: '${ctx}/logInfo/doList',
	    idField: 'id',
	    columns: [
		{
	        field: 'clazzMethod',
	        title: '对象',
			width: '15%',
				formatter: function(value, row, index){
					return "<a href='javascript:;' onclick=\"go('用户查看','${ctx}/logInfo/goView?id="+row.id+"')\" >"+row.clazz+"#"+row.method+"</a>";
				}
	    }, {
			field: 'module',
			width: '6%',
			title: '模块'
		}, {
			field: 'info',
			title: '日志描述'
	    }, {
	        field: 'costTime',
	        title: '耗时',
	        width: '5%'
	    }, {
	        field: 'ip',
	        title: 'ip地址',
	        width: '7%'
	    }, {
	        field: 'createTime',
	        title: '创建时间',
	        width: '9%',
	        formatter: function(value, row, index){
	        	return new Date(value).format("yyyy-MM-dd hh:mm:ss");
	        }
	    }, {
	        field: 'userName',
	        title: '操作人',
	        width: '7%'
	    }, {
	        field: 'rs',
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
							<select id="rs" name="rs" data-placeholder="选择异常..." class="chosen-select" style="width: 165px;">
								<option value="">--请选择--</option>
								<option value="0">处理成功</option>
								<optgroup td-label="异常">
									<option value="1">系统异常</option>
									<option value="2">ORM异常</option>
									<option value="3">运行时异常</option>
									<option value="4">业务异常</option>
									<option value="10">其他异常</option>
								</optgroup>
							</select>
						</td>
						<td class="td-label">模块</td>
						<td>
							<select id="module" name="module" style="width: 165px;">
								<option value="">--请选择--</option>
								<option value="部门">部门</option>
								<option value="用户">用户</option>
								<option value="角色">角色</option>
								<option value="字典">字典</option>
								<option value="字典元素">字典元素</option>

							</select>
						</td>
						<td class="td-label">操作信息</td>
						<td>
							<input type="text" id="info" name="info" style="width:160px;"/>
						</td>
					</tr>
					<tr>

						<td class="td-label">操作人</td>
						<td>
							<input type="text" id="userName" name="userName" style="width:160px;" value="${item.userName}" />

						</td>
						<td class="td-label">操作时间</td>
						<td>
							<input type="text" style="width:130px;" id="startTime" name="startTime" />
							至
							<input type="text" style="width:130px;" id="endTime" name="endTime" />
						</td>
						<td class="td-label">IP</td>
						<td>
							<input type="text" id="ip" name="ip" style="width:160px;" />
						</td>
					</tr>
					<tr>
						<td class="td-label">类名</td>
						<td>
							<input type="text" id="clazz" name="clazz" style="width:160px;" />
						</td>
						<td class="td-label">方法名</td>
						<td>
							<input type="text" id="method" name="method" style="width:160px;" />
						</td>
						<td class="td-label">调用耗时</td>
						<td>
							<label class="label label-danger">></label><input type="text" id="costTime" name="costTime"  onkeyup="value=value.replace(/[^\d]/g,'')" style="width:135px;margin-left: 5px;" />
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