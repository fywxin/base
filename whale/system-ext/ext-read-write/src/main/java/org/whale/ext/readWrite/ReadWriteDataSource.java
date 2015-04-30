package org.whale.ext.readWrite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.CollectionUtils;
import org.whale.ext.readWrite.router.Router;

import com.alibaba.druid.pool.DruidDataSource;

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
    
    private List<String> readDataSourceNames;
    private List<DataSource> readDataSources;
    private int readDataSourceCount;
    
    private Router router;

    private Set<String> lostConnectDataSoucreNames = new HashSet<String>();
    
    private String testSql = "select count(1) from sys_user";
    
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    /**
     * 读取配置的数据源
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(writeDataSource == null) {
            throw new IllegalArgumentException("property 'writeDataSource' is required");
        }
        if(CollectionUtils.isEmpty(readDataSourceMap)) {
            throw new IllegalArgumentException("property 'readDataSourceMap' is required");
        }
        readDataSourceCount = readDataSourceMap.size();
        
        readDataSources = new CopyOnWriteArrayList<DataSource>();
        readDataSourceNames = new CopyOnWriteArrayList<String>();
        
        for(Entry<String, DataSource> e : readDataSourceMap.entrySet()) {
        	readDataSources.add(e.getValue());
        	readDataSourceNames.add(e.getKey());
        }
        log.debug("从读数据源{}个：{}", readDataSourceCount, readDataSourceMap.keySet());
//        
//        ResumeThread thread = new ResumeThread();
//        thread.setName("从读数据源定时恢复线程");
//        thread.start();
    }
    
    /**
     * 根据service 调用方法，判断使用读还是写数据源
     * @return
     */
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
    
    /**
     * 路由选择使用哪个读数据源
     * 
     * @return
     */
    private DataSource determineReadDataSource() {
    	int i = router.route(writeDataSource, readDataSources, readDataSourceNames);
    	if(i < 0 || i > readDataSources.size()-1){
    		return writeDataSource;
    	}
    	//记录当前线程使用的数据源，当数据源失去连接异常时，将此数据源移除
    	ReadWriteDataSourceDecision.get().setDataSourceName(readDataSourceNames.get(i));
    	
    	log.debug("使用线程："+readDataSourceNames.get(i));
    	return readDataSources.get(i);
    }
    
    /**
     * 移除失效的数据源
     * 
     * @param dataSourceName
     */
    public synchronized void removeDataSource(String dataSourceName){
    	log.warn("开始移除数据源：{}", dataSourceName);
    	
    	int index = readDataSourceNames.indexOf(dataSourceName);
    	if(index == -1){
    		log.warn("移除数据源失败：查找不到数据源：{}", dataSourceName);
    		return ;
    	}
    	//TODO 高并发情况下，可能存在不一致
    	readDataSources.remove(index);
    	readDataSourceNames.remove(index);
    	
    	lostConnectDataSoucreNames.add(dataSourceName);
    	
    	try {
			while(!executor.submit(new ResumeTask(dataSourceName)).get().booleanValue()){
				
			}
		} catch (Exception e) {}
    	
    	log.warn("成功移除数据源 ：{}\n当前可用数据源：{}\n失去连接数据源：{}", dataSourceName, readDataSourceNames, lostConnectDataSoucreNames);
    }
    
    public void addDataSource(String name, DataSource dataSource){
    	
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
	public void setReadDataSourceMap(Map<String, DataSource> readDataSourceMap) {
        this.readDataSourceMap = readDataSourceMap;
    }
    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }
    
    public String getTestSql() {
		return testSql;
	}
	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}


	/**
     * 数据源是否可用恢复线程
     * @author 王金绍
     *
    class ResumeThread extends Thread{

		@Override
		public void run() {
			while(true){
				if(lostConnectDataSoucreNames.size() > 0){
					DruidDataSource dataSource = null;
					for(String name : lostConnectDataSoucreNames){
						dataSource = (DruidDataSource)readDataSourceMap.get(name);
						if(dataSource != null){
							try{
								Statement stmt = dataSource.getConnection().createStatement();
								if(stmt.execute(testSql)){
									lostConnectDataSoucreNames.remove(name);
									readDataSources.add(dataSource);
									readDataSourceNames.add(name);
									
									log.warn("恢复数据源："+name+"成功"+"\n当前可用数据源："+readDataSourceNames+"\n失去连接数据源："+lostConnectDataSoucreNames);
								}
								try {
									Thread.sleep(1000L);
								} catch (InterruptedException e) {}
							}catch(Exception e){
								
							}
						}
					}
				}
				
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {}
			}
		}
    }*/
    
    class ResumeTask implements Callable<Boolean>{
    	
    	String dataSourceName;
    	
    	public ResumeTask(String name){
    		this.dataSourceName = name;
    	}

		@Override
		public Boolean call() throws Exception {
			boolean rs = false;
			try{
				DruidDataSource dataSource = (DruidDataSource)readDataSourceMap.get(dataSourceName);
				if(dataSource != null){
					Statement stmt = dataSource.getConnection().createStatement();
					rs = stmt.execute(testSql);
					if(rs){
						lostConnectDataSoucreNames.remove(dataSourceName);
						readDataSources.add(dataSource);
						readDataSourceNames.add(dataSourceName);
						
						log.warn("恢复数据源："+dataSourceName+"成功"+"\n当前可用数据源："+readDataSourceNames+"\n失去连接数据源："+lostConnectDataSoucreNames);
					}
				}
			}catch(Exception e){
				return false;
			}
			return rs;
		}
    }
}
