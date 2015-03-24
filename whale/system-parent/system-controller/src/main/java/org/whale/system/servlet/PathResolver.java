package org.whale.system.servlet;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.WebUtil;


/**
 * 路径管理器
 * @author Administrator
 *
 */
@Component
public class PathResolver implements ServletContextAware {

	private ServletContext servletContext;
	
	@PostConstruct
	public void doConfigPath(){
		String resourceRoot = null; //从字典中获取
		if(Strings.isBlank(resourceRoot)){
			resourceRoot = getServletContext().getContextPath()+"/html";
		}
		
		this.servletContext.setAttribute("ctx", getServletContext().getContextPath());
		this.servletContext.setAttribute("resource", resourceRoot);
		
		String path = this.servletContext.getRealPath("index.jsp");
		WebUtil.setWebPath(path.replaceAll("index.jsp", ""));
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
}
