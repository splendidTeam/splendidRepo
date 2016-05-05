package com.baozun.nebula.sdk.constants;

public class Constants {

	/** 程序返回结果 **/
	public static final Integer	SUCCESS										= 1;

	public static final Integer	FAILURE										= 0;

	/** session当中的 会员信息 key **/
	public static final String	MEMBER_CONTEXT								= "member_context";

	/** engine constants **/
	public static final String	ENGINESKU									= "EngineSku";

	public static final String	ENGINEMEMBER								= "EngineMember";

	public static final String	SHOPPINGCARTSUMMARY							= "ShoppingCartSummary";

	public static final String	ACTIVITY_RES								= "activityRes";

	/** 根据触发点执行活动 **/
	public static final String	PRODUCT_DETAIL_TRIGGER						= "1_PRODUCT_DETAIL";

	public static final String	SHOPPING_CART_TRIGGER						= "3_SHOPPING_CART";

	public static final String	ORDER_CONFIRM_TRIGGER						= "5_ORDER_CONFIRM";

	public static final String	ORDER_FINISH_TRIGGER						= "7_ORDER_FINISH";

	public static final String	USE_CARD_TRIGGER							= "4_USE_CARD";

	public static final String	PRODUCT_LIST_TRIGGER						= "2_PRODUCT_LIST";

	public static final String	BACK_ITEM_LIST_CART_TRIGGER					= "9_BACK_ITEM_LIST";

	public static final String	IMMEDIATELYBUY_SESSION_SHOPCART				= "IMMEDIATELYBUY_SESSION_SHOPCART";

	public static final String	MANUALBUY_SESSION_SHOPCART					= "MANUALBUY_SESSION_SHOPCART";

	public static final String	BACKORDER_SESSION_SHOPCART					= "BACKORDER_SESSION_SHOPCART";
	/**
	 * NativeUpdate 实际操作行数( {0} )跟期待操作行数( {1} )不符
	 */
	public static final Integer	NATIVEUPDATE_ROWCOUNT_NOTEXPECTED			= 10;

	/** 订单号不能为空 **/
	public static final Integer	ORDERCODE_NOT_NULL							= 11001;
	/** 订单不存在 **/
	public static final Integer	ORDER_NOT_EXIST								= 11002;
	/** 该订单已取消 **/
	public static final Integer	ORDER_ALREADY_CANCEL						= 11003;
	/** 该订单已完成 **/
	public static final Integer	ORDER_ALREADY_COMPELETE						= 11004;

	/** 该订单已申请取消 **/
	public static final Integer	ORDER_ALREADY_APPLY_CANCEL					= 11005;

	/** 更新订单失败 **/
	public static final Integer	ORDER_UPDATE_FAILURE						= 11006;

	/** 订单收货人信息更新失败 **/
	public static final Integer	ORDER_CONSIGNEE_UPDATE_FAILURE				= 11007;

	/** 该订单无效 **/
	public static final Integer	ORDER_IS_NOT_VALID							= 11008;

	/** 该优惠券号无效 **/
	public static final Integer	COUPON_IS_NOT_VALID							= 11009;

	/** 该订单已申请退换货 **/
	public static final Integer	ORDER_ALREADY_APPLY_RETURN					= 11010;

	/** 该订单未完成 **/
	public static final Integer	ORDER_NOT_COMPELETE							= 11011;

	/** 购物车为空 **/
	public static final Integer	SHOPCART_IS_NULL							= 11012;

	/** 该订单包含已下架商品 **/
	public static final Integer	THE_ORDER_CONTAINS_NOTVALID_ITEM			= 11013;

	/** 该订单包含库存不足商品 **/
	public static final Integer	THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM	= 11014;

	/** 该订单包含超过限购数量的商品 **/
	public static final Integer	THE_ORDER_CONTAINS_LIMIT_ITEM				= 11015;

	/** 增加库存失败 **/
	public static final Integer	ADD_SKU_INVENTORY_FAILURE					= 11016;

	/** 创建订单失败 **/
	public static final Integer	CREATE_ORDER_FAILURE						= 11017;

	/** 优惠券已经被使用 **/
	public static final Integer	COUPON_IS_USED								= 11018;

	/** 商品未上架 何波 提示信息 **/
	public static final Integer	THE_ORDER_CONTAINS_ITEM_ACTIVEE_TIME		= 11019;

	/** 通过收货地址没有获取到支持的物流方式 **/
	public static final Integer	NOT_FIND_DISTRIBUTIONMODE					= 11020;

	/** 订单或购物车的商品包含已暂停销售的尺寸或颜色 **/
	public static final Integer	CONTAINS_NOTVALID_ITEM						= 11021;

	/** 该商品为赠品, 不可以购买 **/
	public static final Integer	THE_ITEM_IS_GIFT							= 11022;

	/** 该商品尚未上架！ **/
	public static final Integer THE_ITEM_ACTIVE_FAILURE						= 11023;
	
	/** 库存不足 **/
	public static final Integer THE_ITEM_INVENTORY_FAILURE					= 11024;
	
	/** 出生日期大于当前日期异常 */
	public static final Integer	BIRTHDAY_BEFORE_NOW							= 7000;

	public static final Integer	NOCHECKED_CHOOSE_STATE						= 0;

	public static final Integer	CHECKED_CHOOSE_STATE						= 1;

	public static final Integer	SAVE_SHIPPING_CONFIG_FAILURE				= 12001;

	/** 运费模板已经和商品关联 **/
	public static final Integer	PRODUCT_SHIPPING_FEE_CONNECTED				= 12002;

	/** 更新运费模板基本信息失败 **/
	public static final Integer	SHIPPING_TEMEPLATE_UPDATE_FAILURE			= 12003;

	/** 应用商品运费模板失败 **/
	public static final Integer	Apply_PRODUCT_TEMEPLATE_FAILURE				= 12004;

	public static final Integer	ADDRESS_NOT_EXISTS							= 12005;

	/** 引擎检查失败返回错误码 **/
	public static final Integer	CHECK_VALID_FAILURE							= 10;

	public static final Integer	CHECK_LIMIT_FAILURE							= 11;

	// 库存不足
	public static final Integer	CHECK_INVENTORY_FAILURE						= 12;
	// 该商品尚未上架！ 何波
	public static final Integer	CHECK_ITEM_ACTIVE_FAILURE					= 13;
	// sku停止变更过，或者停止销售
	public static final Integer	CHECK_ITEM_SKU_FAILURE						= 14;
	// 该商品是赠品, 不可以购买
	public static final Integer	CHECK_ITEM_TYPE_GIFT						= 15;

	// ******************
	// 商品排序管理*********************************************************************
	/** 造作商品不存在 */
	public static final Integer	ITEM_SORT_NOTEXIST							= 6200;
	/** 没有排序更前的商品 */
	public static final Integer	ITEM_SORT_NOHIGHER							= 6201;
	/** 没有排序更后的商品 */
	public static final Integer	ITEM_SORT_NOLOWER							= 6202;
	/** 商品已经排序 */
	public static final Integer	ITEM_SORT_SORTED							= 6203;
	/** 商品已经移除排序 */
	public static final Integer	ITEM_SORT_UNSORTED							= 6204;

	/** 校验邮箱失败 **/
	public static final int		VALIDEMAIL_ERROR							= 90003;

	/** 绑定邮箱失败 **/
	public static final int		BINDEMAIL_ERROR								= 90004;

	/** 原密码错误 **/
	public static final int		OLDPASSWORD_ISWRONG_ERROR					= 90005;

	/** 新密码不能与原密码相同 **/
	public static final int		OLDPASSWORD_ASSAMEAS_NEWPASSWORD_ERROR		= 90006;

	/** 新密码与重复密码不匹配 **/
	public static final int		RENEWPASSWORD_NOTSAMEAS_NEWPASSWORD_ERROR	= 90007;

	/** 游客保存在cookie当中的购物车 **/
	public static final String	GUEST_COOKIE_GC								= "g_c_s";

	/** 游客保存在cookie当中的购物车商品总数量 **/
	public static final String	GUEST_COOKIE_GC_CNT							= "g_c_s_cnt";

	/** 找不到用户. */
	public static final Integer	USER_USER_NOTFOUND							= 1002;

	/** session已过期 */
	public static final Integer	SESSION_EXPIRE_TIME							= 1003;

	/**************************************** 促销 ****************************************/
	/** 该促销不存在 */
	public static final Integer	PROMOTION_INEXISTENCE						= 13000;
	/** 商品筛选器中的商品不存在 */
	public static final Integer	PRODUCT_FILTER_SKU_INEXISTENCE				= 13013;
	/** 促销取消失败：促销已取消 */
	public static final Integer	PROMOTION_NOT_ACTIVATED						= 13003;
	/** 促销启用失败：促销已启用 */
	public static final Integer	PROMOTION_ALREADY_ACTIVATED					= 13001;
	/** 信息不完整 */
	public static final Integer	PROMOTION_INCOMPLETE_INFORMATION			= 13002;
	/** 现在没有促销活动在运行 */
	public static final Integer	PROMOTION_NOT_RUN							= 13004;
	/** 商品筛选器解析错误 */
	public static final Integer	PRODUCT_FILTER_ERROR						= 13015;
	/** 优先级数据发生变化 ***/
	public static final Integer	PRODUCT_PRIORITYADJUST_ERROR				= 13016;
	/** 优惠券导入：EXCEL文件读取错误 ***/
	public static final Integer	PROMOTION_COUPON_EXCEL_READ_ERROR			= 13017;
	/** 优惠券导入：该优惠券不存在 ***/
	public static final Integer	PROMOTION_COUPON_CODE_INEXISTENCE			= 13018;
	/** 优先级：该优先级不存在 ***/
	public static final Integer	PROMOTION_PRIORITY_INEXISTENCE				= 13019;
	/** 优先级：该优先级状态错误 ***/
	public static final Integer	PROMOTION_PRIORITY_STATUS_ERROR				= 13020;
	/** 优先级：该优先级时间范围错误 ***/
	public static final Integer	PROMOTION_PRIORITY_TIME_ERROR				= 13021;
	/** 优先级：该优先级时间冲突 ***/
	public static final Integer	PROMOTION_PRIORITY_CONFILCTING_TIME			= 13022;
	/** 促销：状态错误 ***/
	public static final Integer	PROMOTION_LIFECYCLE_ERROR					= 13023;
	/** 促销：启用失败，开始时间必须晚于当前时间 ***/
	public static final Integer	PROMOTION_ACTIVATION_LATE					= 13024;

	/********************************************* 筛选器 *********************************************/
	/** 名称重复 */
	public static final Integer	MEMBER_CUSTOM_GROUP_REPEATED_NAME			= 13200;
	/** 表达式错误 */
	public static final Integer	MEMBER_CUSTOM_GROUP_ERROR_EXPRESSION		= 13201;
	/** 会员筛选器不存在 */
	public static final Integer	MEMBER_CUSTOM_GROUP_INEXISTED				= 13202;
	/** 商品筛选器不存在 */
	public static final Integer	PRODUCT_FILTER_INEXISTED					= 13203;

	/**************************************** 限购 ****************************************/
	/** 限购：不存在 */
	public static final Integer	LIMIT_INEXISTED								= 13300;
	/** 限购：信息不完整 */
	public static final Integer	LIMIT_INCOMPLETE_INFO						= 13301;
	/** 限购：生命周期错误 */
	public static final Integer	LIMIT_LIFECYCLE_ERROR						= 13302;
	/** 限购取消失败：限购已取消 */
	public static final Integer	LIMIT_NOT_ACTIVATED							= 13303;

	/** 商品限购数量 **/
	public static final Integer	LIMIT_NUM									= 10;

	// 主卖品
	public static final Integer	ITEM_TYPE_SALE								= 1;

	// 赠品
	public static final Integer	ITEM_TYPE_PREMIUMS							= 0;

	/** 商品上架状态 (有效) **/
	public static final String	ITEM_ADDED_VALID_STATUS						= "1";

	public static final String	CHARSET										= "UTF-8";

	/********************************************* 版块管理 *********************************************/
	/** 版块组件：排序列表错误 */
	public static final Integer	COLUMN_COMPONENT_SORT_ERROR					= 20001;
	/** 版块组件：该组件不存在 */
	public static final Integer	COLUMN_COMPONENT_INEXISTENCE				= 20002;
	/** 版块组件：该模块不存在 */
	public static final Integer	COLUMN_MODULE_INEXISTENCE					= 20003;
	/** 版块组件：扩展字段格式错误 */
	public static final Integer	COLUMN_COMPONENT_EXT_ERROR					= 20004;
	/** 推荐商品：推荐商品总数必须为11个 */
	public static final Integer	RECOMMEND_ITEM_PUBLISH_SIZE_ERROR			= 20005;

	/********************************************* 物流运费模板 *********************************************/
	/** 名称重复 */
	public static final Integer	DISTRIBUTION_REPEATED_NAME					= 23001;

	/*********************************************** 支付start *************************/
	public static final int		GET_URL_AFTER_TYPE							= 1;

	public static final int		DO_RETURN_AFTER_TYPE						= 2;

	public static final int		DO_NOTIFY_AFTER_TYPE						= 3;

	public static final int		CLOSE_AFTER_TYPE							= 4;

	public static final String	PAY_LOG_CALL_BEFORE_MESSAGE					= "调用获取支付链接接口之前";

	public static final String	PAY_LOG_CALL_AFTER_MESSAGE					= "调用获取支付链接接口之后";

	public static final String	PAY_LOG_RETURN_BEFORE_MESSAGE				= "获取同步支付通知之前";

	public static final String	PAY_LOG_RETURN_AFTER_MESSAGE				= "获取同步支付通知之后";

	public static final String	PAY_LOG_NOTIFY_BEFORE_MESSAGE				= "获取异步支付通知之前";

	public static final String	PAY_LOG_NOTIFY_AFTER_MESSAGE				= "获取异步支付通知之后";

	public static final String	PAY_LOG_CLOSE_PAYMENT_BEFORE_MESSAGE		= "关闭交易之前";

	public static final String	PAY_LOG_CLOSE_PAYMENT_AFTER_MESSAGE			= "关闭交易之后";
	/*********************************************** 支付end ****************/

	/********************************************** 默认排序设置start ******************/
	/* 默认排序设置类型： 分类 */
	public static final Integer	SORT_SCORE_TYPE_CATEGORY					= 1;
	/* 默认排序设置类型： 属性 */
	public static final Integer	SORT_SCORE_TYPE_PROPETY						= 2;
	/* 默认排序设置类型： 吊牌价 */
	public static final Integer	SORT_SCORE_TYPE_PRICE						= 3;
	/* 默认排序设置类型： 销量 */
	public static final Integer	SORT_SCORE_TYPE_SALES_VOLUME				= 4;
	/* 默认排序设置类型： 搜藏 */
	public static final Integer	SORT_SCORE_TYPE_COLLECTIONS					= 5;
	/* 默认排序设置类型：销售价 */
	public static final Integer	SORT_SCORE_TYPE_SALES_PRICE					= 6;

	/* 大于等于 */
	public static final String	SORT_SCORE_OPER_GREATER						= "GREATER";
	/* 小于等于 */
	public static final String	SORT_SCORE_OPER_LESS						= "LESS";
	/* 等于 */
	public static final String	SORT_SCORE_OPER_EQUAL						= "EQUAL";
	/********************************************** 默认排序设置end ******************/
	
	/** ITEM_COLOR_VALUE_REFERENCE: 商品色和色值对照 数据*/
	public static final String   ITEM_COLOR_VALUE_REFERENCE = "ITEM_COLOR_VALUE_REFERENCE";
	/**cache字段标识 */
	public static final String	BRAND_ITEM_COLOR_VALUE_REFERENCE = "BRAND_ITEM_COLOR_VALUE_REFERENCE";
	
	/**商品的属性值*/
	public static final String ITEM_COLOR="ITEM_COLOR";
	
	/**商品的属性值*/
	public static final String FILTER_COLOR="FILTER_COLOR";
	
	/** qs队列名 前缀 */
	public static final String	QS_ORDER_QUEUE								= "CACHE_QS_LIST";

	/**qs 用户购买队列id 前缀 */
	public static final String  QS_SALE_ORDER_QID       = "qs_qid";
	
	/**qs 用户购买队列id 记录用户排队状态*/
	public static final String  QS_SALE_ORDER_RID       = "qs_rid";
	
	/** qs相关缓存时间 **/
	public static final int		EXPIRE_TIME									= 1800;
	
	/** qs 消息临时存储*/
	public static final String	QS_ORDER_TEMP_POOL					        = "CACHE_QS_LIST";
	
	/********************************************** 购物车  start****************** /
	/** 购物车商品数量不能小于1*/
	public static final String SHOPPINGCART_ADD_OR_UPDATE_QUANTITY_LESSTHANONE="30001";
	/** 该商品不存在*/
	public static final String SHOPPINGCART_ADD_OR_UPDATE_SKU_NOTEXIST="30002";
	/** 一次性最多可购买30款商品*/
	public static final String SHOPPINGCART_ADD_QUANTITY_TOO_LARGE	=	"30003";
	/** 操作失败*/
	public static final String SHOPPINGCART_DO_OPREATE_FAIL	=	"30004";
	/** 購物車不能為空*/
	public static final String SHOPPINGCART_EMPTY	=	"30005";
	/** [{0}] 已下架*/
	public static final String SHOPPINGCART_SKU_ONLINE	=	"30006";
	/** [{0}] 庫存不足*/
	public static final String SHOPPINGCART_SKU_UNDERSTOCK	=	"30007";
	/********************************************** 购物车  end******************/
}
