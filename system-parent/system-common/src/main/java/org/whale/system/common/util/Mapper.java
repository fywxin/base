package org.whale.system.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.exception.SysException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * TODO 更新为策略模式，缓存类-字段-赋值策略  提升效率
 * Created by 王金绍 on 2016/12/13.
 */
public class Mapper {
    private static Logger logger = LoggerFactory.getLogger(Mapper.class);

    public static Map<Class<?>, Map<String, Field>> clazzFiledMap = new HashMap<Class<?>, Map<String, Field>>();
    public static Map<Class<?>, Map<String, Field>> clazzLowCaseFiledMap = new HashMap<Class<?>, Map<String, Field>>();

    public static <M> M map2Clazz(Map<String, Object> map, Class<M> clazz){
        if(map == null){
            return null;
        }
        M m = null;
        try {
            m = clazz.newInstance();
        } catch (Exception e) {
            throw new SysException("实例化类"+clazz+"出现异常", e);
        }
        Map<String, Field> fieldMap = clazzFiledMap.get(clazz);
        Map<String, Field> lowCaseFieldMap = clazzLowCaseFiledMap.get(clazz);
        if (fieldMap == null){
            fieldMap = new HashMap<String, Field>();
            lowCaseFieldMap = new HashMap<String, Field>();
            clazzFiledMap.put(clazz, fieldMap);
            clazzLowCaseFiledMap.put(clazz, lowCaseFieldMap);
            Class<?> glass = clazz;
            while (!glass.getName().equals("java.lang.Object")){
                for(Field field : glass.getDeclaredFields()){
                    if (!fieldMap.containsKey(field.getName()) && (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))){//非static 或final 字段
                        fieldMap.put(field.getName(), field);
                        lowCaseFieldMap.put(field.getName().toLowerCase(), field);
                    }
                }
                glass = glass.getSuperclass();
            }
        }

        Field field = null;
        try {
            for(Map.Entry<String, Object> entry : map.entrySet()){
                field = fieldMap.get(entry.getKey());
                if(field == null && entry.getKey().indexOf("_") != -1){
                    field = fieldMap.get(Strings.sql2Camel(entry.getKey()));
                }
                if(field == null){
                    field = lowCaseFieldMap.get(entry.getKey().toLowerCase());
                    if (field != null){
                        logger.info("类[" + clazz + "]属性[" + field.getName() + "]与数据库字段[" + entry.getKey() + "]大小写不一致");
                    }else{
                        logger.info("类[" + clazz + "]属性[" + entry.getKey() + "]查找不到");
                        continue;
                    }
                }
                try{
                    if (entry.getValue() == null && field.getType().isPrimitive()){
                        ReflectionUtil.writeField(field, m, 0, true);
                    }else{
                        ReflectionUtil.writeField(field, m, entry.getValue(), true);
                    }
                }catch(IllegalArgumentException e){
                    //类型为日期
                    if(field.getType().equals(Date.class)){
                        Date dateVal = CastValUtil.castToDate(entry.getValue());
                        if (dateVal != null){
                            ReflectionUtil.writeField(field, m, dateVal, true);
                        }
                        //类型为boolean
                    } else if((field.getType().equals(Boolean.class) || field.getType().getName().equals("boolean"))){
                        Boolean booleanVal = CastValUtil.castToBoolean(entry.getValue());
                        if (booleanVal != null){
                            ReflectionUtil.writeField(field, m, booleanVal, true);
                        }
                        //值为数字
                    }else if (entry.getValue() instanceof Number){
                        Number val = (Number)entry.getValue();
                        if (field.getType().getName().equals("java.lang.Long") || field.getType().getName().equals("long")){
                            ReflectionUtil.writeField(field, m, val.longValue(), true);
                        }else if (field.getType().getName().equals("java.lang.Integer") || field.getType().getName().equals("int")){
                            ReflectionUtil.writeField(field, m, val.intValue(), true);
                        }else if (field.getType().getName().equals("java.lang.String")){
                            ReflectionUtil.writeField(field, m, val.toString(), true);
                        }else if (field.getType().getName().equals("java.lang.Double") || field.getType().getName().equals("double")){
                            ReflectionUtil.writeField(field, m, val.doubleValue(), true);
                        }else if (field.getType().getName().equals("java.lang.Float") || field.getType().getName().equals("float")){
                            ReflectionUtil.writeField(field, m, val.floatValue(), true);
                        }else if (field.getType().getName().equals("java.lang.Short") || field.getType().getName().equals("short")){
                            ReflectionUtil.writeField(field, m, val.shortValue(), true);
                        }else if (field.getType().getName().equals("java.lang.Byte") || field.getType().getName().equals("byte")){
                            ReflectionUtil.writeField(field, m, val.byteValue(), true);
                        }else if (field.getType().getName().equals("java.math.BigInteger")){
                            ReflectionUtil.writeField(field, m, new BigInteger(val.toString()), true);
                        }else if (field.getType().getName().equals("java.math.BigDecimal")){
                            ReflectionUtil.writeField(field, m, new BigDecimal(val.toString()), true);
                        }else{
                            throw e;
                        }
                        //值为boolean
                    }else if(entry.getValue() instanceof Boolean){
                        Boolean valBool = (Boolean)entry.getValue();
                        if (field.getType().getName().equals("java.lang.String")){
                            ReflectionUtil.writeField(field, m, valBool.booleanValue() ? "true" : "false", true);
                        }else{
                            ReflectionUtil.writeField(field, m, valBool.booleanValue() ? 1 : 0, true);
                        }

                        //值为字符串
                    }else if(entry.getValue() instanceof String){
                        String valStr = (String)entry.getValue();
                        if (field.getType().getName().equals("java.lang.Long") || field.getType().getName().equals("long")){
                            ReflectionUtil.writeField(field, m, Long.valueOf(valStr), true);
                        }else if (field.getType().getName().equals("java.lang.Integer") || field.getType().getName().equals("int")){
                            ReflectionUtil.writeField(field, m, Integer.valueOf(valStr), true);
                        }else if (field.getType().getName().equals("java.lang.Double") || field.getType().getName().equals("double")){
                            ReflectionUtil.writeField(field, m, Double.valueOf(valStr), true);
                        }else if (field.getType().getName().equals("java.lang.Float") || field.getType().getName().equals("float")){
                            ReflectionUtil.writeField(field, m, Float.valueOf(valStr), true);
                        }else if (field.getType().getName().equals("java.lang.Short") || field.getType().getName().equals("short")){
                            ReflectionUtil.writeField(field, m, Short.valueOf(valStr), true);
                        }else if (field.getType().getName().equals("java.math.BigDecimal")){
                            ReflectionUtil.writeField(field, m, new BigDecimal(valStr), true);
                        }else if (field.getType().getName().equals("java.math.BigInteger")){
                            ReflectionUtil.writeField(field, m, new BigInteger(valStr), true);
                        }else if (field.getType().getName().equals("java.lang.Byte") || field.getType().getName().equals("byte")){
                            if (valStr.getBytes().length == 1){
                                ReflectionUtil.writeField(field, m, valStr.getBytes()[0], true);
                            }else{
                                throw e;
                            }
                        }else if (field.getType().getName().equals("java.lang.Char") || field.getType().getName().equals("char")){
                            if (valStr.length() == 1){
                                ReflectionUtil.writeField(field, m, valStr.charAt(0), true);
                            }else{
                                throw e;
                            }
                        }else{
                            throw e;
                        }
                    }else{
                        throw e;
                    }
                }

            }
        } catch (Exception e) {
            throw new SysException("类["+clazz+"]写入字段["+field+"]值异常", e);
        }
        return m;
    }

    public static <M> List<M> map2List(List<Map<String, Object>> list, Class<M> clazz){
        if (list == null || list.size() < 1){
            return null;
        }
        List<M> rs = new ArrayList<M>(list.size());
        for (Map<String, Object> map : list) {
            rs.add(Mapper.map2Clazz(map, clazz));
        }

        return rs;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        Field f = Test.class.getDeclaredField("id");
        System.out.println((f.getModifiers()>>3) % 2 == 1);
        System.out.println((f.getModifiers() >> 4) % 2 == 1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appId", "search");
        map.put("serviceid", "search-B10001");
        map.put("className", "com.cyou.fz.services.search.api.SearchService");
        map.put("moduleName", "搜索");
        map.put("interfaceName", "搜索");
        map.put("methodName", "search");
        map.put("version", "1.0.1");
        map.put("accessControl", "2");
        map.put("status", 0);
        map.put("lastErrorTime", 0);
        map.put("serviceKey", 1987180619L);
        map.put("logSide", false);
        map.put("tps", 3);

    }

    public static class Test{
        private static Integer id = 0;

        private String name;

        private Boolean sex;

        private Date date;

        public Integer getId() {
            return id;
        }

        // public void setId(Integer id) {
//            this.id = id;
//        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getSex() {
            return sex;
        }

        public void setSex(Boolean sex) {
            this.sex = sex;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Test [id=" + id + ", name=" + name + ", sex=" + sex
                    + ", date=" + date + "]";
        }



    }
}
