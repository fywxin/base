package org.whale.inf.common.impl;

import org.whale.inf.common.EncryptService;
import org.whale.inf.common.InfContext;

public class EmptyEncryptService implements EncryptService {

	@Override
	public byte[] encrypt(byte[] datas, InfContext context) {
		return datas;
	}

	@Override
	public byte[] decrypt(byte[] datas, InfContext context) {
		return datas;
	}
}
