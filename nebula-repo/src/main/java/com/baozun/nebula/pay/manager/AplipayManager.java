package com.baozun.nebula.pay.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;

public interface AplipayManager {

	/*
	 * 新增一个aplipay支付请求
	 */
	Map<String,String> newPaymentRequest(String requestType,String orderNo, BigDecimal amt, Map<String, String> addition,String codeType);
	
	/*
	 * 获得同步通知
	 */
	Map<String,Object> getPaymentResult(HttpServletRequest request);
	
	/*
	 * 从通知中获得支付结果
	 */
	Map<String,Object> getPaymentResultFromNotification(HttpServletRequest request);
	
	/*
	 * 关闭支付请求
	 */
	Map<String,String> closePaymentRequest(String paymentReqId,String orderNum,BigDecimal amt, boolean isBuyerClose);

	/*
	 * 是否支持关闭支付
	 */
	boolean isSupportClosePaymentRequest();
}
