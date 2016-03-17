package com.baozun.nebula.exception;

public class ErrorCodes {
	/** 系统错误. */
	public static final Integer	SYSTEM_ERROR						= 1;

	/** 更新支付信息失败(t_so_payinfo) **/
	public static final Integer	UPDATE_PAYINFO_FAILURE				= 30001;

	/** 更新支付订单信息失败 (t_so_paycode) **/
	public static final Integer	UPDATE_PAYCODE_FAILURE				= 30002;

	/** 取消交易用户类型错误 **/
	public static final int		transaction_cancel_usertype_error	= 60014;

	/** 优惠券验证 **/
	public static final Integer	COUPON_SYSTEM_ERROR					= 140002;

	/** 支付订单号不存在 **/
	public static final int		transaction_ordercode_error			= 60016;

	/** 交易关闭 **/
	public static final int		transaction_closed					= 60008;
	
	/** 订单重复取消 **/
	public static final int		ORDER_ITERATIVE_CANCEL					= 60009;

}
