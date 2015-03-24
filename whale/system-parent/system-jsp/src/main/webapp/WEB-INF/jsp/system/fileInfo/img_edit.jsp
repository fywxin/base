<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>缩放截图</title>
<%@include file="/html/jsp/common.jsp" %>
<script type="text/javascript">
var imgSel;
var emptyRs = {x1: 0,y1: 0,width:0, height:0};
var rs = emptyRs;
var flag = true;

var toolBar = null;
$(function(){
	toolBar = $("#toolbar").ligerToolBar({ items: [
	    {id: 'saveBut', text: '保存', icon:'save', click: function(){save();}},
	    {line: true},
	    {id:"enlargeBut", text:"放大", icon:"enlarge", click: function(){enlarge('1','1');}},
	    {line: true},
	    {id:"reduceBut", text:"缩小", icon:"reduce", click: function(){enlarge('-1','-1');}},
	    {line: true},
	    {id:"restoreBut", text:"还原", icon:"restore", click: function(){resetImg('${fileInfo.width }','${fileInfo.height }');}},
	    {line: true},
	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){$.closeWin();return false; }}
	 ]
	});
	
	var pageHeight = $.h()-30;
	if(pageHeight < 360)
		pageHeight = 360;
	$("#imgFrame,#imgTable").css("height",pageHeight);
	$("#emptyDiv").css("height",pageHeight-180)
	
	$("#userWidth,#userHeight").keydown(function(event){
		if(event.keyCode == 13){
			resetImg(parseInt($('#userWidth').val()), parseInt($('#userHeight').val()));
		}
	}).blur(function(event){
		resetImg(parseInt($('#userWidth').val()), parseInt($('#userHeight').val()));
	});
});


function resetImg(width, height){
	zoom({radioX:0, radioY: 0, width: width, height: height});
	rs = emptyRs;
}

function enlarge(radioX, radioY){
	zoom({radioX:parseInt(radioX), radioY: parseInt(radioY), width: 0, height:0});
}

function zoom(param){
	var e={data:{stepX:param.radioX, stepY: param.radioY, width: param.width, height: param.height}};
	imgSel.zooming(e);
	$("#userWidth").val(imgFrame.getPicInfo().width);
	$("#userHeight").val(imgFrame.getPicInfo().height);
	
	$("#widthRadio").html(Math.round(imgFrame.getPicInfo().width/${fileInfo.width }*100) /100);
	$("#heightRadio").html(Math.round(imgFrame.getPicInfo().height/${fileInfo.height }*100) /100);
	rs = emptyRs;
}

function preview(img, selection) {
	rs = selection;
    $("#userWidth").val(selection.width);
    $("#userHeight").val(selection.height);
}

function save(){
	if(imgFrame.getPicInfo().width == ${fileInfo.width } && imgFrame.getPicInfo().height == ${fileInfo.height } && rs.x1 == 0){
		$.getWindow().close();
		return ;
	}
	var userWidth = parseInt($('#userWidth').val());
	var userHeight = parseInt($('#userHeight').val());
	
	if(userWidth<1 || userHeight<1){
		$.alert("图片长宽值不能小于1");
		return ;
	}
	
	if(userWidth>2000 || userHeight>2000){
		$.alert("图片长宽值不能大于2000");
		return ;
	}

	var datas = {fileId: "${fileId}",nodeX: rs.x1, nodeY: rs.y1, nodeW: rs.width,nodeH: rs.height, width: userWidth,height: userHeight,divWidth: imgFrame.getPicInfo().width,divHeight: imgFrame.getPicInfo().height};
	$.save({url:'${ctx}/fileInfo/doEditImg', datas: datas, onSucess: function(obj){
		$.alert(obj.msg);
		$.getWinOpener().changeImage_${tagId}(obj.datas);;
		$.closeWin();
		
	}}); 
	
}
</script>
</head>
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
		<div class="infoBox" id="infoBoxDiv"></div>
		<div class="edit-form">
			<table id="imgTable">
				<col  width="" />
				<col  width="200px"/>
				<tbody>
					<tr>
						<td>
							<iframe id="imgFrame" name="imgFrame" src="${ctx }/fileInfo/goSplitImg?fileId=${fileId }&finalWidth=${finalWidth }&finalHeight=${finalHeight }"
								width="100%" height="100%" frameborder="0" scrolling="auto" ></iframe>
						</td>
						<td>
							<div style="height: 95px;border:1px solid #879DB1;">
								<span style="color: #183152;font-weight: bold;line-height: 26px;">图片信息：</span><br/>
								<span style="margin-left:5px;">宽度:</span>
								<input type="text" id="userWidth" name="userWidth" value="${fileInfo.width }" title="截取图片宽度,最大2000px" onkeyup="value=value.replace(/[^\d]/g,'')" value="${finalWidth}" style="padding-left:5px;width: 35px;" maxlength="4"/>(px) <br/>
								<span style="margin-left:5px;">高度:</span>
								<input type="text" id="userHeight" name="userHeight" value="${fileInfo.height }" title="截取图片高度,最大2000px" onkeyup="value=value.replace(/[^\d]/g,'')" value="${finalHeight}" style="padding-left:5px;width: 35px;" maxlength="4"/>(px)<br/>
								<span style="margin-left:5px;">缩放:</span>
								<span id="widthRadio" style="padding:0px 2px 0px 2px;">1</span> : <span id="heightRadio" style="padding:0px 2px 0px 2px;">1</span> (倍)<br/>
							</div>
							
							<div style="height: 80px;border:1px solid #879DB1;margin-top: 2px;">
								<span style="color: #183152;font-weight: bold;line-height: 26px;">原图信息：</span><br/>
								<span style="margin-left:5px;">名称：</span> ${fileInfo.fileName }<br/>
								<span style="margin-left:5px;">像素：</span> ${fileInfo.width } * ${fileInfo.height } <br/>
								<span style="margin-left:5px;">大小：</span> ${fileInfo.sizeKB } (KB) <br/>
							</div>
							<div id="emptyDiv">
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
</body>
</html>