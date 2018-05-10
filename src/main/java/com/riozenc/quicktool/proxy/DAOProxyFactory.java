/**
 * @Title:DAOProxyFactory.java
 * @author:Riozenc
 * @datetime:2015年6月2日 上午10:52:41
 */
package com.riozenc.quicktool.proxy;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.common.util.reflect.ObjectToStringUtil;
import com.riozenc.quicktool.mybatis.db.PersistanceManager;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DAOProxyFactory implements MethodInterceptor {

	private static final Logger LOGGER = LogManager.getLogger(DAOProxyFactory.class);

	// private Object targetObject;
	private PersistanceManager persistanceManager;

	private DAOProxyFactory() {
	}

	public static DAOProxyFactory getInstance() {
		return new DAOProxyFactory();
	}

	public Object createProxy(PersistanceManager persistanceManager) {
		this.persistanceManager = persistanceManager;
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
			Object rev = method.invoke(persistanceManager, args);
			persistanceManager.getSession().commit(true);
			return rev;
		} catch (Exception e) {
			// 回滚
			persistanceManager.getSession().rollback(true);
			LOGGER.error("失败..." + args[0] + "的" + methodName + "操作被回滚...\r\n" + e.getMessage());
			for (Object bad : persistanceManager.getBadList()) {
				LOGGER.error("错误数据:" + ObjectToStringUtil.execute(bad));
			}
			e.printStackTrace();
			LogUtil.getLogger(LOG_TYPE.ERROR).error(ExceptionLogUtil.log(e));
			return null;
		} finally {
			// 最终处理
			persistanceManager.getSession().close();
		}
	}

}
