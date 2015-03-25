package org.whale.inf.example.netty.client;

import java.util.concurrent.TimeUnit;

import org.whale.inf.example.netty.common.Header;
import org.whale.inf.example.netty.common.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * 心跳检测
 *
 * @author 王金绍
 * @Date 2015年3月24日 下午3:57:03
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		NettyMessage message = (NettyMessage)msg;
		if(message != null && message.getHeader() != null){
			//应用登录成功后，开始启动心跳消息
			if(message.getHeader().getType() == Header.LOGIN_RESP){
				heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 2000, TimeUnit.MILLISECONDS);
				
			//心跳应答
			} if(message.getHeader().getType() == Header.HEART_BEAT_RESP){
				System.out.println("心跳响应时间 : "+System.currentTimeMillis());
				
			} else{
				ctx.fireChannelRead(msg);
			}
			
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	private class HeartBeatTask implements Runnable{
		
		ChannelHandlerContext ctx;
		
		public HeartBeatTask(ChannelHandlerContext ctx){
			this.ctx = ctx;
		}

		@Override
		public void run() {
			NettyMessage messgae = new NettyMessage();
			Header header = new Header();
			header.setSessionId(System.currentTimeMillis());
			header.setType(Header.HEART_BEAT_REQ);
			
			messgae.setHeader(header);
			System.out.println("发起心跳...");
			ctx.writeAndFlush(messgae);
		}
		
	}
}
