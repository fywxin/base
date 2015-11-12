package org.whale.system.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ServerContext {
	
	public static final String THREAD_KEY = "serverContext";

	private Integer index;
	
	/**
	 * 流获取到的数据
	 * 加密前的数据
	 */
	private byte[] data;
	
	/**
	 * body格式：
	 */
	private String body;
	
	private JSONArray bodyJsonArr;
	

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public void increaseIndex(){
		if(index == null){
			index = 1;
		}else{
			index++;
		}
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
		if(body != null){
			this.bodyJsonArr = JSON.parseArray(body);
		}
	}

	public JSONArray getBodyJsonArr() {
		return bodyJsonArr;
	}

	public JSONObject getByIndex(int index){
		return bodyJsonArr.getJSONObject(index);
	}
	
	public JSONObject getJsonObject(){
		return bodyJsonArr.getJSONObject(index);
	}
		
	public JSONArray getJsonArray(){
		return bodyJsonArr.getJSONArray(index);
	}
	
	public Object getObject(Class<?> clazz){
		return bodyJsonArr.getObject(index, clazz);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}
