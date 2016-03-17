package com.baozun.nebula.payment.convert;

import java.math.BigDecimal;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class OrderCommandParamConvertorAdaptor implements PayParamCommandAdaptor {

	private SalesOrderCommand salesOrderCommand;
	
	public static final String BUY = "buy";
	
	public static final String SELLER = "seller";
	
	public SalesOrderCommand getSalesOrderCommand() {
		return salesOrderCommand;
	}

	public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand) {
		this.salesOrderCommand = salesOrderCommand;
	}

	@Override
	public String getDefault_bank() {
		String bankCode = salesOrderCommand.getOnLinePaymentCommand().getBankCode();
		if(StringUtils.isNotEmpty(bankCode))
			return bankCode;
		else
			return null;
	}

	@Override
	public String getOrderNo() {
		return salesOrderCommand.getCode();
	}

	@Override
	public BigDecimal getTotalFee() {
		return salesOrderCommand.getTotal();
	}

	@Override
	public boolean isInternationalCard() {
		boolean isInternationalCard = salesOrderCommand.getOnLinePaymentCommand().getIsInternationalCard();
		if(BooleanUtils.isTrue(isInternationalCard))
			return true;
		else
			return false;
	}

	@Override
	public String getBody() {
		String body = salesOrderCommand.getDescribe();
		if(StringUtils.isNotEmpty(body))
			return body;
		else
			return null;
	}

	@Override
	public String getTrade_role() {
		String role = salesOrderCommand.getOnLinePaymentCancelCommand().getTrade_role();
		if(BUY.equals(role)){
			return "B";
		}
		if(SELLER.equals(role)){
			return "S";
		}
		return null;
	}

	@Override
	public String getTrade_no() {
		return salesOrderCommand.getOnLinePaymentCancelCommand().getTrade_no();
	}

	@Override
	public String getPaymentType() {
		Integer payType = salesOrderCommand.getOnLinePaymentCommand().getPayType();
		String type = null;
		switch(payType){
			case PaymentConvertFactory.PAY_TYPE_ALIPAY : type = PaymentFactory.PAY_TYPE_ALIPAY;break;
			case PaymentConvertFactory.PAY_TYPE_ALIPAY_BANK : type = PaymentFactory.PAY_TYPE_ALIPAY_BANK;break;
			case PaymentConvertFactory.PAY_TYPE_ALIPAY_CREDIT : type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;break;
			case PaymentConvertFactory.PAY_TYPE_ALIPAY_CREDIT_INT_V : type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;break;
			case PaymentConvertFactory.PAY_TYPE_ALIPAY_CREDIT_INT_M : type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;break;
			case PaymentConvertFactory.PAY_TYPE_UNIONPAY : type = PaymentFactory.PAY_TYPE_UNIONPAY;break;
			case PaymentConvertFactory.PAY_TYPE_WECHAT : type= PaymentFactory.PAY_TYPE_WECHAT;
		}
		return type;
	}

	@Override
	public String getPaymentTime() {
		return salesOrderCommand.getOnLinePaymentCommand().getPayTime();
	}

	@Override
	public String getCustomerIp() {
		return salesOrderCommand.getOnLinePaymentCommand().getCustomerIp();
	}

	@Override
	public String getIt_b_pay() {
		return salesOrderCommand.getOnLinePaymentCommand().getItBPay();
	}

}
