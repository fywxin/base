package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CodeJson<M extends Serializable> extends AbstractCode<M> {

	@Override
	public byte[] doEncode(String cacheName, M m) throws IOException {
//		if(m instanceof String){
//			String str = (String)m;
//			try {
//				return str.getBytes("UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		} else if(m instanceof Integer){
//			Integer i = (Integer)m;
//			byte[] byte1 = new byte[1];
//			byte1[0] = i.byteValue();
//			return byte1;
//		} else if(m instanceof Long){
//			Long i = (Long)m;
//			byte[] byte1 = new byte[1];
//			byte1[0] = i.byteValue();
//			return byte1;
//		} else if(m instanceof Boolean){
//			Boolean i = (Boolean)m;
//			byte[] byte1 = new byte[1];
//			byte a = 0x01;
//			if(!i.booleanValue()){
//				a = 0x00;
//			}
//			byte1[0] = a;
//			return byte1;
//		} else if(m instanceof Float){
//			Float f = (Float)m;
//			byte[] byte1 = new byte[1];
//			byte1[0] = f.byteValue();
//			return byte1;
//		} else if(m instanceof Double){
//			Double d = (Double)m;
//			byte[] byte1 = new byte[1];
//			byte1[0] = d.byteValue();
//			return byte1;
//		} else if(m instanceof Short){
//			Short s = (Short)m;
//			byte[] byte1 = new byte[1];
//			byte1[0] = s.byteValue();
//			return byte1;
//		} else if(m instanceof Byte){
//			byte[] byte1 = new byte[1];
//			byte1[0] = (Byte)m;
//			return byte1;
//		}else{
//			return JSON.toJSONBytes(m, null);
//		}
		
		return JSON.toJSONString(m, SerializerFeature.WriteClassName).getBytes("UTF-8");
	}

	@SuppressWarnings("all")
	@Override
	public M doDecode(Class<?> type, byte[] bytes) throws IOException  {
		if(type == null)
			return null;
		return (M)JSON.parseObject(new String(bytes), type);
	}
	
}
