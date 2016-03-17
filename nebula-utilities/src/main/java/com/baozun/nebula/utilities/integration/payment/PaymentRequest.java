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

import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
import com.baozun.nebula.utilities.integration.payment.exception.RequestTypeNotSupporttedException;

public interface PaymentRequest {	

	/**
	 * 支付前的准备工作，如获取防钓鱼时间戳或者交易流水号等需要在后续发送支付请求前进行的工作
	 * 
	 * @throws PaymentException
	 */
	void prepare() throws PaymentException;

	/**
	 * 判断是否支持请求类型
	 * 
	 * @param requestType
	 * @return
	 */
	boolean supportRequestType(String requestType);

	/**
	 * 设置发送支付请求的Http类型
	 * 
	 * @return
	 */
	void setRequestType(String requestType)
			throws RequestTypeNotSupporttedException;

	/**
	 * 获取发送支付请求的完整URL
	 * 
	 * @return
	 */
	String getRequestURL();

	/**
	 * 获取发送支付请求的完整参数集
	 * 
	 * @return
	 */
	Map<String, String> getPaymentParameters();
	
	/**
	 * 设置发送请求的Http类型
	 * 
	 * @return
	 */
	String getRequestType();
	
	/**
	 * 获取发送支付请求的完整html
	 * 
	 * @return
	 */
	String getRequestHtml();
}
