package org.whale.ext.readWrite;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.CollectionUtils;
import org.whale.ext.readWrite.router.Router;

/**
 * http://jinnianshilongnian.iteye.com/blog/1720618
 * <pre>
 * 读/写动态选择数据库实现
 * 目前实现功能
 *   一写库多读库选择功能，请参考
 *      @see cn.javass.common.datasource.ReadWriteDataSourceDecision
        @see cn.javass.common.datasource.ReadWriteDataSourceDecision.DataSourceType
 *   
 *   默认按顺序轮询使用读库
 *   默认选择写库
 *   
 *   已实现：一写多读、当写时默认读操作到写库、当写时强制读操作到读库
 *   TODO 读库负载均衡、读库故障转移
 * </pre>  
 * @author Zhang Kaitao 
 *
 */
public class ReadWriteDataSource extends AbstractDataSource implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ReadWriteDataSource.class);
    
    private DataSource writeDataSource;
    private Map<String, DataSource> readDataSourceMap;
    
    
    private String[] readDataSourceNames;
    private DataSource[] readDataSources;
    private int readDataSourceCount;
    
    private Router router;

    
    /**
     * 设置读库（name, DataSource）
     * @param readDataSourceMap
     */
    public void setReadDataSourceMap(Map<String, DataSource> readDataSourceMap) {
        this.readDataSourceMap = readDataSourceMap;
    }
    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }
    
    
    @Override
    public void afterPropertiesSet() throws Exception {
        if(writeDataSource == null) {
            throw new IllegalArgumentException("property 'writeDataSource' is required");
        }
        if(CollectionUtils.isEmpty(readDataSourceMap)) {
            throw new IllegalArgumentException("property 'readDataSourceMap' is required");
        }
        readDataSourceCount = readDataSourceMap.size();
        
        readDataSources = new DataSource[readDataSourceCount];
        readDataSourceNames = new String[readDataSourceCount];
        
        int i = 0;
        for(Entry<String, DataSource> e : readDataSourceMap.entrySet()) {
            readDataSources[i] = e.getValue();
            readDataSourceNames[i] = e.getKey();
            i++;
        }
    }
    
    
    private DataSource determineDataSource() {
        if(ReadWriteDataSourceDecision.isChoiceWrite()) {
            log.debug("current determine write datasource");
            return writeDataSource;
        }
        
        if(ReadWriteDataSourceDecision.isChoiceNone()) {
            log.debug("no choice read/write, default determine write datasource");
            return writeDataSource;
        } 
        return determineReadDataSource();
    }
    
    private DataSource determineReadDataSource() {
        return router.route(writeDataSource, readDataSources, readDataSourceNames);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return determineDataSource().getConnection();
    }
    
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineDataSource().getConnection(username, password);
    }
    
	public Router getRouter() {
		return router;
	}
	public void setRouter(Router router) {
		this.router = router;
	}
}
