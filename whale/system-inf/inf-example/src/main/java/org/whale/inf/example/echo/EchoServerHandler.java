package org.whale.inf.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf in = (ByteBuf)msg;
		try {
			while (in.isReadable()) { // (1)
	            System.out.print(in.readByte());
	            System.out.flush();
	        }
		} finally {
	        ReferenceCountUtil.release(msg); // (2)
	    }
		ctx.write(msg);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelRegistered");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelUnregistered");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//ctx.flush();
		System.out.println("进入channel，完成");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("发生异常");
		cause.printStackTrace();
		ctx.close();
	}

}
