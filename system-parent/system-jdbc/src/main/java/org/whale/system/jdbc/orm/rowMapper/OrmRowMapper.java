package org.whale.system.jdbc.orm.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.CastValUtil;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.util.AnnotationUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by 王金绍 on 2016/6/22.
 */
public class OrmRowMapper<T> implements RowMapper<T> {

    private Class<T> mappedClass;

    private List<OrmColumn> ormColumnList;

    private DefaultLobHandler lobHandler;

    public OrmRowMapper(Class<T> mappedClass, List<OrmColumn> ormColumnList, DefaultLobHandler lobHandler){
        this.mappedClass = mappedClass;
        this.ormColumnList = ormColumnList;
        this.lobHandler = lobHandler;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T obj = null;
        Object objVal = null;
        OrmColumn colCopy = null;
        try {
            obj = mappedClass.newInstance();
            //对应关系见 http://hi.baidu.com/linyongboole/item/d3749cbff69121422bebe302
            for(OrmColumn col : ormColumnList){
                if (col.getIdIgnore()){
                    continue;
                }
                objVal = rs.getObject(col.getSqlName());
                colCopy = col;
                if(Types.VARCHAR == col.getType()){
                    AnnotationUtil.setFieldValue(obj, col.getField(), rs.getString(col.getSqlName()));
                    continue ;
                }
                if(Types.BIGINT == col.getType()){
                    if (objVal != null){
                        if (col.getAttrType().getName().equals("java.math.BigInteger")){
                            BigInteger bigIntegerVal = CastValUtil.castToBigInteger(objVal);
                            if (bigIntegerVal != null){
                                AnnotationUtil.setFieldValue(obj, col.getField(), bigIntegerVal);
                            }
                        }else{
                            Long longVal = CastValUtil.castToLong(objVal);
                            if (longVal != null) {
                                AnnotationUtil.setFieldValue(obj, col.getField(), longVal);
                            }
                        }
                    }
                    continue ;
                }
                if(Types.INTEGER == col.getType()){
                    Integer intVal = CastValUtil.castToInt(objVal);
                    if (intVal != null){
                        AnnotationUtil.setFieldValue(obj, col.getField(), intVal);
                    }
                    continue ;
                }
                if(Types.BOOLEAN == col.getType()){
                    Boolean booleanVal = CastValUtil.castToBoolean(objVal);
                    if (booleanVal != null) {
                        AnnotationUtil.setFieldValue(obj, col.getField(), booleanVal);
                    }
                    continue ;
                }
                if(Types.DOUBLE == col.getType()){
                    Double doubleVal = CastValUtil.castToDouble(objVal);
                    if (doubleVal != null){
                        AnnotationUtil.setFieldValue(obj, col.getField(), doubleVal);
                    }
                    continue ;
                }
                if(Types.FLOAT == col.getType()){
                    Float floatVal = CastValUtil.castToFloat(objVal);
                    if (floatVal != null){
                        AnnotationUtil.setFieldValue(obj, col.getField(), floatVal);
                    }
                    continue ;
                }
                //不能用java.sql.Date 否则 时分秒 的值为 0
                if(Types.TIMESTAMP == col.getType() || Types.DATE == col.getType()){
                    Timestamp ts = rs.getTimestamp(col.getSqlName());
                    if (ts != null) {
                        AnnotationUtil.setFieldValue(obj, col.getField(), new Date(ts.getTime()));
                    }
                    continue ;
                }
                if(Types.SMALLINT == col.getType()){
                    Short shortVal = CastValUtil.castToShort(objVal);
                    if (shortVal != null){
                        AnnotationUtil.setFieldValue(obj, col.getField(), shortVal);
                    }
                    continue ;
                }
                if(Types.TINYINT == col.getType()){
                    Byte byteVal = CastValUtil.castToByte(objVal);
                    if(byteVal != null){
                        AnnotationUtil.setFieldValue(obj, col.getField(), byteVal);
                    }
                    continue ;
                }
                if(Types.DECIMAL == col.getType()){
                    BigDecimal bigDecimalVal = CastValUtil.castToBigDecimal(objVal);
                    if (bigDecimalVal != null) {
                        AnnotationUtil.setFieldValue(obj, col.getField(), bigDecimalVal);
                    }
                    continue ;
                }
                if(Types.TIME == col.getType() ){
                    Time time = rs.getTime(col.getSqlName());
                    if(time != null){
                        AnnotationUtil.setFieldValue(obj, col.getField(),new Date(time.getTime()));
                        continue ;
                    }
                }
                if(Types.CLOB == col.getType()){
                    String t = col.getAttrType().getName();
                    if("java.lang.String".equals(t)){
                        String value = lobHandler.getClobAsString(rs, col.getSqlName());
                        AnnotationUtil.setFieldValue(obj, col.getField(), value);
                        continue ;
                    }
                    if("java.io.InputStream".equals(t)){
                        InputStream value = lobHandler.getBlobAsBinaryStream(rs, col.getSqlName());
                        AnnotationUtil.setFieldValue(obj, col.getField(), value);
                        continue ;
                    }
                    if("[Ljava.lang.Byte".equals(t) || "[B".equals(t)){
                        byte[] value = lobHandler.getBlobAsBytes(rs, col.getSqlName());
                        AnnotationUtil.setFieldValue(obj, col.getField(), value);
                        continue ;
                    }
                }
                AnnotationUtil.setFieldValue(obj, col.getField(), rs.getObject(col.getSqlName()));
            }

        } catch (OrmException e){
            throw e;
        } catch (Exception e) {
            throw new OrmException("设置对象["+mappedClass.getName()+"]的字段["+colCopy.getField().getName()+"]值["+rs.getObject(colCopy.getSqlName())+"]异常",e);
        }
        return obj;
    }


}

