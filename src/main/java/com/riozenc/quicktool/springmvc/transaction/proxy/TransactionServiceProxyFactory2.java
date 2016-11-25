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
		buildDAO();
		String methodName = method.getName();

		if (methodName.startsWith("get") || methodName.startsWith("find")) {// 查询方法无事务
			Object rev = method.invoke(targetObject, args);
			return rev;
		}
		try {
			Object rev = method.invoke(targetObject, args);
			for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
				if (entry.getValue().getConnection().getAutoCommit()) {
					throw new Exception(methodName + "方法存在事务自动提交...");
				}
				entry.getValue().getConnection().commit();
			}
			return rev;
		} catch (Exception e) {
			for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
				entry.getValue().getConnection().rollback();
			}

			LogUtil.getLogger(LOG_TYPE.ERROR).error(ExceptionLogUtil.log(e));
			return null;
		} finally {
			// 最终处理
			for (Entry<Integer, SqlSession> entry : sqlSessionMap.entrySet()) {
				close(entry.getValue());
			}
		}
	}

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

	private boolean isValidSqlSession(SqlSession sqlSession) {
		try {
			if (sqlSession ==null || sqlSession.getConnection().isClosed()) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void close(SqlSession sqlSession) {
		if (sqlSession != null) {
			sqlSession.close();
		}
	}
}
