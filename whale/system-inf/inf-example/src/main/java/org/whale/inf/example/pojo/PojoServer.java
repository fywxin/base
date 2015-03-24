package org.whale.inf.example.pojo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class PojoServer {
	
	private int port;
	
	public PojoServer (int port){
		this.port = port;
	}
	
	public void run() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>(){
		
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						//ch.pipeline().addLast(new TimeServerHandler()).addLast(new LoggingHandler(LogLevel.INFO));
					}
					 
				 })
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = boot.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * 使用 telnet 127.0.0.1 8080 测试
	 * 
	 * @param args
	 * @throws Exception
	 * @Date 2015年3月10日 上午11:41:10
	 */
	public static void main(String[] args) throws Exception {
		int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new PojoServer(port).run();
	}

}
