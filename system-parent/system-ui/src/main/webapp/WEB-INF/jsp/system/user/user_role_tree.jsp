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
	var totalRoles = ${totalRoles};
	var hasRoles = ${hasRoles};
	
	$(document).ready(function(){
		$("#treeDiv").height($.h()-120);
		zNodes.push({"id": 0, "pId": null, "name": "设置角色", "isParent": true,"open":true});
		
		var checked = false;
		for(var i=0; i<totalRoles.length; i++){
			checked = false;
			for(var j=hasRoles.length-1; j>=0;j--){
				if(totalRoles[i].roleId == hasRoles[j].roleId){
					checked = true;
					hasRoles.splice(j,1);
					break;
				}
			}
			zNodes.push({"id": totalRoles[i].roleId, "pId": 0, "name": totalRoles[i].roleName, "isParent": false,"checked": checked});
		}
		
		for(var i=0; i<hasRoles.length; i++){
			zNodes.push({"id": hasRoles[i].roleId, "pId": 0, "name": hasRoles[i].roleName, "isParent": false,"checked": true,"chkDisabled":true});
		}
		
		$.fn.zTree.init($("#tree"), setting, zNodes);
		zTree = $.fn.zTree.getZTreeObj("tree");
		
		zTree.expandAll(true);
	});
	
	function save(){
		var nodes = zTree.getCheckedNodes(true);
		var idArr = [];
		if(nodes != null && nodes.length > 0){
			for(var i=0;i<nodes.length;i++){
				if(!nodes[i].isParent){
					idArr.push(nodes[i].id);
				}
			}
		}
		for(var i=0; i<hasRoles.length; i++){
			if(idArr.indexOf(hasRoles[i].roleId) == -1){
				idArr.push(hasRoles[i].roleId);
			}
		}
		
		$.save({url: "${ctx}/user/doSetUserRole?userId=${userId}",datas:{roleIdS: idArr.join(',')}, onSuccess: function(){
			$.msg('设置用户角色成功！');
		}});
	}
    </script>
</head>
<body style="overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div style="flow:left;overflow:auto; margin: 5px 0px 15px 5px;" id="treeDiv">
		<ul id="tree" class="ztree"></ul>
	</div>
	<div class="form-group" style="border-bottom:0px;margin-top:20px;">
         <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
           <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
           <button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
       </div>
   </div>
</body>
</html>


