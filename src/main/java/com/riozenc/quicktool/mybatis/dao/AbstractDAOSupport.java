/**
 * Title:AbstractDAOSupport.java
 * Author:czy
 * Datetime:2016年10月28日 下午3:12:01
 */
package com.riozenc.quicktool.mybatis.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.mybatis.db.PersistanceManager;
import com.riozenc.quicktool.mybatis.db.SqlSessionManager;
import com.riozenc.quicktool.proxy.DAOProxyFactory;

public abstract class AbstractDAOSupport {
	private ExecutorType executorType = ExecutorType.SIMPLE;
	private boolean isProxy = false;
	private boolean autoCommit = false;
	private SqlSession sqlSession = null;
	private String NAMESPACE = null;

	public AbstractDAOSupport() {

	}

	// public AbstractDAOSupport(ExecutorType executorType) {
	// this.executorType = executorType;
	// }
	//
	// public AbstractDAOSupport(ExecutorType executorType, boolean isProxy) {
	// this.executorType = executorType;
	// this.isProxy = isProxy;
	// }

	protected PersistanceManager getPersistanceManager() {
		return getPersistanceManager(this.executorType, this.autoCommit, this.isProxy);
	}

	protected PersistanceManager getPersistanceManager(boolean autoCommit) {

		return getPersistanceManager(this.executorType, autoCommit, this.isProxy);
	}

	protected PersistanceManager getPersistanceManager(ExecutorType executorType) {
		return getPersistanceManager(executorType, this.autoCommit, this.isProxy);
	}

	protected PersistanceManager getPersistanceManager(ExecutorType executorType, boolean autoCommit, boolean isProxy) {

		return getPersistanceManager("", executorType, autoCommit, isProxy);
	}

	protected PersistanceManager getPersistanceManager(String dbName, ExecutorType executorType, boolean autoCommit,
			boolean isProxy) {

		System.out.println(Thread.currentThread().getStackTrace()[3].getMethodName());
		Long l = System.currentTimeMillis();
		sqlSession = SqlSessionManager.getSession(executorType);
		LogUtil.getLogger(LOG_TYPE.DB).info("获取SqlSession用时:" + (System.currentTimeMillis() - l));

		if (isProxy) {
			return (PersistanceManager) DAOProxyFactory.getInstance()
					.createProxy(new PersistanceManager(sqlSession, autoCommit));
		} else {
			return new PersistanceManager(sqlSession, autoCommit);
		}
	}

	public String getNamespace() {
		if (null == NAMESPACE) {
			NAMESPACE = this.getClass().getName();
		}
		return NAMESPACE;
	}

	public SqlSession getSqlSession() {
		return this.sqlSession;
	}

}
