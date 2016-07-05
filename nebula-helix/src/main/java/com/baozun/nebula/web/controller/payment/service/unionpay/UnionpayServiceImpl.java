package com.baozun.nebula.web.controller.payment.service.unionpay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayServiceImpl;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;

@Service
public class UnionpayServiceImpl extends CommonPayServiceImpl implements UnionpayService {
	
	/** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(UnionpayServiceImpl.class);
	
	@Override
	public PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> extraParams) {
		try {
			return super.createPayment(payParamCommand, extraParams);
		} catch (Exception e) {
			LOGGER.error("[CREATEPAYMENT] create payment error.", e);
		}
		return null;
	}
	
	@Override
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request,
			String paymentType) {
		
	    return null;
	}
	
}
