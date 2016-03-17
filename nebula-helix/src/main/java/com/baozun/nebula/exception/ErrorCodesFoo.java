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
 * 自定义错误码,see businessException.properties<br />
 * 从ErrorCodes类中抽离，因为repo里也定义了ErrorCodes这个类，而且路径也与helix里的ErrorCodes相同，造成了类加载时不确定加载了repo中的该类还是helix中的。<br />
 * 所以定义该类，以后helix中有新的ErrorCode定义都写在该类中，原有的ErrorCodes中的定义也迁移到该类中。
 * 
 * @author dianchao.song
 */
public interface ErrorCodesFoo {

	/** The Constant BUSINESS_EXCEPTION_PREFIX. */
	public static final String	BUSINESS_EXCEPTION_PREFIX								= "business_exception_";

	/** 系统错误. */
	public static final Integer	SYSTEM_ERROR											= 1;

	/** 无权访问. */
	public static final Integer	ACCESS_DENIED											= 2;

	/** session无效. */
	public static final Integer	INVALID_SESSION											= 3;

	/** dataBind error */
	public static final Integer	DATA_BIND_EXCEPTION										= 4;

	/** 输入参数不正确 */
	public static final Integer	PARAMS_ERROR											= 5;

	// *******************auth 1000区间**********************************************************************************

	/** 用户名已存在. */
	public static final Integer	USER_USERNAME_EXISTS									= 1001;

	/** 找不到用户. */
	public static final Integer	USER_USER_NOTFOUND										= 1002;

	/** 必须确保有效的用户名不允许重复. */
	public static final Integer	USER_EFFECT_USERNAME_EXISTS								= 1003;

	/** 邮箱已被注册. */
	public static final Integer	EMAIL_ALLREADY_REGISTER									= 1004;

	/** 同步购物车失败. */
	public static final Integer	SYNCHR_SHOPCART_FAILURE									= 1005;

	/** 用户名长度必须在6-20位之间 */
	public static final Integer	USER_USERNAME_LENGTH									= 1006;

	/** 用户名格式错误 */
	public static final Integer	USER_USERNAME_NAMEERROR									= 1007;

	/** 密码长度必须在6-20位之间 */
	public static final Integer	USER_USERPASSWORD_PASSWORDERROR							= 1008;

	/** 判断密码是否一致 */
	public static final Integer	USER_USERPADMATCH_PWDMATCHERROR							= 1009;

	/** 邮箱格式错误 */
	public static final Integer	USER_USEREMAIL_USEREMAILERROR							= 1010;

	/** 邮箱长度必须小于50位 */
	public static final Integer	USER_USEREMAIL_USEREMAILLENGTH							= 1011;
	/** 请输入必填项 */
	public static final Integer	USER_USERREQUIRED_USERREQUIRED							= 1012;

	// *******************baseinfo
	// 2000区间*********************************************************************************
	/** 启用或禁用店铺失败 */
	public static final Integer	SHOP_ENABLE_DISABLE_FAIL								= 2001;

	// ****************** cms 3000区间*********************************************************************************

	// ****************** logs 3000区间*********************************************************************************

	// ****************** member 3000区间*********************************************************************************
	public static final Integer	MEMBER_FAVORITES_EXISTS									= 3001;

	/** 地址保存或更新出错 */
	public static final Integer	MEMBER_CONTACT_SAVE_OR_UPDATE							= 3010;
	/** 地址删除出错 */
	public static final Integer	MEMBER_CONTACT_DELETE									= 3011;
	/** 默认地址修改出错 */
	public static final Integer	MEMBER_CONTACT_SET_DEFAULT								= 3012;

	/** 只有购买了该商品才可以评论 **/
	public static final Integer	MEMBER_ITEMRATE_ORDERLINECOMPLETION						= 3013;

	/** 最多保存10个有效地址 */
	public static final Integer	MEMBER_CONTACT_MAX_NUM									= 3014;

	// ****************** product
	// 3000区间*********************************************************************************

	/** 启用属性失败 */
	public static final Integer	PRODUCT_ENABLE_PROPERTY_FAIL							= 6002;

	/** 禁用属性失败 */
	public static final Integer	PRODUCT_DISABLE_PROPERTY_FAIL							= 6003;

	/** 店铺增加失败 */
	public static final Integer	PRODUCT_ADD_SHOP_FAIL									= 6010;

	/** 店铺修改失败 */
	public static final Integer	PRODUCT_UPDATE_SHOP_FAIL								= 6011;

	/** 系统中已经存在该编码<code>{@value}</code>. */
	public static final Integer	PRODUCT_CATEGORY_CODE_REPEAT							= 6020;

	/** {0} 分类下,存在名字 {1} 的分类 <code>{@value}</code>. */
	public static final Integer	PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY				= 6021;

	/** 店铺修改失败 */
	public static final Integer	PRODUCT_PROPERTY_NAME_REPEAT							= 6022;

	public static final Integer	PRODUCT_ADD_ORGANIZATION_SANME_NAME						= 6012;

	/** 验证码错误 */
	public static final Integer	APP_VCODE_ERROR											= 6013;

	public static final Integer	ITEM_NOT_EXIST											= 6014;

	// ****************** cms 3000区间*********************************************************************

	// ****************** member 3000区间*********************************************************************

	/** 找回密码的帐号不存在 */
	public static final Integer	FINDBACK_USERNOTEXIST									= 3101;

	/** 验证问题错误 */
	public static final Integer	FINDBACK_QAERROR										= 3102;

	// ****************** system 3000区间*********************************************************************

	/** 出生日期大于当前日期异常 */
	public static final Integer	BIRTHDAY_BEFORE_NOW										= 7000;

	/** 设置默认地址失败 **/
	public static final Integer	SET_DEFAULT_ADDRESS_FAILURE								= 8001;

	/** 该订单支付类型({0})不支持支付网关支付. */
	public static final int		transaction_so_paymentType_not_need_PaymentAdaptor		= 60001;

	/** 你没有查看该订单的权限. */
	public static final int		transaction_so_not_yours								= 60002;

	/** 支付网关参数中带有的订单code 和 soCode 不匹配. */
	public static final int		transaction_so_feedbackSoCode_not_equals_soCode			= 60003;

	/** 订单不存在. */
	public static final int		transaction_so_not_exist								= 60004;

	/** 交易付款失败 **/
	public static final int		transaction_so_paystatus_failure						= 60005;

	/** 请稍后在商城我的订单中查询订单交易付款状态 */
	public static final int		transaction_so_paystatus_undefined						= 60006;

	/** 等待买家付款! **/
	public static final int		transaction_so_paystatus_wait_buyer_pay					= 60007;

	/** 交易已关闭! **/
	public static final int		transaction_so_paystatus_trade_close					= 60008;

	/** 支付动作成功,请稍后在商城我的订单中查询订单交易付款状态! **/
	public static final int		transaction_so_paystatus_unconfirumed_payment_success	= 60009;

	/** 订单支付信息不存在. */
	public static final int		transaction_payinfo_not_exist							= 60010;

	/** 获取跳转地址失败 */
	public static final int		transaction_pay_url_error								= 60011;

	/** 获取交易通知失败 **/
	public static final int		transaction_so_getpaynotify_failure						= 60012;

	/** 取消交易用户类型不能为空 **/
	public static final int		transaction_cancel_usertype_not_null					= 60013;

	/** 取消交易用户类型错误 **/
	public static final int		transaction_cancel_usertype_error						= 60014;

	/** 请选择需要合并付款的订单 **/
	public static final int		transaction_choose_order_error							= 60015;

	/** 支付订单号不存在 **/
	public static final int		transaction_ordercode_error								= 60016;

	/** 获取银联支付跳转地址失败 */
	public static final int		transaction_unionpay_url_error							= 60017;

	/** 跳转支付网关失败 **/
	public static final int		transaction_pay_gate_error								= 60018;

	/** 对不起，您没有权限取消该订单. */
	public static final int		transaction_so_not_permission_yours						= 60019;

	/** 该订单不能取消 */
	public static final int		transaction_so_can_not_cancel							= 60020;

	/** 修改外部优惠券失败 */
	public static final int		transaction_brush_coupon_failure						= 60021;

	public static final int		member_not_exist										= 90001;

	public static final int		passwd_not_match										= 90002;

	/** 校验邮箱失败 **/
	public static final int		VALIDEMAIL_ERROR										= 90003;

	/** 修改订单行评论状态失败 **/
	public static final Integer	ORDERLINE_EVALUSTIONSTATUSE_ERROE						= 90004;

	// ****************** 物流方面12000区间*********************************************************************
	public static final Integer	SAVE_SHIPPING_CONFIG_FAILURE							= 12001;

	/** 运费模板已经和商品关联 **/
	public static final Integer	PRODUCT_SHIPPING_FEE_CONNECTED							= 12002;

	// **************************商品搜索相关**************************************
	public static final Integer	RESULT_ERROR											= 130001;

	// **************************订单相关**************************************
	public static final Integer	COUPON_CANNOT_USE_ERROR									= 140001;
	public static final Integer	COUPON_SYSTEM_ERROR										= 140002;
	
	
	//*******************微信支付****************************/
	public static final Integer	WECHAT_ERROR_MSG										= 150001;
}
