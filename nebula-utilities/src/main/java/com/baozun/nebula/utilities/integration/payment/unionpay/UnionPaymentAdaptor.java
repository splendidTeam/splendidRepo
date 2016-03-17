package com.baozun.nebula.utilities.integration.payment.unionpay;

import java.util.Properties;


public class UnionPaymentAdaptor extends
		AbstractUnionPaymentAdaptor {

	public UnionPaymentAdaptor(Properties InComingConf){
		super.configs = InComingConf;
	}
	
	@Override
	public String getServiceProvider() {
		return null;
	}

}
