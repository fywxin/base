package org.whale.inf.example.protobuf.netty;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Message message = (Message)msg;
		System.out.println(JSON.toJSONString(message));
		ctx.fireChannelRead(msg);
		
	}

	
	
}
