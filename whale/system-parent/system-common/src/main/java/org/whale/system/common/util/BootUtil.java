package org.whale.system.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

/**
 * 初始化启动器
 *
 * @author 王金绍
 * 2014年9月6日-下午1:29:51
 */
@Component
public class BootUtil implements ApplicationListener<ContextRefreshedEvent> {
	
	public static final Logger logger = LoggerFactory.getLogger(BootUtil.class);

	@Autowired(required=false)
	private List<Bootable> bootables;
	
	 private transient volatile boolean isInited = false;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			if(isInited)
        		return ;
			isInited = true;
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("event", event);
			context.put("bootables", bootables);
			
			List<Bootable> list = this.fixBootables();
			context.put("fixInitServices", list);
			Object rs = null;
			
			logger.info("触发初始化事件，待执行初始化对象列表："+list);
			for(Bootable bootable : list){
				rs = bootable.init(context);
				context.put(bootable.getClass().getName(), rs);
				logger.info("对象【"+bootable.getClass().getName()+"】初始化完成，返回值："+rs);
			}
			logger.info("完成初始化...");
		}
	}

	/**
	 * 排序待初始化任务列表
	 * @return
	 */
	private List<Bootable> fixBootables(){
		List<Bootable> list = new LinkedList<Bootable>();
		if(bootables != null && bootables.size() > 0){
			for(Bootable bootable : bootables){
				if(bootable.access()){
					list.add(bootable);
				}
			}
			Collections.sort(list, new OrderComparator());
		}
		
		return list;
	}
}

