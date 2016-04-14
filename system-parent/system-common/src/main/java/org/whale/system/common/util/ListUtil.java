package org.whale.system.common.util;

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

    /**
     * 列表是否不为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
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

    /**
     * list对象转为Array对象
     *
     * @param list
     * @return
     */
    public static String[] toArray(Collection<String> list) {
        if (list == null) {
            return null;
        }

        String[] ts = new String[list.size()];
        list.toArray(ts);
        return ts;
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
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    public static <T> List<T> unique(List<T> c) {
        if (c != null && c.size() > 0) {
            Map<T, Integer> map = new LinkedHashMap<T, Integer>();
            for (T o : c) {
                map.put(o, 0);
            }
            return new ArrayList<T>(map.keySet());
        }
        return c;
    }

    public static <T extends Object> List<String> trim(List<T> list) {
        if (list == null) {
            return null;
        }
        List<String> newList = new ArrayList<String>();
        for (Object o : list) {
            if (o != null) {
                newList.add(String.valueOf(o).trim());
            }
        }
        return newList;
    }

    public static <K, V> Map<K, V> hashMap(K key, V value) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    public static Map hashMap(Object... keyValue ) {
        Map map = new HashMap(keyValue.length);
        for (int i=0; i<keyValue.length; i=i+2){
            map.put(keyValue[i], keyValue[i+1]);
        }
        return map;
    }

    public static <T> Set<T> toHashSet(Collection<T> c) {
        if (isEmpty(c)) {
            return null;
        }
        Set<T> set = new HashSet<T>(c.size()*2);
        set.addAll(c);
        return set;
    }
    
    /**
     * 将多个数组，合并成一个数组。如果这些数组为空，则返回 null
     * 
     * @param arys
     *            数组对象
     * @return 合并后的数组对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[]... arys) {
        Queue<T> list = new LinkedList<T>();
        for (T[] ary : arys)
            if (null != ary)
                for (T e : ary)
                    if (null != e)
                        list.add(e);
        if (list.isEmpty())
            return null;
        Class<T> type = (Class<T>) list.peek().getClass();
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }
    
    /**
     * 将数组内容倒着排序
     * 
     * @param arrays
     */
    public static <T> void reverse(T[] arrays) {
        int size = arrays.length;
        for (int i = 0; i < size; i++) {
            int ih = i;
            int it = size - 1 - i;
            if (ih == it || ih > it) {
                break;
            }
            T ah = arrays[ih];
            T swap = arrays[it];
            arrays[ih] = swap;
            arrays[it] = ah;
        }
    }
    
    /**
     * 较方便的创建一个列表，比如：
     * 
     * <pre>
     * List&lt;Pet&gt; pets = Lang.list(pet1, pet2, pet3);
     * </pre>
     * 
     * 注，这里的 List，是 ArrayList 的实例
     * 
     * @param eles
     *            可变参数
     * @return 列表对象
     */
    public static <T> ArrayList<T> list(T... eles) {
        ArrayList<T> list = new ArrayList<T>(eles.length);
        for (T ele : eles)
            list.add(ele);
        return list;
    }
    
    /**
     * 创建一个 Hash 集合
     * 
     * @param eles
     *            可变参数
     * @return 集合对象
     */
    public static <T> Set<T> set(T... eles) {
        Set<T> set = new HashSet<T>();
        for (T ele : eles)
            set.add(ele);
        return set;
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
            rs.add((E)ReflectionUtil.readField(f, obj, true));
        }
        return rs;
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
}

