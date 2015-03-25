package org.whale.inf.example.protobuf.netty;

import java.util.List;

import org.whale.inf.example.protobuf.test.Header;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class ProtobufMessageEncoder extends MessageToMessageEncoder<Message> {

	Codec<Message> messageCodec = ProtobufProxy.create(Message.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		
		ByteBuf buf = Unpooled.buffer();
		byte[] bb = messageCodec.encode(msg);
		buf.writeInt(bb.length);
		buf.writeBytes(bb);
		out.add(buf);
	}

}
