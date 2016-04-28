package org.whale.system.jdbc.filter.dll.impl;

import org.springframework.stereotype.Component;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 空值过滤
 *
 * Created by 王金绍 on 2016/3/24.
 */
public class NullFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {

    @Override
    public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
        Field idField = baseDao._getOrmTable().getIdCol().getField();
        Object idVal = ReflectionUtil.readField(idField, obj);
        LangUtil.trim(obj);
        if (idVal == null){//防止跳过获取自增主键
            try {
                ReflectionUtil.writeField(idField, obj, null);
            } catch (IllegalAccessException e) {
            }
        }
    }

    @Override
    public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        if (objs !=null && objs.size() > 0){
            Field idField = baseDao._getOrmTable().getIdCol().getField();
            Object idVal = null;
            for (T obj : objs){
                idVal = ReflectionUtil.readField(idField, obj);
                LangUtil.trim(obj);
                if (idVal == null){
                    try {
                        ReflectionUtil.writeField(idField, obj, null);
                    } catch (IllegalAccessException e) {
                    }
                }
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
