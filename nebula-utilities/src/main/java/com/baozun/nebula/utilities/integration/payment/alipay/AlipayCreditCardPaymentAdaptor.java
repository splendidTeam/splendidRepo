package com.baozun.nebula.utilities.integration.payment.alipay;

import java.util.Properties;

import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class AlipayCreditCardPaymentAdaptor extends
		AbstractAlipayPaymentAdaptor {

	public AlipayCreditCardPaymentAdaptor(Properties InComingConf) {
		super.configs = InComingConf;
		super.setPayMethod(RequestParam.PAYMETHOE_C);
		super.setIsDefault_login("Y");
		super.getAddress(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT);
	}

	@Override
	public String getServiceProvider() {
		// TODO Auto-generated method stub
		return PaymentAdaptor.PLATFORM_ALIPAY_CREDITCARD;
	}

}
