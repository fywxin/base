<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head >
<%@include file="/jsp/form.jsp" %>
<%@include file="/jsp/ztree.jsp" %>
<script type="text/javascript">
    var initSelIds = [];
	var zTree;

	var setting = {
			check: {
				enable: true,
				nocheckInherit: true
			},
			data: {
				simpleData: {
					enable:true,
					idKey: "id",
					pIdKey: "pId"
				},
				key:{
					name: "name"
				}
			}
		};
	
	var zNodes = [];
	var totalAuths = ${totalAuths};
	var hasAuths = ${hasAuths};
	var allMenus = ${allMenus}
	
	$(document).ready(function(){
		$("#treeDiv").height($.h()-120);
		zNodes.push({"id": 0, "pId": null, "name": "设置权限", "isParent": true,"open":true});
		
		for(var i=0; i<allMenus.length; i++){
			zNodes.push({"id": allMenus[i].menuId, "pId": allMenus[i].parentId, "name": allMenus[i].menuName, "isParent": true,"checked": false});
		}
		
		var checked = false;
		for(var i=0; i<totalAuths.length; i++){
			checked = false;
			for(var j=hasAuths.length-1; j>=0;j--){
				if(totalAuths[i].authCode == hasAuths[j].authCode){
					checked = true;
					hasAuths.splice(j,1);
					break;
				}
			}
			zNodes.push({"id": totalAuths[i].authCode, "pId": totalAuths[i].menuId, "name": totalAuths[i].authName, "isParent": false,"checked": checked});
		}
		
		for(var i=0; i<hasAuths.length; i++){
			zNodes.push({"id": hasAuths[i].authCode, "pId": totalAuths[i].menuId, "name": hasAuths[i].authName, "isParent": false,"checked": true,"chkDisabled":true});
		}
		
		$.fn.zTree.init($("#tree"), setting, zNodes);
		zTree = $.fn.zTree.getZTreeObj("tree");
		
		zTree.expandAll(true);
	});
	
	function save(){
		var nodes = zTree.getCheckedNodes(true);
		var idArr = [];
		var id;
		if(nodes != null && nodes.length > 0){
			for(var i=0;i<nodes.length;i++){
				if(!nodes[i].isParent){
					idArr.push(nodes[i].id);
				}
			}
		}
		for(var i=0; i<hasAuths.length; i++){
			if(idArr.indexOf(hasAuths[i].authCode) == -1){
				idArr.push(hasAuths[i].authCode);
			}
		}
		
		$.save({url: "${ctx}/role/doSetRoleAuth?roleId=${roleId}",datas:{authCodeS: idArr.join(',')}, onSuccess: function(){
			$.msg('设置角色权限成功！');
		}});
	}
    </script>
</head>
<body style="overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div style="flow:left;overflow:auto; margin: 5px 0px 20px 5px;" id="treeDiv">
		<ul id="tree" class="ztree"></ul>
	</div>
	<div class="form-group" style="border-bottom:0px;margin-top:20px;">
         <div class="col-sm-offset-2 col-md-offset-1 col-lg-offset-1" >
           <button class="btn btn-primary" type="button" id="saveAlways" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
           <button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
       </div>
   </div>
</body>
</html>


