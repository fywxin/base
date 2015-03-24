package org.whale.inf.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	private final ByteBuf msgBuf;
	
	public EchoClientHandler(){
		msgBuf = Unpooled.buffer(100);
		for(int i=0; i< msgBuf.capacity(); i++){
			msgBuf.writeByte((byte)i);
		}
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("通道可用..");
		ctx.writeAndFlush(msgBuf);
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("开始发送消息channelRead");
		ctx.write(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();System.out.println("完成发送消息");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("发生异常");
		cause.printStackTrace();
		ctx.close();
	}

}
