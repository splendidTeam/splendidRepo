package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.web.MemberDetails;
import com.feilong.core.Validator;

public class AlipayPaymentResolver extends BasePaymentResolver implements PaymentResolver {
	
	@Autowired
	private SdkPaymentManager sdkPaymentManager;
	
	@Autowired
	private PaymentManager paymentManager;

	@Autowired
	private PayManager payManager;
	
	@Override
	public String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException {
        
		String itBPay = getItBPay(originalSalesOrder.getCreateTime());	
		
		// 超时校验
		if (itBPay.equals("0m")) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAYMENT_OVERTIME);
		}
		
		// 获取各种参数
		String payInfo = payInfoLog.getPayInfo();

		Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
		String payTypeStr = pro.getProperty(payInfo + ".payType");
		String bankCode = pro.getProperty(payInfo + ".bankcode");
		
		Integer payType = null;

		if (Validator.isNotNullOrEmpty(payTypeStr)) {
			payType = Integer.parseInt(payTypeStr.trim());
		}
		
		if (Validator.isNotNullOrEmpty(bankCode)) {
			bankCode = bankCode.trim();
		}
		
		String subOrdinate = payInfoLog.getSubOrdinate();
		
		PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);
		
		// 参数封装
		SalesOrderCommand so = new SalesOrderCommand();
		so.setCode(subOrdinate);
		so.setTotal(pc.getPayMoney());
		so.setOnLinePaymentCommand(getOnLinePaymentCommand(bankCode, payType, itBPay, request));
		
		// 获取支付请求( url)链接对象
		PaymentRequest paymentRequest = paymentManager.createPayment(so);

		if (null == paymentRequest) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_GETURL_ERROR, "获取跳转地址失败");
		} else {
			String url = paymentRequest.getRequestURL();
			if (StringUtils.isNotBlank(url)) {
				try {
					// 记录日志和跳转到支付宝支付页面
					payManager.savePayInfos(so, paymentRequest, memberDetails.getLoginName());
					response.sendRedirect(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
		
		return null;
	}
	
	//TODO
	//注意这里的paytype要设置成
	//order.getOnLinePaymentCancelCommand().getPayType()
	//OnLinePaymentCancelCommand的payType用ReservedPaymentType里的常量

}
