/**
 * @Project:quicktool
 * @Title:TransactionManager.java
 * @Author:Riozenc
 * @Datetime:2016年11月7日 下午10:51:24
 * 
 */
package com.riozenc.quicktool.springmvc.transaction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionManager implements BeanDefinitionRegistryPostProcessor {

	private String namespace;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// TODO Auto-generated method stub

		// Reflections reflections = new Reflections("com.myapp.api.service");
		// Retrofit retrofit = retrofit();
		// Set<Class<?>> annotated =
		// reflections.getTypesAnnotatedWith(RetrofitService.class);

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		// beanDefinition.setInitMethodName(initMethodName);
		// beanDefinition.setBeanClass(ServiceFactoryBean.class);
		// beanDefinition.setLazyInit(true);
		// beanDefinition.getPropertyValues().addPropertyValue("retrofit",
		// retrofit);
		// beanDefinition.getPropertyValues().addPropertyValue("serviceClass",
		// serviceClass);
		// String beanName =
		// this.beanNameGenerator.generateBeanName(beanDefinition, registry)
		String beanName = null;
		registry.registerBeanDefinition(beanName, beanDefinition);
	}

}
