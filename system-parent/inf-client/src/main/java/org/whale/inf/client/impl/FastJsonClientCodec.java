package org.whale.inf.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.whale.inf.client.ClientCodec;
import org.whale.inf.client.ClientContext;
import org.whale.system.common.exception.HttpClientException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 默认为fastJSON协议
 * 
 * @author wjs
 * @date 2015年11月20日 下午6:06:23
 */
public class FastJsonClientCodec implements ClientCodec {

	@Override
	public byte[] encode(List<Object> args, ClientContext clientContext) {
		if(args != null && args.size() > 0) {
			String data = JSON.toJSONString(args, SerializerFeature.WriteClassName);
			clientContext.setReqStr(data);
			try {
				return data.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new HttpClientException("请求参数序列化编码异常", e);
			}
		}
		return null;
	}

	@Override
	public Object decode(byte[] data, ClientContext clientContext) {
		if(data != null && data.length > 0){
			String str =null;
			try {
				str = IOUtils.toString(data, "UTF-8");
			} catch (IOException e) {
				throw new HttpClientException("响应结果反序列化编码异常", e);
			}
			clientContext.setRespStr(str);
			return JSON.parse(str);
		}
		return null;
	}

}
