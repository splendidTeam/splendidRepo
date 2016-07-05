package com.baozun.nebula.web.controller.payment.service.unionpay;

import java.util.Map;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayService;
import com.baozun.nebula.web.controller.payment.service.common.command.CommonPayParamCommand;

public interface UnionpayService extends CommonPayService {
	/**
	 * 获取支付跳转地址
	 * @param payParamCommand
	 * @param additionParams
	 * @param device
	 * @return
	 */
	PaymentRequest createPayment(CommonPayParamCommand payParamCommand, Map<String, String> additionParams);
}
