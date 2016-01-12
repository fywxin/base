package org.whale.system.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.whale.system.common.util.Strings;

/**
 * 下拉选项树控件
 *
 * @author wjs
 * 2014年9月6日-下午3:10:35
 */
public class TreeTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private static final String LOGIC_TRUE = "true";
	
	private String id;
	private String nodes = "[]";
	private String value = "";
	private String canSelectParent = "true";
	private String nodeId = "id";
	private String nodePId = "pId";
	private String nodeName = "name";
	private String width = "160px";
	private String height = "250px";
	private String mulitSel = "false";
	private String beforeClick = "";
	private String onClick = "";
	private String beforeCheck = "";
	private String onCheck = "";
	private String beforeLoadTree = "";
	private String afterLoadTree = "";
	private String labelCheck = "true";
	private String search = "false";
	
	public int doStartTag() throws JspException {
		boolean mulSel = TreeTag.LOGIC_TRUE.equals(mulitSel);
		boolean pCanSel = TreeTag.LOGIC_TRUE.equals(canSelectParent);
		boolean isSearch = TreeTag.LOGIC_TRUE.equals(search);
		boolean mulSearch = mulSel && isSearch;
		
		if(Strings.isBlank(id))
			return SKIP_BODY;
		
		StringBuilder strb = new StringBuilder();
		strb.append(this.bulidInputHtml())
			.append("\n<script type='text/javascript'>\n")
			.append(this.bulidTreeSetting(mulSel, isSearch))
			.append("var zNodes_"+id+" = ").append(nodes).append(";\n\n\n")
			.append(this.bulidBeforeClick(pCanSel))
			.append(this.bulidOnClick(mulSel))
			.append(this.bulidMenuDisplay(isSearch))
			.append(this.bulidOnReady(mulSel, isSearch))
			.append(this.bulidInitTreeJs(mulSel, isSearch))
			.append(this.bulidClearFun(mulSel));
		if(mulSel){
			strb.append(this.bulidBeforeCheck())
				.append(this.bulidOnCheck(pCanSel, mulSearch));
		}
		if(isSearch){
			strb.append(this.bulidSearchFun(mulSearch));
		}
		strb.append("</script>\n\n");
		try {
			pageContext.getOut().print(strb.toString());
		} catch (java.io.IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}
	
	private String bulidTreeSetting(boolean mulSel,boolean isSearch){
		StringBuilder strb = new StringBuilder();
		strb.append("var zTree_"+id+" = null;\n");
		if(isSearch){
			strb.append("var tmpMap_"+id+" = [];\n")
				.append("var pIdObj_"+id+" = {};\n\n");
		}
		
		strb.append("var setting_"+id+" = {\n");
		if(mulSel){
		strb.append("	check: {\n")
			.append("		enable: true,\n")
			.append("		chkboxType: {\"Y\":\"ps\", \"N\":\"ps\"}\n")
			.append("	},\n");
		}
		strb.append("	view: {\n")
			.append("		dblClickExpand: false\n")
			.append("	},\n")
			.append("	data: {\n")
			.append("		simpleData: {\n")
			.append("			enable: true,\n")
			.append("			idKey: \""+nodeId+"\",\n")
			.append("			pIdKey: \""+nodePId+"\"\n")
			.append("		},\n")
			.append("		key:{ \n")
			.append("			name: \""+nodeName+"\"\n")
			.append("		}\n")
			.append("	},\n")
			.append("	callback: {\n");
		if(mulSel){
		strb.append("		beforeCheck: beforeCheck_"+id+",\n")
			.append("		onCheck: onCheck_"+id+",\n");
		}
		strb.append("		beforeClick: beforeClick_"+id+",\n")
			.append("		onClick: onClick_"+id+"\n")
			.append("	}\n")
			.append("};\n\n");
		return strb.toString();
	}
	
	private String bulidBeforeClick(boolean pCanSel){
		StringBuilder strb = new StringBuilder();
		strb.append("function beforeClick_"+id+"(treeId, treeNode) {\n");
		strb.append("	if(treeNode.isParent){\n")
			.append("		if(!"+pCanSel+")\n")
			.append("			zTree_"+id+".expandNode(treeNode, !treeNode.open);\n")
			.append("	}\n");
		if(Strings.isNotBlank(beforeClick)){
		strb.append("	if(typeof("+beforeClick+") == 'function') {\n")
			.append("		if(!"+beforeClick+"(treeId, treeNode)){\n")
			.append("			return false;\n")
			.append("		}\n")
			.append("	}\n");
		}
		strb.append("	if(treeNode.isParent){\n")
			.append("		return "+pCanSel+";\n")
			.append("	}\n")
			.append("	return true;\n")
			.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidOnClick(boolean mulSel){
		StringBuilder strb = new StringBuilder();
		strb.append("function onClick_"+id+"(e, treeId, treeNode) {\n");
		if(mulSel){
			if(TreeTag.LOGIC_TRUE.equals(labelCheck)){
				strb.append("	var zTree = zTree_"+id+";\n")
					.append("	zTree.checkNode(treeNode, !treeNode.checked, true);\n")
					.append("	onCheck_"+id+"(e, treeId, treeNode);\n");
			}
		}else{
		strb.append("	$(\"#"+id+"\").val(treeNode['"+nodeId+"']);\n")
			.append("	$(\"#"+id+"_NAME\").val(treeNode['"+nodeName+"']);\n")
			.append("	hideMenu_"+id+"();\n");
		if(Strings.isNotBlank(onClick)){
			strb.append("	if(typeof("+onClick+") == 'function') {\n")
				.append("		" +onClick+"(e, treeId, treeNode);\n")
				.append("	}\n");
			}
		}
		
		
		strb.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidBeforeCheck(){
		StringBuilder strb = new StringBuilder();
		strb.append("function beforeCheck_"+id+"(treeId, treeNode) {\n");
		
		if(Strings.isNotBlank(beforeCheck)){
		strb.append("	if(typeof("+beforeCheck+") == 'function') {\n")
			.append("		if(!"+beforeCheck+"(treeId, treeNode)){\n")
			.append("			return false;\n")
			.append("		}\n")
			.append("	}\n");
		}
		strb.append("	return true;\n")
			.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidOnCheck(boolean pCanSel, boolean mulSearch){
		StringBuilder strb = new StringBuilder();
		strb.append("function onCheck_"+id+"(e, treeId, treeNode) {\n")
			.append("	var zTree = zTree_"+id+";\n")
			.append("	var ids = [];\n")
			.append("	var names = [];\n")
			.append("	var nodes = zTree.getCheckedNodes(true);\n")
			.append("	for (var i=0, l=nodes.length; i<l; i++) {\n")
			.append("		if(!"+pCanSel+" && nodes[i].isParent){\n")
			.append("			continue;\n")
			.append("		}\n")
			.append("		ids.push(nodes[i]['"+nodeId+"']);\n")
			.append("		names.push(nodes[i]['"+nodeName+"']);\n")
			.append("	}\n");
		if(mulSearch){
		strb.append("	var privateIds = $(\"#"+id+"\").attr('privateIds');\n")
			.append("	if(typeof(privateIds) != \"undefined\" && privateIds != null && $.trim(privateIds) != ''){\n")
			.append("		var privateIdArr = privateIds.split(',');\n")
			.append("		for(i=0, l=privateIdArr.length; i<l; i++){\n")
			.append("			ids.push(privateIdArr[i]);\n")
			.append("			names.push(tmpMap_"+id+"[privateIdArr[i]]['"+nodeName+"']);\n")
			.append("		}\n")
			.append("	}\n");
		}
		
		strb.append("	$(\"#"+id+"\").val(ids.join(','));\n")
			.append("	$(\"#"+id+"_NAME\").val(names.join(','));\n");
		
		if(Strings.isNotBlank(onCheck)){
		strb.append("	if(typeof("+onCheck+") == 'function') {\n")
			.append("		" +onCheck+"(e, treeId, treeNode);\n")
			.append("	}\n");
		}
		strb.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidMenuDisplay(boolean isSearch){
		StringBuilder strb = new StringBuilder();
		strb.append("function showMenu_"+id+"() {\n")
			.append("	var voffset = $(\"#"+id+"_NAME\").offset();\n")
			.append("	var bodyHeight = parseInt(window.document.body.clientHeight);\n")
			.append("	var realHeight = voffset.top+parseInt('"+this.height+"')+$(\"#"+id+"_NAME\").outerHeight();\n")
			.append("	if(realHeight > bodyHeight){\n")
			.append("		$(\"body\").height(realHeight+5);\n")
			.append("	}\n")
			.append("	$(\"#menuContent_"+id+"\").css({left:voffset.left + \"px\", top:voffset.top + $(\"#"+id+"_NAME\").outerHeight() + \"px\"}).slideDown(\"fast\");\n")
			.append("	$(\"body\").bind(\"mousedown\", onBodyDown_"+id+");\n")
			.append("}\n")
			.append("\n")
			.append("function hideMenu_"+id+"() {\n")
			.append("	$(\"#menuContent_"+id+"\").fadeOut(\"fast\");\n")
			.append("	$(\"body\").unbind(\"mousedown\", onBodyDown_"+id+");\n");
		if(isSearch){
		strb.append("	if($(\"#"+id+"_NAME\").attr('newtree') == '1'){\n")
			.append("		initTree_"+id+"();\n")
			.append("		$(\"#"+id+"_NAME\").attr('newtree','0');\n")
			.append("	}\n");
		}
		
		strb.append("}\n")
			.append("\n")
			.append("function onBodyDown_"+id+"(event) {\n")
			.append("	if (!(event.target.id == \"menuBtn\" || event.target.id == \"menuContent_"+id+"\" || $(event.target).parents(\"#menuContent_"+id+"\").length>0)) {\n")
			.append("		hideMenu_"+id+"();\n")
			.append("	}\n")
			.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidOnReady(boolean mulSel, boolean search){
		StringBuilder strb = new StringBuilder();
		strb.append("$(document).ready(function(){\n")
			.append("	if($.browser.msie && parseInt($.browser.version)<=6){\n")
			.append("		document.execCommand(\"BackgroundImageCache\",false,true);\n")
			.append("	}\n");
		
		if(Strings.isNotBlank(beforeLoadTree)){
		strb.append("	if(typeof("+beforeLoadTree+") == 'function')\n")
			.append("		"+beforeLoadTree+"();\n");
		}
		
		strb.append(this.bulidTreeDivHtml(search))
			.append("	initTree_"+id+"();\n");
		
		
		if(Strings.isNotBlank(afterLoadTree)){
		strb.append("	if(typeof("+afterLoadTree+") == 'function')\n")
			.append("		"+afterLoadTree+"(zTree_"+id+");\n");
		}
		
		strb.append("});\n\n");
		return strb.toString();
	}
	
	private String bulidInitTreeJs(boolean mulSel, boolean isSearch){
		StringBuilder strb = new StringBuilder();
		strb.append("function initTree_"+id+"(){\n")
			.append("	zTree_"+id+" = $.fn.zTree.init($(\"#tree_"+id+"\"), setting_"+id+", zNodes_"+id+");\n")
			.append("	var idVal = $(\"#"+id+"\").attr('privateIds','').val();\n");
		if(mulSel){
		strb.append("	var idArr = idVal.split(',');\n")
			.append("	var nameArr = [];\n")
			.append("	for(var i=0; i<idArr.length; i++){\n")
			.append("		var nodes = zTree_"+id+".getNodesByParam(\""+nodeId+"\", idArr[i], null);\n")
			.append("		if(nodes.length > 0){\n")
			.append("			zTree_"+id+".checkNode(nodes[0], true, false);\n")
			.append("			nameArr.push(nodes[0]['"+nodeName+"']);\n")
			.append("		}\n")
			.append("	}\n")
			.append("	$(\"#"+id+"_NAME\").val(nameArr.join(', '));\n");
		}else{
		strb.append("	var nodes = zTree_"+id+".getNodesByParam(\""+nodeId+"\", idVal, null);\n")
			.append("	if(nodes.length > 0){\n")
			.append("		zTree_"+id+".selectNode(nodes[0]);\n")
			.append("		$(\"#"+id+"_NAME\").val(nodes[0]['"+nodeName+"']);\n")
			.append("	}\n");
		}
		
		if(isSearch){
		strb.append("	for (var i=0, l=zNodes_"+id+".length; i<l; i++) {\n")
			.append("		zNodes_"+id+"[i]._innerOrderNum=i;\n")
			.append("		tmpMap_"+id+"[zNodes_"+id+"[i][\""+nodeId+"\"]] = zNodes_"+id+"[i];\n")
			.append("		if(!pIdObj_"+id+"[zNodes_"+id+"[i][\""+nodePId+"\"]]){\n")
			.append("			pIdObj_"+id+"[zNodes_"+id+"[i][\""+nodePId+"\"]] = [];\n")
			.append("		}\n")
			.append("		if(Object.prototype.toString.apply(pIdObj_"+id+"[zNodes_"+id+"[i][\""+nodePId+"\"]]) === \"[object Array]\"){\n")
			.append("			pIdObj_"+id+"[zNodes_"+id+"[i][\""+nodePId+"\"]].push(zNodes_"+id+"[i]);\n")
			.append("		}\n")
			.append("	}\n");
		}
		
		strb.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidSearchFun(boolean mulSearch){
		StringBuilder strb = new StringBuilder();
		strb.append("$(document).ready(function(){\n")
			.append("$(\"#KEYWORD_"+this.id+"\").keydown(function(event){\n")
			.append("	if(event.keyCode == 13){\n")
			.append("		var val = $.trim(this.value);\n")
			.append("		if(val == \"\"){\n")
			.append("			$(\"#"+id+"_NAME\").attr('newtree','0');\n")
			.append("			initTree_"+id+"();\n")
			.append("			return ;\n")
			.append("		}\n")
			.append("		var mNodes = [];\n")
			.append("		for (var i=0, l=zNodes_"+id+".length; i<l; i++) {\n")
			.append("			if(zNodes_"+id+"[i][\""+nodeName+"\"].indexOf(val) > -1){\n")
			.append("				mNodes.push(zNodes_"+id+"[i]);\n")
			.append("			}\n")
			.append("		}\n")
			.append("		var pNodes = [];\n")
			.append("		for (i=0, l=mNodes.length; i<l; i++) {\n")
			.append("			var node = mNodes[i];\n")
			.append("			while(node[\""+nodePId+"\"] != null && tmpMap_"+id+"[node[\""+nodePId+"\"]]){\n")
			.append("				if($.inArray(tmpMap_"+id+"[node[\""+nodePId+"\"]], pNodes) == -1){\n")
			.append("					pNodes.push(tmpMap_"+id+"[node[\""+nodePId+"\"]]);\n")
			.append("				}\n")
			.append("				node = tmpMap_"+id+"[node[\""+nodePId+"\"]];\n")
			.append("			}\n")
			.append("		}\n")
			.append("		var cNodes = [];\n")
			.append("		for (i=0, l=mNodes.length; i<l; i++) {\n")
			.append("			cNodes = cNodes.concat(pickAllChlid(pIdObj_"+id+", mNodes[i],\""+nodeId+"\"));\n")
			.append("		}\n")
			.append("		var allNodes = mNodes.concat(cNodes).concat(pNodes);\n")
			.append("		for(i=allNodes.length; i>=0; i--){\n")
			.append("			for(var j=i-1; j>=0; j--){\n")
			.append("				if(allNodes[i] == allNodes[j]){\n")
			.append("					allNodes.splice(j,1);\n")
			.append("				}\n")
			.append("			}\n")
			.append("		}\n")
			.append("		allNodes=allNodes.sort(function(a,b){return a._innerOrderNum - b._innerOrderNum;});\n")
			.append("		zTree_"+id+"=$.fn.zTree.init($(\"#tree_"+id+"\"), setting_"+id+", allNodes);\n")
			.append("		$(\"#"+id+"_NAME\").attr('newtree','1');\n");
		if(mulSearch){
		strb.append("		var idArr = $(\"#"+id+"\").val().split(',');\n")
			.append("		var privateIds = [];\n")
			.append("		for(i=0; i<idArr.length; i++){\n")
			.append("			var selNodes = zTree_"+id+".getNodesByParam(\""+nodeId+"\", idArr[i], null);\n")
			.append("			if(selNodes.length > 0){\n")
			.append("				zTree_"+id+".checkNode(selNodes[0], true, false);\n")
			.append("			}else{\n")
			.append("				privateIds.push(idArr[i]);\n")
			.append("			\n}")
			.append("		\n}")
			.append("		$(\"#"+id+"\").attr('privateIds',privateIds.join(','));\n");
		}
		strb.append("	}\n")
			.append("});\n")
			.append("});\n\n\n")
			.append("function pickAllChlid(pIdObj, pNode,idKey){\n")
			.append("	var nodes = [];\n")
			.append("	var chlidNodes = pIdObj[pNode[idKey]];\n")
			.append("	if(chlidNodes && Object.prototype.toString.apply(chlidNodes) === \"[object Array]\" && chlidNodes.length > 0){\n")
			.append("		nodes = nodes.concat(chlidNodes);\n")
			.append("		for(var i=0; i<chlidNodes.length; i++){\n")
			.append("			nodes= nodes.concat(pickAllChlid(pIdObj,chlidNodes[i],idKey));\n")
			.append("		}\n")
			.append("	}\n")
			.append("	return nodes;\n")
			.append("}\n\n");
		return strb.toString();
	}
	
	
	private String bulidInputHtml(){
		StringBuilder strb = new StringBuilder();
		strb.append("<input type='text' id='"+id+"_NAME' name='"+id+"_NAME' onclick=\"showMenu_"+id+"(); return false;\" style=\"width: "+width+";\"  readonly=readonly />\n<a href='#' class='i-btn-clear' title='清空' onclick='clear_"+id+"();'>&nbsp;</a>\n")
			.append("<input type='hidden' id='"+id+"' name='"+id+"' value='"+value+"' />\n");
		return strb.toString();
	}
	
	private String bulidTreeDivHtml(boolean search){
		StringBuilder strb = new StringBuilder();
		String maxWidth = width;
		if(Integer.parseInt(maxWidth.substring(0,maxWidth.length()-2)) > 200)
			maxWidth = "200px";

		strb.append("	$(\"body\").append('<div id=\"menuContent_"+id+"\" style=\"display:none; position: absolute;z-index:9999;\">");
		if(search)
			strb.append("<div style=\"background: #D7DFE9;height:26px;width:"+maxWidth+";border:1px solid #617775;text-align: center;\"><input type=text size=22 id=KEYWORD_"+this.id+" title=\"请输入条件，按回车键搜索树\" /></div>");
		strb.append("<ul id=\"tree_"+id+"\" class=\"ztree\" style=\"margin-top: 0px;border: 1px solid #617775;background: #f0f6e4;overflow:auto; width: "+maxWidth+";height: "+height+";\"></ul></div>');\n\n");
		
		return strb.toString();
	}
	
	private String bulidClearFun(boolean mulSel){
		StringBuilder strb = new StringBuilder();
		strb.append("	function clear_"+id+"(){\n")
			.append("		$(\"#"+id+"\").val('').attr('oldVal','');\n")
			.append("		$(\"#"+id+"_NAME\").val('').attr('oldVal','');\n");
		if(mulSel){
		strb.append("		zTree_"+id+".checkAllNodes(false);\n");
		}
		strb.append("	}\n");
		return strb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodePId() {
		return nodePId;
	}

	public void setNodePId(String nodePId) {
		this.nodePId = nodePId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getBeforeClick() {
		return beforeClick;
	}

	public void setBeforeClick(String beforeClick) {
		this.beforeClick = beforeClick;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getMulitSel() {
		return mulitSel;
	}

	public void setMulitSel(String mulitSel) {
		this.mulitSel = mulitSel;
	}

	public String getAfterLoadTree() {
		return afterLoadTree;
	}

	public void setAfterLoadTree(String afterLoadTree) {
		this.afterLoadTree = afterLoadTree;
	}

	public String getBeforeCheck() {
		return beforeCheck;
	}

	public void setBeforeCheck(String beforeCheck) {
		this.beforeCheck = beforeCheck;
	}

	public String getOnCheck() {
		return onCheck;
	}

	public void setOnCheck(String onCheck) {
		this.onCheck = onCheck;
	}

	public String getCanSelectParent() {
		return canSelectParent;
	}

	public void setCanSelectParent(String canSelectParent) {
		this.canSelectParent = canSelectParent;
	}

	public String getBeforeLoadTree() {
		return beforeLoadTree;
	}

	public void setBeforeLoadTree(String beforeLoadTree) {
		this.beforeLoadTree = beforeLoadTree;
	}

	public String getLabelCheck() {
		return labelCheck;
	}

	public void setLabelCheck(String labelCheck) {
		this.labelCheck = labelCheck;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}
