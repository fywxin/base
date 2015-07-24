package org.whale.ext.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.ext.code.CodeEngine;
import org.whale.ext.domain.Attr;
import org.whale.ext.domain.Domain;
import org.whale.ext.service.DomainService;
import org.whale.system.base.BaseController;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.util.OrmUtil;

@Controller
@RequestMapping("/code")
public class CodeController extends BaseController {
	
	@Autowired
	private OrmContext OrmContext;
	@Autowired
	private DomainService domainService;
	@Autowired
	private CodeEngine codeEngine;
	@Autowired
	private DictCacheService dictCacheService;

	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param dictName
	 * @param dictCode
	 * @return
	 */
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response, String tableName) {
		List<Map<String, Object>> tables = this.domainService.queryAllTable();
		
		//取得tableName 对应的数据库记录
		Map<String, Object> table = null;
		if(Strings.isNotBlank(tableName)){
			for(Map<String, Object> t : tables){
				if(tableName.equalsIgnoreCase(t.get("name").toString())){
					table = t;
					break;
				}
			}
		}
		
		//没有默认表或者tableName
		if(table == null){
			table = tables.get(0);
			tableName = table.get("name").toString();
		}
		
		List<Map<String, Object>> cols = this.domainService.queryColsByTable(tableName);
		Domain domain = this.domainService.getBySqlName(tableName);
		
		if(domain == null){
			domain = new Domain();
			List<Attr> attrs = new ArrayList<Attr>(cols.size());
			
			domain.setDomainSqlName(tableName);
			domain.setDomainCnName(table.get("comments") == null ? "" : table.get("comments").toString());
			domain.setDomainName(OrmUtil.sql2DumpStyle(tableName));
			domain.setPkgName("");
			domain.setAttrs(attrs);
			
			Attr attr = null;
			if(cols.size() > 0){
				for(Map<String, Object> col : cols){
					attr = new Attr();
					attr.setSqlName(col.get("name").toString());
					attr.setCnName(col.get("comments") == null ? "" : col.get("comments").toString());
					String dbType = col.get("jdbcType").toString().toLowerCase();
					attr.setDbType(dbType);
					attr.setIsNull("1".equals(col.get("isNull").toString()));
					attr.setMaxLength(col.get("maxLength") == null || "".equals(col.get("maxLength").toString()) ? null : Integer.parseInt(col.get("maxLength").toString()));
					attr.setInOrder(col.get("sort") == null ? 0 : Integer.parseInt(col.get("sort").toString()));
					
					attr.setName(OrmUtil.sql2DumpStyle(attr.getSqlName()));
					
					if(dbType.equalsIgnoreCase("tinyint") || dbType.equalsIgnoreCase("smallint") || dbType.equalsIgnoreCase("mediumint")){
						attr.setType("Integer");
					}else if(dbType.indexOf("int") != -1){
						if(attr.getMaxLength() == null || attr.getMaxLength() >= 10){
							attr.setType("Long");
						}else{
							attr.setType("Integer");
						}
					}else if(dbType.equalsIgnoreCase("bigint")){
						attr.setType("Long");
					}else if(dbType.equalsIgnoreCase("float")){
						attr.setType("Float");
					}else if(dbType.equalsIgnoreCase("double") || dbType.equalsIgnoreCase("numeric") || dbType.equalsIgnoreCase("decimal")){
						attr.setType("Double");
					}else if(dbType.equalsIgnoreCase("datetime") || dbType.equalsIgnoreCase("date") || dbType.equalsIgnoreCase("timestamp")){
						attr.setType("Date");
					}else{
						attr.setType("String");
					}
					
					attrs.add(attr);
				}
			}
		}
		
		return new ModelAndView("system/code/code_list")
				.addObject("domain", domain)
				.addObject("tables", tables);
	}
	
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param auth
	 */
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Domain domain, 
			String sqlName, String name, String cnName,String dbType, String type,
			String isId, String isNull, String isEdit, String isUnique,String inList, String inForm,String inQuery, 
			String queryType, String formType, String dictName, String maxLength, String inOrder, boolean gen){

		try{
			String[] sqlNames = sqlName.split(",");
			String[] names = name.split(",");
			String[] cnNames = cnName.split(",");
			String[] dbTypes = dbType.split(",");
			String[] types = type.split(",");
			String[] isIds = isId.split(",");
			String[] isNulls = isNull.split(",");
			String[] isEdits = isEdit.split(",");
			String[] isUniques = isUnique.split(",");
			String[] inLists = inList.split(",");
			String[] inQuerys = inQuery.split(",");
			String[] queryTypes = queryType.split(",");
			String[] inForms = inForm.split(",");
			String[] formTypes = formType.split(",");
			String[] dictNames = null;
			if(Strings.isNotBlank(dictName)){
				dictNames = dictName.split(",");
			}
			String[] maxLengths = maxLength.split(",");
			String[] inOrders = inOrder.split(",");
			
			List<Attr> attrs = new ArrayList<Attr>(sqlNames.length);
			Attr attr = null;
			for(int i=0; i<sqlNames.length; i++){
				attr = new Attr();
				attr.setSqlName(sqlNames[i]);
				attr.setName(names[i]);
				attr.setCnName(cnNames[i]);
				attr.setDbType(dbTypes[i]);
				attr.setType(types[i]);
				attr.setIsId("1".equals(isIds[i]));
				attr.setIsNull("1".equals(isNulls[i]));
				attr.setIsEdit("1".equals(isEdits[i]));
				attr.setIsUnique("1".equals(isUniques[i]));
				attr.setInList("1".equals(inLists[i]));
				attr.setInQuery("1".equals(inQuerys[i]));
				attr.setQueryType(queryTypes[i]);
				attr.setInForm("1".equals(inForms[i]));
				attr.setFormType(formTypes[i]);
				attr.setDictName((dictNames !=null && dictNames.length >i) ? dictNames[i] : null);
				attr.setMaxLength((maxLengths.length <= i || Strings.isBlank(maxLengths[i])) ? null : Integer.parseInt(maxLengths[i]));
				attr.setInOrder((inOrders.length <= i || Strings.isBlank(inOrders[i])) ? null : Integer.parseInt(inOrders[i]));
				attrs.add(attr);
			}
			domain.setAttrs(attrs);
			if(domain.getId() == null){
				this.domainService.save(domain);
			}else{
				this.domainService.update(domain);
			}
			
			if(gen){
				this.codeEngine.createCode(domain);
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.printFail(request, response, e.getMessage());
			return ;
		}
		WebUtil.printSuccess(request, response);
	}
	
	
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, Long id){
		this.domainService.delete(id);
		WebUtil.printSuccess(request, response);
	}
}
