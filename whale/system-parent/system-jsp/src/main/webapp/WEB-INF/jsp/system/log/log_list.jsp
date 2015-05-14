<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>日志列表</title>
<%@include file="/html/jsp/common.jsp"%>
<script type="text/javascript">
var rsStatus = {1:"<span class='sgreen'>正常</span>",2:"<span class='sred'>系统异常</span>",3:"<span class='sorange'>OrmException</span>",4:"<span class='sorange'>运行时异常</span>",5:"<span class='sred'>业务异常</span>",6:"<span class='sgray'>未知异常</span>"};
var time = new Date();
$(function (){
    $.grid({
    	url :'${ctx}/log/doList',
        columns: [
				{display: '操作', name: 'cnName', width: 70, frozen: true,render: function (row){
					return "<a href='#' onclick='view(\""+row.id+"\");'>查看</a>"
				} },
      	        {display: '对象名称', name: 'cnName', width: 120 },
      	        {display: '表名称', name: 'tableName', minWidth: 140},
      	        {display: 'uri', name: 'uri' },
      	        {display: '操作类型', name: 'opt' },
      	      	{display: '方法耗时(ms)', name: 'methodCostTime' },
    	        {display: '调用耗时(ms)', name: 'costTime' },
      	        {display: 'ip地址', name: 'ip' },
      	        {display: '创建时间', name: 'createTime',type: 'date', width: 170, render: function(row){
      				time.setTime(row.createTime);
      				return time.Format("yyyy-MM-dd hh:mm:ss.S");
      	        }},
      	        {display: '操作人', name: 'userName' },
      	        {display: '结果', name: 'rsType',
      	        	render: function (row){
      	        	    return rsStatus[row.rsType];
      	        	}
      	        }
              ]
	});
});

function view(id){
	$.openWin({url: "${ctx}/log/goView?id="+id,"title":'查看日志'});
}

</script>
</head>

<body style="overflow: hidden;">
	<div class="edit-form">
		<form id="queryForm" >
				<table>
						<col  width="8%" />
						<col  width="25%"/>
						<col  width="8%" />
						<col  width="25%"/>
						<col  width="8%" />
						<col  width="25%"/>
					<tbody>
						<tr>
							<td class="td-label">处理结果</td>
							<td class="td-value">
								<select id="rsType" name="rsType" style="width: 165px;">
									<option value="">--请选择--</option>
									<option value="1">处理正常</option>
									<option value="2">系统异常</option>
									<option value="3">ORM异常</option>
									<option value="4">运行时异常</option>
									<option value="5">业务异常</option>
									<option value="0">其他异常</option>
								</select>
							</td>
							<td class="td-label">操作类型</td>
							<td class="td-value">
								<select id="opt" name="opt" style="width: 165px;">
									<option value="">--请选择--</option>
									<option value="dll">变更操作</option>
									<optgroup label="详细变更操作" >
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
									<optgroup label="详细查询操作" >
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
							<td class="td-value">
								<input type="text" id="appId" name="appId" style="width:160px;" value="${item.appId}" />
							</td>
						</tr>
						<tr>
							<td class="td-label">表名称</td>
							<td class="td-value">
								<input type="text" id="tableName" name="tableName" style="width:160px;" value="${item.tableName}" />
							</td>
							<td class="td-label">uri</td>
							<td class="td-value">
								<input type="text" id="uri" name="uri" style="width:160px;" value="${item.uri}" />
							</td>
							<td class="td-label">操作人</td>
							<td class="td-value">
								<input type="text" id="userName" name="userName" style="width:160px;" value="${item.userName}" />
							</td>
						</tr>
						<tr>
							<td class="td-label">方法耗时</td>
							<td class="td-value">><input type="text" id="methodCostTime" name="methodCostTime" onkeyup="value=value.replace(/[^\d]/g,'')" style="width:160px;" value="${item.userName}" /></td>
						
							<td class="td-label">调用耗时></td>
							<td class="td-value">><input type="text" id="costTime" name="costTime" onkeyup="value=value.replace(/[^\d]/g,'')" style="width:160px;" value="${item.userName}" /></td>
						
							<td class="td-label">操作时间</td>
							<td class="td-value">
								<input type="text" style="width:160px;" id="startTime" name="startTime" class="i-date" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,maxDate:'#F{$dp.$D(\'endTime\')}'})"  value="${startTime }"/>
								至
								<input type="text" style="width:160px;" id="endTime" name="endTime" class="i-date" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,minDate:'#F{$dp.$D(\'startTime\')}'})" value="${endTime }"/>
							</td>
							</tr>
					</tbody>
				</table>
			</form>
	</div>
	<div id="grid" style="margin: 2px;"></div>
	<div style="display: none;"></div>
</body>
</html>