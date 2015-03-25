package org.whale.inf.example.protobuf.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0; i<50; i++){
			Message msg = new Message();
			msg.setId(System.currentTimeMillis());
			msg.setName("name"+i);
			
			Header header = new Header();
			header.setCrcCode(i+1000);
			header.setLength(i);
			header.setToken("token"+i);
			msg.setHeader(header);
			
			ctx.write(msg);
		}
		ctx.flush();
	}

	
}
