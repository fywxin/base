package org.whale.ext.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.ext.code.CodeEngine;
import org.whale.ext.domain.Attr;
import org.whale.ext.domain.Domain;
import org.whale.ext.service.DomainService;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Rs;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.util.OrmUtil;

@Controller
@RequestMapping("/code")
public class CodeRouter extends BaseRouter {
	
	@Autowired
	private OrmContext OrmContext;
	@Autowired
	private DomainService domainService;
	@Autowired
	private CodeEngine codeEngine;
	@Autowired
	private DictCacheService dictCacheService;


	@RequestMapping("/goList")
	public ModelAndView goList(String tableName) {
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
			if(tableName.startsWith("tb_base_")){
				tableName = tableName.substring(8);
			}
			domain.setDomainName(OrmUtil.sql2Camel(tableName));
			domain.setAttrs(attrs);
			
			Attr attr = null;
			if(cols.size() > 0){
				for(Map<String, Object> col : cols){
					attr = new Attr();
					attr.setSqlName(col.get("name").toString());
					attr.setCnName(col.get("comments") == null ? "" : col.get("comments").toString().replaceAll(",", "，"));
					String dbType = col.get("jdbcType").toString().toLowerCase();
					attr.setDbType(dbType);
					attr.setIsNull("1".equals(col.get("isNull").toString()));
					try{
						attr.setMaxLength(col.get("maxLength") == null || "".equals(col.get("maxLength").toString()) ? 0 : Integer.parseInt(col.get("maxLength").toString()));

					}catch(Exception e){
						e.printStackTrace();
					}
					attr.setInOrder(col.get("sort") == null ? 0 : Integer.parseInt(col.get("sort").toString()));
					
					attr.setName(OrmUtil.sql2Camel(attr.getSqlName()));
					//attr.setName(attr.getSqlName());
					
					if(dbType.equalsIgnoreCase("tinyint") || dbType.equalsIgnoreCase("smallint") || dbType.equalsIgnoreCase("mediumint")){
						attr.setType("Integer");
					}else if(dbType.toLowerCase().indexOf("bigint") != -1){
						attr.setType("Long");
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
					}else if(dbType.equalsIgnoreCase("float")){
						attr.setType("Float");
					}else if(dbType.equalsIgnoreCase("double") || dbType.equalsIgnoreCase("numeric") || dbType.equalsIgnoreCase("decimal")){
						attr.setType("Double");
					}else if(dbType.equalsIgnoreCase("datetime") || dbType.equalsIgnoreCase("date") || dbType.equalsIgnoreCase("timestamp")){
						attr.setType("Date");
					}else if(dbType.equalsIgnoreCase("bit") || dbType.equalsIgnoreCase("bit(1)")){
						attr.setType("Boolean");
					}else if(dbType.startsWith("decimal")){
						attr.setType("Double");
					}else if(dbType.equalsIgnoreCase("tinyint(1)")){
						attr.setType("Boolean");
					}else{
						attr.setType("String");
					}
					
					
					if("CREATE_TIME".equals(attr.getSqlName()) || "UPDATE_TIME".equals(attr.getSqlName()) || "UPDATER".equals(attr.getSqlName())){
						attr.setIsEdit(true);
						attr.setIsNull(true);
						attr.setInList(false);
						attr.setInForm(false);
						attr.setInQuery(false);
					}
					if("CREATE_TIME".equals(attr.getSqlName())){
						attr.setIsEdit(false);
					}
					
					attrs.add(attr);
					
					if(attr.getIsId()){
						attr.setInForm(false);
						attr.setInList(false);
					}
				}
			}
		}
		
		if(Strings.isBlank(domain.getCodePath())){
			domain.setCodePath("c://genCode");
		}
		if(Strings.isBlank(domain.getPkgName())){
			domain.setPkgName("net.youboo.ybplat");
		}
		
		return new ModelAndView("system/code/code_list")
				.addObject("domain", domain)
				.addObject("tables", tables);
	}

	@RequestMapping("/doSave")
	@ResponseBody
	public Rs doSave(Domain domain, 
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
						return Rs.fail("只能只有一个id字段");
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
			
//			if(domain.getId() == null){
//				this.domainService.save(domain);
//			}else{
//				this.domainService.update(domain);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return Rs.fail(e.getMessage());
		}
		return Rs.success();
	}
	
	
	private void sort(List<Attr> list){
		Collections.sort(list, new Comparator<Attr>(){
			@Override
			public int compare(Attr o1, Attr o2) {
				return o1.getInOrder() - o2.getInOrder();
			}
		});
	}
	
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(Long id){
		this.domainService.delete(id);
		return Rs.success();
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
