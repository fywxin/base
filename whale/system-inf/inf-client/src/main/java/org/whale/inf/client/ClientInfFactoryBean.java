package org.whale.inf.client;

import org.springframework.beans.factory.FactoryBean;

public class ClientInfFactoryBean<T> implements FactoryBean<T> {
	
	private Class<T> clientInterface;

	@Override
	public T getObject() throws Exception {
		ClientInfRegistry clientInfRegistry = new ClientInfRegistry();
		return clientInfRegistry.getMapper(clientInterface);
	}

	@Override
	public Class<?> getObjectType() {
		return this.clientInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setClientInterface(Class<T> clientInterface) {
		this.clientInterface = clientInterface;
	}
	

}
