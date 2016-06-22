package org.whale.system.jdbc.orm.rowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.whale.system.jdbc.orm.entry.OrmColumn;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王金绍 on 2016/6/22.
 */
public class OrmBeanPropertyRowMapper<T> implements RowMapper<T> {

    /** Logger available to subclasses */
    private static Logger logger = LoggerFactory.getLogger(OrmBeanPropertyRowMapper.class);

    /** The class we are mapping to */
    private Class<T> mappedClass;

    /** Whether we're defaulting primitives when mapping a null value */
    private boolean primitivesDefaultedForNullValue = false;

    /** Map of the fields we provide mapping for */
    private Map<String, PropertyDescriptor> mappedFields;


    public OrmBeanPropertyRowMapper(Class<T> mappedClass) {
        this.initialize(mappedClass, null);
    }

    public OrmBeanPropertyRowMapper(Class<T> mappedClass, List<OrmColumn> list) {
        Map<String, String> ormColumnMap = new HashMap<String, String>(list.size() * 2);
        for (OrmColumn ormColumn : list){
            ormColumnMap.put(ormColumn.getAttrName(), ormColumn.getSqlName());
        }

        this.initialize(mappedClass, ormColumnMap);
    }


    public OrmBeanPropertyRowMapper(Class<T> mappedClass, Map<String, String> sql2ColMap) {
        this.initialize(mappedClass, sql2ColMap);
    }


    private void initialize(Class<T> mappedClass, Map<String, String> sql2ColMap){
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(sql2ColMap == null ? pd.getName() : sql2ColMap.get(pd.getName()), pd);
            }
        }
    }


    /**
     * Set whether we're defaulting Java primitives in the case of mapping a null value
     * from corresponding database fields.
     * <p>Default is {@code false}, throwing an exception when nulls are mapped to Java primitives.
     */
    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    /**
     * Return whether we're defaulting Java primitives in the case of mapping a null value
     * from corresponding database fields.
     */
    public boolean isPrimitivesDefaultedForNullValue() {
        return primitivesDefaultedForNullValue;
    }


    /**
     * Extract the values for all columns in the current row.
     * <p>Utilizes public setters and result set metadata.
     * @see java.sql.ResultSetMetaData
     */
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            PropertyDescriptor pd = this.mappedFields.get(column.replaceAll(" ", ""));
            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, pd);
                    if (logger.isDebugEnabled() && rowNumber == 0) {
                        logger.debug("Mapping column '" + column + "' to property '" +
                                pd.getName() + "' of type " + pd.getPropertyType());
                    }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    }
                    catch (TypeMismatchException e) {
                        if (value == null && primitivesDefaultedForNullValue) {
                            logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
                                    " and column '" + column + "' with value " + value +
                                    " when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() +
                                    " on object: " + mappedObject);
                        }
                        else {
                            throw e;
                        }
                    }
                }
                catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException(
                            "Unable to map column " + column + " to property " + pd.getName(), ex);
                }
            }
        }

        return mappedObject;
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>The default implementation calls
     * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
     * Subclasses may override this to check specific value types upfront,
     * or to post-process values return from {@code getResultSetValue}.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @param pd the bean property that each result object is expected to match
     * (or {@code null} if none specified)
     * @return the Object value
     * @throws SQLException in case of extraction failure
     * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
     */
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

}
