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
import org.springframework.context.ConfigurableApplicationContext;

import com.riozenc.quicktool.annotation.TransactionService;
import com.riozenc.quicktool.common.util.StringUtils;
import com.riozenc.quicktool.config.Global;

public class TransactionServiceRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

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

		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		scanner.setAnnotationClass(TransactionService.class);

		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(Global.getConfig(this.namespace),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

//		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		// List<Class<?>> list =
		// FileIoUtil.getClasssFromPackage(Global.getConfig(namespace));
		// for (Class<?> clazz : list) {
		// if (AbstractServiceSupport.class.isAssignableFrom(clazz)) {
		// for (Annotation annotation : clazz.getAnnotations()) {
		// if (annotation instanceof TransactionService) {
		//
		// beanDefinition.setBeanClass(clazz);
		//
		// String beanName = StringUtils
		// .decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));
		// beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanName);
		// beanDefinition.setBeanClass(this.transactionServiceFactoryBean.getClass());
		//
		// // beanDefinition.setInitMethodName("getTransactionService");
		//
		// beanDefinition.setLazyInit(false);
		// beanDefinition.setScope("prototype");
		// registry.registerBeanDefinition(beanName, beanDefinition);
		// }
		// }
		// }
		// }
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
