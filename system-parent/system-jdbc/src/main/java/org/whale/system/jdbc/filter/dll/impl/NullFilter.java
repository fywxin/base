package org.whale.system.jdbc.filter.dll.impl;

import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;
import org.whale.system.jdbc.orm.entry.OrmColumn;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

/**
 * 空值过滤
 *
 * Created by 王金绍 on 2016/3/24.
 */
public class NullFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {

    @Override
    public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
        this.trim(obj, baseDao);
    }

    @Override
    public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        if (objs !=null && objs.size() > 0){
            for (T obj : objs){
                this.trim(obj, baseDao);
            }
        }
    }

    @Override
    public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
        this.trim(obj, baseDao);
    }

    @Override
    public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
        if (objs !=null && objs.size() > 0){
            for (T obj : objs){
                this.trim(obj, baseDao);
            }
        }
    }

    @Override
    public int getOrder() {
        return 120;
    }


    private void trim(T obj, IOrmDao<T, PK> baseDao){
        List<OrmColumn> ormColumns = baseDao._getOrmTable().getOrmCols();
        try {
            Object val = null;
            Field field = null;
            for (OrmColumn ormColumn : ormColumns) {
                if (!ormColumn.getIsId()) {
                    field = ormColumn.getField();
                    val = ReflectionUtil.readField(field, obj, true);
                    if (Types.VARCHAR == ormColumn.getType()) {
                        if (val != null) {
                            ReflectionUtil.writeField(field, obj, ((String)val).trim(), true);
                        } else {
                            ReflectionUtil.writeField(field, obj, "", true);
                        }
                    } else {
                        if (val == null) {
                            if (Types.INTEGER == ormColumn.getType()) {
                                ReflectionUtil.writeField(field, obj, 0, true);
                            } else if (Types.BIGINT == ormColumn.getType()) {
                                ReflectionUtil.writeField(field, obj, 0L, true);
                            } else if (Types.DOUBLE == ormColumn.getType()) {
                                ReflectionUtil.writeField(field, obj, 0D, true);
                            } else if (Types.FLOAT == ormColumn.getType()) {
                                ReflectionUtil.writeField(field, obj, 0F, true);
                            } else if (Types.SMALLINT == ormColumn.getType()){
                                ReflectionUtil.writeField(field, obj, 0, true);
                            } else if (Types.DECIMAL == ormColumn.getType()){
                                ReflectionUtil.writeField(field, obj, new BigDecimal(0), true);
                            } else if (Types.BOOLEAN == ormColumn.getType()){
                                ReflectionUtil.writeField(field, obj, false, true);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
    }
}
