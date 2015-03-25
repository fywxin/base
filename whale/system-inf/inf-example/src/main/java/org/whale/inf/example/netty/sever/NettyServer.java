package org.whale.inf.example.netty.sever;

import org.whale.inf.example.netty.common.NettyMessageDecoder;
import org.whale.inf.example.netty.common.NettyMessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {

	public void bind(int port) throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
						.addLast(new NettyMessageDecoder(1024*1024, 4, 4))
						.addLast(new NettyMessageEncoder())
						.addLast(new ReadTimeoutHandler(10))
						.addLast(new LoginAuthRespHandler())
						.addLast(new HeartBeatRespHandler());
				}
			});
		
		boot.bind(port).sync();
		
		System.out.println("绑定端口成功："+port);
	}
	
	public static void main(String[] args) throws InterruptedException {
		new NettyServer().bind(8021);
	}
}
