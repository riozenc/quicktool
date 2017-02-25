/**
 * @Title:DAOProxyFactory.java
 * @author:Riozenc
 * @datetime:2015年6月2日 上午10:52:41
 */
package com.riozenc.quicktool.springmvc.transaction.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.annotation.TransactionDAO;
import com.riozenc.quicktool.common.util.annotation.AnnotationUtil;
import com.riozenc.quicktool.common.util.date.DateUtil;
import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;
import com.riozenc.quicktool.mybatis.dao.AbstractDAOSupport;
import com.riozenc.quicktool.mybatis.db.SqlSessionManager;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TransactionServiceProxyFactory2 implements MethodInterceptor {

	private static final Logger LOGGER = LogManager.getLogger(TransactionServiceProxyFactory2.class);

	private LinkedHashMap<Integer, SqlSession> sqlSessionMap = new LinkedHashMap<Integer, SqlSession>();;
	private Object targetObject;
	private Class<?> clazz;

	private TransactionServiceProxyFactory2() {
	}

	public static TransactionServiceProxyFactory2 getInstance() {
		return new TransactionServiceProxyFactory2();
	}

	@SuppressWarnings("unchecked")
	public <T> T createProxy(Object obj) throws InstantiationException, IllegalAccessException {

		if (null == obj) {
			throw new ClassCastException(DateUtil.formatDateTime(new Date()) + "代理对象不存在....");
		} else {
			if (obj instanceof Object) {
				this.targetObject = obj;
				this.clazz = obj.getClass();
				Enhancer enhancer = new Enhancer();
				enhancer.setSuperclass(obj.getClass());
				enhancer.setCallback(this);
				return (T) enhancer.create();
			} else {
				throw new ClassCastException(DateUtil.formatDateTime(new Date()) + "代理对象未构建....");
			}
		}
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub

		// buildDAO();
		String methodName = method.getName();

		try {
			if (methodName.startsWith("get") || methodName.startsWith("find")) {// 查询方法无事务
				Object rev = method.invoke(targetObject, args);
				recovery();// 回收sqlSession！注意select也需要回收
				return rev;
			}
			method.setAccessible(true);// 垃圾回收时，无法调用protected方法
			Object rev = method.invoke(targetObject, args);
			commit(sqlSessionMap, methodName);
			return rev;
		} catch (Exception e) {
			rollback(sqlSessionMap);
			LogUtil.getLogger(LOG_TYPE.ERROR).error(ExceptionLogUtil.log(e));
			return null;
		} finally {
			// 最终处理
			for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
				close(entry.getValue());
			}
		}
	}

	/**
	 * 回收service中dao的sqlSession
	 */
	private void recovery() {
		// targetObject;
		Field[] fields = this.clazz.getDeclaredFields();
		for (Field dao : fields) {
			AbstractDAOSupport abstractDAOSupport = (AbstractDAOSupport) ReflectUtil.getFieldValue(targetObject,
					dao.getName());
			close(sqlSessionMap.put(abstractDAOSupport.hashCode(), abstractDAOSupport.getSqlSession()));
		}
	}

	/**
	 * 注入sqlSession
	 * 
	 * @throws SQLException
	 */
	private void buildDAO() throws SQLException {
		Field[] fields = this.clazz.getDeclaredFields();
		for (Field dao : fields) {
			if (null != dao.getAnnotation(TransactionDAO.class)) {
				AbstractDAOSupport abstractDAOSupport = (AbstractDAOSupport) ReflectUtil.getFieldValue(targetObject,
						dao.getName());
				replaceSqlSession(abstractDAOSupport, dao);
			}
		}
	}

	/**
	 * 替换sqlSession
	 * 
	 * @param abstractDAOSupport
	 * @param dao
	 * @return
	 * @throws SQLException
	 */
	private SqlSession replaceSqlSession(AbstractDAOSupport abstractDAOSupport, Field dao) throws SQLException {
		String dbName = (String) AnnotationUtil.getAnnotationValue(dao, TransactionDAO.class);
		if (dbName.length() < 1) {
			dbName = (String) AnnotationUtil.getAnnotationValue(dao.getType(), TransactionDAO.class);
		}

		SqlSession oldSqlSession = abstractDAOSupport.getSqlSession();
		if (!isValidSqlSession(oldSqlSession)) {
			SqlSession sqlSession = SqlSessionManager.getSession(dbName, false);
			sqlSession.getConnection().setAutoCommit(false);// 不自动提交
			close(sqlSessionMap.put(abstractDAOSupport.hashCode(), sqlSession));
			ReflectUtil.setFieldValue(abstractDAOSupport, "sqlSession", sqlSession);
			return sqlSession;
		}
		return oldSqlSession;
	}

	/**
	 * 校验sqlSession有效性
	 * 
	 * @param sqlSession
	 * @return
	 */
	private boolean isValidSqlSession(SqlSession sqlSession) {
		try {
			if (sqlSession == null || sqlSession.getConnection().isClosed()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	private void close(Map<Integer, SqlSession> sqlSessionMap) {
		for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
			close(entry.getValue());
		}
		sqlSessionMap.clear();
	}

	private void close(SqlSession sqlSession) {
		if (sqlSession != null) {

			sqlSession.close();

			System.out.println(isValidSqlSession(sqlSession));
		}
	}

	private void commit(Map<Integer, SqlSession> sqlSessionMap, String methodName) throws SQLException, Exception {
		recovery();// 回收sqlSession
		for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
			if (entry.getValue() != null) {
				if (entry.getValue().getConnection().getAutoCommit()) {
					LOGGER.error(methodName + "方法存在事务自动提交,事务管理无效.");
				} else {
					entry.getValue().commit();// connection autocommit=true时 失效
				}
			}
		}
	}

	private void rollback(Map<Integer, SqlSession> sqlSessionMap) throws SQLException {
		recovery();// 回收sqlSession
		for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
			if (entry.getValue() != null) {
				entry.getValue().rollback();// connection autocommit=true时 失效
				// entry.getValue().getConnection().rollback();
			}
		}
	}
}
