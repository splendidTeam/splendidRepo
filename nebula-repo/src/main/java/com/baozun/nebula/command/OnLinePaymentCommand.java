package com.baozun.nebula.command;

import java.io.Serializable;

public class OnLinePaymentCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3305161699681577232L;

	/**支付类型**/
	private Integer payType;
	
	/**是否是国际卡**/
	private Boolean isInternationalCard;
	
	/** 银行编码  **/
	private String bankCode;
	
	/** 订单支付时间  **/
	private String payTime;
	
	/**
	 * 设置过期时间
	 */
	private String itBPay;
	
	/**
	 * 持卡人IP地址
	 */
	private String customerIp;
	
	/**
	 * 扫描支付方式
	 */
	private String qrPayMode;

	
	public OnLinePaymentCommand() {
		super();
	}

	public OnLinePaymentCommand(Integer payType, Boolean isInternationalCard,
			String bankCode) {
		super();
		this.payType = payType;
		this.isInternationalCard = isInternationalCard;
		this.bankCode = bankCode;
	}
	
	public OnLinePaymentCommand(Integer payType, Boolean isInternationalCard,
			String bankCode,String payTime,String customerIp) {
		super();
		this.payType = payType;
		this.isInternationalCard = isInternationalCard;
		this.bankCode = bankCode;
		this.payTime = payTime;
		this.customerIp = customerIp;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Boolean getIsInternationalCard() {
		return isInternationalCard;
	}

	public void setIsInternationalCard(Boolean isInternationalCard) {
		this.isInternationalCard = isInternationalCard;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getItBPay() {
		return itBPay;
	}

	public void setItBPay(String itBPay) {
		this.itBPay = itBPay;
	}

	public String getQrPayMode() {
		return qrPayMode;
	}

	public void setQrPayMode(String qrPayMode) {
		this.qrPayMode = qrPayMode;
	}
	
	
}
