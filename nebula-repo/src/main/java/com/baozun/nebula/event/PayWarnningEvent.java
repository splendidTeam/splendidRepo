package com.baozun.nebula.event;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

public class PayWarnningEvent extends ApplicationEvent {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8177309198475572784L;

	private String orderCode;
	private String thirPayNo;
	private Date createTime;
	private String paystate_shop;
    private String paystate_payment;
    private Boolean isSupportQuery;
    private  String result;

	@SuppressWarnings("unused")
	private String message;
	public PayWarnningEvent(Object source, String orderCode, String thirPayNo, Date createTime,
			String paystate_shop, String paystate_payment,
			Boolean isSupportQuery, String result) {
		super(source);
		this.orderCode = orderCode;
		this.thirPayNo = thirPayNo;
		this.createTime = createTime;
		this.paystate_shop = paystate_shop;
		this.paystate_payment = paystate_payment;
		this.isSupportQuery = isSupportQuery;
		this.result = result;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getThirPayNo() {
		return thirPayNo;
	}
	public void setThirPayNo(String thirPayNo) {
		this.thirPayNo = thirPayNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPaystate_shop() {
		return paystate_shop;
	}
	public void setPaystate_shop(String paystate_shop) {
		this.paystate_shop = paystate_shop;
	}
	public String getPaystate_payment() {
		return paystate_payment;
	}
	public void setPaystate_payment(String paystate_payment) {
		this.paystate_payment = paystate_payment;
	}
	public Boolean getIsSupportQuery() {
		return isSupportQuery;
	}
	public void setIsSupportQuery(Boolean isSupportQuery) {
		this.isSupportQuery = isSupportQuery;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}


}
