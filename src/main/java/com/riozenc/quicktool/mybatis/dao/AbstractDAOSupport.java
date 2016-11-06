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
	private boolean flag = true;
	private SqlSession sqlSession = null;
	private static String NAMESPACE = null;

	public AbstractDAOSupport() {

	}

	public AbstractDAOSupport(ExecutorType executorType) {
		this.executorType = executorType;
	}

	public AbstractDAOSupport(ExecutorType executorType, boolean flag) {
		this.executorType = executorType;
		this.flag = flag;
	}

	protected PersistanceManager getPersistanceManager() {

		return getPersistanceManager(this.executorType, this.flag);

	}

	protected PersistanceManager getPersistanceManager(String dbName) {

		return getPersistanceManager(dbName, this.executorType, this.flag);

	}

	protected PersistanceManager getPersistanceManager(boolean flag) {
		return getPersistanceManager(this.executorType, flag);
	}

	protected PersistanceManager getPersistanceManager(ExecutorType executorType) {
		return getPersistanceManager(executorType, this.flag);
	}

	protected PersistanceManager getPersistanceManager(ExecutorType executorType, boolean flag) {

		System.out.println(Thread.currentThread().getStackTrace()[3].getMethodName());

		Long l = System.currentTimeMillis();

		sqlSession = SqlSessionManager.getSession(executorType);
		LogUtil.getLogger(LOG_TYPE.DB).info("获取SqlSession用时:" + (System.currentTimeMillis() - l));

		if (flag) {
			return (PersistanceManager) DAOProxyFactory.getInstance().createProxy(new PersistanceManager(sqlSession));
		} else {
			return new PersistanceManager(sqlSession);
		}
	}

	protected PersistanceManager getPersistanceManager(String dbName, ExecutorType executorType, boolean flag) {

		System.out.println(Thread.currentThread().getStackTrace()[3].getMethodName());

		Long l = System.currentTimeMillis();

		sqlSession = SqlSessionManager.getSession(dbName, executorType);
		LogUtil.getLogger(LOG_TYPE.DB).info("获取SqlSession用时:" + (System.currentTimeMillis() - l));

		if (flag) {
			return (PersistanceManager) DAOProxyFactory.getInstance().createProxy(new PersistanceManager(sqlSession));
		} else {
			return new PersistanceManager(sqlSession);
		}
	}

	protected String getNamespace() {
		if (null == NAMESPACE) {
			NAMESPACE = this.getClass().getName();
		}
		return NAMESPACE;
	}

}
