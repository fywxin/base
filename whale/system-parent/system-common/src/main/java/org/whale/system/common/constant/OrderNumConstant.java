package org.whale.system.common.constant;

/**
 * 初始化顺序登记类
 *
 * @author wjs
 * @date 2015年1月6日 上午9:58:34
 */
public class OrderNumConstant {

	/**ORM 容器初始化顺序 @InitOrmCache */
	public static int ORM_INIT_ORDER = 0;
	
	/**ORM 容器初始化顺序 @DictCacheService */
	public static int DICT_CACHE_INIT_ORDER = 50;
	
	/**ORM 容器初始化顺序  @UserAuthCacheService */
	public static int AUTH_CACHE_INIT_ORDER = 100;
	
	/**ORM 容器初始化顺序  @AuthBeansCreator */
	public static int AUTH_BEAN_INIT_ORDER = 150;
}
