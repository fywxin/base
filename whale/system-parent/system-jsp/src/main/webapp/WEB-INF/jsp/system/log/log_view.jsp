<%@page import="org.whale.system.common.util.TimeUtil"%>
<%@page import="org.whale.system.domain.Log"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>查看 日志</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
	    	$.closeWin();
	    	return false; 
	    	}
	    }
	 ]
	});
});
</script>

</head>
    
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<input type="hidden" id="id" name="id" value="${item.id}" />
				<table>
					<col width="10%"/>
					<col width="40%"/>
					<col width="10%"/>
					<col width="40%"/>
					<tbody>
						<tr>
							<td class="td-label">操作类型</td>
							<td class="td-value">
								<c:if test="${item.opt == 'save' }">新增</c:if>
								<c:if test="${item.opt == 'update' }">修改</c:if>
								<c:if test="${item.opt == 'delete' }">删除</c:if>
								<c:if test="${item.opt == 'saveBatch' }">批量新增</c:if>
								<c:if test="${item.opt == 'updateBatch' }">批量修改</c:if>
								<c:if test="${item.opt == 'deleteBatch' }">批量删除</c:if>
							</td>
							<td class="td-label">对象名称</td>
							<td class="td-value">
								${item.cnName}
							</td>
						</tr>
						<tr>
							<td class="td-label">表名称</td>
							<td class="td-value">
								${item.tableName}
							</td>
							<td class="td-label">ip地址</td>
							<td class="td-value">
								${item.ip}
							</td>
						</tr>
						<tr>
							<td class="td-label">操作人</td>
							<td class="td-value">
								${userName}
							</td>
							<td class="td-label">创建时间</td>
							<td class="td-value">
								<%
									Log log = (Log)request.getAttribute("item");
									out.print(TimeUtil.formatTime(log.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
								%>
							</td>
						</tr>
						<tr>
							<td class="td-label">uri</td>
							<td class="td-value">
								${item.uri}
							</td>
							<td class="td-label">调用链顺序</td>
							<td class="td-value">
								${item.callOrder}
							</td>
						</tr>
						<tr>
							<td class="td-label">方法耗时</td>
							<td class="td-value">
								${item.methodCostTime}(ms)
							</td>
							<td class="td-label">总耗时</td>
							<td class="td-value">
								${item.costTime}(ms)
							</td>
						</tr>
						<tr>
							<td class="td-label">处理结果</td>
							<td class="td-value" colspan="3">
								<c:if test="${item.rsType == 1 }"><span class='sgreen'>正常</span></c:if>
								<c:if test="${item.rsType == 2 }"><span class='sred'>系统异常</span></c:if>
								<c:if test="${item.rsType == 3 }"><span class='sorange'>OrmException</span></c:if>
								<c:if test="${item.rsType == 4 }"><span class='sorange'>运行时异常</span></c:if>
								<c:if test="${item.rsType == 5 }"><span class='sred'>业务异常</span></c:if>
								<c:if test="${item.rsType == 0 }"><span class='sgray'>未知异常</span></c:if>
							</td>
						</tr>
						<tr>
							<td class="td-label">数据</td>
							<td class="td-value" colspan="3">
								<div style="height:300px;" class="textAreaDiv">${item.datas}</div>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>
