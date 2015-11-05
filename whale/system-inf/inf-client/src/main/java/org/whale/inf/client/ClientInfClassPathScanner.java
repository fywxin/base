package org.whale.inf.client;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class ClientInfClassPathScanner extends ClassPathBeanDefinitionScanner {
	
	private static Logger logger = LoggerFactory.getLogger(ClientInfClassPathScanner.class);
	
	private boolean addToConfig = true;
	
	private Class<? extends Annotation> annotationClass;

	  private Class<?> markerInterface;

	public ClientInfClassPathScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}
	
	public void registerFilters() {
	    boolean acceptAllInterfaces = true;

	    // if specified, use the given annotation and / or marker interface
	    if (this.annotationClass != null) {
	      addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
	      acceptAllInterfaces = false;
	    }

	    // override AssignableTypeFilter to ignore matches on the actual marker interface
	    if (this.markerInterface != null) {
	      addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
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
	        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
	          return true;
	        }
	      });
	    }

	    // exclude package-info.java
	    addExcludeFilter(new TypeFilter() {
	      public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
	        String className = metadataReader.getClassMetadata().getClassName();
	        return className.endsWith("package-info");
	      }
	    });
	  }

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		if (beanDefinitions.isEmpty()) {
			logger.warn("路径["+Arrays.toString(basePackages)+"]没有接口定义类");
		}else{
			for (BeanDefinitionHolder holder : beanDefinitions) {
				GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
				if (logger.isDebugEnabled()) {
					logger.debug("创建客户端接口 '" + holder.getBeanName() 
				              + "' 类名 '" + definition.getBeanClassName());
				}
				definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
		        definition.setBeanClass(ClientInfScannerConfigurer.class);
		        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			}
		}
		
		
		return beanDefinitions;
	}

	public boolean isAddToConfig() {
		return addToConfig;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public Class<?> getMarkerInterface() {
		return markerInterface;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

}
