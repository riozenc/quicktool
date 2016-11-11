/**
 * @Title:DAOProxyFactory.java
 * @author:Riozenc
 * @datetime:2015年6月2日 上午10:52:41
 */
package com.riozenc.quicktool.springmvc.transaction.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.common.util.date.DateUtil;
import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.mybatis.dao.AbstractDAOSupport;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TransactionServiceProxyFactory2 implements MethodInterceptor {

	private static final Logger LOGGER = LogManager.getLogger(TransactionServiceProxyFactory2.class);

	private LinkedList<SqlSession> sqlSessionList = new LinkedList<SqlSession>();;
	private Object targetObject;

	private TransactionServiceProxyFactory2() {
	}

	public static TransactionServiceProxyFactory2 getInstance() {
		return new TransactionServiceProxyFactory2();
	}

	@SuppressWarnings("unchecked")
	public <T> T createProxy(Object obj, AbstractDAOSupport... abstractDAOSupports)
			throws InstantiationException, IllegalAccessException {

		if (null == obj) {
			throw new ClassCastException(DateUtil.formatDateTime(new Date()) + "代理对象不存在....");
		} else {
			if (obj instanceof Class) {
				this.targetObject = obj;
				for (AbstractDAOSupport abstractDAOSupport : abstractDAOSupports) {
					sqlSessionList.add(abstractDAOSupport.getSqlSession());
				}
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
		String methodName = method.getName();
		try {

			// Object result = proxy.invokeSuper(obj, args);

			Object rev = method.invoke(targetObject, args);

			return rev;
		} catch (Exception e) {

			LogUtil.getLogger(LOG_TYPE.ERROR).error(ExceptionLogUtil.log(e));
			return null;
		} finally {
			// 最终处理

		}
	}
}
