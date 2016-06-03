/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.exception;

/**
 * 自定义错误码,see businessException.properties
 * 
 * @author dianchao.song
 */
public interface ErrorCodes {

	/** The Constant BUSINESS_EXCEPTION_PREFIX. */
	public static final String	BUSINESS_EXCEPTION_PREFIX					= "business_exception_";

	/** 系统错误. */
	public static final Integer	SYSTEM_ERROR								= 1;

	/** 无权访问. */
	public static final Integer	ACCESS_DENIED								= 2;

	/** session无效. */
	public static final Integer	INVALID_SESSION								= 3;

	/** 数据错误. */
	public static final Integer	DATA_ERROR									= 4;

	/**
	 * NativeUpdate 实际操作行数( {0} )跟期待操作行数( {1} )不符
	 */
	public static final Integer	NATIVEUPDATE_ROWCOUNT_NOTEXPECTED			= 10;

	/**
	 * 操作与期待操作行数不一致
	 */
	public static final Integer	ROWCOUNT_NOTEXPECTED						= 12;

	/**
	 * 名称已重复
	 */
	public static final Integer	NAME_EXISTS									= 13;

	/**
	 * 路径不合法
	 */
	public static final Integer	PATH_NOT_MATCH								= 14;
	// *******************auth
	// 1000区间**********************************************************************************

	/** 用户名已存在. */
	public static final Integer	USER_USERNAME_EXISTS						= 1001;

	/** 找不到用户. */
	public static final Integer	USER_USER_NOTFOUND							= 1002;

	/** 必须确保有效的用户名不允许重复. */
	public static final Integer	USER_EFFECT_USERNAME_EXISTS					= 1003;

	// *******************baseinfo
	// 2000区间*********************************************************************************
	/** 启用或禁用店铺失败 */
	public static final Integer	SHOP_ENABLE_DISABLE_FAIL					= 2001;

	/** 店铺的属性名重复 */
	public static final Integer	SHOP_PROPERTY_NAME_REPEAT					= 2002;

	/** 页面编码重复 */
	public static final Integer	PAGETEMPLATE_PAGECODE_REPEAT				= 2010;

	// ****************** logs
	// 3000区间********************************************

	// ****************** member
	// 3000区间******************************************

	// ****************** product
	// 3000区间*****************************************

	/** 启用属性失败 */
	public static final Integer	PRODUCT_ENABLE_PROPERTY_FAIL				= 6002;

	/** 禁用属性失败 */
	public static final Integer	PRODUCT_DISABLE_PROPERTY_FAIL				= 6003;

	/** 店铺增加失败 */
	public static final Integer	PRODUCT_ADD_SHOP_FAIL						= 6010;

	/** 店铺修改失败 */
	public static final Integer	PRODUCT_UPDATE_SHOP_FAIL					= 6011;

	/** 系统中已经存在该编码<code>{@value}</code>. */
	public static final Integer	PRODUCT_CATEGORY_CODE_REPEAT				= 6020;

	/** {0} 分类下,存在名字 {1} 的分类 <code>{@value}</code>. */
	public static final Integer	PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY	= 6021;
	
	public static final Integer	PRODUCT_CODE_REPEAT							= 6020;

	/** 店铺修改失败 */
	public static final Integer	PRODUCT_PROPERTY_NAME_REPEAT				= 6022;

	public static final Integer	PRODUCT_ADD_ORGANIZATION_SANME_CODE			= 6012;

	public static final Integer	PRODUCT_SET_SHOP_FAIL						= 6013;

	/** 删除行业失败 */
	public static final Integer	INDUSTRY_REMOVE_FAIL						= 6100;

	/** 属性排序失败 */
	public static final Integer	PROPERTY_SORT_FAIL							= 6101;
	/** 属性修改失败 */
	public static final Integer	PROPERTY_SAVE_FAIL							= 6102;
	/** 行业修改失败 */
	public static final Integer	INDUSTRY_SAVE_FAIL							= 6103;
	/** 行业不可用 */
	public static final Integer	INDUSTRY_NOT_AVAILABLE						= 6104;
	/** 行业不可存在 */
	public static final Integer	INDUSTRY_NOT_EXISTS							= 6105;

	/** 属性启用失败 */
	public static final Integer	PRODUCT_PROPERTY__ENABLE_FAIL				= 6023;
	/** 属性禁用失败 */
	public static final Integer	PRODUCT_PROPERTY_DISABLED_FAIL				= 6024;
	/** 属性删除失败 */
	public static final Integer	PRODUCT_PROPERTY_DELETION_FAIL				= 6025;
	
	
	/** {0} 导航下,已存在名字 {1} 的导航 <code>{@value}</code>. */
	public static final Integer	SYSTEM_NAVIGATION_PARENT_EXIST_NAME			= 6026;
	
	/** 父级导航 {0} 已不存在<code>{@value}</code>. */
	public static final Integer	SYSTEM_NAVIGATION_PARENT_NO_EXISTS			= 6027;
	
	/** 属性被商品引用删除失败  */
	public static final Integer	PRODUCT_PROPERTY_DELETION_QUOTE				= 6028;
	/** 属性被商品引用禁用失败 */
	public static final Integer	PRODUCT_PROPERTY_DISABLED_QUOTE				= 6029;
	

	// ****************** cms
	// 3000区间*********************************************************************

	/** 路径已存在 */
	public static final Integer	CMS_PATH_EXISTS								= 3001;

	/**
	 * 模板定义不存在
	 */
	public static final Integer	DEFTEMPLATE_NO_EXIST						= 3002;

	/** 地址保存或更新出错 */
	public static final Integer	MEMBER_CONTACT_SAVE_OR_UPDATE				= 3010;
	/** 地址删除出错 */
	public static final Integer	MEMBER_CONTACT_DELETE						= 3011;
	/** 默认地址修改出错 */
	public static final Integer	MEMBER_CONTACT_SET_DEFAULT					= 3012;

	/** 只有购买了该商品才可以评论 **/
	public static final Integer	MEMBER_ITEMRATE_ORDERLINECOMPLETION			= 3013;

	/** 最多保存10个有效地址 */
	public static final Integer	MEMBER_CONTACT_MAX_NUM						= 3014;

	/** 页面修改失败 */
	public static final Integer	PAGE_UPDATE_FAIL							= 3101;
	/** 页面新增失败 */
	public static final Integer	PAGE_ADD_FAIL								= 3102;
	/** 页面不存在 */
	public static final Integer	PAGE_NO_EXIST								= 3103;
	/**
	 * 参数错误
	 */
	public static final Integer	PAGE_PARAMS_ERROR							= 3104;
	/**
	 * 模板定义不存在
	 */
	public static final Integer	PAGETEMPLATE_NO_EXIST						= 3105;
	/**
	 * 结束时间必须大于开始时间
	 */
	public static final Integer	TIME_ERROR									= 3106;
	/**
	 * 日期格式必须为yyyy-MM-dd HH:mm:ss
	 */
	public static final Integer	DATE_FORMAT_FAIL							= 3107;
	/**
	 * 资源路径不存在
	 */
	public static final Integer	PATH_NOT_EXIST								= 3108;

	/**
	 * component或者area的解析失败
	 */
	public static final Integer	PARSE_FAIL									= 3109;

	/** 组件修改失败 */
	public static final Integer	COMPONENT_UPDATE_FAIL						= 3201;

	/** 组件新增失败 */
	public static final Integer	COMPONENT_ADD_FAIL							= 3202;

	/** 组件不存在 */
	public static final Integer	COMPONENT_NO_EXIST							= 3203;
	/**
	 * 参数错误
	 */
	public static final Integer	COMPONENT_PARAMS_ERROR						= 3204;

	/**
	 * 模板定义不存在
	 */
	public static final Integer	COMPONENT_TEMPLATE_NO_EXIST					= 3205;

	/**
	 * 实例模板不存在
	 */
	public static final Integer	INSTANCE_TEMPLATE_NO_EXIST					= 3206;
	/**
	 * 实例状态必须为已发布状态
	 */
	public static final Integer	INSTANCE_STATUS_PUBLISHED					= 3300;
	/**
	 * 实例状态必须为未修改状态
	 */
	public static final Integer	INSTANCE_STATUS_NOT_MODIFIED				= 3301;

	/** 同步实例失败 */
	public static final Integer	CMS_SYNC_INSTANCE_FAIL						= 3401;

	/** 同步资源失败 */
	public static final Integer	CMS_SYNC_INSTANCE_RESOURCE_FAIL				= 3402;

	/** 同步公共资源失败 */
	public static final Integer	CMS_SYNC_COMMON_RESOURCE_FAIL				= 3403;

	/** 商品不存在 */
	public static final Integer	ITEM_NOT_EXIST								= 3404;

	// *********************** item image batch import
	// ****************************
	public static final Integer	IMPORT_ITEM_CODE_NOT_EXISTS					= 3405;

	public static final Integer	IMPORT_FILE_NOT_TRUE						= 3406;

	public static final Integer	IMPORT_FILE_NAME_NOT_TRUE_DIR				= 3407;

	public static final Integer	IMPORT_FILE_NAME_NOT_TRUE					= 3408;

	public static final Integer	IMPORT_FILE_COLOR_PROP_NOT_EXIST			= 3409;

	public static final Integer	IMPORT_FILE_IMAGE_TYPE_NOT_EXIST			= 3410;
	
	public static final Integer	IMPORT_IMAGE_FILE_ROLE_NOT_EXIST			= 3411;
	
	
	// *********************** item image manager *****************************
	/** 商品图片类型不存在 */
	public static final Integer	ITEM_IMAGE_TYPE_NOT_EXISTS					= 3412;
	
	/** 无法定位颜色*/
	public static final Integer	IMPORT_IMAGE_CAN_NOT_FIX_COLOR				= 3413;

	// ****************** sales
	// 3000区间*********************************************************************

	// ****************** system
	// 3000区间*********************************************************************
	/** 区域不存在 **/
	public static final Integer	AREA_NOT_EXSIST								= 7001;

	/** 参数错误 **/
	public static final Integer	AREA_PARAMS_ERROR							= 7002;

	/** 更新模版文件失败 **/
	public static final Integer	UPDATE_REPO_FILE_FAILURE					= 7003;

	/** 区域模版定义不存在 **/
	public static final Integer	AREADEFINITION_NOT_EXSIST					= 7004;

	/** DIALOG不存在 **/
	public static final Integer	DIALOG_NOT_EXSIST							= 7005;

	/** 创建area失败 **/
	public static final Integer	AREA_CREATE_FAILURE							= 7006;

	/** 创建css失败 **/
	public static final Integer	CSS_CREATE_FAILURE							= 7007;

	/** 创建script失败 **/
	public static final Integer	SCRIPT_CREATE_FAILURE						= 7008;

	/** 更新AREA失败 **/
	public static final Integer	AREA_UPDATE_FAILURE							= 7009;

	/** 模版文件不存在 **/
	public static final Integer	AREA_TEMPLATE_FILE_NOT_EXIST				= 7013;

	/** 区域已经存在 **/
	public static final Integer	AREA_EXIST									= 7020;

	/** 区域的父节点不存在 **/
	public static final Integer	AREA_PARENT_NOT_EXIST						= 7021;

	/** 区域文件解析错误 **/
	public static final Integer	AREA_PARSER_ERROR							= 7022;

	/** 仓库路径不存在 **/
	public static final Integer	REPO_PATH_NOT_EXIST							= 7023;

	/** 目标结点已经存在 **/

	public static final Integer	NODE_EXISTS									= 7024;

	/** 复制失败 **/

	public static final Integer	COPY_FAILURE								= 7025;
	/** 发布失败 */
	public static final Integer	PUBLISH_FAILURE								= 7026;
	/** 备份失败 */
	public static final Integer	BACKUP_FAILURE								= 7027;

	/** 创建css、js失败 **/
	public static final Integer	CSS_JS_CREATE_FAILURE						= 7028;
	/** 删除失败 */
	public static final Integer	DELETE_FAILURE								= 7029;

	/** 文件读取失败 */
	public static final Integer	FILE_READ_FAIL								= 7030;

	/** 读取/写入.xml失败 */
	public static final Integer	XML_READORWRITE_FAIL						= 7031;

	/** 文件创建失败 */
	public static final Integer	FILE_CREATE_FAIL							= 7032;

	/** 文件压缩失败 */
	public static final Integer	ZIP_FAIL									= 7033;

	/** 失败 */
	public static final Integer	EXPORT_FAIL									= 7034;

	/** DIALOG路径不存在或者为空 **/
	public static final Integer	DIALOG_IS_BLANK								= 7035;

	/**
	 * 文件后缀错误
	 */
	public static final Integer	FILE_SUFFIX_ERROR							= 9001;

	/**
	 * 请选择上传文件
	 */
	public static final Integer	PLEASE_CHOOSE_FILE							= 9002;

	/**
	 * 模版安装失败
	 */
	public static final Integer	INSTALL_DEFINITION_ERROR					= 9003;

	/**
	 * 模版配置文件解析失败
	 */
	public static final Integer	PARSE_XML_FILE_ERROR						= 9004;

	/**
	 * 模版路径错误
	 */
	public static final Integer	TEMPLATE_PATH_ERROR							= 9005;

	/**
	 * 该模版已存在
	 */
	public static final Integer	TEMPLATE_EXISTS								= 9006;

	/**
	 * 属性节点不存在
	 */
	public static final Integer	PROPERTY_NOT_EXIST							= 9021;

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

	// ******************
	// 物流方面12000区间*********************************************************************
	public static final Integer	SAVE_SHIPPING_CONFIG_FAILURE				= 12001;

	/**************************************** 促销 ****************************************/
	/** 该促销不存在 */
	public static final Integer	PROMOTION_INEXISTENCE						= 13000;
	/** 促销启用失败：促销已启用 */
	public static final Integer	PROMOTION_ALREADY_ACTIVATED					= 13001;
	/** 信息不完整 */
	public static final Integer	PROMOTION_INCOMPLETE_INFORMATION			= 13002;
	/** 促销取消失败：促销已取消 */
	public static final Integer	PROMOTION_NOT_ACTIVATED						= 13003;
	/** 促销修改失败：有效期内禁止修改除了起始时间与结束时间以外的信息 */
	public static final Integer	PROMOTION_EDIT_DURING_EFFECTIVE				= 13004;
	/** 该促销步骤不存在 */
	public static final Integer	PROMOTION_STEP_INEXISTENCE					= 13005;
	/** 促销修改失败：促销已取消 */
	public static final Integer	PROMOTION_UPDATE_ALREADY_CANCELED			= 13006;
	/** 促销启用失败：商品不存在 */
	public static final Integer	PROMOTION_NO_SKU							= 13007;
	/** 促销启用失败：优惠价大于原价 */
	public static final Integer	PROMOTION_WRONG_PRICE						= 13008;
	/** 促销复制失败：待启用无法复制 */
	public static final Integer	PROMOTION_ILLEAGAL_COPY						= 13009;
	/** 重复的促销名称 */
	public static final Integer	PROMOTION_REPEATED_NAME						= 13010;
	/** 会员筛选器不存在 */
	public static final Integer	MEMBER_FILTER_INEXISTENCE					= 13011;
	/** 商品筛选器不存在 */
	public static final Integer	PRODUCT_FILTER_INEXISTENCE					= 13012;
	/** 商品筛选器中的商品不存在 */
	public static final Integer	PRODUCT_FILTER_SKU_INEXISTENCE				= 13013;
	/** 促销条件表达式错误 */
	public static final Integer	PROMOTION_CONDITION_EXPRESSION_ERROR		= 13014;
	/** 商品筛选器解析错误 */
	public static final Integer	PRODUCT_FILTER_ERROR						= 13015;

	/** 启用活动该活动以过期 */
	public static final Integer	PRODUCT_FILTER_STARTIME_FAIL				= 13016;
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
	/**************************************** 促销优先级 ****************************************/
	/** 该促销默认优先级不存在 */
	public static final Integer	PROMOTION_DEFAULT_INEXISTENCE				= 13100;

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

	/********************** 优惠卷 *********************/

	/** 数据不存在 */
	public static final Integer	COUPON_DATE_ISNULL							= 14000;
	/*** 文件大小超过限制5M */
	public static final Integer	COUPON_MAX_EXCELSIZE						= 14001;
	/*** 文件类型仅限Excel */
	public static final Integer	COUPON_EXCEL_TYPE							= 14002;

	/** 禁用或启用优惠卷必须是未使用的 */
	public static final Integer	COUPON_DISABLEORENABLE_UNUSED				= 14005;

	/****** solr ****/
	/** solr 刷新异常 **/
	public static final Integer	SOLR_REFRESH_ERROR							= 15001;

	/** 单元格错误 **/
	public static final Integer	CELL_ERROR									= 16001;
	/** 店铺不匹配 **/
	public static final Integer	SHOP_NOT_MATCH_ERROR						= 16002;
	/** 属性列不匹配 **/
	public static final Integer	PROP_NOT_MATCH_ERROR						= 16003;
	/** upc重复 **/
	public static final Integer	UPC_REPEAT_ERROR							= 16004;
	/** 行业不匹配 **/
	public static final Integer	INDUSTRY_NOT_MATCH_ERROR					= 16005;

	/** 首页版块-组件 发布时间 **/
	public static final Integer	COLUMN_PUBLISH_TIME							= 16006;

	/** 版块 不存在 */
	public static final Integer	MODULE_NOT_EXIST							= 16007;

	/** 分类不存在 */
	public static final Integer	PRODUCT_CATEGORY_NOT_EXIST					= 16008;

	/** 页面 不存在 */
	public static final Integer	COLUMN_PAGE_NOT_EXIST						= 16009;

	/** 商品编码重复 */
	public static final Integer	ITEMCODE_REPEAT_ERROR						= 16010;
	
	/** 商品分类不是叶子节点 */
	public static final Integer	PRODUCT_CATEGORY_NOT_LEAFNODE				= 16011;
	
	/** 商品编码重复（excel中） */
	public static final Integer	ITEMCODE_REPEAT_ERROR_EXCEL					= 16012;

	/********************************************* 筛选条件管理 ******************************************/

	/** 筛选条件不存在 */
	public static final Integer	SEARCH_CODITION_NOT_EXIST					= 17001;
	/** 删除筛选条件错误 */
	public static final Integer	SEARCH_CODITION_DELETE_FAIL					= 17002;
	/** 筛选条件选项不存在 */
	public static final Integer	SEARCH_CODITION_ITEM_NOT_EXIST				= 17003;

	/********************************************* 索引管理 ******************************************/
	/** 索引删除失败 */
	public static final Integer	SOLR_SETTING_DELETE_FAIL					= 18001;
	/** 索引更新失败 */
	public static final Integer	SOLR_SETTING_UPDATE_FAIL					= 18002;

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

	/********************************************* 推荐商品管理 *********************************************/
	/** 推荐的商品编码不存在 */
	public static final Integer	RECOMMEND_ITEM_CODE_NOT_EXISTS				= 20006;

	/****************************************** 定时任务管理 *********************************************/
	/** 定时任务管理：任务不存在 */
	public static final Integer	TASK_NOT_EXIST								= 21001;

	/****************************************** cms *********************************************/
	/** 页面编辑区域的code不存在 */
	public static final Integer	EDIT_AREA_CODE_NOT_EMPTY					= 22001;
	/** 模板页面不存在 */
	public static final Integer	PAGE_NOT_EXITES								= 22002;
	/** 页面实例编码已存在 */
	public static final Integer	PAGE_CODE_EXITES							= 22003;
	/** 页面 实例url已存在 */
	public static final Integer	PAGE_URL_EXITES								= 22004;

	/****************************************** 物流运费模板 *********************************************/
	/** 名称重复 */
	public static final Integer	DISTRIBUTION_REPEATED_NAME					= 23001;
	/** 导入运费表error 物流方式不存在 */
	public static final Integer	IMPORT_FEE_DISTRIBUTION_IS_NO				= 23002;
	/** 导入运费表error 同一物流同一地址有重复 */
	public static final Integer	IMPORT_FEE_DISTRIBUTION_AREA_SAME			= 23003;
	/** 导入运费表error 目的地不存在 */
	public static final Integer	IMPORT_FEE_DEST_AREA_NOT_EXIST				= 23004;

	/** 导入支持区域error 类型错误 */
	public static final Integer	IMPORT_AREA_TYPE_ERROR						= 23005;
	/** 导入支持区域error 黑名单没有所属白名单 */
	public static final Integer	IMPORT_AREA_GROUPNO_IS_NULL					= 23006;
	/** 导入支持区域error 区域数据重复 */
	public static final Integer	IMPORT_AREA_SUPPORTEDAREA_SAME				= 23007;
	
	/***************************************** 商品导出和导入 *********************************************/

	/** 导出的商品编码最多只能输入100个 */
	public static final Integer	ITEM_EXPORT_ITEM_CODE_OUT_SIZE				= 24001;

	/** 导出的模板文件不存在 */
	public static final Integer	EXCEL_TEMPLATE_FILE_NOT_EXISTS				= 24002;
	
	/** 请选择你需要导出的字段 */
	public static final Integer	ITEM_EXPORT_SELECTED_COLUMN_EMPTY			= 24003;

	/** 导入的商品分类不存在 */
	public static final Integer	ITEM_IMPORT_CATEGORY_NOT_EXISTS				= 24004;

	/** 导入的商品编码不存在 */
	public static final Integer	ITEM_IMPORT_CODE_NOT_EXISTS					= 24005;

	/** 导入的商品属性不存在 */
	public static final Integer	ITEM_IMPORT_PROPERTY_NOT_EXISTS				= 24006;

	/** 商品编码集合为空 */
	public static final Integer	ITEM_CODE_LIST_IS_NULL						= 24007;
	
	/** 商品导入: 开启国际化后,模板中默认语言不可以为空且要与DB中为设置的默认语言一致  */
	public static final Integer	ITEM_IMPORT_DEFAULT_LANG_IS_ERROR			= 24008;
	
	/** 商品导入: 语言名称不存在  */
	public static final Integer ITEM_IMPORT_LANGUAGE_VALUE_NOT_EXISTS		= 24009;

	/** 商品导入: 导入的商品数据为空  */
	public static final Integer ITEM_IMPORT_ITEM_DATA_IS_EMPTY				= 24010;
	
	/***************************************** 商品创建/更新校验(25001-26000) ********************************/
	/** 商品默认分类已不存在 */
	public static final Integer	ITEM_UPDATE_DEFCATE_NOT_EXISTS				= 25001;
	
	/** 商品默认分类不在商品分类里 */
	public static final Integer	ITEM_UPDATE_CATE_NOT_CONTAIN_DEF			= 25002;
	
	/** 商品编码不符合正则表达式要求 */
	public static final Integer	ITEM_CODE_VALID_ERROR						= 25003;
	
	/** bundle成员款号丢失 */
	public static final Integer	ITEM_BUNDLE_ELEMENT_STYLE_LOST				= 25011;
	
	/** bundle商品扩展信息不能为空  */
	public static final Integer ITEM_BUNDLE_EXPANDINFO_NULL					= 25012;
	
	/** bundle商品的item重复 */
	public static final Integer	ITEM_BUNDLE_PRODUCT_CODE_REPEAT				= 25013;
}
