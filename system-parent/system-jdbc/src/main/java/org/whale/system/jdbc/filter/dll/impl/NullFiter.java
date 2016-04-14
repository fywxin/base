package org.whale.system.jdbc.filter.dll.impl;

import org.springframework.stereotype.Component;
import org.whale.system.common.util.LangUtil;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王金绍 on 2016/3/24.
 */
public class NullFiter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {

    @Override
    public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
        LangUtil.trim(obj);
    }

    @Override
    public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        if (objs !=null && objs.size() > 0){
            for (T obj : objs){
                LangUtil.trim(obj);
            }
        }
    }

    @Override
    public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
        LangUtil.trim(obj);
    }

    @Override
    public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        if (objs !=null && objs.size() > 0){
            for (T obj : objs){
                LangUtil.trim(obj);
            }
        }
    }

    @Override
    public int getOrder() {
        return 120;
    }
}
