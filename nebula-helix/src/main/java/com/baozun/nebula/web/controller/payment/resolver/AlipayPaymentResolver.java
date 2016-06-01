package com.baozun.nebula.web.controller.payment.resolver;

import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
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
	
	@Override
	public String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException {
		
		Date orderCreateDate = originalSalesOrder.getCreateTime();
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

		SalesOrderCommand so = new SalesOrderCommand();
		so.setCode(subOrdinate);
		so.setTotal(pc.getPayMoney());
		String itBPay = getItBPay(orderCreateDate);	
		
		if (itBPay.equals("0m")) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAID);
		}
		
		so.setOnLinePaymentCommand(getOnLinePaymentCommand(bankCode, payType, itBPay, request));
		// 获取支付请求( url)链接对象
		PaymentRequest paymentRequest = paymentManager.createPayment(so);

		if (null == paymentRequest) {
			throw new BusinessException(ErrorCodes.transaction_pay_url_error);
		} else {
			String url = paymentRequest.getRequestURL();
			if (StringUtils.isNotBlank(url) && PaymentFactory.PAY_TYPE_ALIPAY.equals(PayTypeConvertUtil.getPayType(payType))) {
				// 支付宝跳转
				payManager.savePayInfos(so, paymentRequest, getOperator(request));
				response.sendRedirect(url);
			} else if ( StringUtils.isNotBlank(paymentRequest.getRequestHtml()) && PaymentFactory.PAY_TYPE_UNIONPAY.equals(PayTypeConvertUtil.getPayType(payType))) {
				// 银联跳转
				payManager.savePayInfos(so, paymentRequest, getOperator(request));
				response.getWriter().write(paymentRequest.getRequestHtml());
			} else {
				// payManager.savePaymentRequestPaymentLog(paymentRequest,
				// getOperator(request));
				throw new BusinessException(ErrorCodes.transaction_pay_url_error);
			}
		}
		
		return null;
	}

	
	//TODO
	//注意这里的paytype要设置成
	//order.getOnLinePaymentCancelCommand().getPayType()
	//OnLinePaymentCancelCommand的payType用ReservedPaymentType里的常量
	
	

}
