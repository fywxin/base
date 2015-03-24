package org.whale.inf.example.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TimeClient {
	
	

	public static void main(String[] args) throws InterruptedException {
		
		for(int i=0; i<100; i++){
			EventLoopGroup group = new NioEventLoopGroup();
		
			try{
				Bootstrap boot = new Bootstrap();
				boot.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
		
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new TimeClientHandlerSafe()).addLast(new LoggingHandler(LogLevel.INFO));
						}
					});
				
				ChannelFuture f = boot.connect("127.0.0.1", 8080).sync();
				
				f.channel().closeFuture().sync();
			}finally{
				group.shutdownGracefully();
			}
		}
	}

}
