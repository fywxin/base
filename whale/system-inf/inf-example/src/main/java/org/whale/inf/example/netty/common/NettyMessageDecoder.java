package org.whale.inf.example.netty.common;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		
		ByteBuf frame = (ByteBuf)super.decode(ctx, in);
		//System.out.println("decode...."+(frame == null));
		if(frame == null){
			System.out.println("frame 解析不匹配");
			return null;
		}
		
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionId(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		byte[] byteArr = null;
//		in.readBytes(byteArr);
//		header.setToken(new String(byteArr, "UTF-8"));
		int size = frame.readInt();
		if(size > 0){
			Map<String, String> attach = new HashMap<String, String>();
			int keySize = 0;
			String key = null;
			String value = null;
			for(int i=0; i<size; i++){
				keySize = frame.readInt();
				byteArr = new byte[keySize];
				frame.readBytes(byteArr);
				key = new String(byteArr, "UTF-8");
				keySize = frame.readInt();
				byteArr = new byte[keySize];
				frame.readBytes(byteArr);
				value = new String(byteArr, "UTF-8");
				attach.put(key, value);
			}
			header.setAttachment(attach);
		}
		size = frame.readInt();
		if(size > 0){
			byteArr = new byte[size];
			frame.readBytes(byteArr);
			Object body = JSON.parseObject(new String(byteArr, "UTF-8"));
			nettyMessage.setBody(body);
		}
		nettyMessage.setHeader(header);
		
		return nettyMessage;
	}


}
