/**
 * @Project:quicktool
 * @Title:SpringContextholder.java
 * @Author:Riozenc
 * @Datetime:2016年11月1日 下午9:22:36
 * 
 */
package com.riozenc.quicktool.springmvc.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.riozenc.quicktool.springmvc.context.listener.SpringContextListener;

public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	private static List<SpringContextListener> list = new ArrayList<>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		if (SpringContextHolder.applicationContext == null) {
			SpringContextHolder.applicationContext = applicationContext;
			System.out.println("完成applicationContext赋值");
		}
		for (SpringContextListener listener : list) {
			listener.run();
		}
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		applicationContext = null;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	private static void assertContextInjected() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
		}
	}

	public static void addListener(SpringContextListener springContextListener) {
		list.add(springContextListener);
	}
}
