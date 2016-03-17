package com.baozun.nebula.utilities.integration.payment.alipay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ResourceUtil;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentAdaptorInitialFailureException;


public class AlipayBankPaymentAdaptor extends AbstractAlipayPaymentAdaptor{

	private static final Logger logger = LoggerFactory
			.getLogger(AlipayBankPaymentAdaptor.class);
	
	public AlipayBankPaymentAdaptor(Properties InComingConf) {
		super.configs = InComingConf;
		super.setPayMethod(RequestParam.PAYMETHOE_BANK);
		super.setIsDefault_login("Y");
		super.getAddress(PaymentFactory.PAY_TYPE_ALIPAY_BANK);
	}
	
	@Override
	public String getServiceProvider() {
		return PaymentAdaptor.PLATFORM_ALIPAY_BANK;
	}

}
