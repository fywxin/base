package org.whale.system.base;

/**
 * 基础增删改查时间监听接口
 *
 * @author wjs
 * 2014年9月6日-下午2:32:09
 */
public interface BaseCrudEvent<T> {
	
	/**新增前事件编码*/
	public final static int BEFORE_SAVE = 1;
	/**新增后事件编码*/
	public final static int AFTER_SAVE = 2;
	
	/**修改前事件编码*/
	public final static int BEFORE_UPDATE = 3;
	/**修改后事件编码*/
	public final static int AFTER_UPDATE = 4;
	
	/**删除前事件编码*/
	public final static int BEFORE_DEL = 5;
	/**删除后事件编码*/
	public final static int AFTER_DEL = 6;

	/**
     * 在保存之前调用这个方法
     * @param t 用户对象
     */
    void onBeforeCreate(T t);
    
    /**
     * 在保存之后调用这个方法
     * @param t 用户对象
     */
    void onAfterCreate(T t);

    /**
     * 在更改之前调用这个方法
     * @param t 用户对象
     */
    void onBeforeUpdate(T t);

    /**
     * 在更改之后调用这个方法
     * @param t 用户对象
     */
    void onAfterUpdate(T t);

    /**
     * 在删除之前调用这个方法
     * @param t 用户对象
     */
    void onBeforeDelete(T t);

    /**
     * 在删除之后调用这个方法
     * @param t 用户对象
     */
    void onAfterDelete(T t);
}
