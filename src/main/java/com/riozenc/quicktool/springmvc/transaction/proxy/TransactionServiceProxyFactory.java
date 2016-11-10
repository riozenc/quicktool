/**
 * @Title:DAOProxyFactory.java
 * @author:Riozenc
 * @datetime:2015年6月2日 上午10:52:41
 */
package com.riozenc.quicktool.springmvc.transaction.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.common.util.StringUtils;
import com.riozenc.quicktool.common.util.date.DateUtil;
import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.springmvc.context.SpringContextHolder;
import com.riozenc.quicktool.springmvc.transaction.ClassPathMapperScanner;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TransactionServiceProxyFactory implements MethodInterceptor {

	private static final Logger LOGGER = LogManager.getLogger(TransactionServiceProxyFactory.class);

	private List<SqlSession> sqlSessionList = new ArrayList<SqlSession>();;
	private Object targetObject;
	private Class<?> clazz;

	private TransactionServiceProxyFactory() {
	}

	public static TransactionServiceProxyFactory getInstance() {
		return new TransactionServiceProxyFactory();
	}

	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<T> clazz) throws InstantiationException, IllegalAccessException {

		if (null == clazz) {
			throw new ClassCastException(DateUtil.formatDateTime(new Date()) + "代理对象不存在....");
		} else {
			if (clazz instanceof Class) {
				this.clazz = clazz;
				targetObjectInjected();
				Enhancer enhancer = new Enhancer();
				enhancer.setSuperclass(clazz);
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
		targetObjectInjected();
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

	private void targetObjectInjected() {
		if (targetObject == null) {
			targetObject = SpringContextHolder.getBean("aaa_"+StringUtils.decapitalize(this.clazz.getSimpleName()));
		}
	}

}
