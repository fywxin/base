package org.whale.ext.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.ext.domain.Domain;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.util.Strings;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class CodeEngine {
	
	private Configuration cfg;
	@Autowired
	private DictCacheService dictCacheService;

	public void createCode(Domain domain) throws IOException, TemplateException{
		if(Strings.isBlank(domain.getPkgName())){
			domain.setPkgName(dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_PACKAGE));
		}
		
		this.doCreateJava(domain, "Domain", dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_CODE_DOMAIN_PATH, "c:"+File.separator+"code")+File.separator+domain.getName()+".java");
		this.doCreateJava(domain, "Dao", dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_CODE_DAO_PATH, "c:"+File.separator+"code")+File.separator+domain.getName()+"Dao.java");
		this.doCreateJava(domain, "Service", dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_CODE_SERVICE_PATH, "c:"+File.separator+"code")+File.separator+domain.getName()+"Service.java");
		this.doCreateJava(domain, "Controller", dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_CODE_CONTROLLER_PATH, "c:"+File.separator+"code")+File.separator+domain.getName()+"Controller.java");
		this.doCreateJsp(domain, "tree");
		this.doCreateJsp(domain, "list");
		this.doCreateJsp(domain, "save");
		this.doCreateJsp(domain, "update");
		this.doCreateJsp(domain, "view");
	}
	
	private void doCreateJava(Domain domain, String ftlName, String toFile) throws IOException, TemplateException{
		this.doCreateCode(domain, ftlName, toFile);
	}
	
	private void doCreateJsp(Domain domain, String ftlName) throws IOException, TemplateException{
		String codeDir = dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_CODE_JSP_PATH, "c:"+File.separator+"code"+File.separator+"jsp");
		String toFile = codeDir+"//"+Strings.capitalize(domain.getName())+"_"+ftlName+".jsp";
		this.doCreateCode(domain, ftlName, toFile);
	}
	
	private void doCreateCode(Domain domain, String ftlName, String toFile) throws IOException, TemplateException{
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("domain", domain);
		
		Template temp = this.loadCfg().getTemplate(ftlName+".ftl");
		FileWriter fw = null;
		try {
			File file = new File(toFile);
			if(!file.getParentFile().exists()){
				FileUtils.forceMkdir(file.getParentFile());
			}
			if(!file.exists()){
				file.createNewFile();
				file.setWritable(true);
			}
			fw = new FileWriter(file);
			temp.process(root, fw);
			fw.flush();
		} finally{
			if(fw != null){
				fw.close();
			}
		}
	}
	
	
	private Configuration loadCfg() throws IOException{
		if(cfg == null){
			synchronized (this) {
				if(cfg == null){
					cfg= new Configuration();
					cfg.setObjectWrapper(new DefaultObjectWrapper());
				}
			}
		}
		File file = new File(dictCacheService.getItemValue(DictConstant.DICT_CODE, DictConstant.DICT_ITEM_FTL_PATH, "E:\\github\\platform\\base\\whale\\system-ext\\ext-code\\src\\main\\resources\\templates"));
		cfg.setDirectoryForTemplateLoading(file);
		return cfg;
	}
	
	
}
