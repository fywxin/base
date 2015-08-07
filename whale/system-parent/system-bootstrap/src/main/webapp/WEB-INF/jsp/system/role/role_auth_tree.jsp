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
	var totalAuths = ${totalAuths};
	var hasAuths = ${hasAuths};
	var allMenus = ${allMenus}
	
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
		
		zNodes.push({"id": 0, "pId": null, "name": "设置权限", "isParent": true,"open":true});
		
		for(var i=0; i<allMenus.length; i++){
			zNodes.push({"id": allMenus[i].menuId, "pId": allMenus[i].parentId, "name": allMenus[i].menuName, "isParent": true,"checked": false});
		}
		
		var checked = false;
		for(var i=0; i<totalAuths.length; i++){
			checked = false;
			for(var j=hasAuths.length-1; j>=0;j--){
				if(totalAuths[i].authId == hasAuths[j].authId){
					checked = true;
					hasAuths.splice(j,1);
					break;
				}
			}
			zNodes.push({"id": "A_"+totalAuths[i].authId, "pId": totalAuths[i].menuId, "name": totalAuths[i].authName, "isParent": false,"checked": checked});
		}
		
		for(var i=0; i<hasAuths.length; i++){
			zNodes.push({"id": "A_"+hasAuths[i].authId, "pId": totalAuths[i].menuId, "name": hasAuths[i].authName, "isParent": false,"checked": true,"chkDisabled":true});
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
					id = nodes[i].id;
					id=id.substring(2,id.length);
					idArr.push(id);
				}
			}
		}
		for(var i=0; i<hasAuths.length; i++){
			if(idArr.indexOf(hasAuths[i].authId) == -1){
				idArr.push(hasAuths[i].authId);
			}
		}
		
		$.save({url: "${ctx}/role/doSetRoleAuth?roleId=${roleId}",datas:{authIdS: idArr.join(',')}, onSuccess: function(){
			$.alert('设置角色权限成功！');
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


