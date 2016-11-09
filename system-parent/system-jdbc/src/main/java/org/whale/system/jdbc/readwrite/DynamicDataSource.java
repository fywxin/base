package org.whale.system.jdbc.readwrite;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.jdbc.readwrite.router.RoundRobinRouter;
import org.whale.system.jdbc.readwrite.router.Router;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源读写分离，支持 1写主 多读从
 *
 * Created by 王金绍 on 2016/11/8.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    //默认写主库KEY
    private String writeDsKey;
    //读从库key列表
    private List<String> readDsKeyList;
    //读从库列表
    private List<DataSource> readDsList;
    //读从库路由选择器
    private Router router;

    /**
     *
     * @param dataSource
     * @return
     * @throws IllegalArgumentException
     */
    protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        DataSource ds = super.resolveSpecifiedDataSource(dataSource);

        if (writeDsKey == null){
            try {
                Class superClazz = DynamicDataSource.class.getSuperclass();
                Field filed = superClazz.getDeclaredField("targetDataSources");
                filed.setAccessible(true);
                Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>)filed.get(this);
                if (dataSource instanceof DataSource) {
                    for (Map.Entry<Object, DataSource> entry : resolvedDataSources.entrySet()){
                        if (entry.getValue() == dataSource){
                            writeDsKey = (String)entry.getKey();
                            break;
                        }
                    }

                }else {
                    writeDsKey = (String) dataSource;
                }
                if (Strings.isBlank(writeDsKey)){
                    throw new IllegalArgumentException("默认写数据源key值为空");
                }

                //获取从读库列表
                readDsKeyList = new ArrayList<String>(resolvedDataSources.size());
                readDsList = new ArrayList<DataSource>(resolvedDataSources.size());
                for (Map.Entry<Object, DataSource> entry : resolvedDataSources.entrySet()){
                    if (!writeDsKey.equals((String)entry.getKey())) {
                        readDsKeyList.add((String)entry.getKey());
                        readDsList.add(entry.getValue());
                    }
                }
            }catch (Exception e){
                throw new OrmException("动态数据源初始化错误", e);
            }
        }
        return ds;
    }

    /**
     * 决定走主写库，还是读从库
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //强制事务走写主库标志
        Boolean transFocusWriteFlag = ThreadContext.getContext().getBoolean(ThreadContext.DYNAMIC_DS_TRANS_FOCUS_WRITE_FLAG);
        if (transFocusWriteFlag != null && transFocusWriteFlag.booleanValue()){
            return writeDsKey;
        }

        //是否设置为读标志
        Boolean readModeFlag = ThreadContext.getContext().getBoolean(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
        //save，update，delete，走主写库
        if (readModeFlag == null || !readModeFlag.booleanValue()){
            if (logger.isDebugEnabled()){
                logger.debug("dynamic ds chose write ds :" + writeDsKey);
            }
            return writeDsKey;
        }else{
            //读从库
            String key = this.getNextReadDsKey();
            if (logger.isDebugEnabled()){
                logger.debug("dynamic ds chose read ds :" + key);
            }
            return key;
        }
    }

    /**
     * 路由获取下一个读
     * @return
     */
    public String getNextReadDsKey(){
        if (router == null){
            router = new RoundRobinRouter();
        }
        return router.route(this.readDsList, this.readDsKeyList);
    }
}
