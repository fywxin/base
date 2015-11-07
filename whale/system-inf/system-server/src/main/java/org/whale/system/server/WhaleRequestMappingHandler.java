package org.whale.system.server;

import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class WhaleRequestMappingHandler extends RequestMappingHandlerAdapter {

	@Override
	public HandlerMethodReturnValueHandlerComposite getReturnValueHandlers() {
		return super.getReturnValueHandlers();
	}
}
