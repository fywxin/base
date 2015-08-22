<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>用户列表</title>
<%@include file="/html/jsp/common.jsp" %>
<style type="text/css">
.result{clear:both; width:100%; border-spacing:0px; *border-spacing:expression(this.cellSpacing="0px"); border-collapse:collapse; font-size:12px; text-align:center; }
.result thead{font-weight:bold; background:#F4F5F7; }

.result td{padding:2px 0; border-width:1px; border-style:solid; border-color:#A3C0E8;text-align: left;padding-left: 5px;}
.result th{padding:5px 0; text-align: center; font-weight: bold;border-width:1px; border-style:solid; border-color:#A3C0E8;background: #E2F0FF url('${resource}/ui/skins/Aqua/images/grid/header-bg.gif') repeat-x left bottom;overflow: hidden;}
.result tbody tr:hover{background:#C1DBFA;}
.myButtom{text-decoration: none;background-color: #ddd;background-image: linear-gradient(#eee, #ddd);background-repeat: repeat-x;border-color: #ccc;position: relative;display: inline-block;padding: 7px 25px;font-size: 13px;font-weight: bold;color: #333;text-shadow: 0 1px 0 rgba(255,255,255,0.9);white-space: nowrap;vertical-align: middle;cursor: pointer;border: 1px solid #d5d5d5;border-radius: 3px;-webkit-user-select: none;box-sizing: border-box;margin-left: 25px;}
</style>

<script>
function save(){
	doSave(false);
}

function code(){
	doSave(true);
}

function doSave(gen){
	if(!$("#dataForm").valid()) {return false;}
	var pass = true;
	$("input[name='domainName']").each(function(index){
	    if ($.trim($(this).val()) == "") {
	        $(this)[0].focus();
	        pass = false;
	    }
	});
	
	$("input[name='cnName']").each(function(index){
	    if ($.trim($(this).val()) == "") {
	        $(this)[0].focus();
	        pass = false;
	    }
	});
	
	if(!pass){
		return false;
	}
	
	$.ajax({
		url:'${ctx}/code/doSave?gen='+gen,
		type: 'post',
		dataType: 'json',
		data: $("#dataForm").serialize(),
		error: function(obj){
			alert('保存数据出错~');
	    },
	    success: function(obj){
	    	$("input[type='checkbox']").each(function(){
	    	    if ($(this).val() == 0) {
	    	        $(this).val(1).removeAttr("checked"); 
	    	    }
	    	});
	    	if(obj.rs){
	    		alert(obj.msg);
	    	}else{
	    		alert(obj.msg);
	    	}
	    }
	 });
}


function reloadWin(){
	window.location.href="${ctx}/code/goList?tableName="+$("#domainSqlName").val();
}

function del(){
	$.ajax({
		url:'${ctx}/code/doDelete?id=${domain.id}',
		type: 'post',
		dataType: 'json',
		error: function(obj){
			alert('删除出错~');
	    },
	    success: function(obj){
	    	if(obj.rs){
	    		alert("删除成功");
	    		window.location.reload();
	    	}else{
	    		alert(obj.msg);
	    	}
	    }
	 });
}

$(function() {
	$("#dataForm").validate({
		rules: {
			"domainName": {
				validIllegalChar: true,
				required: true
			},
			"domainCnName": {
				required: true
			},
			"pkgName":{
				required: true
			}
		}
	});
	
	$("input[type='checkbox']").change(function(){
	    if ($(this).is(':checked')) {
	        $(this).attr("checked", "checked"); 
	    }else{
	    	$(this).removeAttr("checked"); 
	    }
	});
});

function pid(){
	if($("#ftlType").val() == 5 || $("#ftlType").val() == 6){
		$("#domainPId").show();
	}else{
		$("#domainPId").hide();
	}
}
</script>
</head>
<body style="overflow-x: hidden;">
<form action="" id="dataForm">
	<div class="edit-form">
		<input type="hidden" id="attrVals" name="attrVals"/>
		<input type="hidden" id="id" name="id" value="${domain.id }" />
		<table id="tableTable">
			<col width="8%" />
			<col width="25%"/>
			<col width="8%" />
			<col width="25%"/>
			<col width="8%" />
			<col />
			<tbody>
				<tr>
					<td class="td-label">业务表名</td>
					<td class="td-value">
						<select style="width:165px;" id="domainSqlName" name="domainSqlName" onchange="reloadWin()">
		                	<c:forEach items="${tables }" var="item">
		                		<option value="${item.name }" <c:if test="${domain.domainSqlName == item.name }">selected="selected"</c:if> >${item.name } | ${item.comments }</option> 
		                	</c:forEach>
		                </select>
					</td>
					<td class="td-label"><span class="required">*</span>实体名</td>
					<td class="td-value"><input type="text" id="domainName" name="domainName" style="width:160px;"  value="${domain.domainName }" /></td>
					<td class="td-label"><span class="required">*</span>中文名</td>
					<td class="td-value"><input type="text" id="domainCnName" name="domainCnName" style="width:160px;" value="${domain.domainCnName }" /></td>
				</tr>
				<tr>
					<td class="td-label"><span class="required">*</span>基础包路径</td>
					<td class="td-value"><input type="text" id="pkgName" name="pkgName" style="width:200px;" value="${domain.pkgName }" /></td>
					<td class="td-label">生产代码路径</td>
					<td class="td-value">
						<input type="text" name="codePath" id="codePath" style="width:200px;" value="${domain.codePath }" />
					</td>
					<td class="td-label">模板分类</td>
					<td class="td-value">
						<select name="ftlType" id="ftlType" style="width: 125px;" onchange="pid()">
							<option value="1" >增删改查(单表)</option>
							<option value="2" >增删改查(一对多)</option>
							<option value="3" >仅持久化层(dao/domain)</option>
							<option value="4" >仅后台层(dao/service/control)</option>
							<option value="5" >树结构表(一体)</option>
							<option value="6" >树结构表(左树右表)</option>
						</select>
						<input type="text" id="domainPId" name="domainPId" style="width: 80px;display: none;"   placeholder="树父Id字段名" ></input>
					</td>
				</tr>
			</tbody>
		</table>
	
</div>
	<div>
		<table class="result">
			<thead>
				<tr>
					<th>列名</th>
					<th>字段名</th>
					<th>中文名</th>
					<th>物理类型</th>
					<th>Java类型</th>
					<th>最大长度</th>
					<th>主键</th>
					<th>编辑</th>
					<th>可空</th>
					<th>唯一</th>
					<th>列表</th>
					<th>查询</th>
					<th>匹配方式</th>
					<th>表单</th>
					<th>表单类型</th>
					<th>字典</th>
					<th>排序</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${domain.attrs }" var="attr" varStatus="index_item">
				<tr>
					<td>
						${attr.sqlName }
						<input type="hidden" name="sqlName" value="${attr.sqlName }"/>
					</td>
					<td><input type="text" name="name" value="${attr.name }" style="width:120px;"/></td>
					<td><input type="text" name="cnName" value="${attr.cnName }" style="width:150px;"/></td>
					<td>
						${attr.dbType }
						<input type="hidden" name="dbType" value="${attr.dbType }" />
					</td>
					<td>
						<select name="type" style="width:80px;">
							<option value="String" <c:if test="${attr.type == 'String' }">selected="selected"</c:if> >String</option>
							<option value="Integer" <c:if test="${attr.type == 'Integer' }">selected="selected"</c:if> >Integer</option>
							<option value="Long" <c:if test="${attr.type == 'Long' }">selected="selected"</c:if> >Long</option>
							<option value="Double" <c:if test="${attr.type == 'Double' }">selected="selected"</c:if> >Double</option>
							<option value="Float" <c:if test="${attr.type == 'Float' }">selected="selected"</c:if> >Float</option>
							<option value="Date" <c:if test="${attr.type == 'Date' }">selected="selected"</c:if> >Date</option>
							<option value="Boolean" <c:if test="${attr.type == 'Boolean' }">selected="selected"</c:if> >Boolean</option>
						</select>
					</td>
					<td><input type="text" name="maxLength" value="${attr.maxLength }" style="width:60px;" title="[${attr.sqlName }]最大长度"/></td>
					<td>
						<select name="isId" title="[${attr.sqlName }]是否主键">
							<option value="1"  <c:if test="${attr.isId || (index_item.index == 0)}">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.isId && (index_item.index != 0)}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="isEdit" title="[${attr.sqlName }]是否可编辑">
							<option value="1"  <c:if test="${attr.isEdit }">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.isEdit}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="isNull" title="[${attr.sqlName }]是否可空">
							<option value="1"  <c:if test="${attr.isNull }">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.isNull}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="isUnique" title="[${attr.sqlName }]是否唯一">
							<option value="1"  <c:if test="${attr.isUnique }">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.isUnique}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="inList" title="[${attr.sqlName }]列表显示">
							<option value="1"  <c:if test="${attr.inList }">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.inList}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="inQuery" title="[${attr.sqlName }]列表查询条件显示">
							<option value="1"  <c:if test="${attr.inQuery }">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.inQuery}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="queryType" >
							<option value="=" <c:if test="${attr.queryType == '=' }">selected="selected"</c:if> >=</option>
							<option value="like" <c:if test="${attr.queryType == 'like' }">selected="selected"</c:if> >Like</option>
                       		<option value="!=" <c:if test="${attr.queryType == '!=' }">selected="selected"</c:if> >!=</option>
                            <option value=">" <c:if test="${attr.queryType == '>' }">selected="selected"</c:if> >&gt;</option>
                            <option value=">=" <c:if test="${attr.queryType == '>=' }">selected="selected"</c:if> >&gt;=</option>
                            <option value="<" <c:if test="${attr.queryType == '<' }">selected="selected"</c:if> >&lt;</option>
                            <option value="<=" <c:if test="${attr.queryType == '<=' }">selected="selected"</c:if> >&lt;=</option>
                            <option value="between" <c:if test="${attr.queryType == 'between' }">selected="selected"</c:if> >Between</option>
                            <option value="left_like" <c:if test="${attr.queryType == 'left_like' }">selected="selected"</c:if> >Left Like</option>
                            <option value="right_like" <c:if test="${attr.queryType == 'right_like' }">selected="selected"</c:if> >Right Like</option>
                          </select>
					</td>
					<td>
						<select name="inForm" title="[${attr.sqlName }]表单显示">
							<option value="1"  <c:if test="${attr.inForm }">selected="selected"</c:if>>是</option>
							<option value="0"  <c:if test="${!attr.inForm}">selected="selected"</c:if>>&nbsp;&nbsp;</option>
						</select>
					</td>
					<td>
						<select name="formType">
                        	<option value="input" <c:if test="${attr.formType == 'input'}">selected="selected"</c:if> >单行文本</option>
	                        <option value="textarea" <c:if test="${attr.formType == 'textarea'}">selected="selected"</c:if> >多行文本</option>
	                        <option value="select" <c:if test="${attr.formType == 'select'}">selected="selected"</c:if> >下拉选项</option>
                            <option value="radiobox" <c:if test="${attr.formType == 'radiobox' || attr.type == 'Boolean'}">selected="selected"</c:if> >单选按钮</option>
                            <option value="checkbox" <c:if test="${attr.formType == 'checkbox'}">selected="selected"</c:if> >复选框</option>
                            <option value="date" <c:if test="${item.type == 'Date' || attr.type == 'Date' }">selected="selected"</c:if> >日期选择</option> 
                            <option value="dict" <c:if test="${!empty item.dictName || attr.formType == 'dict' }">selected="selected"</c:if> >字典</option> 
                            <option value="file" <c:if test="${attr.formType == 'file'}">selected="selected"</c:if> >文件上传选择</option>
                            <option value="image" <c:if test="${attr.formType == 'image'}">selected="selected"</c:if> >图片上传选择</option>            
                        </select>
					</td>
					<td><input type="text" name="dictName" value="${attr.dictName }" value="" style="width:140px;"/></td>
					<td><input type="text" name="inOrder" value="${attr.inOrder }" style="width:60px;"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	</form>
	<div align="center" style="margin-top: 15px;">
		<input type="button" class="myButtom" value="保存" onclick="save()"/>
		<input type="button" class="myButtom" value="生成代码" onclick="code()"/>
		<input type="button" class="myButtom" value="删除" onclick="del()"/>
	</div>
</body>

</html>				