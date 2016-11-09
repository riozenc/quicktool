/**
 * @Project:quicktool
 * @Title:AbstractServiceSupport.java
 * @Author:Riozenc
 * @Datetime:2016年11月8日 下午9:05:11
 * 
 */
package com.riozenc.quicktool.springmvc.transaction;

import com.riozenc.quicktool.springmvc.transaction.proxy.TransactionServiceProxyFactory;

public abstract class AbstractServiceSupport {

	public Object getTransactionService() throws InstantiationException, IllegalAccessException {
		return get(this.getClass());
	}

	private Object get(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return TransactionServiceProxyFactory.getInstance().createProxy(clazz);
	}
}
