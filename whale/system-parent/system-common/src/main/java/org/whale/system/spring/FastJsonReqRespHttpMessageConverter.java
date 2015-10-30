package org.whale.system.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonReqRespHttpMessageConverter extends FastJsonHttpMessageConverter {

    private Charset charset  = UTF8;
    
    private ReqRespHandler reqRespHandler;
    
    public FastJsonReqRespHttpMessageConverter(){
    	super();
    }
    
	 @Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,HttpMessageNotReadableException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        InputStream in = inputMessage.getBody();
	        byte[] buf = new byte[1024];
	        for (;;) {
	            int len = in.read(buf);
	            if (len == -1) {
	                break;
	            }
	            if (len > 0) {
	                baos.write(buf, 0, len);
	            }
	        }
	        byte[] bytes = baos.toByteArray();
	        
	        if(reqRespHandler != null){
	        	//解密与校验操作
	        	bytes = this.reqRespHandler.onRead(bytes);
	        }
	        
	        return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), clazz);
	    }

	    @Override
	    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
	        OutputStream out = outputMessage.getBody();
	        String text = JSON.toJSONString(obj, this.getFeatures());
	        byte[] bytes = text.getBytes(charset);
	        
	        if(reqRespHandler != null){
	        	//加密操作
	        	bytes = this.reqRespHandler.onWrite(bytes);
	        }
	        
	        out.write(bytes);
	    }

		public ReqRespHandler getReqRespHandler() {
			return reqRespHandler;
		}

		public void setReqRespHandler(ReqRespHandler reqRespHandler) {
			this.reqRespHandler = reqRespHandler;
		}
	    
	    
}
