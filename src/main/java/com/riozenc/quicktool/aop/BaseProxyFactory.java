/**
 * @Title:BaseProxyFactory.java
 * @Author:Riozenc
 * @Datetime:2015年10月30日 下午9:28:53
 * @Project:interactionGis
 */
package com.riozenc.quicktool.aop;

import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public abstract class BaseProxyFactory<T> implements MethodInterceptor {

	private Object proxyObject;

	protected BaseProxyFactory() {
	}

	public Object getProxyObject() {
		return proxyObject;
	}

	public T createProxy(Class<?> clazz) {
		try {
			this.proxyObject = clazz.newInstance();
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clazz);
			enhancer.setCallback(this);

			return (T) enhancer.create();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			// 无法初始化
			e.printStackTrace();
			LogUtil.getLogger(LOG_TYPE.ERROR).error("BaseProxyFactory无法初始化" + clazz.getName() + "类...");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			// 没有权限访问
			e.printStackTrace();
			LogUtil.getLogger(LOG_TYPE.ERROR).error("BaseProxyFactory无权限访问" + clazz.getName() + "类...");
		}
		return null;
	}

}
