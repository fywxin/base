package org.whale.system.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.SpringContextHolder;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.DictItem;

/**
 * 字典控件,用户需提供字典编码，或查询sql，获取数据集
 * @author wjs
 *
 */
public class DictTag extends TagSupport {
	
	private static final long serialVersionUID = 1343L;
	private static final Logger logger = LoggerFactory.getLogger(DictTag.class);
	
	private static final String SQL_VALUE_KEY = "SQL_VALUE_KEY";
	private static final String SQL_LABEL_KEY = "SQL_LABEL_KEY";
	
	private static final String LOGIC_TRUE = "true";
	
	/** 字典select 的ID属性 */
	private String id;
	/** 字典编码 */
	private String dictCode;
	/** 初始默认选择值 */
	private String value;
	/** 选择空值提示信息 */
	private String headerLabel = "";
	/**默认显示数据**/
	private String valDefault = "";
	/** 宽度 */
	private String width = "165px";
	/** sql查询语句 */
	private String sql;
	/** sql对应返回值得key 为空sqlValue */
	private String sqlValueKey = DictTag.SQL_VALUE_KEY;
	/** sql对应返回值 为空sqlLable*/
	private String sqlLableKey = DictTag.SQL_LABEL_KEY;
	
	/** 只读模式，只会生成文本和<input type="hidden">的html*/
	private String readonly;
	/** 不可用模式，值为true 会在select 中加入disabled = disabled 属性*/
	private String disabled;
	
	@Override
	public int doStartTag() throws JspException {
		StringBuffer strb = new StringBuffer();
		if(Strings.isBlank(id))
			return SKIP_BODY;
		if(LOGIC_TRUE.equals(readonly)){
			if(Strings.isBlank(value))
				return SKIP_BODY;
			String val = "";
			if(Strings.isBlank(dictCode)){
				JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextHolder.getApplicationContext().getBean("jdbcTemplate");
				List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
				
				if(list != null && list.size() > 0){
					for(Map<String, Object> map : list){
						if(map.get(sqlValueKey) != null && map.get(sqlValueKey).toString().equals(value)){
							val = map.get(sqlLableKey) == null ? "" : map.get(sqlLableKey).toString();
						}
					}
				}
			}else{
				if(Strings.isNotBlank(DictCacheService.getThis().getItemLabel(dictCode, value)))
					val = DictCacheService.getThis().getItemLabel(dictCode, value).trim();
			}
			strb.append(val)
				.append("<input type='hidden' id='"+id+"' name='"+id+"' value='"+value+"' />");
		}else{
			strb.append("<select id='"+id+"' name='"+id+"' style='width:"+width+";'");
			if(LOGIC_TRUE.equals(disabled)){
				strb.append(" disabled = 'disabled'");
			}
			strb.append(" >");
			if(Strings.isNotBlank(headerLabel)){
				strb.append("\t<option value='' >").append(headerLabel).append("</option>");
			}
			
			if(Strings.isBlank(dictCode)){
				if(Strings.isBlank(sql))
					return SKIP_BODY;
				
				JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextHolder.getApplicationContext().getBean("jdbcTemplate");
				List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
				
				if(list != null && list.size() > 0){
					for(Map<String, Object> map : list){
						if(Strings.isNotBlank(valDefault)){
							String[] vals = valDefault.split(",");
							for (String s : vals) {
								if(map.get(sqlValueKey)!=null&&map.get(sqlValueKey).toString().trim().equals(s.trim())){
									strb.append(this.bulidOption(map.get(sqlValueKey), map.get(sqlLableKey), value, false));
								}
							}
						}else{
							strb.append(this.bulidOption(map.get(sqlValueKey), map.get(sqlLableKey), value, false));
						}
					}
				}
			}else{
				List<DictItem> list = DictCacheService.getThis().getItemsByDictCode(dictCode.trim());
				if(list != null && list.size() > 0){
					for(DictItem dictItem : list){
						if(dictItem != null){
							if(Strings.isNotBlank(valDefault)){
								String[] vals = valDefault.split(",");
								for (String s : vals) {
									if(dictItem.getItemCode()!=null&&dictItem.getItemCode().toString().trim().equals(s.trim())){
										strb.append(this.bulidOption(dictItem.getItemCode(), dictItem.getItemName(), value, SysConstant.STATUS_UNUSE == dictItem.getStatus()));
									}
								}
							}else{
								strb.append(this.bulidOption(dictItem.getItemCode(), dictItem.getItemName(), value, SysConstant.STATUS_UNUSE == dictItem.getStatus()));
							}
						}
					}
				}
			}
			strb.append("</select>");
		}
		try {
			pageContext.getOut().print(strb.toString());
		} catch (java.io.IOException e) {
			e.printStackTrace();
			logger.error("生成字典 ["+strb.toString()+"] 出错", e);
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}
	
	private String bulidOption(Object value, Object label, String selectValue, boolean disabled){
		if(value == null || disabled)
			return "";
		
		String val = value.toString().trim();
		String option="\t<option value='"+val+"'";
		if(val.equals(selectValue)){
			option += " selected = 'selected'";
		}
//		if(disabled){
//			option += " disabled = 'disabled'";
//		}
		option +=">"+label+"</option>";
		return option;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getHeaderLabel() {
		return headerLabel;
	}

	public void setHeaderLabel(String headerLabel) {
		this.headerLabel = headerLabel;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
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

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getValDefault() {
		return valDefault;
	}

	public void setValDefault(String valDefault) {
		this.valDefault = valDefault;
	}
	
}
