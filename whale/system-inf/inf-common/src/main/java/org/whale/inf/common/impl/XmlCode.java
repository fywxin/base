package org.whale.inf.common.impl;

import java.io.IOException;
import java.io.Serializable;

import org.whale.inf.common.ProtocolCode;

import com.thoughtworks.xstream.XStream;

public class XmlCode<M extends Serializable> implements ProtocolCode<M> {
	
	private static XStream xstream = new XStream();

	@Override
	public String encode(M m) throws IOException {
		return xstream.toXML(m);
	}

	@Override
	public M decode(String body) throws IOException {
		return (M)xstream.fromXML(body);
	}

}
