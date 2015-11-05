package org.whale.inf.client;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

public class ClientInfScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

	private String basePackage;
	
	private String beanName;
	
	private boolean addToConfig = true;
	
	private Class<? extends Annotation> annotationClass;
	
	private Class<?> markerInterface;
	
	private boolean processPropertyPlaceHolders;
	
	private ApplicationContext applicationContext;
	
	private BeanNameGenerator nameGenerator;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		

	}
	
	/**
	   * Same as {@code MapperFactoryBean#setAddToConfig(boolean)}.
	   *
	   * @param addToConfig
	   * @see MapperFactoryBean#setAddToConfig(boolean)
	   */
	  public void setAddToConfig(boolean addToConfig) {
	    this.addToConfig = addToConfig;
	  }
	  
	/**
	   * This property specifies the annotation that the scanner will search for.
	   * <p>
	   * The scanner will register all interfaces in the base package that also have the
	   * specified annotation.
	   * <p>
	   * Note this can be combined with markerInterface.
	   *
	   * @param annotationClass annotation class
	   */
	  public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
	    this.annotationClass = annotationClass;
	  }

	
	 /**
	   * This property specifies the parent that the scanner will search for.
	   * <p>
	   * The scanner will register all interfaces in the base package that also have the
	   * specified interface class as a parent.
	   * <p>
	   * Note this can be combined with annotationClass.
	   *
	   * @param superClass parent class
	   */
	  public void setMarkerInterface(Class<?> superClass) {
	    this.markerInterface = superClass;
	  }

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (this.processPropertyPlaceHolders) {
		      processPropertyPlaceHolders();
		    }
		ClientInfClassPathScanner scanner = new ClientInfClassPathScanner(registry);
		scanner.setAddToConfig(this.addToConfig);
	    scanner.setAnnotationClass(this.annotationClass);
	    scanner.setMarkerInterface(this.markerInterface);
	    scanner.setResourceLoader(this.applicationContext);
	    scanner.setBeanNameGenerator(this.nameGenerator);
	    scanner.registerFilters();
	    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	 private void processPropertyPlaceHolders() {
		    Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		    if (!prcs.isEmpty() && applicationContext instanceof GenericApplicationContext) {
		      BeanDefinition mapperScannerBean = ((GenericApplicationContext) applicationContext)
		          .getBeanFactory().getBeanDefinition(beanName);

		      // PropertyResourceConfigurer does not expose any methods to explicitly perform
		      // property placeholder substitution. Instead, create a BeanFactory that just
		      // contains this mapper scanner and post process the factory.
		      DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		      factory.registerBeanDefinition(beanName, mapperScannerBean);

		      for (PropertyResourceConfigurer prc : prcs.values()) {
		        prc.postProcessBeanFactory(factory);
		      }

		      PropertyValues values = mapperScannerBean.getPropertyValues();

		      this.basePackage = updatePropertyValue("basePackage", values);
		    }
		  }
	 private String updatePropertyValue(String propertyName, PropertyValues values) {
		    PropertyValue property = values.getPropertyValue(propertyName);

		    if (property == null) {
		      return null;
		    }

		    Object value = property.getValue();

		    if (value == null) {
		      return null;
		    } else if (value instanceof String) {
		      return value.toString();
		    } else if (value instanceof TypedStringValue) {
		      return ((TypedStringValue) value).getValue();
		    } else {
		      return null;
		    }
		  }

	public boolean isProcessPropertyPlaceHolders() {
		return processPropertyPlaceHolders;
	}

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

	public String getBeanName() {
		return beanName;
	}
	 
	/**
	   * Gets beanNameGenerator to be used while running the scanner.
	   *
	   * @return the beanNameGenerator BeanNameGenerator that has been configured
	   * @since 1.2.0
	   */
	  public BeanNameGenerator getNameGenerator() {
	    return nameGenerator;
	  }

	  /**
	   * Sets beanNameGenerator to be used while running the scanner.
	   *
	   * @param nameGenerator the beanNameGenerator to set
	   * @since 1.2.0
	   */
	  public void setNameGenerator(BeanNameGenerator nameGenerator) {
	    this.nameGenerator = nameGenerator;
	  }
}
