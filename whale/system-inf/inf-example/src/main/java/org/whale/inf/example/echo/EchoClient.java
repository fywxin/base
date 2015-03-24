package org.whale.inf.example.echo;

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

public class EchoClient {
	
	

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try{
			Bootstrap boot = new Bootstrap();
			boot.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
	
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new EchoClientHandler()).addLast(new LoggingHandler(LogLevel.INFO));
					}
				});
			
			ChannelFuture f = boot.connect("127.0.0.1", 8080).sync();
			
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}

}
