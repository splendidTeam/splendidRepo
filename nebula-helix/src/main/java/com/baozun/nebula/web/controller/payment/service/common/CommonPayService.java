package com.baozun.nebula.web.controller.payment.service.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;

public interface CommonPayService {
	
	/**
	 * 获取支付跳转地址
	 * @param payParamCommand
	 * @param additionParams
	 * @param device
	 * @return
	 */
	PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> extraParams, Device device);
	
	/**
	 * 通用同步结果通知
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForSyn(HttpServletRequest request, String paymentType);
	
	
	/**
	 * 通用异步结果通知
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForAsy(HttpServletRequest request, String paymentType);
	
}
