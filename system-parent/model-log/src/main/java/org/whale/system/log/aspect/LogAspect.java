package org.whale.system.log.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.annotation.log.Log;
import org.whale.system.base.UserContext;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.log.domain.LogInfo;
import org.whale.system.log.service.LogStoreService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面
 */
@Component
@Aspect
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	public Map<String, LogInfo> LogInfoCache = new HashMap<String, LogInfo>(256);

	public Object lock = new Object();

	@Autowired
	private LogStoreService logStoreService;
	
	@Pointcut("@annotation(org.whale.system.annotation.log.Log)")
	public void logCut() {
		
	}

	/**
	 * 创建日志对象或从缓存中获取该对象
	 * 注意：@Log 宿主方法重载则可能获取错误
	 *
	 * @param pjp
	 * @return
	 */
	private LogInfo get(ProceedingJoinPoint pjp){
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		int len = pjp.getArgs() == null ? 0 : pjp.getArgs().length;
		String key = className + "#" + methodName+"#"+len;

		LogInfoCache.put(key, null);
		LogInfo logInfo = LogInfoCache.get(key);
		if (logInfo == null){
			synchronized (lock){
				logInfo = LogInfoCache.get(key);
				if (logInfo == null){
					logInfo = new LogInfo();
					logInfo.setClazz(className);
					logInfo.setMethod(methodName);

					MethodSignature signature = (MethodSignature) pjp.getSignature();
					Method method = signature.getMethod();

					Log logMethod = (Log)method.getAnnotation(Log.class);
					logInfo.setInfo(logMethod.value() + ".");
					logInfo.splitDesc();

					if (Strings.isBlank(logMethod.module())){
						Log logHost = (Log)pjp.getTarget().getClass().getAnnotation(Log.class);
						if (logHost != null){
							logInfo.setModule(logHost.module());
						}
					}else{
						logInfo.setModule(logMethod.module());
					}
					LogInfoCache.put(key, logInfo);
				}
			}
		}
		return logInfo.cloneThis();
	}

	/**
	 * 设置日志运行时信息
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("logCut()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		LogInfo logInfo = this.get(pjp);

		UserContext uc = UserContext.get();
		if(uc != null){
			logInfo.setUserName(uc.getUserName());
			logInfo.setIp(uc.getIp());
		}else{
			logInfo.setUserName(Thread.currentThread().getName());
			logInfo.setIp(ThreadContext.getContext().getStr(ThreadContext.KEY_LOG_IP));
		}
		
		Long startTime =System.currentTimeMillis();
		try{
			Object rs = pjp.proceed();
			logInfo.setCostTime(new Long(System.currentTimeMillis() - startTime).intValue());
			logInfo.setRs(LogInfo.RS_SUCCESS);
			this.replaceDesc(logInfo);
			this.logStoreService.addLogRecvQueue(logInfo);
			//一定要返回该对象，否则前台返回空
			return rs;
		}catch(Throwable ex){
			logInfo.setCostTime(new Long(System.currentTimeMillis() - startTime).intValue());

			if(ex instanceof BusinessException || ex.getCause() instanceof BusinessException){
				logInfo.setRs(LogInfo.RS_BusinessException);
			}else if(ex instanceof OrmException || ex.getCause() instanceof OrmException){
				logInfo.setRs(LogInfo.RS_OrmException);
			}else if(ex instanceof SysException || ex.getCause() instanceof SysException){
				logInfo.setRs(LogInfo.RS_SysException);
			}else if(ex instanceof RuntimeException || ex.getCause() instanceof RuntimeException){
				logInfo.setRs(LogInfo.RS_RunTimeException);
			}else{
				logInfo.setRs(LogInfo.RS_OTHER);
			}
			String exInfo = this.getExceptionInfo(ex);

			logInfo.setInfo(exInfo.length() > 255 ? exInfo.substring(0, 255) : exInfo);
			Object[] args = pjp.getArgs();
			if (args != null && args.length > 0){
				String params = JSON.toJSONString(args);
				logInfo.setParams(params.length() > 65534 ? params.substring(0, 65534) : params);
			}
			this.logStoreService.addLogRecvQueue(logInfo);
			throw ex;
		}
	}

	/**
	 * 替换日志模板占位符
	 *
	 * @param logInfo
	 */
	private void replaceDesc(LogInfo logInfo){
		String desc = logInfo.getInfo();
		String[] args = (String[]) ThreadContext.getContext().get(ThreadContext.KEY_LOG_DATAS);
		if (args != null){
			StringBuilder strb = new StringBuilder(logInfo.getInfo().length()*2);
			if (args.length != logInfo.getSplitDescItem().size()-1){
				logger.warn("日志参数不匹配: {}", logInfo);
			}
			for (int i=0; i<logInfo.getSplitDescItem().size()-1; i++){
				strb.append(logInfo.getSplitDescItem().get(i));
				if (i < args.length){
					strb.append(args[i]);
				}else {
					strb.append("\\{\\}");
				}
			}
			strb.append(logInfo.getSplitDescItem().get(logInfo.getSplitDescItem().size()-1));
			logInfo.setInfo(strb.toString());
		}
	}


	private String getExceptionInfo(Throwable ex){
		StringBuilder strb = new StringBuilder();
		StackTraceElement[] trace = ex.getStackTrace();

		for (StackTraceElement s : trace) {
			strb.append("\tat ").append(s).append("\r\n");
		}
		return strb.toString();
	}
}
