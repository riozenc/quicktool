/**
 * Title:ClassPathMapperScanner.java
 * Author:czy
 * Datetime:2016年11月9日 下午12:11:14
 */
package com.riozenc.quicktool.springmvc.transaction;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

	private Class<? extends Annotation> annotationClass;
	private Class<?> transactionServiceInterface;
	private TransactionServiceFactoryBean<?> transactionServiceFactoryBean = new TransactionServiceFactoryBean<Object>();

	public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
		// TODO Auto-generated constructor stub
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setTransactionServiceInterface(Class<?> transactionServiceInterface) {
		this.transactionServiceInterface = transactionServiceInterface;
	}

	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

		if (beanDefinitions.isEmpty()) {
			logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages)
					+ "' package. Please check your configuration.");
		} else {
			processBeanDefinitions(beanDefinitions);
		}

		return beanDefinitions;
	}

	/**
	 * Configures parent scanner to search for the right interfaces. It can
	 * search for all interfaces or just for those that extends a
	 * markerInterface or/and those annotated with the annotationClass
	 */
	public void registerFilters() {
		boolean acceptAllInterfaces = true;

		// if specified, use the given annotation and / or marker interface
		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		// override AssignableTypeFilter to ignore matches on the actual marker
		// interface
		if (this.transactionServiceInterface != null) {
			addIncludeFilter(new AssignableTypeFilter(this.transactionServiceInterface) {
				@Override
				protected boolean matchClassName(String className) {
					return false;
				}
			});
			acceptAllInterfaces = false;
		}

		if (acceptAllInterfaces) {
			// default include filter that accepts all classes
			addIncludeFilter(new TypeFilter() {
				@Override
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
						throws IOException {
					return true;
				}
			});
		}

		// exclude package-info.java
		addExcludeFilter(new TypeFilter() {
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
					throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		GenericBeanDefinition definition;
		for (BeanDefinitionHolder holder : beanDefinitions) {
			definition = (GenericBeanDefinition) holder.getBeanDefinition();

			Object o1 = holder.getSource();

			if (logger.isDebugEnabled()) {
				logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '"
						+ definition.getBeanClassName() + "' mapperInterface");
			}

			// the mapper interface is the original class of the bean
			// but, the actual class of the bean is MapperFactoryBean
			definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName()); // issue
																												// #59

			try {
				definition.getPropertyValues().add("serviceInterface", Class.forName(definition.getBeanClassName()));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				logger.debug(definition.getBeanClassName() + "is Not Found");
			}
			definition.setBeanClass(this.transactionServiceFactoryBean.getClass());

			boolean explicitFactoryUsed = false;

			if (!explicitFactoryUsed) {
				if (logger.isDebugEnabled()) {
					logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName()
							+ "'.");
				}
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			}
		}
	}
}
