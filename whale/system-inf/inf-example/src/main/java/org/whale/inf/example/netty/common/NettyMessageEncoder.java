package org.whale.inf.example.netty.common;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
		System.out.println("encode....");
		if(msg == null || msg.getHeader() == null){
			throw new RpcException("消息对象不能为空");
		}
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionId());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeByte(msg.getHeader().getPriority());
		//sendBuf.writeBytes(msg.getHeader().getToken().getBytes("UTF-8"));
		sendBuf.writeInt(msg.getHeader().getAttachment().size());
		
		byte[] key = null;
		byte[] value = null;
		for(Map.Entry<String, String> param : msg.getHeader().getAttachment().entrySet()){
			key = param.getKey().getBytes("UTF-8");
			value = param.getValue().getBytes("UTF-8");
			sendBuf.writeInt(key.length);
			sendBuf.writeBytes(key);
			sendBuf.writeInt(value.length);
			sendBuf.writeBytes(value);
		}
		
		byte[] valueJson = null;
		if(msg.getBody() != null){
			valueJson = JSON.toJSONBytes(msg.getBody(), SerializerFeature.WriteClassName);
			sendBuf.writeInt(valueJson.length);
			sendBuf.writeBytes(valueJson);
		}else{
			sendBuf.writeInt(0);
		}
		sendBuf.setInt(4, sendBuf.readableBytes());
		
		out.add(sendBuf);
	}

	public static void main(String[] args) throws UnsupportedEncodingException  {
		
		byte[] b = null;
		b = JSON.toJSONBytes("asdasd", SerializerFeature.WriteClassName);
		
		JSON.parseObject(new String(b, "UTF-8"));
		
	}
}
