package com.baozun.nebula.web.controller.payment.service.common.command;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class CommonPayParamCommand implements BasePayParamCommandAdaptor {
	
    public static final String BUY = "buy";
	
	public static final String SELLER = "seller";

	/** 银行简码 */
	private String defaultBank;

	/** 订单号 */
	private String orderNo;

	/** 总金额 */
	private BigDecimal totalFee;

	/** 是否是国际卡 */
	private boolean isInternationalCard;

	/** 商品描述 */
	private String body;

	/** 取消订单角色 */
	private String tradeRole;

	/** 交易号 */
	private String tradeNo;

	/** 支付类型 */
	private String paymentType;
	
	/** 交易时间 */
	private String paymentTime;
	
	/** 持卡人真实IP地址 */
	private String customerIp;
	
	/** 过期时间 */
	private String itBPay;
	
	/** 扫码支付方式 */
	private String qrPayMode;
	
	
	public void setDefaultBank(String defaultBank) {
		this.defaultBank = defaultBank;
	}

	public void setTradeRole(String tradeRole) {
		this.tradeRole = tradeRole;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public void setItBPay(String itBPay) {
		this.itBPay = itBPay;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public void setIsInternationalCard(boolean isInternationalCard) {
		this.isInternationalCard = isInternationalCard;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public void setQrPayMode(String qrPayMode) {
		this.qrPayMode = qrPayMode;
	}

	@Override
	public String getDefault_bank() {
		if(StringUtils.isNotEmpty(defaultBank))
			return defaultBank;
		else
			return null;
	}

	@Override
	public String getOrderNo() {
		return this.orderNo;
	}

	@Override
	public BigDecimal getTotalFee() {
		return this.totalFee;
	}

	@Override
	public boolean isInternationalCard() {
		return this.isInternationalCard;
	}

	@Override
	public String getBody() {
		return this.body;
	}

	@Override
	public String getTrade_role() {
		if(BUY.equals(tradeRole)){
			return "B";
		}
		if(SELLER.equals(tradeRole)){
			return "S";
		}
		return null;
	}

	@Override
	public String getTrade_no() {
		return this.tradeNo;
	}

	@Override
	public String getPaymentTime() {
		return this.paymentTime;
	}

	@Override
	public String getCustomerIp() {
		return this.customerIp;
	}

	@Override
	public String getIt_b_pay() {
		return this.itBPay;
	}

	@Override
	public String getQrPayMode() {
		return this.qrPayMode;
	}
	
	@Override
	public String getPaymentType() {
		if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_NETPAY.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_BANK;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY_CREDIT.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_INTERNATIONALCARD.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
    	} else {
    		throw new IllegalArgumentException("not an valide alipay type. payType:[" + paymentType + "]");
    	}
	}
}
