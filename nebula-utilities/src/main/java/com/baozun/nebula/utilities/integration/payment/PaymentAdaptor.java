/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utilities.integration.payment;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;

public interface PaymentAdaptor{
	public static final String	PAYMENT_GATEWAY			= "payment_gateway";
	
	public static final String	PAYMENT_GATEWAY_MOBILE_C			= "serviceForMobileCreateDirect";
	
	public static final String	PAYMENT_GATEWAY_MOBILE_P			= "serviceForMobileAuthAndExecute";

	public static final String	PAYMENT_PARAM_PREFIX	= "param.";

	public static final String	PLATFORM_ALIPAY			= "PaymentServiceProvider:Alipay";
	
	public static final String	PLATFORM_ALIPAY_BANK	= "PaymentServiceProvider:AlipayBank";
	
	public static final String	PLATFORM_ALIPAY_CREDITCARD	= "PaymentServiceProvider:AlipayCreditCard";
	
	public static final String	PLATFORM_ALIPAY_INTERNATIONALCREDITCARD	= "PaymentServiceProvider:AlipayInternationCreditCard";

	public static final String	PLATFORM_UNIONPAY		= "PaymentServiceProvider:Unionpay";
	
	public static final String	PLATFORM_CHINAPNR		= "PaymentServiceProvider:ChinaPnR";

	public static final String	PLATFORM_PAYPAL			= "PaymentServiceProvider:Paypal";

	public static final String	BANK_CCB				= "PaymentServiceProvider:Ccb";
	
	/**
	 * 获取服务提供方编码
	 * 
	 * @return
	 */
	String getServiceProvider();

	/**
	 * 获取服务提供方提供的该类服务的编码或者类型标识，如Alipay提供的移动支付
	 * 
	 * @return
	 */
	String getServiceType();

	/**
	 * 获取服务版本
	 * 
	 * @return
	 */
	String getServiceVersion();

	/**
	 * 创建一个新的PC端支付请求
	 * 
	 * @param httpType
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @return
	 * @throws PaymentException
	 */
	PaymentRequest newPaymentRequest(String httpType,Map<String, String> addition);
	
	/**
	 * 创建一个新的MOBILE端授权请求
	 * 
	 * @param httpType
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @return
	 * @throws PaymentException
	 */
	PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> addition);
	
	/**
	 * 创建一个新的MOBILE端交易请求
	 * 
	 * @param httpType
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @return
	 * @throws PaymentException
	 */
	PaymentRequest newPaymentRequestForMobileAuthAndExecute(Map<String, String> addition);

	/**
	 * 返回支付结果(正常页面跳转返回的结果)
	 * 
	 * @param request
	 * @return
	 */
	PaymentResult getPaymentResult(HttpServletRequest request);
	
	/**
	 * 手机WAP端授权结果
	 * 
	 * @param request
	 * @return
	 */
	PaymentResult getPaymentResultForMobileCreateDirect(Map<String,String> resultStr);
	
	/**
	 * 手机WAP端同步交易结果
	 * 
	 * @param request
	 * @return
	 */
	PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request);
	
	/**
	 * 手机WAP端异步交易结果
	 * 
	 * @param request
	 * @return
	 */
	PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request);

	/**
	 * 解析通知返回支付结果
	 * 
	 * @param request
	 * @return
	 */
	PaymentResult getPaymentResultFromNotification(HttpServletRequest request);

	/**
	 * 关闭交易
	 * @param amt
	 *            退款金额 ,暂时这个参数没有用, 以后可能出现 预付款交易退款的情况, 需要先验证 支付金额和退款金额是否一致,如果不一致 返回false
	 * @return
	 */
	PaymentResult closePaymentRequest(Map<String,String> parm);

	/**
	 * 是否支持关闭交易
	 * 
	 * @return
	 */
	boolean isSupportClosePaymentRequest();
	
	/**
	 * 交易查询
	 * 
	 * @return
	 */
	PaymentResult getOrderInfo(Map<String,String> addition);
	
	/**
	 * 微信统一下单接口
	 * @param addition
	 * @return
	 */
	PaymentResult unifiedOrder(Map<String,String> addition);
	
}
