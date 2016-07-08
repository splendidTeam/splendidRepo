package com.baozun.nebula.web.controller.payment.service.alipay;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayService;

public interface AlipayService extends CommonPayService {
	
	
	/**
	 * 结果通知 WAP 同步
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForSynOfWap(HttpServletRequest request, String paymentType);
	
	
	/**
	 * 结果通知 WAP 异步
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForAsyOfWap(HttpServletRequest request, String paymentType);

}
