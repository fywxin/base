package org.whale.inf.example.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import org.whale.inf.example.netty.common.NettyMessageDecoder;
import org.whale.inf.example.netty.common.NettyMessageEncoder;

public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	private EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect(final String host, final int port) throws InterruptedException{
		try{
			Bootstrap boot = new Bootstrap();
			boot.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline()
							//frame最大长度，
							.addLast(new NettyMessageDecoder(1024*1024, 4, 4))
							.addLast(new NettyMessageEncoder())
							.addLast(new ReadTimeoutHandler(50))
							.addLast(new LoginAuthReqHandler())
							.addLast(new HeartBeatReqHandler());
					}
				});
			
			ChannelFuture future = boot.connect(new InetSocketAddress(host, port)).sync();
			
			future.channel().closeFuture().sync();
		} finally{
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(500);
						connect(host, port); //发起重新连接
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new NettyClient().connect("127.0.0.1", 8021);
		
	}
	
}
