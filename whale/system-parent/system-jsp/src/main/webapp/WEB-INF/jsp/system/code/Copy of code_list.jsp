<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>用户列表</title>
<%@include file="/html/jsp/common.jsp" %>
<script language="javascript">

$(function(){
	tolTr();
	$("#clazzSel").change(function(){
		window.location.href = "${ctx}/code/goList?clazzName="+$(this).val();
	})
	
});


function add(){
	$.openWin({url: "${ctx}/user/goSave","title":'新增用户'});
}

function doclear(){
	var clazzName = $("#clazzName").val();
	if(clazzName == null || clazzName == ""){
		$.alert('请选择带清空的对象');
		return ;
	}
	$.ajax({
		url:'${ctx}/code/doClear?clazzName='+clazzName,
		type: 'post',
		data: null,
		dataType: 'json',
		cache: false,
		error: function(obj){
			$.alert('保存数据出错~');
	    },
	    success: function(obj){
	    	if(obj.rs){
				$.info(obj.msg);
				window.location.href = "${ctx}/code/goList?clazzSel="+clazzName;
	    	}else{
	    		$.alert(obj.msg);
	    	}
	    }
	 });
}

function tolTr(){
	var trs = $("#gridTable > tbody > tr");
	trs.each(function(){
		$(this).hover(function(){
			$(this).addClass("hover");
		},function(){
			$(this).removeClass("hover");
		}).click(function(){
			trs.filter(".selected").removeClass("selected");
			$(this).addClass("selected");
		});
	});
}

function save(){
	var datas = $("#dataForm").serialize();
	$.ajax({
		url:'${ctx}/code/doSave',
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

function newTable(){
	$("#gridTable > tbody").empty();
	$("input[type='text']").val("");
}

function addTr(){
	var index= $("#gridTable > tbody > tr:last").attr("index");
	if(index == "" || index == null){
		index = 0;
	}
	index = parseInt(index);
	index1 = index+1;
	var html = "<tr index="+index1+" >"+
					"<td class=\"td-seq\">"+index1+"</td>"+
					"<td><input name=\"cnName\" /></td>"+
					"<td><input name=\"name\" /></td>"+
					"<td><input name=\"sqlName\" /></td>"+
					"<td>"+
						"<select name=\"type\" >"+
							"<option value=\"String\" >String</option>"+
							"<option value=\"Long\" >Long</option>"+
							"<option value=\"Integer\" >Integer</option>"+
							"<option value=\"Float\" >Float</option>"+
							"<option value=\"Double\" >Double</option>"+
							"<option value=\"Short\" >Short</option>"+
							"<option value=\"Byte\" >Byte</option>"+
							"<option value=\"Char\" >Char</option>"+
							"<option value=\"Date\" >Date</option>"+
							"<option value=\"Boolean\" >Boolean</option>"+
						"</select>"+
					"</td>"+
					"<td>"+
						"<select name=\"dbType\" >"+
							"<option value=\"12\" >VARCHAR</option>"+
							"<option value=\"4\" >INTEGER</option>"+
							"<option value=\"93\" >TIMESTAMP</option>"+
							"<option value=\"91\" >DATE</option>"+
							"<option value=\"92\" >TIME</option>"+
							"<option value=\"-2\" >BINARY</option>"+
							"<option value=\"1\" >CHAR</option>"+
							"<option value=\"6\" >FLOAT</option>"+
							"<option value=\"8\" >DOUBLE</option>"+
							"<option value=\"-5\" >BIGINT</option>"+
							"<option value=\"-6\" >TINYINT</option>"+
						"</select>"+
					"</td>"+
					"<td><input name=\"width\" /></td>"+
					"<td><input name=\"preci\" value=0 /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"isId\"  /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"nullAble\"  /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"updateAble\" checked=\"checked\" /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"uniqueAble\"  /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"inList\" checked=\"checked\" /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"inForm\" checked=\"checked\" /></td>"+
					"<td><input type=\"checkbox\" value=\""+index+"\" name=\"inQuery\"  /></td>"+
					"<td><input name=\"inOrder\" value=\""+index1+"\" /></td>"+
				"</tr>";
				
		$("#gridTable > tbody").append(html);
		tolTr();
}

function delTr(){
	$("#gridTable > tbody > tr.selected").remove();
}
</script>
</head>
    
<body>
	<div class="body-box-list" id="bodyBoxDiv">
		<div class="pageContent">
			<!--panelBar-->
			<div class="panelBar" id="panelBarDiv">
            	<ul>
            		<li>
                    	<a href="#" class="a1" onclick="newTable()">
					       <span>新建实体</span>
						</a>	
                    </li>
            		<li>
                    	<a href="#" class="a1" onclick="addTr()">
					       <span>增加一行</span>
						</a>	
                    </li>
                    <li>
                    	<a href="#" class="a3" onclick="delTr()">
					       <span>删除一行</span>
						</a>	
                    </li>
                	<li>
                    	<a href="#" class="save" onclick="save()">
					       <span>保存实体</span>
						</a>	
                    </li>
                    <li>
                    	<a href="#" class="a3" onclick="doclear()">
					       <span>清空实体</span>
						</a>	
                    </li>
                	<li>
                    	<a href="#" class="a1" onclick="create()">
					       <span>生产代码</span>
						</a>	
                    </li>
                    <li class="line"></li>
                </ul>
                
				<div class="clear_float"> </div>
			</div>

			<div class="content-list">
			<form action="" id="dataForm">
				<div class="query-form">
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
							<td class="td-value"><input type="text" id="pkgName" name="pkgName" style="width:160px;" value="${domain.pkgName }" /></td>
							<td class="td-label">是否树</td>
							<td class="td-value">
								<select name="isTree" id="isTree" style="width: 160px;">
									<option value="false" <c:if test="${!domain.isTree }">selected="selected"</c:if> >否</option>
									<option value="true" <c:if test="${domain.isTree }">selected="selected"</c:if> >是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="td-label">操作</td>
							<td class="td-value" colspan="5">
								<input type="checkbox" checked="checked" name="opt" value="save"/>新增
								<input type="checkbox" checked="checked" name="opt" value="update"/>修改
								<input type="checkbox" checked="checked" name="opt" value="delete"/>删除
								<input type="checkbox" checked="checked" name="opt" value="view"/>查看
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<table cellpadding="0" cellspacing="0" id="gridTable">
					<col width="2%"/>
					<col width="12%"/>
					<col width="12%"/>
					<col width="12%"/>
					<col width="8%"/>
					<col width="8%"/>
					<col width="6%"/>
					<col width="4%"/>
					<col />
					<col />
					<col />
					<col />
					<col />
					<col />
					<col />
					<col width="4%"/>
					<thead>
						<tr>
							<th>
								序号
							</th>
							<th>
								中文名
							</th>
							<th>
								字段名
							</th>
							<th>
								数据库名
							</th>
							<th>
								字段类型
							</th>
							<th>
								数据库类型
							</th>
							<th>
								最大长度
							</th>
							<th>
								小数点
							</th>
							<th>
								主键
							</th>
							<th>
								可空
							</th>
							<th>
								可更新
							</th>
							<th>
								唯一
							</th>
							<th>
								列表
							</th>
							<th>
								表单
							</th>
							<th>
								查询
							</th>
							<th>
								排列顺序
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${domain.attrs}"  varStatus="item_index">
						<tr index="${item_index.index+1}">
							<td class="td-seq">
								${item_index.index+1}
							</td>
							<td title="${item.cnName}">
								<input name="cnName" value="${item.cnName}"/>
							</td>
							<td title="${item.name}">
								<input name="name" value="${item.name}"/>
							</td>
							<td title="${item.sqlName}">
								<input name="sqlName" value="${item.sqlName}"/>
							</td>
							<td title="${item.type}">
								<select name="type">
									<option value="String" <c:if test="${item.type == 'String' }">selected="selected"</c:if> >String</option>
									<option value="Long" <c:if test="${item.type == 'Long' }">selected="selected"</c:if> >Long</option>
									<option value="Integer" <c:if test="${item.type == 'Integer' }">selected="selected"</c:if> >Integer</option>
									<option value="Float" <c:if test="${item.type == 'Float' }">selected="selected"</c:if> >Float</option>
									<option value="Double" <c:if test="${item.type == 'Double' }">selected="selected"</c:if> >Double</option>
									<option value="Short" <c:if test="${item.type == 'Short' }">selected="selected"</c:if> >Short</option>
									<option value="Byte" <c:if test="${item.type == 'Byte' }">selected="selected"</c:if> >Byte</option>
									<option value="Char" <c:if test="${item.type == 'Char' }">selected="selected"</c:if> >Char</option>
									<option value="Boolean" <c:if test="${item.type == 'Boolean' }">selected="selected"</c:if> >Boolean</option>
									<option value="Date" <c:if test="${item.type == 'Date' }">selected="selected"</c:if> >Date</option>
								</select>
							</td>
							<td title="${item.dbType}">
								<select name="dbType">
									<option value="-5" <c:if test="${item.dbType == -5 }">selected="selected"</c:if> >BIGINT</option>
									<option value="4" <c:if test="${item.dbType == 4 }">selected="selected"</c:if> >INTEGER</option>
									<option value="-6" <c:if test="${item.dbType == -6 }">selected="selected"</c:if> >TINYINT</option>
									<option value="6" <c:if test="${item.dbType == 6 }">selected="selected"</c:if> >FLOAT</option>
									<option value="8" <c:if test="${item.dbType == 8 }">selected="selected"</c:if> >DOUBLE</option>
									<option value="12" <c:if test="${item.dbType == 12 }">selected="selected"</c:if> >VARCHAR</option>
									<option value="1" <c:if test="${item.dbType == 1 }">selected="selected"</c:if> >CHAR</option>
									<option value="93" <c:if test="${item.dbType == 93 }">selected="selected"</c:if> >TIMESTAMP</option>
									<option value="91" <c:if test="${item.dbType == 91 }">selected="selected"</c:if> >DATE</option>
									<option value="92" <c:if test="${item.dbType == 92 }">selected="selected"</c:if> >TIME</option>
									<option value="-2" <c:if test="${item.dbType == -2 }">selected="selected"</c:if> >BINARY</option>
								</select>
							</td>
							<td title="${item.width}">
								<input name="width" value="${item.width}" style="width: 70px;" />
							</td>
							<td title="${item.preci}">
								<input name="preci" value="${item.preci}" style="width: 40px;" />
							</td>
							<td>
								<input type="checkbox" name="isId" value="${item_index.index}" <c:if test="${item.isId }">checked="checked"</c:if> />
							</td>
							<td>
								<input type="checkbox" name="nullAble" value="${item_index.index}" <c:if test="${item.nullAble }">checked="checked"</c:if> />
							</td>
							<td>
								<input type="checkbox" name="updateAble" value="${item_index.index}" <c:if test="${item.updateAble }">checked="checked"</c:if>/>
							</td>
							<td>
								<input type="checkbox" name="uniqueAble" value="${item_index.index}" <c:if test="${item.uniqueAble }">checked="checked"</c:if>/>
							</td>
							<td>
								<input type="checkbox" name="inList" value="${item_index.index}" <c:if test="${item.inList }">checked="checked"</c:if>/>
							</td>
							<td>
								<input type="checkbox" name="inForm" value="${item_index.index}" <c:if test="${item.inForm }">checked="checked"</c:if>/>
							</td>
							<td>
								<input type="checkbox" name="inQuery" value="${item_index.index}" <c:if test="${item.inQuery }">checked="checked"</c:if>/>
							</td>
							<td>
								<input type="text" name="inOrder" style="width: 70px;" value="${item_index.index+1}"/>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				</form>
			</div>
		</div> 
	</div>
</body>
</html>


