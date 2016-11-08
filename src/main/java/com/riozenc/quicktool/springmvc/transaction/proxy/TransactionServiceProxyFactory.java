/**
 * @Title:DAOProxyFactory.java
 * @author:Riozenc
 * @datetime:2015年6月2日 上午10:52:41
 */
package com.riozenc.quicktool.springmvc.transaction.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.mybatis.db.PersistanceManager;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TransactionServiceProxyFactory implements MethodInterceptor {

	private static final Logger LOGGER = LogManager.getLogger(TransactionServiceProxyFactory.class);

	private List<SqlSession> sqlSessionList = new ArrayList<SqlSession>();;
	private Object targetObject;

	private TransactionServiceProxyFactory() {
	}

	public static TransactionServiceProxyFactory getInstance() {
		return new TransactionServiceProxyFactory();
	}

	public Object createProxy(Object object) throws InstantiationException, IllegalAccessException {
		targetObject = object;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(PersistanceManager.class);
		enhancer.setCallback(this);

		return enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		String methodName = method.getName();
		try {

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
