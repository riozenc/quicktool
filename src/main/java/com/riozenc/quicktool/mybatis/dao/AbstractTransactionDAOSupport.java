/**
 * Title:AbstractDAOSupport.java
 * Author:czy
 * Datetime:2016年10月28日 下午3:12:01
 */
package com.riozenc.quicktool.mybatis.dao;

import com.riozenc.quicktool.mybatis.db.PersistanceManager;
import com.riozenc.quicktool.proxy.DAOProxyFactory;

public abstract class AbstractTransactionDAOSupport extends AbstractDAOSupport {

	protected PersistanceManager getPersistanceManager() {
		return getPersistanceManager(false);
	}

	protected PersistanceManager getPersistanceManager(boolean autoCommit) {
		return getPersistanceManager(autoCommit, false);
	}

	protected PersistanceManager getPersistanceManager(boolean autoCommit, boolean isProxy) {
		if (isProxy) {
			return (PersistanceManager) DAOProxyFactory.getInstance()
					.createProxy(new PersistanceManager(getSqlSession(), autoCommit));
		} else {
			return new PersistanceManager(getSqlSession(), autoCommit);
		}
	}

}
