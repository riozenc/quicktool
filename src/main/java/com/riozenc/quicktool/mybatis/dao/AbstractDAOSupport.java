/**
 * Title:AbstractDAOSupport.java
 * Author:czy
 * Datetime:2016年10月28日 下午3:12:01
 */
package com.riozenc.quicktool.mybatis.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	private String dbName = null;
	private String NAMESPACE = null;

	private ThreadLocal<Map<String, SqlSession>> localSqlSessionMap = new ThreadLocal<>();

	public AbstractDAOSupport() {
		
	}

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

		return getPersistanceManager(dbName, executorType, autoCommit, isProxy);
	}

	/**
	 * 
	 * @param dbName
	 * @param executorType
	 * @param autoCommit
	 *            已经在配置文件中，启动数据库时就关闭了自动提交，该属性已经没用了
	 * @param isProxy
	 * @return
	 */
	protected PersistanceManager getPersistanceManager(String dbName, ExecutorType executorType, boolean autoCommit,
			boolean isProxy) {

		Long l = System.currentTimeMillis();

		SqlSession sqlSession = getSqlSessionMap().get(dbName + executorType);

		if (sqlSession == null) {
			sqlSession = SqlSessionManager.getSession(dbName, executorType);
		} else {
			try {
				if (sqlSession.getConnection().isClosed()) {
					getSqlSessionMap().remove(sqlSession);
					sqlSession = SqlSessionManager.getSession(dbName, executorType);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getSqlSessionMap().remove(sqlSession);
				sqlSession = SqlSessionManager.getSession(dbName, executorType);
			}
		}
		
		LogUtil.getLogger(LOG_TYPE.DB).info(
				"[" + Thread.currentThread().getName() + "]获取SqlSession("+sqlSession.getConnection()+")用时:" + (System.currentTimeMillis() - l) / 1000);
		getSqlSessionMap().put(dbName + executorType, sqlSession);

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

	public Collection<SqlSession> getSqlSessions() {
		return getSqlSessionMap().values();
	}

	protected String getDbName() {
		return this.dbName;
	}

	protected ExecutorType getExecutorType() {
		return executorType;
	}

	private Map<String, SqlSession> getSqlSessionMap() {
		if (localSqlSessionMap.get() == null) {
			localSqlSessionMap.set(new HashMap<String, SqlSession>());
		}
		return localSqlSessionMap.get();
	}
}
