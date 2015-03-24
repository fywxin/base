package org.whale.inf.example.time;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 *
 * @author 王金绍
 * @Date 2015年3月10日 下午4:23:39
 */
public class TimeClientHandlerUnSafe extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf m = (ByteBuf)msg;
		try{
			 long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
	            System.out.println(new Date(currentTimeMillis));
	            ctx.close();
		}finally{
			m.release();
		}
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
        ctx.close();
	}

	
}
