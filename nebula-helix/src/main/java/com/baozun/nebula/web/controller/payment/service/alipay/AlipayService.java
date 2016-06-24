package com.baozun.nebula.web.controller.payment.service.alipay;

import java.util.Map;

import org.springframework.mobile.device.Device;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;

public interface AlipayService {

	PaymentRequest createPayment(SalesOrderCommand order, Map<String, String> additionParams, Device device);
}
