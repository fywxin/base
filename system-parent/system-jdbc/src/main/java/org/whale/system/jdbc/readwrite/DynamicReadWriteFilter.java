package org.whale.system.jdbc.readwrite;

import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.jdbc.readwrite.DynamicDataSource;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.BaseDaoFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源自动过滤选择器
 *
 * Created by 王金绍 on 2016/11/8.
 */
public class DynamicReadWriteFilter<T extends Serializable,PK extends Serializable>  implements BaseDaoFilter<T, PK> {

    @Override
    public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterSave(T obj, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterUpdate(T obj, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeUpdateNotNull(T obj, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterUpdateNotNull(T obj, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeDelete(PK id, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterDelete(PK id, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeDelete(Iquery query, IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, false);
    }

    @Override
    public void afterDelete(Iquery query, IOrmDao<T, PK> baseDao) {

    }

    @Override
    public void beforeGet(IOrmDao<T, PK> baseDao, PK id) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeGet(IOrmDao<T, PK> baseDao, Iquery query) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterGet(IOrmDao<T, PK> baseDao, T rs, Iquery query) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeQueryAll(IOrmDao<T, PK> baseDao) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeQuery(IOrmDao<T, PK> baseDao, Iquery query) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, Iquery query) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeQueryPage(IOrmDao<T, PK> baseDao, Page page) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeCount(IOrmDao<T, PK> baseDao, Iquery query) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterCount(IOrmDao<T, PK> baseDao, Number num, Iquery query) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeExist(IOrmDao<T, PK> baseDao, PK id) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterExist(IOrmDao<T, PK> baseDao, boolean contain, PK id) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeQueryForList(IOrmDao<T, PK> baseDao, Iquery query) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, Iquery query) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public void beforeQueryForMap(IOrmDao<T, PK> baseDao, Iquery query) {
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG, true);
    }

    @Override
    public void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, Iquery query) {
        ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_READ_MODE_FLAG);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE+1;
    }
}
