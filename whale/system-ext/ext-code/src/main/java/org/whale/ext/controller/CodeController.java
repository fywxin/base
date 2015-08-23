package org.whale.ext.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
					attr.setMaxLength(col.get("maxLength") == null || "".equals(col.get("maxLength").toString()) ? 0 : Integer.parseInt(col.get("maxLength").toString()));
					attr.setInOrder(col.get("sort") == null ? 0 : Integer.parseInt(col.get("sort").toString()));
					
					attr.setName(OrmUtil.sql2DumpStyle(attr.getSqlName()));
					
					if(dbType.equalsIgnoreCase("tinyint") || dbType.equalsIgnoreCase("smallint") || dbType.equalsIgnoreCase("mediumint")){
						attr.setType("Integer");
					}else if(dbType.indexOf("int") != -1){
						if(attr.getMaxLength() == null || attr.getMaxLength() >= 10){
							attr.setType("Long");
						}else{
							if(dbType.indexOf("int(1") != -1){
								attr.setType("Long");
							}else{
								attr.setType("Integer");
							}
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
		
		if(Strings.isBlank(domain.getCodePath())){
			domain.setCodePath("c://genCode");
		}
		if(Strings.isBlank(domain.getPkgName())){
			domain.setPkgName("org.whale.system");
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

		if(Strings.isBlank(domain.getPkgName())){
			domain.setPkgName("c://genCode");
		}
		
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
			List<Attr> listAttrs = new ArrayList<Attr>();
			List<Attr> formAttrs = new ArrayList<Attr>();
			List<Attr> queryAttrs = new ArrayList<Attr>();
			
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
				
				if(attr.getIsId()){
					if(domain.getIdAttr() == null){
						domain.setIdAttr(attr);
					}else{
						WebUtil.printFail(request, response, "只能只有一个id字段");
						return ;
					}
				}
				
				if(attr.getInList()){
					listAttrs.add(attr);
				}
				if(attr.getInForm()){
					formAttrs.add(attr);
				}
				if(attr.getInQuery()){
					queryAttrs.add(attr);
				}
			}
			sort(attrs);
			sort(listAttrs);
			sort(formAttrs);
			sort(queryAttrs);
			
			domain.setAttrs(attrs);
			domain.setListAttrs(listAttrs);
			domain.setQueryAttrs(queryAttrs);
			domain.setFormAttrs(formAttrs);
			
			if(gen){
				this.codeEngine.createCode(domain);
			}
			
			if(domain.getId() == null){
				this.domainService.save(domain);
			}else{
				this.domainService.update(domain);
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.printFail(request, response, e.getMessage());
			return ;
		}
		WebUtil.printSuccess(request, response);
	}
	
	
	private void sort(List<Attr> list){
		Collections.sort(list, new Comparator<Attr>(){
			@Override
			public int compare(Attr o1, Attr o2) {
				return o1.getInOrder() - o2.getInOrder();
			}
		});
	}
	
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, Long id){
		this.domainService.delete(id);
		WebUtil.printSuccess(request, response);
	}
	
//	private String getProjectPath(){
//		try {
//			File file = new DefaultResourceLoader().getResource("").getFile();
//			if(file != null){
//				return file.getAbsolutePath();
//			}
//		} catch (IOException e) {
//			throw new BaseException(e);
//		}
//		return null;
//	}
}
