<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<title>缩放截图</title>
	
<link rel="stylesheet" type="text/css" href="${resource}/plugin/imgSelect/img.css" />
<script type="text/javascript" src="${resource}/com/jquery-1.7.2.js"></script>
<script type="text/javascript" src="${resource}/plugin/imgSelect/jquery.imgareaselect.js"></script>

<script type="text/javascript">
var vPath = "${resource}";

$(document).ready(function () {
	parent.imgSel = $('#imgSrcObj').imgAreaSelect({
	fadeSpeed: 200,
	instance: true,
	handles: true, 
	onSelectChange: preview
	});
});

function preview(img, selection){
	window.parent.preview(img, selection);
}

function getPicInfo(){
	return {"width": parseInt($("#imgSrcObj").css("width")),"height":parseInt($("#imgSrcObj").css("height"))};
}
</script>
</head>

<body style="padding:0;margin:0;">
	<div style="float:left" id="imgDiv">
		<img id="imgSrcObj" src="${fileInfo.urlPath }" width="${fileInfo.width }px" height="${fileInfo.height }px" border="0" />
	</div>
</body>
</html>
