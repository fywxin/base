package org.whale.inf.example.protobuf.netty;

import com.google.protobuf.*;
import java.io.IOException;
import com.baidu.bjf.remoting.protobuf.utils.*;
import java.lang.reflect.*;
import com.baidu.bjf.remoting.protobuf.*;
import java.util.*;
import org.whale.inf.example.protobuf.netty.Message;

public class Message$$JProtoBufClass implements com.baidu.bjf.remoting.protobuf.Codec<org.whale.inf.example.protobuf.netty.Message> {
	public byte[] encode(org.whale.inf.example.protobuf.netty.Message t)
			throws IOException {
		int size = 0;
		Long f_3 = null;
		if (!CodedConstant.isNull(t.getId())) {
			f_3 = t.getId();
		}
		if (!CodedConstant.isNull(t.getId())) {
			size += com.google.protobuf.CodedOutputStream.computeInt64Size(3, f_3.longValue());
		}
		if (f_3 == null) {
			throw new UninitializedMessageException(CodedConstant.asList("id"));
		}
		Object f_2 = null;
		if (!CodedConstant.isNull(t.getHeader())) {
			f_2 = t.getHeader();
		}
		if (!CodedConstant.isNull(t.getHeader())) {
			size += CodedConstant.computeSize(2, f_2, FieldType.OBJECT, true);
		}
		if (f_2 == null) {
			throw new UninitializedMessageException(CodedConstant.asList("header"));
		}
		final byte[] result = new byte[size];
		final CodedOutputStream output = CodedOutputStream.newInstance(result);
		if (f_3 != null) {
			output.writeInt64(3, f_3.longValue());
		}
		if (f_2 != null) {
			CodedConstant.writeObject(output, 2, FieldType.OBJECT, f_2, false);
		}
		return result;
	}

	public org.whale.inf.example.protobuf.netty.Message decode(byte[] bb)
			throws IOException {
		org.whale.inf.example.protobuf.netty.Message ret = new org.whale.inf.example.protobuf.netty.Message();
		CodedInputStream input = CodedInputStream.newInstance(bb, 0, bb.length);
		try {
			boolean done = false;
			Codec codec = null;
			while (!done) {
				int tag = input.readTag();
				if (tag == 0) {
					break;
				}
				if (tag == 24) {
					ret.setId(input.readInt64());
					continue;
				}
				if (tag == 18) {
					codec = ProtobufProxy.create(
							org.whale.inf.example.protobuf.netty.Header.class,
							true);
					int length = input.readRawVarint32();
					final int oldLimit = input.pushLimit(length);
					ret.setHeader((org.whale.inf.example.protobuf.netty.Header) codec
							.readFrom(input));
					input.checkLastTagWas(0);
					input.popLimit(oldLimit);
					continue;
				}
				input.skipField(tag);
			}
		} catch (com.google.protobuf.InvalidProtocolBufferException e) {
			throw e;
		} catch (java.io.IOException e) {
			throw e;
		}
		if (CodedConstant.isNull(ret.getId())) {
			throw new UninitializedMessageException(CodedConstant.asList("id"));
		}
		if (CodedConstant.isNull(ret.getHeader())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("header"));
		}
		return ret;
	}

	public int size(org.whale.inf.example.protobuf.netty.Message t)
			throws IOException {
		int size = 0;
		Long f_3 = null;
		if (!CodedConstant.isNull(t.getId())) {
			f_3 = t.getId();
		}
		if (!CodedConstant.isNull(t.getId())) {
			size += com.google.protobuf.CodedOutputStream.computeInt64Size(3,
					f_3.longValue());
		}
		if (f_3 == null) {
			throw new UninitializedMessageException(CodedConstant.asList("id"));
		}
		Object f_2 = null;
		if (!CodedConstant.isNull(t.getHeader())) {
			f_2 = t.getHeader();
		}
		if (!CodedConstant.isNull(t.getHeader())) {
			size += CodedConstant.computeSize(2, f_2, FieldType.OBJECT, true);
		}
		if (f_2 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("header"));
		}
		return size;
	}

	public void writeTo(org.whale.inf.example.protobuf.netty.Message t,
			CodedOutputStream output) throws IOException {
		Long f_3 = null;
		if (!CodedConstant.isNull(t.getId())) {
			f_3 = t.getId();
		}
		if (f_3 == null) {
			throw new UninitializedMessageException(CodedConstant.asList("id"));
		}
		Object f_2 = null;
		if (!CodedConstant.isNull(t.getHeader())) {
			f_2 = t.getHeader();
		}
		if (f_2 == null) {
			throw new UninitializedMessageException(
					CodedConstant.asList("header"));
		}
		if (f_3 != null) {
			output.writeInt64(3, f_3.longValue());
		}
		if (f_2 != null) {
			CodedConstant.writeObject(output, 2, FieldType.OBJECT, f_2, false);
		}
	}

	public org.whale.inf.example.protobuf.netty.Message readFrom(
			CodedInputStream input) throws IOException {
		org.whale.inf.example.protobuf.netty.Message ret = new org.whale.inf.example.protobuf.netty.Message();
		try {
			boolean done = false;
			Codec codec = null;
			while (!done) {
				int tag = input.readTag();
				if (tag == 0) {
					break;
				}
				if (tag == 24) {
					ret.setId(input.readInt64());
					continue;
				}
				if (tag == 18) {
					codec = ProtobufProxy.create(
							org.whale.inf.example.protobuf.netty.Header.class,
							true);
					int length = input.readRawVarint32();
					final int oldLimit = input.pushLimit(length);
					ret.setHeader((org.whale.inf.example.protobuf.netty.Header) codec
							.readFrom(input));
					input.checkLastTagWas(0);
					input.popLimit(oldLimit);
					continue;
				}
				input.skipField(tag);
			}
		} catch (com.google.protobuf.InvalidProtocolBufferException e) {
			throw e;
		} catch (java.io.IOException e) {
			throw e;
		}
		if (CodedConstant.isNull(ret.getId())) {
			throw new UninitializedMessageException(CodedConstant.asList("id"));
		}
		if (CodedConstant.isNull(ret.getHeader())) {
			throw new UninitializedMessageException(
					CodedConstant.asList("header"));
		}
		return ret;
	}
}