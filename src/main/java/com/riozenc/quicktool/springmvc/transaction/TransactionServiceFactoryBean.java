/**
 * Title:TransactionServiceFactoryBean.java
 * Author:czy
 * Datetime:2016年11月9日 上午10:07:47
 */
package com.riozenc.quicktool.springmvc.transaction;

import org.springframework.beans.factory.FactoryBean;

import com.riozenc.quicktool.springmvc.transaction.proxy.TransactionServiceProxyFactory;

public class TransactionServiceFactoryBean<T> implements FactoryBean<T> {

	private Class<T> serviceInterface;
	
	
	public TransactionServiceFactoryBean() {
	    //intentionally empty 
	  }

	public void setServiceInterface(Class<T> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@Override
	public T getObject() throws Exception {
		// TODO Auto-generated method stub
		if (this.serviceInterface == null) {
			afterPropertiesSet();
		}
		return TransactionServiceProxyFactory.getInstance().createProxy(serviceInterface);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return this.serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		this.serviceInterface = null;
	}
}
