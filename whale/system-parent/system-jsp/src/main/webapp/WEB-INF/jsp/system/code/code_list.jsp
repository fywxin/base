<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>用户列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script language="javascript">

var typeData = [{ type: 'String', text: 'String' }, { type: 'Long', text: 'Long' },{ type: 'Integer', text: 'Integer' },
                { type: 'Boolean', text: 'Boolean' },{ type: 'Date', text: 'Date' },
                { type: 'Float', text: 'Float' },{ type: 'Double', text: 'Double' },{ type: 'Short', text: 'Short' },
                { type: 'Byte', text: 'Byte' },{ type: 'Char', text: 'Char' }];

var isIdData = [{isId:true, text:true},{isId:false, text:false}];

var optData = [{opt: "1000", text:"主键"},{opt: "0100", text:"非空"},{opt: "0010", text:"非更新"},{opt: "0001", text:"非重复"},
				{opt: "0110", text:"非空|非更新"},{opt: "0101", text:"非空|非重复"},{opt: "0011", text:"非更新|非重复"},
				{opt: "0111", text:"非空|非更新|非重复"}, {opt: "0000", text:"可空|可更新|可重复"}];

var showData = [{show: "111", text:"表单|列表|查询"},{show: "100", text:"表单"},{show: "010", text:"列表"},
               {show: "110", text:"表单|列表"},{show: "011", text:"列表|查询"},{show: "000", text:"不展示"}];
                
var attrs = {datas: ${attrs}, total : ${total}};                
var grid = null;

$(function(){
	grid = $("#grid").ligerGrid({
        columns: [
        { display: '中文名', name: 'cnName', minWidth: 150, 
            editor: { type: 'text' }
        },
        { display: '字段名', name: 'name', minWidth: 150, 
            editor: { type: 'text' }
        },
        { display: '数据库名', name: 'sqlName', minWidth: 150,
            editor: { type: 'text' }
        },
        { display: '字段类型', minWidth: 100, name: 'type',
            editor: { type: 'select', data: typeData, valueColumnName: 'type'}
        },
        { display: '主键', minWidth: 80, name: 'isId',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.isId === true) return true;
            	return false;
            }}
        },
        { display: '可空', minWidth: 80, name: 'nullAble',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.nullAble === true) return true;
            	return false;
            }}
        },
        { display: '可更新', minWidth: 80, name: 'updateAble',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.nullAble === true) return true;
            	return false;
            }}
        },
        { display: '唯一', minWidth: 80, name: 'uniqueAble',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.nullAble === true) return true;
            	return false;
            }}
        },
        { display: '表单', minWidth: 80, name: 'inForm',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.nullAble === true) return true;
            	return false;
            }}
        },
        { display: '列表', minWidth: 80, name: 'inList',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.nullAble === true) return true;
            	return false;
            }}
        },
        { display: '查询', minWidth: 80, name: 'inQuery',
            editor: { type: 'select', data: isIdData, valueColumnName: 'isId',render: function (item){
            	if(item.nullAble === true) return true;
            	return false;
            }}
        },
        { display: '属性排序', name: 'inOrder', minWidth: 80, type: 'int', editor: { type: 'int'} },
        { display: '@Order排序', name: 'orderNum', minWidth: 80, type: 'int', editor: { type: 'int'} }
        ],
        toolbar: {items: [
            {text: '新增一行', icon: 'add', click: function(){addTr();}},
            {text: '删除选择行', icon: 'del', click: function(){delTr();}},
          	{text: '创建对象', icon: 'config', click: function(){newTable();} },
          	{text: '删除对象', icon: 'del', click: function(){delTable();} },
          	{text: '保存且生成代码', icon: 'right', click: function(){doSave(true);}},
			{text: '保存对象', icon: 'save', click: function(){doSave(false);}}
        ]},
        checkbox : true,
        enabledEdit: true, 
        clickToEdit: true,   
        data:attrs,
        width: '100%',
        usePager: false,
        height: ($.h()-105)
    });   
	
});


function addTr(){
	var data = grid.getData();
	var inOrder = -1;
	if(data.length > 0){
		for(var i=0; i<data.length; i++){
			if(data[i].inOrder > inOrder){
				inOrder = data[i].inOrder;
			}
		}
	}
	grid.addRow({
		cnName: '', 
		name: '', 
		sqlName: '', 
		type: 'String', 
		isId : false,
		nullAble : true,
		updateAble : true,
		uniqueAble : false,
		inForm : true,
		inList: true,
		inQuery: false,
        inOrder: 1+inOrder,
        orderNum: 0 
    });
}

function delTr(){
	grid.deleteSelectedRow();
}

function newTable(){
	$.confirm({info: "创建表格会丢失当前数据，你确定要继续吗？", ok: function(){
		$("#tcnName, #tname, #dbName, #clazzName, #pkgName").val("");
		$("#isTree").val(false);
		$("#clazzSel").val("");
		grid.loadData({datas:[{"cnName":"id","dbType":-5,"defVal":"","inForm":false,"inList":false,"inOrder":1,"inQuery":false,"isId":true,"name":"dictId","nullAble":false,"orderNum":0,"preci":0,"sqlName":"dictId","type":"Long","uniqueAble":true,"updateAble":false,"width":0}], total:1});
	}});
}

function delTable(){
	var clazzName = $("#clazzName").val();
	if(clazzName == null || clazzName == ""){
		$.alert('请选择带清空的对象');
		return ;
	}
	$.del({url:"${ctx}/code/doDelete?clazzName"+clazzName, onSuccess: function(){
		window.location.href="${ctx}/code/goList";
	}});
}

function getSelected(){ 
    var row = grid.getSelectedRow();
    if (!row) { alert('请选择行'); return; }
    alert(JSON.stringify(row));
}

function doSave(code){
	var data = grid.getData();
   	$("#attrVals").val((JSON.stringify(data)));
    var datas = $("#dataForm").serialize();
	
	$.ajax({
		url:'${ctx}/code/doSave?code='+code,
		type: 'post',
		data: datas,
		dataType: 'json',
		cache: false,
		error: function(obj){
			$.alert('保存数据出错~');
	    },
	    success: function(obj){
	    	if(obj.rs){
				$.info(obj.msg);
	    	}else{
	    		$.alert(obj.msg);
	    	}
	    }
	 });
}

function create(){
	var datas = $("#dataForm").serialize();
	$.ajax({
		url:'${ctx}/code/doFtl',
		type: 'post',
		data: datas,
		dataType: 'json',
		cache: false,
		error: function(obj){
			$.alert('创建代码出错~');
	    },
	    success: function(obj){
	    	if(obj.rs){
				$.info(obj.msg);
	    	}else{
	    		$.alert(obj.msg);
	    	}
	    }
	 });
}

function reloadWin(){
	window.location.href="${ctx}/code/goList?clazzName="+$("#clazzSel").val();
}

</script>
</head>
<body style="overflow: hidden;">
	<div class="edit-form">
	<form action="" id="dataForm">
		<input type="hidden" id="attrVals" name="attrVals"/>
		<table id="tableTable">
			<col width="8%" />
			<col width="25%"/>
			<col width="8%" />
			<col width="25%"/>
			<col width="8%" />
			<col />
			<tbody>
				<tr>
					<td class="td-label">中文名</td>
					<td class="td-value"><input type="text" id="tcnName" name="tcnName" style="width:160px;" value="${domain.cnName }" /></td>
					<td class="td-label">实体名</td>
					<td class="td-value"><input type="text" id="tname" name="tname" style="width:160px;"  value="${domain.name }" /></td>
					<td class="td-label">数据库表</td>
					<td class="td-value"><input type="text" id="dbName" name="dbName" style="width:160px;"  value="${domain.dbName }" /></td>
				</tr>
				<tr>
					<td class="td-label">类名</td>
					<td class="td-value"><input type="text" id="clazzName" name="clazzName" style="width:260px;" value="${domain.clazzName }" /></td>
					<td class="td-label">包名</td>
					<td class="td-value"><input type="text" id="pkgName" name="pkgName" style="width:160px;" value="${pkgName }" /></td>
					<td class="td-label">是否树</td>
					<td class="td-value">
						<select name="isTree" id="isTree" style="width: 160px;">
							<option value="false" <c:if test="${!domain.isTree }">selected="selected"</c:if> >否</option>
							<option value="true" <c:if test="${domain.isTree }">selected="selected"</c:if> >是</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td-label"></td>
					<td class="td-value" colspan="5">
						<select style="width:165px;" id="clazzSel" name="clazzSel" onchange="reloadWin()">
		                	${options }
		                </select>
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