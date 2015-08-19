<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/html/jsp/frame.jsp" %>

<script type="text/javascript">
var rsStatus = {1:"<button type='button' class='btn btn-primary btn-xs'>成功</button>",
				2:"<button type='button' class='btn btn-warn btn-xs'>系统异常</button>",
				3:"<button type='button' class='btn btn-warn btn-xs'>OrmException</button>",
				4:"<button type='button' class='btn btn-info btn-xs'>运行时异常</button>",
				5:"<button type='button' class='btn btn-danger btn-xs'>业务异常</button>",
				6:"<button type='button' class='btn btn-info btn-xs'>未知异常</button>"};
var time = new Date();
$(function (){
	$(window).resize(function(){
		$("#gridTable").jqGrid('setGridWidth', $("#navbarDiv").width()-20).jqGrid('setGridHeight', $.h()-115-$("#queryForm").height());
	});
	
	$("#gridTable").jqGrid({
		url: "${ctx}/log2/doList",
		datatype: "json",
		colNames: ['操作', '对象名称', '表名称', 'uri', '操作类型', '方法耗时(ms)','调用耗时(ms)', 'ip地址', '创建时间', '操作人', '结果'],
		colModel: [{name:'myac',index:'', width:150, fixed:true, sortable:false, resize:false, align: "center",
					formatter: function(cellvalue, options, rowObject){
						return "<button type='button' class='btn btn-default btn-xs'><i class='fa fa-pencil'></i> 修  改</button>  <button type='button' class='btn btn-default btn-xs'><i class='fa fa-minus'></i> 删  除</button>";
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
			],
		rowNum : 20,
		rowList : [ 10, 20, 30, 50],
		pager : '#gridPager',
		height: $.h()-150-$("#queryForm").height(),
		repeatitems: false,
		altRows: true,
		autowidth: true,
		mtype : "post"
	});
	
});

function view(id){
	$.openWin({url: "${ctx}/log/goView?id="+id,"title":'查看日志'});
}

</script>
		
        <div class="row" style="background-color: white;">
        <div style="margin: 10px">
		<form id="queryForm" >
				<table class="query">
						<col  width="8%" />
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
								><input type="text" id="methodCostTime" name="methodCostTime" onkeyup="value=value.replace(/[^\d]/g,'')" style="width:130px;padding:1px;" />
								
							</td>
						
							<td class="td-label">调用耗时></td>
							<td>
								> <input type="text" id="costTime" name="costTime"  onkeyup="value=value.replace(/[^\d]/g,'')" style="width:130px;padding:1px;" />
							
							</td>
						
							<td class="td-label">操作时间</td>
							<td>
								<input type="text" style="width:120px;" id="startTime" name="startTime" class="i-date" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,maxDate:'#F{$dp.$D(\'endTime\')}'})"  value="${startTime }"/>
								至
								<input type="text" style="width:120px;" id="endTime" name="endTime" class="i-date" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,minDate:'#F{$dp.$D(\'startTime\')}'})" value="${endTime }"/>
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
        <button type="button" class="btn btn-success btn-xs"><i class="fa fa-search"></i> 查  询</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
			<div style="margin:3px 0px;border: 1px solid #CCCCCC; padding:3px 7px">
			  <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-plus"></i> 新  增</button>
			  <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-pencil"></i> 修  改</button>
			  <button type="button" class="btn btn-danger btn-sm"><i class="fa fa-minus"></i> 删  除</button>
			</div>
			
				<table id="gridTable" ></table>
				<div id="gridPager" style="height:35px;"></div>
			</div>
			</div>
<%@include file="/html/jsp/foot.jsp" %>