package org.whale.inf.example.netty.client;

import java.util.HashMap;
import java.util.Map;

import org.whale.inf.example.netty.common.Header;
import org.whale.inf.example.netty.common.NettyMessage;
import org.whale.inf.example.netty.common.RpcException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * 应用登录认真
 *
 * @author 王金绍
 * @Date 2015年3月24日 下午3:35:59
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
	
	public static String sessionId;

	/**
	 * 应用登录
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setSessionId(System.currentTimeMillis());
		header.setType(Header.LOGIN_REQ);
		
		Map<String, String> attach = new HashMap<String, String>();
		attach.put("userName", "fywxin");
		attach.put("password", "mei123");
		header.setAttachment(attach);
		nettyMessage.setHeader(header);
		System.out.println("0. 发起用户登录请求...");
		ctx.writeAndFlush(nettyMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		NettyMessage message = (NettyMessage)msg;
		if(message != null){
			Map<String, String> attach = null;
			if(message.getHeader() != null && message.getHeader().getType() == Header.LOGIN_RESP && (attach = message.getHeader().getAttachment()).size() > 0){
				System.out.println("1. 接受到用户登录结果");
				String rs = (String)attach.get("rs");
				String sid = (String)attach.get("sid");
				if(rs == null || sid == null || !"true".equalsIgnoreCase(rs)){
					ctx.close();
					throw new RpcException("应用登录失败");
				}else{
					System.out.println("用户登录成功，返回sId:"+sid);
					sessionId = sid;
					ctx.fireChannelRead(msg); //应用登录成功后，开始触发心跳检测
				}
			}else{
				ctx.fireChannelRead(msg);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	}

	
	
}
