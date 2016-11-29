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
	public final static String CMS_PAGE_KEY="MC_CMS_CMSPAGEINSTANCEMANAGER_FINDPUBLISHPAGE";
	
	/**
	 * 获取页面版本数据的数据
	 */
	public final static String CMS_PAGE_VERSION_KEY="MC_CMS_CMSPAGEINSTANCEVERSIONMANAGER_FINDPUBLISHPAGE";
	
	/**
	 * 获取页面模块的数据
	 */
	public final static String CMS_MODULE_KEY="MC_CMS_CMSMODULEINSTANCEMANAGER_FINDPUBLISHMODULE";
	
	/**
	 * 获取页面模块版本的数据
	 */
	public final static String CMS_MODULE_VERSION_KEY="MC_CMS_CMSMODULEINSTANCEVERSIONMANAGER_FINDPUBLISHMODULE";
	
	/**
	 * 获取板块管理的数据
	 */
	public final static String COLUMN_KEY = "MC_COLUMN_COLUMNMANAGER_GETPUBLISHEDPAGEBYCODE";
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
	
	/**
	 *  navigation filter 中导航 map cache key
	 *  @see nebula helix com.baozun.nebula.manager.navigation.NavigationHelperManagerImpl.getAllNavigationMap()
	 */
	public static final  String	CACHE_KEY_FILTER_NAV = "cache_key_filter_nav";
	
	/**
	 *  navigation filter 中导航 map cache field
	 *  @see nebula helix com.baozun.nebula.manager.navigation.NavigationHelperManagerImpl.getAllNavigationMap()
	 */
	public static final  String	CACHE_FIELD_FILTER_NAV = "cache_field_filter_nav";
	
	/**
	 *  RecommandItem 商品推荐模块 中缓存 cache key
	 *  @see nebula protostar com.baozun.nebula.manager.product.RecommandItemManagerImpl.removeRecommandItemCache(Integer type, Long param)
	 */
	public static final  String	CACHE_KEY_RECOMMANDITEM = "cache_field_recommand_item";
	
	/**
	 * 导航元数据在缓存中的key，完整的key还要加上语言
	 * @see com.baozun.nebula.search.FacetFilterHelperImpl.loadFacetFilterMetaData()
	 */
	public final static String			NAVIGATIONMETACACHEKEY		= "navigationMetaCacheKey_";
	
	/**
	 * 基于搜索导航的搜索条件缓存
	 */
	public final static String				CONDITION_NAV_CACHEKEY		= "CONDITION_NAV_CACHEKEY_";
	
	
	/** 分类元数据在缓存中的key，完整的key还要加上语言 */
	public final static String			categoryMetaCacheKey			= "categoryMetaCacheKey_";

	/** 属性元数据在缓存中的key，完整的key还要加上语言 */
	public final static String			propertyMetaCacheKey			= "propertyMetaCacheKey_";

	/** 属性值元数据在缓存中的key，完整的key还要加上语言 */
	public final static String			propertyValueMetaCacheKey		= "propertyValueMetaCacheKey_";

	/** 搜索条件元数据在缓存中的key，完整的key还要加上语言 */
	public final static String			searchConditionMetaCacheKey		= "searchConditionMetaCacheKey_";
	
	/** 公共推荐缓存key */
	public final static String CACHE_KEY_SHOP_RECOMMAND_ITEM_LIST = "recommandItemListCacheKey_";

}
