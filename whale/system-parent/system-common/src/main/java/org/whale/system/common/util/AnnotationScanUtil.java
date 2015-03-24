package org.whale.system.common.util;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 获取注解类
 *
 * @author 王金绍
 * 2014年9月19日-下午12:14:30
 */
public class AnnotationScanUtil {
	
	private static Logger logger = LoggerFactory.getLogger(AnnotationScanUtil.class);

	/**
	 * 获取所有包含注释的类定义
	 * 
	 * @param basePackage 基本包路径
	 * @param annotationType  注释类型
	 * @return
	 */
	public static Set<BeanDefinition> getAnnotations(String basePackage, Class<? extends Annotation> annotationType){
		//ClassPathBeanDefinitionScanner
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
		return scanner.findCandidateComponents(basePackage);
	}
	
	/**
	 * 获取所有包含注释的类
	 * 
	 * @param basePackage
	 * @param annotationType
	 * @return
	 */
	public static Set<Class<?>> getAnnotationsClass(String basePackage, Class<? extends Annotation> annotationType){
		Set<BeanDefinition> beanDefinitions = getAnnotations(basePackage, annotationType);
		
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		if(beanDefinitions != null && beanDefinitions.size() > 0){
			for(BeanDefinition beanDefinition : beanDefinitions){
				try {
					clazzes.add(Class.forName(beanDefinition.getBeanClassName()));
				} catch (ClassNotFoundException e) {
					logger.error("查找不到类", e);
				}
			}
		}
		return clazzes;
	}
}
