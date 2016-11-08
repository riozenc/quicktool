/**
 * @Project:quicktool
 * @Title:TransactionManager.java
 * @Author:Riozenc
 * @Datetime:2016年11月7日 下午10:51:24
 * 
 */
package com.riozenc.quicktool.springmvc.transaction;

import java.lang.annotation.Annotation;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.riozenc.quicktool.annotation.TransactionService;
import com.riozenc.quicktool.common.util.file.FileIoUtil;
import com.riozenc.quicktool.config.Global;

public class TransactionManager implements BeanDefinitionRegistryPostProcessor {

	private String namespace = "namespace";

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		System.out.println("postProcessBeanFactory");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// TODO Auto-generated method stub
		System.out.println("postProcessBeanDefinitionRegistry");

		RootBeanDefinition beanDefinition = new RootBeanDefinition();

		List<Class<?>> list = FileIoUtil.getClasssFromPackage(Global.getConfig(namespace));
		for (Class<?> clazz : list) {
			if (AbstractServiceSupport.class.isAssignableFrom(clazz)) {
				for (Annotation annotation : clazz.getAnnotations()) {
					if (annotation instanceof TransactionService) {
//						beanDefinition.setFactoryMethodName("getTransactionService");
//						beanDefinition.setFactoryBeanName(factoryBeanName);
						beanDefinition.setInitMethodName("getTransactionService");
						beanDefinition.setBeanClass(clazz);
						beanDefinition.setLazyInit(false);
						beanDefinition.setScope("prototype");
						String beanName = clazz.getSimpleName();
						registry.registerBeanDefinition(beanName, beanDefinition);
					}
				}
			}
		}
		// beanDefinition.setInitMethodName(initMethodName);
		// beanDefinition.setBeanClass(ServiceFactoryBean.class);
		// beanDefinition.setLazyInit(true);
		// beanDefinition.getPropertyValues().addPropertyValue("retrofit",
		// retrofit);
		// beanDefinition.getPropertyValues().addPropertyValue("serviceClass",
		// serviceClass);
		// String beanName =
		// this.beanNameGenerator.generateBeanName(beanDefinition, registry)

	}
	
	
}
