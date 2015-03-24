package org.whale.system.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.jdbc.core.JdbcTemplate;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.SpringContextHolder;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.DictItem;

import com.alibaba.fastjson.JSON;

/**
 * 字典下拉多选树
 * @author wjs
 *
 */
public class DictSelTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private static final String LOGIC_TRUE = "true";
	
	/** 文本输入框的id, 树对象: zTree_id */
	private String id;
	/** 字典编码 */
	private String dictCode;
	/** 已经选中的节点，回显作用 多个值与","分隔 */
	private String value = "";
	/** 虚拟根节点文本 */
	private String virtualRootLabel = "请选择";
	/** 虚拟根节点ID */
	private String virtualRootId = "_VIRTUAL_RO0T_ID_";
	/** sql查询语句 */
	private String sql;
	/** sql对应返回值得key 为空sqlValue */
	private String sqlValueKey;
	/** sql对应返回值 为空sqlLable*/
	private String sqlLableKey;
	
	/** 下拉框宽度 */
	private String width = "160px";
	/** 下拉框高度 */
	private String height = "250px";
	/** 点击选择框前事件 */
	private String beforeCheck = "";
	/** 点击选择框事件 */
	private String onCheck = "";
	/** 树加载前调用事件 */
	private String beforeLoadTree = "";
	/** 树加载显示完后调用事件 */
	private String afterLoadTree = "";
	
	/** 只读模式，只会生成文本和<input type="hidden">的html*/
	private String readonly;
	
	/**默认显示数据**/
	private String valDefault;
	
	/** 点击文本，选中多选框 */
	private String labelCheck = "true";
	
	
	public int doStartTag() throws JspException {
		if(Strings.isBlank(id))
			return SKIP_BODY;
		StringBuilder strb = new StringBuilder();
		if(LOGIC_TRUE.equals(readonly)){
			if(Strings.isBlank(value))
				return SKIP_BODY;
			String[] values = value.trim().split(",");
			
			StringBuilder rstr = new StringBuilder();
			String temp = "";
			if(Strings.isBlank(dictCode)){
				JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextHolder.getApplicationContext().getBean("jdbcTemplate");
				List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
				
				if(list != null && list.size() > 0){
					for(String val : values){
						if(Strings.isBlank(val))
							continue;
						for(Map<String, Object> map : list){
							if(map.get(sqlValueKey) != null && map.get(sqlValueKey).toString().equals(val)){
								rstr.append(",").append(map.get(sqlLableKey) == null ? "" : map.get(sqlLableKey).toString());
							}
						}
					}
				}
			}else{
				for(String val : values){
					if(Strings.isNotBlank(temp = DictCacheService.getThis().getItemLabel(dictCode, val))){
						rstr.append(",").append(temp.trim());
					}
				}
			}
			if(rstr.length() > 1){
				temp = rstr.substring(1);
			}else{
				temp = rstr.toString();
			}
			strb.append(temp)
				.append("<input type='hidden' id='"+id+"' name='"+id+"' value='"+value+"' />");
		}else{
			strb.append(this.bulidHtml())
				.append("\n<script type='text/javascript'>\n")
				.append(this.bulidTreeSetting())
				.append("var zNodes_"+id+" = ").append(this.bulidNodes()).append(";\n\n\n")
				.append(this.bulidBeforeCheck())
				.append(this.bulidOnCheck())
				.append(this.bulidMenuDisplay())
				.append(this.bulidOnReady())
				.append(this.bulidClearFun());
			if(LOGIC_TRUE.equals(labelCheck)){
				strb.append(this.bulidOnClick());
			}
			strb.append("</script>\n\n");
		}
		try {
			pageContext.getOut().print(strb.toString());
		} catch (java.io.IOException e) {
			e.printStackTrace();
			throw new JspTagException(e.getMessage());
		}
		
		return SKIP_BODY;
	}
	
	private String bulidTreeSetting(){
		String strb = "";
		strb += "var zTree_"+id+" = null;\n" +
				"var setting_"+id+" = {\n" +
				"	check: {\n" +
				"		enable: true,\n" +
				"		chkboxType: {\"Y\":\"ps\", \"N\":\"ps\"}\n" +
				"	},\n" +
				"	view: {\n" +
				"		dblClickExpand: false,\n" +
				"		expandSpeed: ($.browser.msie && parseInt($.browser.version)<=6)?\"\":\"fast\"\n" +
				"	},\n" +
				"	data: {\n" +
				"		simpleData: {\n" +
				"			enable: true\n" +
				"		}\n" +
				"	},\n" +
				"	callback: {\n" +
				"		beforeCheck: beforeCheck_"+id+",\n" +
				"		onCheck: onCheck_"+id+",\n" +
				"		onClick: onClick_"+id+"\n" +
				"	}\n" +
				"};\n\n";
		return strb;
	}
	
	
	private String bulidOnClick(){
		StringBuilder strb = new StringBuilder();
		
		strb.append("function onClick_"+id+"(e, treeId, treeNode) {\n")
			.append("	var zTree = zTree_"+id+";\n")
			.append("	zTree.checkNode(treeNode, !treeNode.checked, true);\n")
			.append("	onCheck_"+id+"(e, treeId, treeNode);\n")
			.append("}\n\n");
		
		return strb.toString();
	}
	
	private String bulidBeforeCheck(){
		String strb = "";
		strb += "function beforeCheck_"+id+"(treeId, treeNode) {\n";
		
		if(Strings.isNotBlank(beforeCheck)){
		strb += "	if(typeof("+beforeCheck+") == 'function') {\n" +
				"		if(!"+beforeCheck+"(treeId, treeNode)){\n" +
				"			return false;\n" +
				"		}\n" +
				"	}\n";
		}
		strb += "	return true;\n" +
				"}\n\n";
		return strb;
	}
	
	private String bulidOnCheck(){
		String strb = "";
		strb += "function onCheck_"+id+"(e, treeId, treeNode) {\n" +
				"	var zTree = zTree_"+id+";\n" +
				"	var ids = [];\n" +
				"	var names = [];\n" +
				"	var nodes = zTree.getCheckedNodes(true);\n" +
				"	for (var i=0, l=nodes.length; i<l; i++) {\n" +
				"		if(nodes[i].isParent){\n" +
				"			continue;\n" +
				"		}\n" +
				"		ids.push(nodes[i]['id']);\n" +
				"		names.push(nodes[i]['name']);\n" +
				"	}\n" +
				"	$(\"#"+id+"\").val(ids.join(','));\n" +
				"	$(\"#"+id+"_NAME\").val(names.join(','));\n";
		
		if(Strings.isNotBlank(onCheck)){
		strb += "	if(typeof("+onCheck+") == 'function') {\n" +
				"		" +onCheck+"(e, treeId, treeNode);\n" +
				"	}\n";
		}
		strb += "}\n\n";
		return strb;
	}
	
	private String bulidMenuDisplay(){
		String strb = "";
		strb += "function showMenu_"+id+"() {\n" +
				"	var voffset = $(\"#"+id+"_NAME\").offset();\n" +
				"	var bodyHeight = parseInt(window.document.body.clientHeight);\n" +
				"	var realHeight = voffset.top+parseInt('"+this.height+"')+$(\"#"+id+"_NAME\").outerHeight();\n" +
				"	if(realHeight > bodyHeight){\n" +
				"		$(\"body\").height(realHeight+5);\n" +
				"	}\n" +
				"	$(\"#menuContent_"+id+"\").css({left:voffset.left + \"px\", top:voffset.top + $(\"#"+id+"_NAME\").outerHeight() + \"px\"}).slideDown(\"fast\");\n" +
				"	$(\"body\").bind(\"mousedown\", onBodyDown_"+id+");\n" +
				"}\n" +
				"\n" +
				"function hideMenu_"+id+"() {\n" +
				"	$(\"#menuContent_"+id+"\").fadeOut(\"fast\");\n" +
				"	$(\"body\").unbind(\"mousedown\", onBodyDown_"+id+");\n" +
				"}\n" +
				"\n" +
				"function onBodyDown_"+id+"(event) {\n" +
				"	if (!(event.target.id == \"menuBtn\" || event.target.id == \"menuContent_"+id+"\" || $(event.target).parents(\"#menuContent_"+id+"\").length>0)) {\n" +
				"		hideMenu_"+id+"();\n" +
				"	}\n" +
				"}\n\n";
		return strb;
	}
	
	private String bulidOnReady(){
		String strb = "";
		strb += "$(document).ready(function(){\n" +
				"	if($.browser.msie && parseInt($.browser.version)<=6){\n" +
				"		document.execCommand(\"BackgroundImageCache\",false,true);\n" +
				"	}\n";
		
		if(Strings.isNotBlank(beforeLoadTree)){
		strb += "	if(typeof("+beforeLoadTree+") == 'function'){\n" +
				"		"+beforeLoadTree+"();\n" +
				"	}\n";
		}
		String maxWidth = width;
		if(Integer.parseInt(maxWidth.substring(0,maxWidth.length()-2)) > 200){
			maxWidth = "200px";
		}
		strb += "	$(\"body\").append('<div id=\"menuContent_"+id+"\" style=\"display:none; position: absolute;z-index:9999;\"><ul id=\"tree_"+id+"\" class=\"ztree\" style=\"margin-top: 0px;border: 1px solid #617775;background: #f0f6e4;overflow-y:auto;overflow-x:auto; width: "+maxWidth+";height: "+height+";\"></ul></div>');\n\n" +
				"	$.fn.zTree.init($(\"#tree_"+id+"\"), setting_"+id+", zNodes_"+id+");\n" +
				"	zTree_"+id+" = $.fn.zTree.getZTreeObj(\"tree_"+id+"\");\n";
		if(Strings.isNotBlank(value)){
		strb += "	var idArr = \""+value+"\".split(',');\n" +
				"	var nameArr = [];\n" +
				"	for(var i=0; i<idArr.length; i++){\n" +
				"		var nodes = zTree_"+id+".getNodesByParam(\"id\", idArr[i], null);\n" +
				"		if(nodes.length > 0){\n" +
				"			zTree_"+id+".checkNode(nodes[0], true, true);\n" +
				"			nameArr.push(nodes[0]['name']);\n" +
				"		}\n" +
				"	}\n" +
				"	$(\"#"+id+"\").val(\""+value+"\");\n" +
				"	$(\"#"+id+"_NAME\").val(nameArr.join(', '));\n";
		}
		
		if(Strings.isNotBlank(afterLoadTree)){
		strb += "	if(typeof("+afterLoadTree+") == 'function'){\n" +
				"		"+afterLoadTree+"(zTree_"+id+");\n" +
				"	}\n";
		}
		strb += "});\n\n";
		
		return strb;
	}
	
	private String bulidHtml(){
		String strb = "";
		strb += "<input type='text' readonly = 'readonly' id='"+id+"_NAME' onclick=\"showMenu_"+id+"(); return false;\" style=\"width: "+width+";\" />\n" +
				"<a href='#' class='i-btn-clear' title='清空' onclick='clear_"+id+"();'>&nbsp;</a>\n"+
				"<input type='hidden' id='"+id+"' name='"+id+"' />\n";
				
		return strb;
	}
	
	private String bulidClearFun(){
		StringBuilder strb = new StringBuilder();
		strb.append("	function clear_"+id+"(){\n")
			.append("		$(\"#"+id+"\").val('').attr('oldVal','');\n")
			.append("		$(\"#"+id+"_NAME\").val('').attr('oldVal','');\n")
			.append("		zTree_"+id+".checkAllNodes(false);\n")
			.append("	}\n");
		return strb.toString();
	}
	
	private String bulidNodes(){
		List<Map<String,Object>> rsList = new ArrayList<Map<String,Object>>();
		Map<String,Object> item = new HashMap<String, Object>();
		item.put("id", this.virtualRootId);
		item.put("pId", "");
		item.put("name", this.virtualRootLabel);
		item.put("isParent", true);
		item.put("open", true);
		rsList.add(item);
		if(Strings.isBlank(dictCode)){
			if(Strings.isBlank(sql))
				return "[]";
			JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextHolder.getApplicationContext().getBean("jdbcTemplate");
			List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
			if(list != null && list.size() > 0){
				for(Map<String, Object> map : list){
					if(Strings.isNotBlank(valDefault)){
						String[] vals = valDefault.split(",");
						for (String s : vals) {
							if(map.get(sqlValueKey)!=null&&map.get(sqlValueKey).toString().trim().equals(s.trim())){
								item = new HashMap<String, Object>();
								item.put("id", map.get(sqlValueKey));
								item.put("pId", this.virtualRootId);
								item.put("name", map.get(sqlLableKey));
								rsList.add(item);
							}
						}
					}else{
						item = new HashMap<String, Object>();
						item.put("id", map.get(sqlValueKey));
						item.put("pId", this.virtualRootId);
						item.put("name", map.get(sqlLableKey));
						rsList.add(item);
					}
					
				}
			}
		}else{
			List<DictItem> list = DictCacheService.getThis().getItemsByDictCode(dictCode.trim());
			if(list != null && list.size() > 0){
				for(DictItem dictItem : list){
					if(dictItem != null && SysConstant.STATUS_NORMAL == dictItem.getStatus()){
						if(Strings.isNotBlank(valDefault)){
							String[] vals = valDefault.split(",");
							for (String s : vals) {
								if(dictItem.getItemCode()!=null&&dictItem.getItemCode().toString().trim().equals(s.trim())){
									item = new HashMap<String, Object>();
									item.put("id", dictItem.getItemCode());
									item.put("pId", this.virtualRootId);
									item.put("name", dictItem.getItemName());
									rsList.add(item);
								}
							}
						}else{
							item = new HashMap<String, Object>();
							item.put("id", dictItem.getItemCode());
							item.put("pId", this.virtualRootId);
							item.put("name", dictItem.getItemName());
							rsList.add(item);
						}
					}
				}
			}
		}
		return JSON.toJSONString(rsList);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSqlValueKey() {
		return sqlValueKey;
	}

	public void setSqlValueKey(String sqlValueKey) {
		this.sqlValueKey = sqlValueKey;
	}

	public String getSqlLableKey() {
		return sqlLableKey;
	}

	public void setSqlLableKey(String sqlLableKey) {
		this.sqlLableKey = sqlLableKey;
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

	public String getBeforeLoadTree() {
		return beforeLoadTree;
	}

	public void setBeforeLoadTree(String beforeLoadTree) {
		this.beforeLoadTree = beforeLoadTree;
	}

	public String getAfterLoadTree() {
		return afterLoadTree;
	}

	public void setAfterLoadTree(String afterLoadTree) {
		this.afterLoadTree = afterLoadTree;
	}

	public String getVirtualRootLabel() {
		return virtualRootLabel;
	}

	public void setVirtualRootLabel(String virtualRootLabel) {
		this.virtualRootLabel = virtualRootLabel;
	}

	public String getVirtualRootId() {
		return virtualRootId;
	}

	public void setVirtualRootId(String virtualRootId) {
		this.virtualRootId = virtualRootId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	public String getLabelCheck() {
		return labelCheck;
	}

	public void setLabelCheck(String labelCheck) {
		this.labelCheck = labelCheck;
	}

	public String getValDefault() {
		return valDefault;
	}

	public void setValDefault(String valDefault) {
		this.valDefault = valDefault;
	}

}
