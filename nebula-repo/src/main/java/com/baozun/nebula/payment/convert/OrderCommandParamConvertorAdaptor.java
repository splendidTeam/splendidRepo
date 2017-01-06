package com.baozun.nebula.payment.convert;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class OrderCommandParamConvertorAdaptor implements PayParamCommandAdaptor {

	private SalesOrderCommand salesOrderCommand;
	
	private Map<String,Object> requestParams;
	
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
		else if(null!=requestParams.get("bankCode"))
			return requestParams.get("bankCode").toString();
		else
			return null;
	}

	@Override
	public String getOrderNo() {
		if(StringUtils.isNotEmpty(salesOrderCommand.getCode()))
			return salesOrderCommand.getCode();
		else if(null!=requestParams.get("code"))
			return requestParams.get("code").toString();
		else
			return null;
	}

	@Override
	public BigDecimal getTotalFee() {
		if(null!=salesOrderCommand.getTotal())
			return salesOrderCommand.getTotal();
		else if(null!=requestParams.get("total"))
			return new BigDecimal(requestParams.get("total").toString());
		else
			return null;
	}

	@Override
	public boolean isInternationalCard() {
		boolean isInternationalCard = salesOrderCommand.getOnLinePaymentCommand().getIsInternationalCard();
		if(BooleanUtils.isTrue(isInternationalCard))
			return true;
		else if(null!=requestParams.get("isInternationalCard")&&Boolean.valueOf(requestParams.get("isInternationalCard").toString()))
			return true;
		else
			return false;
	}

	@Override
	public String getBody() {
		String body = salesOrderCommand.getDescribe();
		if(StringUtils.isNotEmpty(body))
			return body;
		else if(null!=requestParams.get("describe"))
			return requestParams.get("describe").toString();
		else
			return null;
	}

	@Override
	public String getTrade_role() {
		String role = salesOrderCommand.getOnLinePaymentCancelCommand().getTrade_role();
		if(StringUtils.isEmpty(role)){
			role = requestParams.get("trade_role").toString();
		}
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
		if(StringUtils.isNotEmpty(salesOrderCommand.getOnLinePaymentCancelCommand().getTrade_no()))
			return salesOrderCommand.getOnLinePaymentCancelCommand().getTrade_no();
		else if(null!=requestParams.get("trade_no"))
			return requestParams.get("trade_no").toString();
		else
			return null;
	}

	@Override
	public String getPaymentType() {
		Integer payType = salesOrderCommand.getOnLinePaymentCommand().getPayType();
		if(null == payType){
			payType = Integer.valueOf(requestParams.get("payType").toString());
		}
		String type = null;
		switch(payType){
			case ReservedPaymentType.ALIPAY : type = PaymentFactory.PAY_TYPE_ALIPAY;break;
			case ReservedPaymentType.ALIPAY_BANK : type = PaymentFactory.PAY_TYPE_ALIPAY_BANK;break;
			case ReservedPaymentType.ALIPAY_CREDIT : type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;break;
			case ReservedPaymentType.ALIPAY_CREDIT_INT_V : type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;break;
			case ReservedPaymentType.ALIPAY_CREDIT_INT_M : type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;break;
			case ReservedPaymentType.UNIONPAY : type = PaymentFactory.PAY_TYPE_UNIONPAY;break;
			case ReservedPaymentType.WECHAT : type= PaymentFactory.PAY_TYPE_WECHAT;
		}
		return type;
	}

	@Override
	public String getPaymentTime() {
		if(StringUtils.isNotEmpty(salesOrderCommand.getOnLinePaymentCommand().getPayTime()))
			return salesOrderCommand.getOnLinePaymentCommand().getPayTime();
		else if(null != requestParams.get("payTime"))
			return requestParams.get("payTime").toString();
		else
			return null;
	}

	@Override
	public String getCustomerIp() {
		if(StringUtils.isNotEmpty(salesOrderCommand.getOnLinePaymentCommand().getCustomerIp()))
			return salesOrderCommand.getOnLinePaymentCommand().getCustomerIp();
		else if(null != requestParams.get("customerIp"))
			return requestParams.get("customerIp").toString();
		else
			return null;
	}

	@Override
	public String getIt_b_pay() {
		if(StringUtils.isNotEmpty(salesOrderCommand.getOnLinePaymentCommand().getItBPay()))
			return salesOrderCommand.getOnLinePaymentCommand().getItBPay();
		else if(null != requestParams.get("itBPay"))
			return requestParams.get("itBPay").toString();
		else
			return null;
	}

	@Override
	public String getQrPayMode() {
		if(StringUtils.isNotEmpty(salesOrderCommand.getOnLinePaymentCommand().getQrPayMode()))
			return salesOrderCommand.getOnLinePaymentCommand().getQrPayMode();
		else if(null != requestParams.get("qrPayMode"))
			return requestParams.get("qrPayMode").toString();
		else
			return null;
	}

	/**
	 * @return the requestParams
	 */
	@Override
	public Map<String,Object> getRequestParams() {
		return requestParams;
	}

	/**
	 * @param requestParams the requestParams to set
	 */
	@Override
	public void setRequestParams(Map<String,Object> requestParams) {
		this.requestParams = requestParams;
	}

}
