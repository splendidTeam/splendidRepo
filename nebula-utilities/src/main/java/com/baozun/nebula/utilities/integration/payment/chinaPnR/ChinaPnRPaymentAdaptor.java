package com.baozun.nebula.utilities.integration.payment.chinaPnR;

import java.util.Map;
import java.util.Properties;

import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public class ChinaPnRPaymentAdaptor extends AbstractChinaPnRPaymentAdaptor {

	public ChinaPnRPaymentAdaptor(Properties InComingConf){
		
	}
	
	@Override
	public String getServiceProvider() {
		return PaymentAdaptor.PLATFORM_CHINAPNR;
	}

	@Override
	public PaymentResult getOrderInfo(Map<String, String> addition) {
		// TODO Auto-generated method stub
		return null;
	}

}
