package org.whale.inf.example.protobuf.netty;

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


/**
 * 
 * @author Administrator
 *
 */
public class NettyClient {

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
							.addLast(new ProtobufMessageDecoder(1024*1024, 0, 4))
							.addLast(new ProtobufMessageEncoder())
							.addLast(new MessageClientHandler());
					}
				});
			
			ChannelFuture future = boot.connect(new InetSocketAddress(host, port), new InetSocketAddress("127.0.0.1", 12122)).sync();
			
			future.channel().closeFuture().sync();
		} catch(Exception e){
			//需要清除心跳旧的定时器和连接， 否则之前连接在已经断开的channel上的心跳定时器会一直空转
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new NettyClient().connect("127.0.0.1", 8022);
		
	}
	
}
