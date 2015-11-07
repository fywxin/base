package org.whale.system.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.inf.OneValueObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;


/**
 *  由于springmvc 处理返回参数的 处理链不支持顺序变更，@RequestMappingHandlerAdapter .getDefaultReturnValueHandlers() 
 *  未预留处理口给开发人员干预返回值处理链的顺序，导致自定义类型优先级低于spring 内部自带处理类型
 * 	当返回类型为void 或 String 时，先执行spring内部的 @ViewNameMethodReturnValueHandler
 *  导致自定义的 @ReqRespBodyProcessorProxy 类无法被执行到
 *  因此：Controller返回类型必须已经是包装类型 
 *  
 *  目前使用 @Result<T>
 * 
 * @author 王金绍
 * 2015年11月7日 下午5:59:42
 */
public class FastJsonReqRespHttpMessageConverter extends FastJsonHttpMessageConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(FastJsonReqRespHttpMessageConverter.class);

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
	        	bytes = this.reqRespHandler.onRead(bytes);
	        }
	        
	        if(ReflectionUtil.isBaseDataType(clazz)){
	        	if(logger.isDebugEnabled()){
	        		logger.debug("参数为基本类型 {}", clazz);
	        	}
	        	OneValueObject oneValueObj = JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), OneValueObject.class);
	        	if(clazz.equals(Long.class)){
	        		return Long.valueOf(oneValueObj.getValue().toString());
	        	}
	        	
	        	//TODO
	        	return oneValueObj.getValue();
	        }else{
	        	return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), clazz);
	        }
	    }

	    @Override
	    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
	        OutputStream out = outputMessage.getBody();
	                
	        //String text = JSON.toJSONString(obj, this.getFeatures());
	        //保证客户端可以反序列化成功
	        String text = JSON.toJSONString(obj, SerializerFeature.WriteClassName);
	        
	        byte[] bytes = text.getBytes(charset);
	        
	        if(reqRespHandler != null){
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
