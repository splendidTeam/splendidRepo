package com.baozun.nebula.web.controller.payment.resolver;

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
			MemberDetails memberDetails, Device device, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException;
}
