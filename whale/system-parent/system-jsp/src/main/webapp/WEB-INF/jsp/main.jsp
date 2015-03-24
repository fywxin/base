<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>基础框架</title>

<link href="${resource}/ui/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${resource}/plugin/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
<script src="${resource}/com/jquery.js" type="text/javascript"></script>
<script src="${resource}/ui/js/ligerui.all.js" type="text/javascript"></script>
<script src="${resource}/plugin/zTree/js/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
<script src="${resource}/js/Common.js" type="text/javascript"></script>
<script src="${resource}/com/cookie.js" type="text/javascript"></script>
<script type="text/javascript">
	var mark= false;
	var tab = null;
	var accordion = null;
	var tree = null;
	var tabItems = [];
	
	var setting = {
			view: {selectedMulti: false},
			data: {
				simpleData: {idKey: "menuId",pIdKey: "parentId"},
				key:{name: "menuName"}
			},

			callback: {
				onClick: function(event, treeId, treeNode){
					var openType = treeNode.openType; //打开类型 
					var menuType = treeNode.menuType; //菜单类型
					var sUrl = "${ctx}" + treeNode.menuUrl;

					if(menuType == 3){
						if(openType && openType == 1){
		                	f_addTab(treeNode.menuId, treeNode.menuName, sUrl);
						}else if(openType && openType == 2){
							$.fnShowWindow_mid(sUrl);
						}
					}else if(menuType == 2){
						zTree = $.fn.zTree.getZTreeObj(treeId);
						zTree.expandNode(treeNode, !treeNode.open);
					}
				}
			}
		};
	
	$(function (){
	    $("#layout1").ligerLayout({ leftWidth: 190, height: '100%',heightDiff:-34,space:4, onHeightChanged: f_heightChanged });
	    
	    var height = $(".l-layout-center").height();
	    $("#framecenter").ligerTab({
	        height : height,
	        showSwitchInTab : true,
	        showSwitch : true,
	        onAfterAddTabItem: function (tabdata){
	            tabItems.push(tabdata);
	        },
	        onAfterRemoveTabItem: function (tabid){ 
	            for (var i = 0; i < tabItems.length; i++) {
	                var o = tabItems[i];
	                if (o.tabid == tabid){
	                    tabItems.splice(i, 1);
	                    break;
	                }
	            }
	        },
	        onReload: function (tabdata){
	            var tabid = tabdata.tabid;
	        }
	    });
	
	    //面板
	    $("#accordion1").ligerAccordion({ height: height - 24, speed: null });
	
	    ${js}
	
	    tab = liger.get("framecenter");
	    accordion = liger.get("accordion1");
	    $("#pageloading").hide();
	});
	function f_heightChanged(options){  
	    if (tab){
	    	tab.addHeight(options.diff);
	    }
	    if (accordion && options.middleHeight - 24 > 0){
	    	accordion.setHeight(options.middleHeight - 24);
	    }
	}
	function f_addTab(tabid, text, url) {
		if(tab.isTabItemExist(tabid)){
	        tab.selectTabItem(tabid);
	    	tab.reload(tabid);
	    }else{
	    	if(tab.getTabItemCount() >= 15){
		    	$.alert('你打开的窗口太多，请先关闭一些再打开！');
		    	return ;
		    }
		    while(url.indexOf('//')==0){
		    	url = url.substring(1,url.length);
		    }
		   	tab.addTabItem({ tabid : tabid,text: text, url: url });
	    }
	}
	
	function loginOut(){
		$.ajax({
			    url: "${ctx}/loginOut",
			    type: 'post',
			    data: null,
			    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			   	dataType: 'json',
			    error: function(){
			        window.location.href="${ctx}";
			    },
			    success: function(obj){
			    	$.cookie("userName", null);
		    		$.cookie("encryptedPwd", null); 
			    	window.location.href="${ctx}";
				}
			});
	}

	function changePassword(){
		$.openWin({url:"${ctx }/user/goChangePassword", width: 600, height: 300});
	}
</script>
<style type="text/css">
body, html {height: 100%;}
body {padding: 0px;margin: 0;overflow: hidden;}
.l-link {display: block;height: 26px;line-height: 26px;padding-left: 10px;text-decoration: underline;color: #333;}
.l-link2 {text-decoration: underline;color: white;margin-left: 2px;margin-right: 2px;}
.l-layout-top {background: #102A49;color: White;}
.l-layout-bottom {background: #E5EDEF;text-align: center;}
#pageloading {position: absolute;left: 0px;top: 0px;background: white url('${resource}/img/loading.gif') no-repeat center;width: 100%;height: 100%;z-index: 99999;}
.l-link {display: block;line-height: 22px;height: 22px;padding-left: 16px;border: 1px solid white;margin: 4px;}
.l-link-over {background: #FFEEAC;border: 1px solid #DB9F00;}
.l-winbar {background: #2B5A76;height: 30px;position: absolute;left: 0px;bottom: 0px;width: 100%;z-index: 99999;}
.space {color: #E7E7E7;}
/* 顶部 */
.l-topmenu {margin: 0;padding: 0;height: 31px;line-height: 31px;background: url('${resource}/img/top.jpg') repeat-x bottom;position: relative;border-top: 1px solid #1D438B;}
.l-topmenu-logo {color: #E7E7E7;padding-left: 35px;line-height: 26px;background: url('${resource}/img/topicon.gif') no-repeat 10px 5px;}
.l-topmenu-welcome {position: absolute;height: 24px;line-height: 24px;right: 30px;top: 2px;color: #070A0C;}
.l-topmenu-welcome a {color: #E7E7E7;text-decoration: underline}
.body-gray2014 #framecenter {margin-top: 3px;}
.viewsourcelink {background: #B3D9F7;display: block;position: absolute;right: 10px;top: 3px;padding: 6px 4px;color: #333;text-decoration: underline;}
.viewsourcelink-over {background: #81C0F2;}
.l-topmenu-welcome label {color: white;}
</style>
</head>
<body style="padding: 0px; background: #EAEEF5;">
	<div id="pageloading"></div>
	<div id="topmenu" class="l-topmenu">
		<div class="l-topmenu-logo">cyou</div>
		<div class="l-topmenu-welcome">
			<span style="color: #E7E7E7;">${realName }&nbsp;您好</span>
	        <span class="space">|</span>
	        <a href="#" class="l-link2" onclick="changePassword()">修改密码</a> 
	        <span class="space">|</span>
	        <a href="#" class="l-link2" onclick="loginOut()">退出</a>
		</div>
	</div>
	<div id="layout1" style="width: 99.2%; margin: 0 auto; margin-top: 4px;">
		<div position="left"  title="功能菜单" id="accordion1"> 
              ${html }
        </div>
        <div position="center" id="framecenter"> 
	        <div tabid="home" title="我的主页" style="height:300px" >
	           <iframe frameborder="0" name="home" id="home" src="${ctx }/user/goUserMainPage"></iframe>
	        </div> 
		</div> 
	</div>
	<div style="height: 32px; line-height: 32px; text-align: center;">
		Copyright © 2011-2015 王金绍 <a href="http://www.miitbeian.gov.cn/" target="_blank">闽ICP备09046932号-2</a>
	</div>
</body>
</html>
