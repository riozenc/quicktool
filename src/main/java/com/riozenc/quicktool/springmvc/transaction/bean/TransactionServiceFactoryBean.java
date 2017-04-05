/**
 * Title:TransactionServiceFactoryBean.java
 * Author:czy
 * Datetime:2016年11月9日 上午10:07:47
 */
package com.riozenc.quicktool.springmvc.transaction.bean;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.riozenc.quicktool.annotation.TransactionDAO;
import com.riozenc.quicktool.common.util.ClassUtils;
import com.riozenc.quicktool.common.util.StringUtils;
import com.riozenc.quicktool.common.util.annotation.AnnotationUtil;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;
import com.riozenc.quicktool.mybatis.dao.AbstractTransactionDAOSupport;
import com.riozenc.quicktool.springmvc.transaction.proxy.TransactionServiceProxyFactory2;

public class TransactionServiceFactoryBean<T> implements FactoryBean<T> {

	private static Map<String, BeanDefinitionHolder> definitionHolderMap = new ConcurrentHashMap<>();
	private Class<T> serviceInterface;

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
		return TransactionServiceProxyFactory2.getInstance().createProxy(build());
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

	private T build() throws Exception {
		T service = this.serviceInterface.newInstance();
		Field[] fields = this.serviceInterface.getDeclaredFields();
		for (Field dao : fields) {
			if (null != dao.getAnnotation(TransactionDAO.class)) {
				ReflectUtil.setFieldValue(service, dao.getName(), processTransactionDAO(dao));// 给service赋值dao
			}
		}
		return service;
	}

	private AbstractTransactionDAOSupport processTransactionDAO(Field dao) throws Exception {
		if (null != dao.getAnnotation(TransactionDAO.class)) {

			BeanDefinitionHolder beanDefinitionHolder = definitionHolderMap
					.get(StringUtils.decapitalize(dao.getType().getSimpleName()));
			if (beanDefinitionHolder == null) {
				throw new Exception(dao.getType() + " is not found @TransactionDAO!");
			}
			AbstractTransactionDAOSupport abstractDAOSupport = (AbstractTransactionDAOSupport) BeanUtils
					.instantiate(dao.getType());
//			Field sqlSessionField = ClassUtils.getField(dao.getType(), SqlSession.class);
//			if (sqlSessionField == null) {
//				throw new Exception(dao.getName() + " is not found AbstractTransactionDAOSupport support!");
//			}

			String dbName = (String) AnnotationUtil.getAnnotationValue(dao, TransactionDAO.class);
			if (dbName.length() < 1) {
				dbName = (String) AnnotationUtil.getAnnotationValue(dao.getType(), TransactionDAO.class);
			}
			ReflectUtil.setFieldValue(abstractDAOSupport, "dbName", dbName);

			return abstractDAOSupport;
		}
		return null;
	}

	public static void main(String[] args) {
		String s = "UserDAO";
		System.out.println(StringUtils.decapitalize(s));
	}
}
