package com.baozun.nebula.exception;

/**
 * 支付时应为订单状态无效抛出的异常
 *
 */
public class IllegalPaymentStateException extends Exception {

	private static final long serialVersionUID = -7658396030138861041L;
	
	private IllegalPaymentState state;
	
	private String subOrdinate;
	
	public IllegalPaymentStateException(){
		super();
	}

	public IllegalPaymentStateException(IllegalPaymentState state){
		super();
		this.state = state;
	}

	public IllegalPaymentStateException(String message, Throwable cause){
		super(message, cause);
	}

	public IllegalPaymentStateException(String message){
		super(message);
	}
	
	public IllegalPaymentStateException(IllegalPaymentState state, String message){
		super(message);
		this.state = state;
	}
	
	public IllegalPaymentStateException(IllegalPaymentState state, String subOrdinate, String message){
		super(message);
		this.state = state;
		this.subOrdinate = subOrdinate;
	}

	public IllegalPaymentStateException(Throwable cause){
		super(cause);
	}
	
	public IllegalPaymentState getState() {
		return state;
	}


	public String getSubOrdinate() {
		return subOrdinate;
	}


	public enum IllegalPaymentState {
		
		/** 无效的支付方式 */
		PAYMENT_ILLEGAL_INVALID_PAYTYPE,
		
		/** 交易流水不存在或已支付 */
		PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_PAID,
		
		/** 交易流水不存在或尚未支付 */
		PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_UNPAID,
		
		/** 交易流水对应多个未支付订单（暂不支持合并付款） */
		PAYMENT_ILLEGAL_SUBORDINATE_MULTI_ORDERS,
		
<<<<<<< HEAD
		/** 交易流水不存在 */
		PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS,
		
=======
>>>>>>> branch 'Nebula5.3.2' of http://git.baozun.cn/nebula/nebula.git
		/** 订单不存在 */
		PAYMENT_ILLEGAL_ORDER_NOT_EXISTS,
		
		/** 订单状态不允许支付 */
		PAYMENT_ILLEGAL_ORDER_STATUS,
		
		/** 订单已经被支付 */
		PAYMENT_ILLEGAL_ORDER_PAID,
		
		/** 订单已经被取消 */
		PAYMENT_ILLEGAL_ORDER_CANCLED,
		
		/** 订单超过支付时间 */
		PAYMENT_ILLEGAL_ORDER_PAYMENT_OVERTIME,
		
		/** 非当前用户的订单 */
		PAYMENT_ILLEGAL_ORDER_INVALID_OWNER,
		
		/**获取跳转地址失败 */
		PAYMENT_GETURL_ERROR,
		
		/**微信扫码支付的url不存在 */
		PAYMENT_WECHATPAY_CODEURL_NOT_EXISTS,
		
		/**微信公众号支付的预支付id不存在 */
		PAYMENT_WECHATPAY_PREPAYID_NOT_EXISTS

	}
}
