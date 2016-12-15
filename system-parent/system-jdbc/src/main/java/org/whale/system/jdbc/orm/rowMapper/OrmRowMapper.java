package org.whale.system.jdbc.orm.rowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.util.AnnotationUtil;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by 王金绍 on 2016/6/22.
 */
public class OrmRowMapper<T> implements RowMapper<T> {

    private static Logger logger = LoggerFactory.getLogger(OrmRowMapper.class);

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
        String tmp = null;
        OrmColumn colCopy = null;
        try {
            obj = mappedClass.newInstance();
            //对应关系见 http://hi.baidu.com/linyongboole/item/d3749cbff69121422bebe302
            for(OrmColumn col : ormColumnList){
                colCopy = col;
                if(Types.VARCHAR == col.getType()){
                    AnnotationUtil.setFieldValue(obj, col.getField(), rs.getString(col.getSqlName()));
                    continue ;
                }
                //TODO FIXME BIGINT 对应 LONG 与 BIGINT
                if(Types.BIGINT == col.getType()){
                    tmp = rs.getString(col.getSqlName());
                    if (tmp != null && tmp.length() > 0){
                        AnnotationUtil.setFieldValue(obj, col.getField(), Long.parseLong(tmp));
                    }
                    continue ;
                }
                if(Types.INTEGER == col.getType()){
                    tmp = rs.getString(col.getSqlName());
                    if (tmp != null && tmp.length() > 0){
                        AnnotationUtil.setFieldValue(obj, col.getField(), Integer.parseInt(tmp));
                    }
                    continue ;
                }
                if(Types.BOOLEAN == col.getType()){
                    tmp = rs.getString(col.getSqlName());
                    if (tmp != null && tmp.length() > 0) {
                        int c = Character.toLowerCase(tmp.charAt(0));
                        boolean b = ((c == 't') || (c == 'y') || (c == '1') || tmp.equals("-1") || Long.parseLong(tmp) > 0);
                        AnnotationUtil.setFieldValue(obj, col.getField(), b);
                    }
                    continue ;
                }
                if(Types.DOUBLE == col.getType()){
                    tmp = rs.getString(col.getSqlName());
                    if (tmp != null && tmp.length() > 0){
                        AnnotationUtil.setFieldValue(obj, col.getField(), Double.parseDouble(tmp));
                    }
                    continue ;
                }
                if(Types.FLOAT == col.getType()){
                    tmp = rs.getString(col.getSqlName());
                    if (tmp != null && tmp.length() > 0){
                        AnnotationUtil.setFieldValue(obj, col.getField(), Float.parseFloat(tmp));
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
                    tmp = rs.getString(col.getSqlName());
                    if (tmp != null && tmp.length() > 0){
                        AnnotationUtil.setFieldValue(obj, col.getField(), Short.parseShort(tmp));
                    }
                    continue ;
                }
                if(Types.TINYINT == col.getType()){
                    AnnotationUtil.setFieldValue(obj, col.getField(), rs.getByte(col.getSqlName()));
                    continue ;
                }
                if(Types.DECIMAL == col.getType()){
                    AnnotationUtil.setFieldValue(obj, col.getField(), rs.getBigDecimal(col.getSqlName()));
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
                    Type type = col.getField().getType();
                    String t = type.toString();
                    if("class java.lang.String".equals(t)){
                        String value = lobHandler.getClobAsString(rs, col.getSqlName());
                        AnnotationUtil.setFieldValue(obj, col.getField(), value);
                        continue ;
                    }
                    if("class java.io.InputStream".equals(t)){
                        InputStream value = lobHandler.getBlobAsBinaryStream(rs, col.getSqlName());
                        AnnotationUtil.setFieldValue(obj, col.getField(), value);
                        continue ;
                    }
                    if("class [Ljava.lang.Byte".equals(t) || "class [B".equals(t)){
                        byte[] value = lobHandler.getBlobAsBytes(rs, col.getSqlName());
                        AnnotationUtil.setFieldValue(obj, col.getField(), value);
                        continue ;
                    }
                }
                AnnotationUtil.setFieldValue(obj, col.getField(), rs.getObject(col.getSqlName()));
            }

        } catch (OrmException e){
            throw e;
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new OrmException("设置对象["+mappedClass.getName()+"]的字段["+colCopy.getField().getName()+"]值["+rs.getObject(colCopy.getSqlName())+"]异常",e);
        }
        return obj;
    }

}

