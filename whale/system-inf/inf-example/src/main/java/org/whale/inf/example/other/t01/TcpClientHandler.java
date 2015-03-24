package org.whale.inf.example.other.t01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpClientHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger logger = LoggerFactory.getLogger(TcpClientHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
        logger.info("client接收到服务器返回的消息:"+msg); 
	}

}
