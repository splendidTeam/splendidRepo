package com.baozun.nebula.web.controller.payment.service.unionpay;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayServiceImpl;

@Service
public class UnionpayServiceImpl extends CommonPayServiceImpl implements UnionpayService {
	
	/** The Constant LOGGER. */
    @SuppressWarnings("unused")
	private static final Logger LOGGER          = LoggerFactory.getLogger(UnionpayServiceImpl.class);
	
	@Override
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request, String paymentType) {
		throw new IllegalAccessError("union pay donot support return invoke.");
	}
	
}
