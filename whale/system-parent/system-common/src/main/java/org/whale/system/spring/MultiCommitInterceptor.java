package org.whale.system.spring;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.whale.system.annotation.web.MultiCommit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 
 * User: 王金绍
 * Date: 2015/10/14
 * Time: 16:20
 */
@SuppressWarnings("all")
public class MultiCommitInterceptor extends HandlerInterceptorAdapter {

    private final static String MULTI_COMMIT_TOKEN = "multiCommitToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            MultiCommit annotation = method.getAnnotation(MultiCommit.class);
            if (annotation != null) {
                boolean needRestrict = annotation.needRestrict();
                if (needRestrict) {
                    String multiFlag = (String)request.getSession(false).getAttribute(getTokenKey(handlerMethod));
                    if (multiFlag != null) {
                        return false;
                    }
                    request.getSession(false).setAttribute(getTokenKey(handlerMethod), UUID.randomUUID().toString());
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        MultiCommit annotation = method.getAnnotation(MultiCommit.class);
        if (annotation != null) {
            boolean needRestrict = annotation.needRestrict();
            if (needRestrict) {
                request.getSession(false).removeAttribute(getTokenKey(handlerMethod));
            }
        }
    }

    private String getTokenKey(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        return MULTI_COMMIT_TOKEN + "_" + method.getName().hashCode();
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute(MULTI_COMMIT_TOKEN);
        if (serverToken == null) {
            return true;
        }
        String clientToken = request.getParameter(MULTI_COMMIT_TOKEN);
        if (clientToken == null) {
            return true;
        }
        if (!serverToken.equals(clientToken)) {
            return true;
        }
        return false;
    }
}
