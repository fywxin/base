package org.whale.inf.example.protobuf.netty;

import com.google.protobuf.*;
import java.io.IOException;
import com.baidu.bjf.remoting.protobuf.utils.*;
import java.lang.reflect.*;
import com.baidu.bjf.remoting.protobuf.*;
import java.util.*;
import org.whale.inf.example.protobuf.netty.Header;

public class Header$$JProtoBufClass implements com.baidu.bjf.remoting.protobuf.Codec<org.whale.inf.example.protobuf.netty.Header> {
	
	public byte[] encode(org.whale.inf.example.protobuf.netty.Header t) throws IOException {
		int size = 0;
		Integer f_1 = null;
		if (!CodedConstant.isNull(t.getCrcCode())) {
			f_1 = t.getCrcCode();
		}
		if (!CodedConstant.isNull(t.getCrcCode())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(1,
					f_1.intValue());
		}
		if (f_1 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("crcCode"));
		}
		Integer f_2 = null;
		if (!CodedConstant.isNull(t.getLength())) {
			f_2 = t.getLength();
		}
		if (!CodedConstant.isNull(t.getLength())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(2,
					f_2.intValue());
		}
		if (f_2 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("length"));
		}
		Long f_3 = null;
		if (!CodedConstant.isNull(t.getSessionId())) {
			f_3 = t.getSessionId();
		}
		if (!CodedConstant.isNull(t.getSessionId())) {
			size += com.google.protobuf.CodedOutputStream.computeInt64Size(3,
					f_3.longValue());
		}
		if (f_3 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("sessionId"));
		}
		Integer f_4 = null;
		if (!CodedConstant.isNull(t.getType())) {
			f_4 = t.getType();
		}
		if (!CodedConstant.isNull(t.getType())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(4,
					f_4.intValue());
		}
		if (f_4 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("type"));
		}
		Integer f_5 = null;
		if (!CodedConstant.isNull(t.getPriority())) {
			f_5 = t.getPriority();
		}
		if (!CodedConstant.isNull(t.getPriority())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(5,
					f_5.intValue());
		}
		if (f_5 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("priority"));
		}
		com.google.protobuf.ByteString f_6 = null;
		if (!CodedConstant.isNull(t.getToken())) {
			f_6 = com.google.protobuf.ByteString.copyFromUtf8(t.getToken());
		}
		if (!CodedConstant.isNull(t.getToken())) {
			size += com.google.protobuf.CodedOutputStream.computeBytesSize(6,
					f_6);
		}
		if (f_6 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("token"));
		}
		final byte[] result = new byte[size];
		final CodedOutputStream output = CodedOutputStream.newInstance(result);
		if (f_1 != null) {
			output.writeInt32(1, f_1.intValue());
		}
		if (f_2 != null) {
			output.writeInt32(2, f_2.intValue());
		}
		if (f_3 != null) {
			output.writeInt64(3, f_3.longValue());
		}
		if (f_4 != null) {
			output.writeInt32(4, f_4.intValue());
		}
		if (f_5 != null) {
			output.writeInt32(5, f_5.intValue());
		}
		if (f_6 != null) {
			output.writeBytes(6, f_6);
		}
		return result;
	}

	public org.whale.inf.example.protobuf.netty.Header decode(byte[] bb) throws IOException {
		org.whale.inf.example.protobuf.netty.Header ret = new org.whale.inf.example.protobuf.netty.Header();
		CodedInputStream input = CodedInputStream.newInstance(bb, 0, bb.length);
		try {
			boolean done = false;
			Codec codec = null;
			while (!done) {
				int tag = input.readTag();
				if (tag == 0) {
					break;
				}
				if (tag == 8) {
					ret.setCrcCode(input.readInt32());
					continue;
				}
				if (tag == 16) {
					ret.setLength(input.readInt32());
					continue;
				}
				if (tag == 24) {
					ret.setSessionId(input.readInt64());
					continue;
				}
				if (tag == 32) {
					ret.setType(input.readInt32());
					continue;
				}
				if (tag == 40) {
					ret.setPriority(input.readInt32());
					continue;
				}
				if (tag == 50) {
					ret.setToken(input.readString());
					continue;
				}
				input.skipField(tag);
			}
		} catch (com.google.protobuf.InvalidProtocolBufferException e) {
			throw e;
		} catch (java.io.IOException e) {
			throw e;
		}
		if (CodedConstant.isNull(ret.getCrcCode())) {
			throw new UninitializedMessageException(CodedConstant.asList("crcCode"));
		}
		if (CodedConstant.isNull(ret.getLength())) {
			throw new UninitializedMessageException(CodedConstant.asList("length"));
		}
		if (CodedConstant.isNull(ret.getSessionId())) {
			throw new UninitializedMessageException(CodedConstant.asList("sessionId"));
		}
		if (CodedConstant.isNull(ret.getType())) {
			throw new UninitializedMessageException(CodedConstant.asList("type"));
		}
		if (CodedConstant.isNull(ret.getPriority())) {
			throw new UninitializedMessageException(CodedConstant.asList("priority"));
		}
		if (CodedConstant.isNull(ret.getToken())) {
			throw new UninitializedMessageException(CodedConstant.asList("token"));
		}
		return ret;
	}

	public int size(org.whale.inf.example.protobuf.netty.Header t)
			throws IOException {
		int size = 0;
		Integer f_1 = null;
		if (!CodedConstant.isNull(t.getCrcCode())) {
			f_1 = t.getCrcCode();
		}
		if (!CodedConstant.isNull(t.getCrcCode())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(1,
					f_1.intValue());
		}
		if (f_1 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("crcCode"));
		}
		Integer f_2 = null;
		if (!CodedConstant.isNull(t.getLength())) {
			f_2 = t.getLength();
		}
		if (!CodedConstant.isNull(t.getLength())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(2,
					f_2.intValue());
		}
		if (f_2 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("length"));
		}
		Long f_3 = null;
		if (!CodedConstant.isNull(t.getSessionId())) {
			f_3 = t.getSessionId();
		}
		if (!CodedConstant.isNull(t.getSessionId())) {
			size += com.google.protobuf.CodedOutputStream.computeInt64Size(3,
					f_3.longValue());
		}
		if (f_3 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("sessionId"));
		}
		Integer f_4 = null;
		if (!CodedConstant.isNull(t.getType())) {
			f_4 = t.getType();
		}
		if (!CodedConstant.isNull(t.getType())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(4,
					f_4.intValue());
		}
		if (f_4 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("type"));
		}
		Integer f_5 = null;
		if (!CodedConstant.isNull(t.getPriority())) {
			f_5 = t.getPriority();
		}
		if (!CodedConstant.isNull(t.getPriority())) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(5,
					f_5.intValue());
		}
		if (f_5 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("priority"));
		}
		com.google.protobuf.ByteString f_6 = null;
		if (!CodedConstant.isNull(t.getToken())) {
			f_6 = com.google.protobuf.ByteString.copyFromUtf8(t.getToken());
		}
		if (!CodedConstant.isNull(t.getToken())) {
			size += com.google.protobuf.CodedOutputStream.computeBytesSize(6,
					f_6);
		}
		if (f_6 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("token"));
		}
		return size;
	}

	public void writeTo(org.whale.inf.example.protobuf.netty.Header t,
			CodedOutputStream output) throws IOException {
		Integer f_1 = null;
		if (!CodedConstant.isNull(t.getCrcCode())) {
			f_1 = t.getCrcCode();
		}
		if (f_1 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("crcCode"));
		}
		Integer f_2 = null;
		if (!CodedConstant.isNull(t.getLength())) {
			f_2 = t.getLength();
		}
		if (f_2 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("length"));
		}
		Long f_3 = null;
		if (!CodedConstant.isNull(t.getSessionId())) {
			f_3 = t.getSessionId();
		}
		if (f_3 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("sessionId"));
		}
		Integer f_4 = null;
		if (!CodedConstant.isNull(t.getType())) {
			f_4 = t.getType();
		}
		if (f_4 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("type"));
		}
		Integer f_5 = null;
		if (!CodedConstant.isNull(t.getPriority())) {
			f_5 = t.getPriority();
		}
		if (f_5 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("priority"));
		}
		com.google.protobuf.ByteString f_6 = null;
		if (!CodedConstant.isNull(t.getToken())) {
			f_6 = com.google.protobuf.ByteString.copyFromUtf8(t.getToken());
		}
		if (f_6 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("token"));
		}
		if (f_1 != null) {
			output.writeInt32(1, f_1.intValue());
		}
		if (f_2 != null) {
			output.writeInt32(2, f_2.intValue());
		}
		if (f_3 != null) {
			output.writeInt64(3, f_3.longValue());
		}
		if (f_4 != null) {
			output.writeInt32(4, f_4.intValue());
		}
		if (f_5 != null) {
			output.writeInt32(5, f_5.intValue());
		}
		if (f_6 != null) {
			output.writeBytes(6, f_6);
		}
	}

	public org.whale.inf.example.protobuf.netty.Header readFrom(
			CodedInputStream input) throws IOException {
		org.whale.inf.example.protobuf.netty.Header ret = new org.whale.inf.example.protobuf.netty.Header();
		try {
			boolean done = false;
			Codec codec = null;
			while (!done) {
				int tag = input.readTag();
				if (tag == 0) {
					break;
				}
				if (tag == 8) {
					ret.setCrcCode(input.readInt32());
					continue;
				}
				if (tag == 16) {
					ret.setLength(input.readInt32());
					continue;
				}
				if (tag == 24) {
					ret.setSessionId(input.readInt64());
					continue;
				}
				if (tag == 32) {
					ret.setType(input.readInt32());
					continue;
				}
				if (tag == 40) {
					ret.setPriority(input.readInt32());
					continue;
				}
				if (tag == 50) {
					ret.setToken(input.readString());
					continue;
				}
				input.skipField(tag);
			}
		} catch (com.google.protobuf.InvalidProtocolBufferException e) {
			throw e;
		} catch (java.io.IOException e) {
			throw e;
		}
		if (CodedConstant.isNull(ret.getCrcCode())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("crcCode"));
		}
		if (CodedConstant.isNull(ret.getLength())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("length"));
		}
		if (CodedConstant.isNull(ret.getSessionId())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("sessionId"));
		}
		if (CodedConstant.isNull(ret.getType())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("type"));
		}
		if (CodedConstant.isNull(ret.getPriority())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("priority"));
		}
		if (CodedConstant.isNull(ret.getToken())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("token"));
		}
		return ret;
	}
}