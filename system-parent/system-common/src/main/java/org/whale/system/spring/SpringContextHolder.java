package org.whale.system.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.whale.system.common.util.Strings;

/**
 * spring 容器
 *
 * @author wjs
 * 2014年9月6日-下午1:32:52
 */
@Component
public class SpringContextHolder implements ApplicationContextAware{
	
	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		logger.info("spring 容器注入对象  ApplicationContext={}", ctx);
		applicationContext = ctx;
	}
	
	public static ApplicationContext getApplicationContext(){
		if(applicationContext == null){
			logger.error("ApplicationContext 未被注入，请确认是否spring注释解析<context:component-scan>");
			throw new RuntimeException("ApplicationContext 未被注入，请确认是否spring注释解析<context:component-scan>");
		}
		return applicationContext;
	}
	
	public static Object getBean(String name){
		return getApplicationContext().getBean(name);
	}
	
	public static <T> T getBean(Class<T> requiredType){
		return getApplicationContext().getBean(requiredType);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType){
		return getApplicationContext().getBean(name, requiredType);
	}
	
	/**
	 * 
	 *功能说明: Spring 容器查找所有的接口实现类
	 *创建时间:Jan 30, 2013 10:38:08 AM
	 *@param <T>
	 *@param clazz
	 *@return List<T>
	 */
	public static <T> List<T> getAutowiredClasses(Class<T> clazz){
		List<T> list = new ArrayList<T>();
		Map<String, T> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), clazz, true, false);
		if(matchingBeans == null || matchingBeans.size() < 1) return null;
		list.addAll(matchingBeans.values());
		return list;
	}
	
	/**
	 * 
	 *功能说明: Spring 容器查找所有的一个实现类
	 *创建时间:Jan 30, 2013 10:38:28 AM
	 *@param <T>
	 *@param clazz
	 *@return T
	 */
	public static <T> T getAutowiredClass(Class<T> clazz){
		List<T> list = getAutowiredClasses(clazz);
		if(list == null || list.size() < 1) return null;
		return list.get(0);
	}
	
	/**
	 * 
	 *功能说明:Spring 容器查找所有的一个类名称为“name” 的实现类
	 *创建时间:Jan 30, 2013 10:39:17 AM
	 *@param <T>
	 *@param clazz
	 *@param name
	 *@return T
	 */
	public static <T> T getAutowiredClass(Class<T> clazz, String name){
		if(Strings.isBlank(name)) return getAutowiredClass(clazz);
		
		Map<String, T> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				SpringContextHolder.getApplicationContext(), clazz, true, false);
		if(matchingBeans == null || matchingBeans.size() < 1) return null;
		for(Map.Entry<String, T> entry : matchingBeans.entrySet()){
			if(name.trim().equals(entry.getKey()))
				return entry.getValue();
		}
		return null;
	}

}
