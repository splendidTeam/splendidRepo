package com.baozun.nebula.web.controller.payment.service.common;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public interface CommonPayService {
	
	/**
	 * 通用同步结果通知
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request,String paymentType);
	
	
	/**
	 * 通用异步结果通知
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForAsy(HttpServletRequest request,String paymentType);
	
}
