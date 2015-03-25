package org.whale.inf.example.protobuf.netty;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtobufMessageDecoder extends LengthFieldBasedFrameDecoder {
	
	Codec<Message> messageCodec = ProtobufProxy.create(Message.class);

	public ProtobufMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf =  (ByteBuf)super.decode(ctx, in);
		if(buf != null){
			int length = buf.readInt();
			byte[] bb = new byte[length];
			buf.readBytes(bb);
			Message message = messageCodec.decode(bb);
			return message;
		}else{
			return null;
		}
	}

	
}
