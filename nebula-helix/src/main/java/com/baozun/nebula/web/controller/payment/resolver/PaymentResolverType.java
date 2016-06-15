package com.baozun.nebula.web.controller.payment.resolver;

import java.io.Serializable;
import java.util.Map;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;

public class PaymentResolverType implements Serializable {

	private static final long serialVersionUID = -216019187342593163L;
	
	private Map<String, PaymentResolver> paymentResolverTypes;
	
	public PaymentResolver getInstance(String type) throws IllegalPaymentStateException {
		PaymentResolver paymentResolver = paymentResolverTypes.get(type);
		if(paymentResolver == null) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_INVALID_PAYTYPE, "无效的支付方式：" + type);
		}
		return paymentResolver;
	}

	public Map<String, PaymentResolver> getPaymentResolverTypes() {
		return paymentResolverTypes;
	}

	public void setPaymentResolverTypes(
			Map<String, PaymentResolver> paymentResolverTypes) {
		this.paymentResolverTypes = paymentResolverTypes;
	}
	
	
}
