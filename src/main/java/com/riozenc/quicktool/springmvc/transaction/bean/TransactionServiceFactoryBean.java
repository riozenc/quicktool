/**
 * Title:TransactionServiceFactoryBean.java
 * Author:czy
 * Datetime:2016年11月9日 上午10:07:47
 */
package com.riozenc.quicktool.springmvc.transaction.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.riozenc.quicktool.annotation.TransactionDAO;
import com.riozenc.quicktool.common.util.ClassUtils;
import com.riozenc.quicktool.common.util.annotation.FieldAnnotationUtil;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;
import com.riozenc.quicktool.mybatis.dao.AbstractDAOSupport;
import com.riozenc.quicktool.mybatis.db.SqlSessionManager;
import com.riozenc.quicktool.springmvc.transaction.proxy.TransactionServiceProxyFactory2;

public class TransactionServiceFactoryBean<T> implements FactoryBean<T> {

	private static Map<String, BeanDefinitionHolder> definitionHolderMap = new ConcurrentHashMap<>();
	private Class<T> serviceInterface;
	private List<AbstractDAOSupport> params = new ArrayList<AbstractDAOSupport>();

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
		return TransactionServiceProxyFactory2.getInstance().createProxy(build(), params);
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

		return processTransactionDAO();
	}

	private T processTransactionDAO() throws Exception {
		T service = this.serviceInterface.newInstance();

		Field[] fields = this.serviceInterface.getDeclaredFields();
		for (Field dao : fields) {

			if (null != dao.getAnnotation(TransactionDAO.class)) {
				String dbName = (String) FieldAnnotationUtil.getAnnotation(dao, TransactionDAO.class);
				SqlSession sqlSession = SqlSessionManager.getSession(dbName, ExecutorType.SIMPLE);

				BeanDefinitionHolder beanDefinitionHolder = definitionHolderMap.get(dao.getName());
				if (beanDefinitionHolder == null) {
					throw new Exception(dao.getName() + " is not found @TransactionDAO!");
				}

				// Object daoParam = BeanUtils.instantiate(dao.getType());
				AbstractDAOSupport abstractDAOSupport = (AbstractDAOSupport) BeanUtils.instantiate(dao.getType());

				ReflectUtil.setFieldValue(service, dao.getName(), abstractDAOSupport);// 给service赋值dao

				Field sqlSessionField = ClassUtils.getField(dao.getType(), SqlSession.class);

				if (sqlSessionField == null) {
					throw new Exception(dao.getName() + " is not found SqlSession support!");
				}

				// 给dao赋值SqlSession
				ReflectUtil.setFieldValue(abstractDAOSupport, sqlSessionField.getName(), sqlSession);
				params.add(abstractDAOSupport);
			}
		}

		return service;
	}
}
