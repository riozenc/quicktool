/**
 * Title:TransactionServiceFactoryBean.java
 * Author:czy
 * Datetime:2016年11月9日 上午10:07:47
 */
package com.riozenc.quicktool.springmvc.transaction.bean;

 java.util.Map;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.SimpleInstantiationStrategy;

import com.riozenc.quicktool.annotation.TransactionDAO;
import com.riozenc.quicktool.common.util.annotation.FieldAnnotationUtil;
import com.riozenc.quicktool.mybatis.db.SqlSessionManager;
import com.riozenc.quicktool.springmvc.transaction.proxy.TransactionServiceProxyFactory2;

public class TransactionServiceFactoryBean<T> implements FactoryBean<T> {

	private Class<T> serviceInterface;

	private static Map<String, BeanDefinitionHolder> definitionHolderMap = new ConcurrentHashMap<>();

	public TransactionServiceFactoryBean() {
		// intentionally empty
	}

	public TransactionServiceFactoryBean(Class<T> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public void setServiceInterface(Class<T> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@Override
	public T getObject() throws Exception {

		// TODO Auto-generated method stub
		if (this.serviceInterface == null) {
			afterPropertiesSet();
		}
		return build();
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return this.serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	public static Map<String, BeanDefinitionHolder> getDefinitionHolderMap() {
		return definitionHolderMap;
	}

	public void afterPropertiesSet() throws Exception {
		throw new Exception("没有可实例化的目标...");
	}

	private T build() {
		return TransactionServiceProxyFactory2.getInstance().createProxy(serviceInterface);
	}

	private void post() throws Exception {
		Field[] fields = this.serviceInterface.getDeclaredFields();
		for (Field field : fields) {

			if (null != field.getAnnotation(TransactionDAO.class)) {
				String dbName = (String) FieldAnnotationUtil.getAnnotation(field, TransactionDAO.class);
				SqlSession sqlSession = SqlSessionManager.getSession(dbName, ExecutorType.SIMPLE);

				BeanDefinitionHolder beanDefinitionHolder = definitionHolderMap.get(field.getName());
				if (beanDefinitionHolder == null) {
					throw new Exception(field.getName() + " is not found @" + this.serviceInterface.getSimpleName());
				}
				
				new SimpleInstantiationStrategy().instantiate(bd, beanName, owner, ctor, args);
			}

		}

	}
}
