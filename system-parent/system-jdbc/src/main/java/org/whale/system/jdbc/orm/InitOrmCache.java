package org.whale.system.jdbc.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.whale.system.base.BaseDao;
import org.whale.system.common.constant.OrderNumConstant;
import org.whale.system.common.util.Bootable;
import org.whale.system.jdbc.orm.alter.DbInfoFetcher;
import org.whale.system.jdbc.orm.entry.OrmClass;
import org.whale.system.spring.SpringContextHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 初始化所有所有的orm实体
 *
 * @author wjs
 * 2014年9月6日-下午2:05:56
 */
@Component
public class InitOrmCache implements Bootable {
	
	private static Logger logger = LoggerFactory.getLogger(InitOrmCache.class);

	@Autowired
	private OrmContext ormContext;
	@Autowired
	private DbInfoFetcher dbInfoFetcher;
	
	private transient volatile boolean isInited = false;

	@Override
	@SuppressWarnings("all")
	public Object init(Map<String, Object> context) {
		if(isInited){
			logger.warn("ORM: ORM容器已经初始化过，不需要再初始化");
			return null;
		}
		isInited = true;
		
		this.dbInfoFetcher.getDbInfo();
		
		logger.info("ORM：初始化ORM实体配置信息开始...");
		// 获取所有baseDao子类
		List<BaseDao> daos = SpringContextHolder.getAutowiredClasses(BaseDao.class);
		if(daos == null || daos.size() < 1){
			logger.warn("ORM: ORM容器初始化没有找到任何DAO类 !!!");
			return null;
		}
		
		Method getClazz = null;
		Method setRowMapper = null;
		Class<?> clazz = null;
		for (BaseDao dao : daos) {
			try {
				// 根据反射获取子类dao对应的orm实体对象
				getClazz = dao.getClass().getMethod("getClazz");
				setRowMapper = dao.getClass().getMethod("setRowMapper", RowMapper.class);
				clazz = (Class<?>) getClazz.invoke(dao);
				
				logger.info("ORM：初始化类["+clazz.getName()+"]配置信息...");
				OrmClass ormclass = ormContext.getOrmClass(clazz);

				//使用反射设置RowMapper，优化IOrmDao接口
				setRowMapper.invoke(dao, ormclass.getRowMapper());
				
				this.dbInfoFetcher.alertTable(clazz);
				
				logger.info("ORM：初始化类["+clazz.getName()+"]配置信息完成!");
			} catch (Exception e) {
				logger.error("ORM：初始化类["+clazz.getName()+"] 出现异常", e);
			}
		}
		logger.info("ORM：初始化ORM实体配置信息完成!");
		return null;
	}

	@Override
	public boolean access() {
		return true;
	}

	@Override
	public int getOrder() {
		return OrderNumConstant.ORM_INIT_ORDER;
	}

}
