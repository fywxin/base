package org.whale.system.ueditor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.whale.system.domain.FileInfo;
import org.whale.system.ueditor.define.ActionMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 配置管理器
 * @author hancong03@baidu.com
 *
 */
public final class ConfigManager {
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private JSONObject jsonConfig = null;
	// 涂鸦上传filename定义
	private final static String SCRAWL_FILE_NAME = "scrawl";
	// 远程图片抓取filename定义
	private final static String REMOTE_FILE_NAME = "remote";
	
	/**
	 * 配置管理器构造工厂
	 * @param rootPath 服务器根路径
	 * @param contextPath 服务器所在项目路径
	 * @param uri 当前访问的uri
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance (Integer saveWay) {
		try {
			return new ConfigManager(saveWay);
		} catch (Exception e) {
			return null;
		}
	}
	
	private ConfigManager (Integer saveWay) throws IOException {
		this.initEnv(saveWay);
	}
	
	/**
	 * 重写保存方式
	 */
	private void initEnv (Integer saveWay) throws IOException {
		String configContent = this.readFile();
		try{
			JSONObject jsonConfig = JSON.parseObject(configContent);
			this.jsonConfig = jsonConfig;
			if(saveWay != null && (FileInfo.SAVE_FTP.equals(saveWay) || FileInfo.SAVE_BOTH.equals(saveWay) || FileInfo.SAVE_DISK.equals(saveWay))){
				this.jsonConfig.put("saveWay", saveWay);
			}
		} catch (Exception e) {
			this.jsonConfig = null;
		}
	}
	
	// 验证配置文件加载是否正确
	public boolean valid () {
		return this.jsonConfig != null;
	}
	
	public JSONObject getAllConfig () {
		return this.jsonConfig;
	}
	
	public Map<String, Object> getConfig (int type) {
		Map<String, Object> conf = new HashMap<String, Object>();
		String savePath = null;
		
		switch (type) {
		
			case ActionMap.UPLOAD_FILE:
				conf.put("isBase64", "false");
				conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
				conf.put("allowFiles", this.getArray("fileAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
				conf.put("saveWay", this.jsonConfig.getInteger("saveWay"));
				savePath = this.jsonConfig.getString("filePathFormat");
				break;
				
			case ActionMap.UPLOAD_IMAGE:
				conf.put("isBase64", "false");
				conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
				conf.put("allowFiles", this.getArray("imageAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
				conf.put("saveWay", this.jsonConfig.getInteger("saveWay"));
				savePath = this.jsonConfig.getString("imagePathFormat");
				break;
				
			case ActionMap.UPLOAD_VIDEO:
				conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
				conf.put("allowFiles", this.getArray("videoAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
				conf.put("saveWay", this.jsonConfig.getInteger("saveWay"));
				savePath = this.jsonConfig.getString("videoPathFormat");
				break;
				
			case ActionMap.UPLOAD_SCRAWL:
				conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
				conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
				conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
				conf.put("isBase64", "true");
				savePath = this.jsonConfig.getString("scrawlPathFormat");
				break;
				
			case ActionMap.CATCH_IMAGE:
				conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
				conf.put("filter", this.getArray("catcherLocalDomain"));
				conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
				conf.put("allowFiles", this.getArray("catcherAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("catcherFieldName") + "[]");
				savePath = this.jsonConfig.getString("catcherPathFormat");
				break;
				
			case ActionMap.LIST_IMAGE:
				conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
				conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
				conf.put("count", this.jsonConfig.getInteger("imageManagerListSize"));
				break;
				
			case ActionMap.LIST_FILE:
				conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
				conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
				conf.put("count", this.jsonConfig.getInteger("fileManagerListSize"));
				break;
				
		}
		
		conf.put("savePath", savePath);
		
		return conf;
	}
	
	

	private String[] getArray (String key) {
		JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
		String[] result = new String[ jsonArray.size() ];
		
		for (int i = 0, len = jsonArray.size(); i < len; i++) {
			result[i] = jsonArray.getString(i);
		}
		
		return result;
	}
	
	private String readFile () throws IOException {
		Resource resource = this.resourceLoader.getResource("ueditorConfig.json");
		
		String json = FileUtils.readFileToString(resource.getFile());
		return this.filter(json);
	}
	
	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter (String input) {
		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
	}
	
}
