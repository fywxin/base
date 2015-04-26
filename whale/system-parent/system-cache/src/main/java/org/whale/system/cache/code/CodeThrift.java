package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;

/**
 * http://www.micmiu.com/soa/rpc/thrift-sample/
 * http://itindex.net/detail/46937-thrift-%E5%8E%9F%E7%90%86-java
 *
 * @author 王金绍
 * 2015年4月26日 上午12:29:58
 */
public class CodeThrift<M extends Serializable> extends AbstractCode<M> {

	@Override
	public byte[] doEncode(String cacheName, M m) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public M doDecode(Class<?> type, byte[] bytes) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
