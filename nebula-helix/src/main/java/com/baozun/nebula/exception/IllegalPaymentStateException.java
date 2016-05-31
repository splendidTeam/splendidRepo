package com.baozun.nebula.exception;

/**
 * 支付时应为订单状态无效抛出的异常
 *
 */
public class IllegalPaymentStateException extends Exception {

	private static final long serialVersionUID = -7658396030138861041L;
	
	private IllegalPaymentState state;
	
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

	public IllegalPaymentStateException(Throwable cause){
		super(cause);
	}
	
	public IllegalPaymentState getState() {
		return state;
	}


	public enum IllegalPaymentState {
		
		/** 无效的支付方式 */
		PAYMENT_ILLEGAL_INVALID_PAYTYPE,
		
		/** 交易流水不存在或已支付 */
		PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_PAID,
		
		/** 订单不存在 */
		PAYMENT_ILLEGAL_ORDER_NOT_EXISTS,
		
		/** 订单已经被支付 */
		PAYMENT_ILLEGAL_ORDER_PAID,
		
		/** 订单已经被取消 */
		PAYMENT_ILLEGAL_ORDER_CANCLED,
		
		/** 订单超过支付时间 */
		PAYMENT_ILLEGAL_ORDER_PAYMENT_OVERTIME,
		
		/** 非当前用户的订单 */
		PAYMENT_ILLEGAL_ORDER_INVALID_OWNER;

	}
}
