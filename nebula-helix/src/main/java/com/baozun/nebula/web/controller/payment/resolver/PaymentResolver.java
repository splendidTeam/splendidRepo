package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.MemberDetails;

public interface PaymentResolver {
	
	String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, Map<String,Object> extra, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException;

	String doPayReturn(HttpServletRequest request,
			HttpServletResponse response, String payType, Device device) throws IllegalPaymentStateException;

	void doPayNotify(HttpServletRequest request, HttpServletResponse response,
			String payType, Device device) throws IllegalPaymentStateException, IOException;
}
