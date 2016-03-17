package com.baozun.nebula.event;

import org.springframework.context.ApplicationEvent;

import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public class PaymentEvent extends ApplicationEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016070621835223245L;
	
	private PaymentResult paymentResult;
	private String operator;
	private Integer type;

	@SuppressWarnings("unused")
	private String message;
	public PaymentEvent(Object source, PaymentResult paymentResult,String operator,Integer type) {
		super(source);
		this.paymentResult=paymentResult;
		this.operator=operator;
		this.type=type;
	}
	public PaymentResult getPaymentResult() {
		return paymentResult;
	}
	public void setPaymentResult(PaymentResult paymentResult) {
		this.paymentResult = paymentResult;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

}
