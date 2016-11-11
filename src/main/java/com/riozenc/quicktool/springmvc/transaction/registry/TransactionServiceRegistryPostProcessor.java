/**
 * @Project:quicktool
 * @Title:TransactionManager.java
 * @Author:Riozenc
 * @Datetime:2016年11月7日 下午10:51:24
 * 
 */
package com.riozenc.quicktool.springmvc.transaction.registry;

import java.lang.annotation.Annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;

import com.riozenc.quicktool.annotation.TransactionService;
import com.riozenc.quicktool.common.util.StringUtils;
import com.riozenc.quicktool.config.Global;
import com.riozenc.quicktool.springmvc.transaction.scanner.ClassPathTransactionServiceScanner;

public class TransactionServiceRegistryPostProcessor extends AbstractRegistryPostProcessor
		implements BeanDefinitionRegistryPostProcessor {

	private static final Class<? extends Annotation> annotationClass = TransactionService.class;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		System.out.println("postProcessBeanFactory");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// TODO Auto-generated method stub
		System.out.println("postProcessBeanDefinitionRegistry");

		ClassPathTransactionServiceScanner scanner = new ClassPathTransactionServiceScanner(registry);
		scanner.setAnnotationClass(annotationClass);

		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(Global.getConfig(getNamespace()),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

}
