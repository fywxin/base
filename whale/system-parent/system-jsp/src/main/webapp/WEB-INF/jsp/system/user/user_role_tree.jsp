<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head >
<%@include file="/html/jsp/common.jsp" %>
<%@include file="/html/jsp/ztree.jsp" %>
<script type="text/javascript">
    var initSelIds = [];
	var zTree;
	var toolBar = null;

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
		toolBar = $("#toolbar").ligerToolBar({ items: [
                                    	    {id: 'saveBut', text: '保存', icon:'save', click: function(){
                                    	    	save(); 
                                    	    	}
                                    	    },
                                    	    { line:true },
                                    	    {id: 'closeBut', text: '关闭', icon:"close", click: function(){ 
                                    	    	$.closeWin();
                                    	    	return false; 
                                    	    	}
                                    	    }
                                    	 ]
                                    	});
		if($.browser.msie && parseInt($.browser.version)<=6){
			document.execCommand("BackgroundImageCache",false,true);//IE6缓存背景图片
		}
		
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
			$.alert('设置用户角色成功！');
    		$.closeWin();
		}});
	}
    </script>
</head>
<body style="padding:0px; overflow-x:hidden; "> 
	<div id="toolbar" style="margin: 0px 2px 0px 2px;"></div> 
	<div class="infoBox" id="infoBoxDiv"></div>
	<div style="flow:left;height:420px;overflow:auto;" id="treeDiv">
		<ul id="tree" class="ztree"></ul>
	</div>
</body>
</html>


