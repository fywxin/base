package org.whale.ext.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.whale.ext.domain.Domain;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.util.Strings;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class CodeEngine {
	
	private Configuration cfg;

	public void createCode(Domain domain) throws IOException, TemplateException{
		this.doCreateJava(domain, "Domain", domain.getCodePath()+File.separator+domain.getDomainName()+".java");
		this.doCreateJava(domain, "Dao", domain.getCodePath()+File.separator+domain.getDomainName()+"DaoImpl.java");
		this.doCreateJava(domain, "Service", domain.getCodePath()+File.separator+domain.getDomainName()+"ServiceImpl.java");
		this.doCreateJava(domain, "Router", domain.getCodePath()+File.separator+domain.getDomainName()+"Action.java");
		this.doCreateJsp(domain, "tree", domain.getCodePath()+File.separator+Strings.capitalize(domain.getDomainName())+File.separator+Strings.capitalize(domain.getDomainName())+"Tree.jsp");
		this.doCreateJsp(domain, "list", domain.getCodePath()+File.separator+Strings.capitalize(domain.getDomainName())+File.separator+Strings.capitalize(domain.getDomainName())+"List.jsp");
		this.doCreateJsp(domain, "save", domain.getCodePath()+File.separator+Strings.capitalize(domain.getDomainName())+File.separator+"add"+domain.getDomainName()+".jsp");
		this.doCreateJsp(domain, "update", domain.getCodePath()+File.separator+Strings.capitalize(domain.getDomainName())+File.separator+"edit"+domain.getDomainName()+".jsp");
		this.doCreateJsp(domain, "view", domain.getCodePath()+File.separator+Strings.capitalize(domain.getDomainName())+File.separator+Strings.capitalize(domain.getDomainName())+"View.jsp");
	}
	
	
	private void doCreateJava(Domain domain, String ftlName, String toFile) throws IOException, TemplateException{
		this.doCreateCode(domain, ftlName, toFile);
	}
	
	private void doCreateJsp(Domain domain, String ftlName, String toFile) throws IOException, TemplateException{
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
		File file = new File(getFltPath());
		cfg.setDirectoryForTemplateLoading(file);
		return cfg;
	}
	
	public String getFltPath(){
		return DictCacheService.getThis().getItemValue("DICT_SYS_CONF", "FTL_PATH", this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()
																+File.separator+"templates"+File.separator+"shop"+File.separator);
	}
}
