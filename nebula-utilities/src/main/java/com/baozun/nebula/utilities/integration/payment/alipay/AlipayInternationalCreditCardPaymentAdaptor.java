package com.baozun.nebula.utilities.integration.payment.alipay;

import java.util.Properties;

import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;

public class AlipayInternationalCreditCardPaymentAdaptor extends
		AbstractAlipayPaymentAdaptor {

	public AlipayInternationalCreditCardPaymentAdaptor(Properties InComingConf) {
		super.configs = InComingConf;
		super.setPayMethod(RequestParam.PAYMETHOE_MOTO);
		super.getAddress(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT);
	}

	@Override
	public String getServiceProvider() {
		return PaymentAdaptor.PLATFORM_ALIPAY_INTERNATIONALCREDITCARD;
	}

}
