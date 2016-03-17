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
package com.baozun.nebula.utilities.integration.payment.unionpay;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
import com.baozun.nebula.utilities.integration.payment.exception.RequestTypeNotSupporttedException;

public class UnionPaymentRequest implements PaymentRequest, Serializable {

	private static final long serialVersionUID = -7441188005486354584L;

	private static final Logger logger = LoggerFactory
			.getLogger(UnionPaymentRequest.class);

	private Map<String, String> paymentParameters;

	/**
	 * 请求类型
	 */
	private String requestType;
	
	private String requestURL;
	
	private String requestHtml;
	
	@Override
	public void prepare() throws PaymentException {
	}

	@Override
	public boolean supportRequestType(String requestType) {
		if (RequestParam.HTTP_TYPE_GET.equalsIgnoreCase(requestType)
				|| RequestParam.HTTP_TYPE_POST.equalsIgnoreCase(requestType))
			return true;
		return false;
	}

	@Override
	public void setRequestType(String requestType)
			throws RequestTypeNotSupporttedException {
		if (supportRequestType(requestType))
			this.requestType = requestType;
		else
			throw new RequestTypeNotSupporttedException(
					(requestType == null ? "null" : requestType)
							+ " is not one supported request type.");
	}

	@Override
	public String getRequestURL() {
		return requestURL;
	}

	
	
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	@Override
	public Map<String, String> getPaymentParameters() {
		return paymentParameters;
	}
	
	public void setPaymentParameters(Map<String, String> paymentParameters) {
		this.paymentParameters = paymentParameters;
	}

	@Override
	public String getRequestType() {
		return this.requestType;
	}

	@Override
	public String getRequestHtml() {
		return requestHtml;
	}
	
	public void setRequestHtml(String requestHtml) {
		this.requestHtml = requestHtml;
	}

}
