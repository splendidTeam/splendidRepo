package com.baozun.nebula.web.controller.payment.service.alipay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayService;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;

public interface AlipayService extends CommonPayService{
	
	/**
	 * 获取支付跳转地址
	 * @param payParamCommand
	 * @param additionParams
	 * @param device
	 * @return
	 */
	PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> additionParams, Device device);
	
	/**
	 * 结果通知 WAP 同步
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForSynOfWap(HttpServletRequest request,String paymentType);
	
	
	/**
	 * 结果通知 WAP 异步
	 * @param request
	 * @param paymentType
	 * @return
	 */
	public PaymentResult getPaymentResultForAsyOfWap(HttpServletRequest request,String paymentType);

}
