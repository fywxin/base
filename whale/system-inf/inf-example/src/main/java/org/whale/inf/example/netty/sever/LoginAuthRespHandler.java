package org.whale.inf.example.netty.sever;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.whale.inf.example.netty.common.Header;
import org.whale.inf.example.netty.common.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
	
	public static Map<String, String> sessions = new HashMap<String, String>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		NettyMessage message = (NettyMessage)msg;
		if(message != null){
			
			//应用登录消息
			if(message.getHeader() != null && message.getHeader().getType() == Header.LOGIN_REQ){
				System.out.println("收到用户登录请求");
				Map<String, String> attach = message.getHeader().getAttachment();
				if(attach == null || attach.size() < 1){
					ctx.writeAndFlush(error(message.getHeader().getSessionId(), 401, "用户名或密码不能为空"));
					return ;
				}
				String userName = (String)attach.get("userName");
				String password = (String)attach.get("password");
				if(this.checkLogin(userName, password)){
					
					String sid = UUID.randomUUID().toString();
					sessions.put(userName, sid);
					System.out.println("用户登录成功，sid:"+sid);
					ctx.writeAndFlush(success(message.getHeader().getSessionId(), sid));
					return ;
				}else{
					System.out.println("用户登录失败： 用户名或密码错误");
					ctx.writeAndFlush(error(message.getHeader().getSessionId(), 401, "用户名或密码错误"));
					return ;
				}
				
			}else{
				ctx.fireChannelRead(msg);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	
	private boolean checkLogin(String userName, String password){
		if("fywxin".equals(userName) && "mei123".equals(password)){
			return true;
		}
		return false;
	}
	
	private NettyMessage error(Long sessionId, Integer code, String info){
		NettyMessage messgae = new NettyMessage();
		Header header = new Header();
		header.setSessionId(sessionId);
		header.setType(Header.LOGIN_RESP);
		
		Map<String, String> attach = new HashMap<String, String>();
		attach.put("rs", "false");
		attach.put("code", code+"");
		attach.put("info", info);
		header.setAttachment(attach);
		messgae.setHeader(header);
		
		return messgae;
	}

	private NettyMessage success(Long sessionId, String sid){
		NettyMessage messgae = new NettyMessage();
		Header header = new Header();
		header.setSessionId(sessionId);
		header.setType(Header.LOGIN_RESP);
		
		Map<String, String> attach = new HashMap<String, String>();
		attach.put("rs", "true");
		attach.put("sid", sid);
		header.setAttachment(attach);
		messgae.setHeader(header);
		
		return messgae;
	}
}
