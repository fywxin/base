package org.whale.ext.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmClass;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;

import com.alibaba.fastjson.JSON;

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
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response, String clazzName) {
		Map<String, Object> options = new HashMap<String, Object>();
		List<Domain> domains = this.domainService.queryAll();
		if(domains != null && domains.size() > 0){
			for(Domain item : domains){
				options.put("[db]"+item.getCnName(), item.getClazzName());
			}
		}
		
		
		Domain domain = null;
		if(Strings.isNotBlank(clazzName)){
			domain = this.domainService.getByClazzName(clazzName);
		}
		
		Collection<OrmClass> ormClasses = this.OrmContext.getOrmClassCache().values();
		for(OrmClass ormClass : ormClasses){
			if(domain == null){
				if(Strings.isNotBlank(clazzName)){
					if(clazzName.equals(ormClass.getOrmTable().getClazz().getName())){
						domain = this.doParse(ormClass.getOrmTable());
					}
				}else{
					domain = this.doParse(ormClass.getOrmTable());
				}
			}
			
			if(!options.containsValue(ormClass.getOrmTable().getClazz().getName())){
				options.put("[jv]"+ormClass.getOrmTable().getTableCnName(), ormClass.getOrmTable().getClazz().getName());
			}
		}
		
		return new ModelAndView("system/code/code_list")
				.addObject("domain", domain)
				.addObject("attrs", JSON.toJSONString(domain.getAttrs()))
				.addObject("total", domain.getAttrs().size())
				.addObject("options", LangUtil.bulidOptions(options, domain.getClazzName()))
				.addObject("pkgName", dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_PACKAGE));
	}
	
	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param auth
	 */
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Domain domain, String attrVals,
			String tcnName, String tname, boolean code){

		try{
			domain.setCnName(tcnName);
			domain.setName(tname);
			Domain dbDomain = null;
			List<Attr> attrs = JSON.parseArray(attrVals, Attr.class);
			domain.setAttrs(attrs);
			if((dbDomain = this.domainService.getByName(tname)) != null){
				domain.setId(dbDomain.getId());
				this.domainService.update(domain);
			}else{
				this.domainService.save(domain);
			}
			if(code){
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
	public void doDelete(HttpServletRequest request, HttpServletResponse response, String clazzName) {
		Domain domain = this.domainService.getByClazzName(clazzName);
		if(domain == null){
			WebUtil.printFail(request, response, "数据库查找不到clazzName=["+clazzName+"]的数据记录");
			return ;
		}
		this.domainService.delete(domain.getId());
		WebUtil.printSuccess(request, response);
	}
	
	private Domain doParse(OrmTable ormTable){
		Domain domain = new Domain();
		
		domain.setClazzName(ormTable.getClazz().getName());
		domain.setCnName(ormTable.getTableCnName());
		domain.setDbName(ormTable.getTableDbName());
		domain.setName(ormTable.getEntityName());
		
		List<OrmColumn> ormCols = ormTable.getOrmCols();
		if(ormCols != null && ormCols.size() > 0){
			List<Attr> attrs = new ArrayList<Attr>(ormCols.size());
			Attr attr = null;
			
			OrmColumn col = null;
			for(int i=1; i<= ormCols.size(); i++){
				col = ormCols.get(i-1);
				
				attr = new Attr();
				attr.setCnName(col.getCnName());
				attr.setDbType(col.getType());
				attr.setDefVal("");
				attr.setInOrder(i);
				attr.setInQuery(false);
				attr.setName(col.getAttrName());
				attr.setSqlName(col.getSqlName());
				attr.setType(Attr.parseType(col.getField().getType()));
				attr.setWidth(col.getWidth());
				attr.setPreci(col.getPrecision());
				
				if(col.getIsId()){
					attr.setIsId(true);
					attr.setInForm(false);
					attr.setInList(false);
					attr.setNullAble(false);
					attr.setUniqueAble(true);
					attr.setUpdateAble(false);
				}else{
					attr.setIsId(false);
					attr.setInForm(true);
					attr.setInList(true);
					attr.setNullAble(col.getOrmValidate()==null? true : col.getOrmValidate().isRequired());
					attr.setUniqueAble(col.getUnique());
					attr.setUpdateAble(col.getUpdateAble());
				}
				
				attrs.add(attr);
			}
			domain.setAttrs(attrs);
		}
		
		return domain;
	}
	
	private boolean isCheck(int[] checkeds, int i){
		if(checkeds == null || checkeds.length < 1)
			return false;
		for(int checked : checkeds){
			if(checked == i)
				return true;
		}
		return false;
	}
	
}
