package com.baozun.nebula.web.controller.payment.service.alipay;

import java.util.Map;

import org.springframework.mobile.device.Device;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.web.controller.payment.service.alipay.command.AlipayPayParamCommand;

public interface AlipayService {

	PaymentRequest createPayment(AlipayPayParamCommand payParamCommand, Map<String, String> additionParams, Device device);
}
