package org.whale.inf.example.protobuf.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;


/**
 * 抛出空指针异常，则需要把tools copy到jre目录中
 * http://www.cnblogs.com/fangwenyu/archive/2011/10/12/2209051.html
 * 
 * @author Administrator
 *
 */
public class TestMain {

	public static void main(String[] args) throws IOException {
		
		Codec<Header> simpleTypeCodec = ProtobufProxy
                .create(Header.class);

		//NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(123);
		header.setType(2);
		header.setToken("asdfasdfasd");
		header.setPriority(1);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("asd", "3434");
		
		//header.setAttachment(map);
		
//		message.setBody(new Object());
//		message.setHeader(header);
		
		
		System.out.println(JSON.toJSONString(header));
        try {
            // 序列化
            byte[] bb = simpleTypeCodec.encode(header);
            System.out.println(JSON.toJSONString(header).getBytes().length);
            System.out.println(bb.length);
            // 反序列化
            Header newStt = simpleTypeCodec.decode(bb);
            System.out.println(JSON.toJSONString(newStt));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
//			    // 通过 .proto描述文件生成动态解析对象
//			    String protoCotent = "package mypackage.test; " +
//			            "option java_package = \"com.baidu.bjf.remoting.protobuf.simplestring\";" +
//			            "option java_outer_classname = \"StringTypeClass\";  " +
//			            "message StringMessage { " +
//			            "  required string message = 1; }" ;
//
//			    IDLProxyObject object = ProtobufIDLProxy.createSingle(protoCotent);
//			    //if .proto IDL defines multiple messages use as follow
//			    //Map<String, IDLProxyObject> objects = ProtobufIDLProxy.create(protoCotent);
//			    // 动态设置字段值
//			    object.put("message", "hello你好");
//			    //propogation object set
//			    //object.put("sub.field", "hello world");
//			    // protobuf 序列化
//			    byte[] bb = object.encode();
//
//			    // protobuf 反序列化
//			    IDLProxyObject result = object.decode(bb);
	}
}
