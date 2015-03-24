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
		System.out.println("decode...."+(frame == null));
//		if(frame == null)
//			return null;
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(in.readInt());
		header.setLength(in.readInt());
		header.setSessionId(in.readLong());
		header.setType(in.readByte());
		header.setPriority(in.readByte());
		byte[] byteArr = null;
//		in.readBytes(byteArr);
//		header.setToken(new String(byteArr, "UTF-8"));
		int size = in.readInt();
		if(size > 0){
			Map<String, String> attach = new HashMap<String, String>();
			int keySize = 0;
			String key = null;
			String value = null;
			for(int i=0; i<size; i++){
				keySize = in.readInt();
				byteArr = new byte[keySize];
				in.readBytes(byteArr);
				key = new String(byteArr, "UTF-8");
				keySize = in.readInt();
				byteArr = new byte[keySize];
				in.readBytes(byteArr);
				value = new String(byteArr, "UTF-8");
				attach.put(key, value);
			}
			header.setAttachment(attach);
		}
		size = in.readInt();
		if(size > 0){
			byteArr = new byte[size];
			in.readBytes(byteArr);
			Object body = JSON.parseObject(new String(byteArr, "UTF-8"));
			nettyMessage.setBody(body);
		}
		nettyMessage.setHeader(header);
		
		return nettyMessage;
	}


}
