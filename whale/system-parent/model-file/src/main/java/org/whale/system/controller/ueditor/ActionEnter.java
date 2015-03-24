package org.whale.system.controller.ueditor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.whale.system.common.util.Strings;
import org.whale.system.controller.ueditor.define.ActionMap;
import org.whale.system.controller.ueditor.define.AppInfo;
import org.whale.system.controller.ueditor.define.BaseState;
import org.whale.system.controller.ueditor.define.State;
import org.whale.system.controller.ueditor.hunter.FileManager;
import org.whale.system.controller.ueditor.hunter.ImageHunter;
import org.whale.system.controller.ueditor.upload.BinaryUploader;

public class ActionEnter {
	
	private HttpServletRequest request = null;
	
	private String actionType = null;
	
	private ConfigManager configManager = null;

	public ActionEnter(HttpServletRequest request){
		this.request = request;
		this.actionType = request.getParameter("action");
		//jsp页面传递的保存方式可以覆盖此保存方式
		String saveWay = request.getParameter("saveWay");
		this.configManager = ConfigManager.getInstance(Strings.isBlank(saveWay) ? null : Integer.parseInt(saveWay));
	}
	
	public String exec(){
		String callbackName = this.request.getParameter("callback");
		if(callbackName != null){
			if(!validCallbackName(callbackName)){
				return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
			}
			return callbackName+"("+this.invoke()+");";
		} else {
			return this.invoke();
		}
	}
	
	public String invoke(){
		if(actionType == null || !ActionMap.mapping.containsKey(actionType)){
			return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
		}
		
		if(this.configManager == null || !this.configManager.valid()){
			return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
		}
		
		State state = null;
		
		int actionCode = ActionMap.getType(this.actionType);
		Map<String, Object> conf = null;
		switch(actionCode){
			case ActionMap.CONFIG:
				return this.configManager.getAllConfig().toString();
				
			case ActionMap.UPLOAD_IMAGE:
			case ActionMap.UPLOAD_SCRAWL:
			case ActionMap.UPLOAD_VIDEO:
			case ActionMap.UPLOAD_FILE:
				conf = this.configManager.getConfig(actionCode);
				state = BinaryUploader.save(request, conf);
				break;
				
			case ActionMap.CATCH_IMAGE:
				conf = configManager.getConfig(actionCode);
				String[] list = this.request.getParameterValues((String)conf.get("fieldName"));
				state = new ImageHunter(conf).capture(list);
				break;
				
			case ActionMap.LIST_IMAGE:
			case ActionMap.LIST_FILE:
				conf = configManager.getConfig(actionCode);
				int start = this.getStartIndex();
				state = new FileManager(conf).listFile(start);
				break;
				
		}
		
		return state.toJSONString();
	}
	
	public int getStartIndex(){
		String start = this.request.getParameter("start");
		try {
			return Integer.parseInt(start);
		} catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * callback参数验证
	 */
	public boolean validCallbackName(String name){
		if(name.matches("^[a-zA-Z_]+[\\w0-9_]*$")){
			return true;
		}
		return false;
	}
	
}