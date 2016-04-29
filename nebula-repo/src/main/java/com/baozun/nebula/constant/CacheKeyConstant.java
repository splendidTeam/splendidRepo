package com.baozun.nebula.constant;

/**
 * 保存全局用户的缓存 key
 * 
 * @author Justin Hu
 */
public class CacheKeyConstant{

	/**
	 * 首页kv列表
	 */
	public static final String	GLOBAL_KV_LIST						= "global-kv-list";

	/** 促销列表 --这里需要按照规范修改 */
	public final static String	PROMOTION_LIST_KEY					= "m_promotion_list";

	/**
	 * 获取页面的数据
	 */
	public final static String	CMS_PAGE_KEY						= "MC_CMS_CMSPAGEINSTANCEMANAGER_FINDPUBLISHPAGE";

	/**
	 * 获取板块管理的数据
	 */
	public final static String	COLUMN_KEY							= "MC_COLUMN_COLUMNMANAGER_GETPUBLISHEDPAGEBYCODE";

	/**
	 * 促销活动自定义商品筛选器、自定义会员、自定义条件 缓存key的前缀
	 */
	public final static String	PROMTION_CUSTOMIZE_FILTER_PREFIX	= "PROMTION_CUSTOMIZE_FILTER_PREFIX";

	/** 购物车促销msg **/
	public static final String	SHOPPING_CART_PRM					= "shopping_cart_prm";

	/*** 购物车促销存储时间 **/
	public static final Integer	SHOPPING_CART_PRM_TIME				= 60 * 60 * 24 * 360;
	
	/** bundle(将bundle类商品的id作为key值) */
	public static final String 	BUNDLE_CACHE_KEY					= "BUNDLE";
	
	/** bundle缓存时间 */
	public static final Integer	BUNDLE_CACHE_TIME					= 60 * 60;
	
}
