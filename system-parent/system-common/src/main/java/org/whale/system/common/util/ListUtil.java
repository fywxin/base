package org.whale.system.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * CollectionUtil
 *
 * @author longlin(longlin@cyou-inc.com)
 * @date 2013-10-15
 * @since V1.0
 */
public class ListUtil {
    private static final Logger logger = LoggerFactory.getLogger(ListUtil.class);

    public static final String DEFAULT_DELIMITER = ",";

    /**
     * 列表是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<T> copy(List<T> list){
        if (list == null){
            return null;
        }
        List<T> copys = new ArrayList<T>(list.size());
        for (T t: list){
            copys.add(t);
        }
        return copys;
    }


    /**
     * 将列表内容倒排序
     * @param list
     * @param <T>
     */
    public static <T> List<T> reverse(List<T> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int ih = i;
            int it = size - 1 - i;
            if (ih == it || ih > it) {
                break;
            }
            T ah = list.get(ih);
            T swap = list.get(it);
            list.set(ih, swap);
            list.set(it, ah);
        }
        return list;
    }

    /**
     * 过滤空
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> filterEmpty(Collection<T> list) {
        if (list == null) {
            return null;
        }

        List<T> newList = new ArrayList<T>();
        for (T t : list) {
            if (t != null && t.toString().length() > 0) {
                newList.add(t);
            }
        }
        return newList;
    }

    /**
     * list1是否包含list2
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> boolean contains(Collection<T> list1, Collection<T> list2) {
        if (ListUtil.isEmpty(list2)) {
            return true;
        } else if (ListUtil.isEmpty(list1)) {
            return false;
        } else {
            for (T o : list2) {
                if (!list1.contains(o)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 字符串分割
     *
     * @param s
     * @param delimiter
     * @return
     */
    public static List<String> split(String s, String delimiter) {
        List<String> list = null;
        if (s != null && s.length() != 0) {
            String[] arr = s.split(delimiter);
            list = new ArrayList<String>(arr.length);
            for (int i = 0; i < arr.length; i++) {
                if (Strings.isNotBlank(arr[i])) {
                    list.add(arr[i].trim());
                }
            }
        }

        return list;
    }

    public static Set<String> split2Set(String s, String delimiter) {
        Set<String> set = null;
        if (s != null && s.length() != 0) {
            String[] arr = s.split(delimiter);
            set = new HashSet<String>(arr.length);
            for (int i = 0; i < arr.length; i++) {
                if (Strings.isNotBlank(arr[i])) {
                    set.add(arr[i].trim());
                }
            }
        }

        return set;
    }

    /**
     * 字符串分割
     *
     * @param s
     * @return
     */
    public static List<String> split(String s) {
        return split(s, DEFAULT_DELIMITER);
    }

    /**
     * 将list对象用separator连接起来
     *
     * @param list
     * @param separator
     * @param <T>
     * @return
     */
    public static <T> String join(Collection<T> list, String separator) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (T t : list) {
                if (Strings.isNotBlank(t)){
                    sb.append(t.toString()).append(separator);
                }
            }
            if (sb.length() > 0){
                sb.delete(sb.length()-separator.length(),sb.length());
            }
        }
        return sb.toString();
    }

    public static <T> String join(Collection<T> list) {
        return join(list, DEFAULT_DELIMITER);
    }

    /**
     * 合并数组里面的重复字段
     * @param c
     * @param <T>
     * @return
     */
    public static <T> Collection<T> unique(Collection<T> c) {
        if (c != null && c.size() > 0) {
            Map<T, Integer> map = new LinkedHashMap<T, Integer>();
            for (T o : c) {
                map.put(o, 0);
            }
            return new ArrayList<T>(map.keySet());
        }
        return c;
    }





    /**
     * 获取对象列表中的 特地字段元素值
     * @param list
     * @return
     */
    public static <E> List<E> listCol(List<?> list, String colName){
        if(list == null || list.size()<1 || Strings.isBlank(colName))
            return null;

        Field f = ReflectionUtil.getDeclaredField(list.get(0), colName);
        if (f == null){
            throw new RuntimeException("字段"+colName+"找不到");
        }
        List<E> rs = new ArrayList<E>(list.size());
        for(Object obj : list){
            if(obj == null)
                continue;
            rs.add((E)ReflectionUtil.readField(f, obj, true));
        }
        return rs;
    }

    /**
     * 获取对象列表中的 特地字段元素值 并且去重
     *
     * @param list
     * @param colName
     * @param <E>
     * @return
     */
    public static <E> Set<E> listCol2Set(List<?> list, String colName){
        if(list == null || list.size()<1 || Strings.isBlank(colName))
            return null;

        Field f = ReflectionUtil.getDeclaredField(list.get(0), colName);
        if (f == null){
            throw new RuntimeException("字段"+colName+"找不到");
        }
        Set<E> rs = new HashSet<E>(list.size()*2);
        for(Object obj : list){
            if(obj == null)
                continue;
            rs.add((E) ReflectionUtil.readField(f, obj, true));
        }
        return rs;
    }

    /**
     * colName 的值唯一，建立索引
     * List<T> 转 Map<colVal, T>
     *
     * @param list
     * @param colName
     * @param <T>
     * @return
     */
    public static <K, T> Map<K, T> list2Map(List<T> list, String colName){
        if(list == null || list.size()<1 || Strings.isBlank(colName))
            return null;

        Field f = ReflectionUtil.getDeclaredField(list.get(0), colName);
        if (f == null){
            throw new RuntimeException("字段"+colName+"找不到");
        }
        Map<K, T> map = new HashMap<K, T>(list.size() * 2);
        for(T t : list){
            if(t == null)
                continue;
            map.put((K) ReflectionUtil.readField(f, t, true), t);
        }
        return map;
    }

    /**
     * colName 的值存在重复，按colName分组
     * List<T> 转 Map<colVal, List<T>>
     *
     * @param list
     * @param colName
     * @param <T>
     * @return
     */
    public static <K, T> Map<K, List<T>> list2MapGroup(List<T> list, String colName){
        if(list == null || list.size()<1 || Strings.isBlank(colName))
            return null;

        Field f = ReflectionUtil.getDeclaredField(list.get(0), colName);
        if (f == null){
            throw new RuntimeException("字段"+colName+"找不到");
        }
        Map<K, List<T>> map = new HashMap<K, List<T>>(list.size());
        List<T> tmp = null;
        K colVal = null;
        for(T t : list){
            if(t == null)
                continue;
            colVal = (K)ReflectionUtil.readField(f, t, true);
            tmp = map.get(colVal);
            if (tmp == null){
                tmp = new ArrayList<T>();
                map.put(colVal, tmp);
            }
            tmp.add(t);
        }
        return map;
    }

    public static String joinCol(List<?> list, String colName) {
        return joinCol(list, colName, ",");
    }

    public static String joinCol(List<?> list, String colName, String joinStr) {
        if(list == null || list.size()<1 || Strings.isBlank(colName))
            return null;
        Field f = ReflectionUtil.getDeclaredField(list.get(0), colName);
        if (f == null){
            throw new RuntimeException("字段"+colName+"找不到");
        }

        StringBuilder strb = new StringBuilder();
        for(Object obj : list){
            if(obj == null)
                continue;
            Object val= ReflectionUtil.readField(f, obj, true);

            if(val != null && Strings.isNotBlank(val.toString()))
                strb.append(joinStr).append(val.toString().trim());
        }
        if(strb.length() > joinStr.length()){
            return strb.substring(joinStr.length());
        }
        return strb.toString();
    }

    /**
     * 获取两个列表的交集
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> List<T> bothContain(Collection<T> list1, Collection<T> list2) {
        if (list1 == null || list1.size() < 1 || list2 == null || list2.size() < 1){
            return null;
        }

        List<T> list = new LinkedList<T>();
        for (T t1 : list1) {
            if (list2.contains(t1)){
                list.add(t1);
            }
        }
        return list;
    }

    /**
     * 获取list1 不在list2 中的部分
     * list1 - list2
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> List<T> notContain(Collection<T> list1, Collection<T> list2) {
        if(list1 == null || list1.size() < 1){
            return null;
        }
        List<T> list = null;
        if (list2 == null || list2.size() < 1){
            list = new ArrayList<T>(list1.size());
            list.addAll(list1);
        }else{
            list = new LinkedList<T>();
            for (T t1 : list1) {
                if (!list2.contains(t1)){
                    list.add(t1);
                }
            }
        }
        return list;
    }

    /**
     * 将以多个ID组成的字符串，分割组装成List<Long>
     * 默认分割为","
     * @param ids 多个ID组成的字符串
     * @return
     */
    public static List<Long> longList(String ids){
        return longList(ids, ",");
    }

    /**
     * 将以多个ID组成的字符串，分割组装成List<Long>
     * @param ids 多个ID组成的字符串
     * @param splitStr 分割符
     * @return
     */
    public static List<Long> longList(String ids, String splitStr){
        if(Strings.isBlank(ids))
            return null;
        if(Strings.isBlank(splitStr)){
            splitStr = ",";
        }
        String[] idS = ids.split(splitStr);
        List<Long> list = new ArrayList<Long>(idS.length);
        for(String id : idS){
            if(Strings.isNotBlank(id))
                try {
                    list.add(Long.parseLong(id.trim()));
                } catch (NumberFormatException e) {
                    logger.error("ID转换成Long类型出错", e);
                    throw new RuntimeException("ID转换成Long类型出错", e);
                }
        }
        return list;
    }


    /**
     * 将以多个ID组成的字符串，分割组装成List<Long>
     * 默认分割为","
     * @param ids 多个ID组成的字符串
     * @return
     */
    public static List<Integer> intList(String ids){
        return intList(ids, ",");
    }

    /**
     * 将以多个ID组成的字符串，分割组装成List<Long>
     * @param ids 多个ID组成的字符串
     * @param splitStr 分割符
     * @return
     */
    public static List<Integer> intList(String ids, String splitStr){
        if(Strings.isBlank(ids))
            return null;
        if(Strings.isBlank(splitStr)){
            splitStr = ",";
        }
        String[] idS = ids.split(splitStr);
        List<Integer> list = new ArrayList<Integer>(idS.length);
        for(String id : idS){
            if(Strings.isNotBlank(id))
                try {
                    list.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    logger.error("ID转换成Integer类型出错", e);
                    throw new RuntimeException("ID转换成Integer类型出错", e);
                }
        }
        return list;
    }


    /**
     * 数组转List对象
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T... array) {
        if (array == null) {
            return null;
        }

        List<T> list = new ArrayList<T>(array.length);
        for (T t : array) {
            list.add(t);
        }
        return list;
    }


    public static <T> Set<T> asSet(T... array) {
        if (array == null) {
            return null;
        }
        Set<T> set = new HashSet<T>(array.length*2);
        for (T t : array) {
            set.add(t);
        }
        return set;
    }



    public static Map asMap(Object... keyValue ) {
        if (keyValue.length % 2 != 0){
            throw new IllegalArgumentException("参数个数必须是偶数个");
        }
        Map map = new HashMap(keyValue.length);
        for (int i=0; i<keyValue.length; i=i+2){
            map.put(keyValue[i], keyValue[i+1]);
        }
        return map;
    }

    /**
     *
     *功能说明: 列表转数组
     *创建人: wjs
     *创建时间:2013-3-14 下午5:37:16
     *@param list
     *@return int[]
     *
     */
    public static int[] toArray(List<Integer> list){
        if(list == null || list.size() < 1)
            return null;
        for(int i=0; i<list.size(); i++){
            if(list.get(i) == null)
                list.remove(i);
        }

        int[] types = new int[list.size()];
        for(int i=0; i<list.size(); i++){
            types[i] = list.get(i);
        }
        return types;
    }

    public static <T> Set<T> toHashSet(Collection<T> c) {
        if (isEmpty(c)) {
            return null;
        }
        Set<T> set = new HashSet<T>(c.size()*2);
        set.addAll(c);
        return set;
    }


}

